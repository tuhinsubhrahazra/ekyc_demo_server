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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "ACC_Opening_verifyOTP", urlPatterns = {"/acc_opening_verifyOTP"})
public class ACC_Opening_verifyOTP extends HttpServlet {

    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JSONObject jsonBody = ReqBody.getBody(req);

        String mobile = jsonBody.getString("mobile");
        String emailId = jsonBody.getString("emailId");
        String otp = jsonBody.getString("otp");
        int relativeCode = jsonBody.getInt("relativeCode");


        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_VERIFY_OTP);

            cs.setString(1, mobile);
            cs.setString(2, otp);

            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);
            
            if(jsonArray.getJSONObject(0).getString("Msg").equals("Authentication Successfull")){
                jsonArray = saveAccInfo(mobile,emailId,"",relativeCode);
            }
            
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonArray.toString());
            
            return;

        } catch (Exception e) {
            System.out.println("Server error: " + e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("An internal server error occurred.");
            return;
        }
    }

    public JSONArray saveAccInfo(String mobile, String emailId, String UserRights,int relativeCode) {
        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_SAVE_ACC_INFO);

            cs.setString(1, mobile);
            cs.setString(2, emailId);
            cs.setString(3, UserRights);
            cs.setInt(4, relativeCode);

            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);

            return jsonArray;

        } catch (Exception e) {
            System.out.println("Server error: " + e);
            return new JSONArray();
        }
    }
}
