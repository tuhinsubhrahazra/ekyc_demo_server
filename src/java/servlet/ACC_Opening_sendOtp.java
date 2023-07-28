/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import connection.DBConstraints;
import connection.DBUtil;
import connection.ReqBody;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.*;
import java.sql.*;
import java.net.URL;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "ACC_Opening_sendOtp", urlPatterns = {"/acc_Opening_sendOtp"})
public class ACC_Opening_sendOtp extends HttpServlet {

    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        exec Proc_GetSMSCredential '9564980726','',1
        JSONObject jsonBody = ReqBody.getBody(req);

        String mobileNo = jsonBody.getString("mobile");
        String emailId = jsonBody.getString("emailId");

        try {
            conn = DBUtil.getConnection();
            
            cs = conn.prepareCall(DBConstraints.EKYC_IS_ALREADY_ACC);

            cs.setString(1, mobileNo);
            cs.setString(2, emailId);

            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);
            
            if(jsonArray.getJSONObject(0).getInt("Status")==0){
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write(jsonArray.getJSONObject(0).getString("Msg"));
                return;
            }else if(jsonArray.getJSONObject(0).getInt("Status")==2){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(jsonArray.getJSONObject(0).getString("Msg"));
            }
            
        } catch (Exception e) {
            System.out.println("Server error: " + e);
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("An internal server error occurred.");
        }
        
        
        
        
        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_SEND_OTP);

            cs.setString(1, mobileNo);
            cs.setString(2, "");
            cs.setInt(3, 1);

            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);

            System.out.println("" + jsonArray);
            boolean b = sendOtpTOMobile(jsonArray.getJSONObject(0).getString("URL"));

            if (b) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("SMS Send successfull");
            } else {
                resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                resp.getWriter().write("Failed to send SMS");
            }

            return;

        } catch (Exception e) {
            System.out.println("Server error: " + e);
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("An internal server error occurred.");
            return;
        }

    }

    public boolean sendOtpTOMobile(String url) throws MalformedURLException, ProtocolException, IOException {
        boolean isSMSSend = false;
        StringBuilder response = new StringBuilder();

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response using a BufferedReader
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject json = new JSONObject(response.toString());
            System.out.println("json: "+json);
            if (json.getString("status").equals("OK")) {
                isSMSSend = true;
            }

            in.close();
        } else {
            System.out.println("GET request failed with response code: " + responseCode);
        }

        connection.disconnect();

        return isSMSSend;
    }

}
