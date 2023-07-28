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
import org.json.JSONObject;
import java.sql.*;
import org.json.JSONArray;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "savePandetails", urlPatterns = {"/savePandetails"})
public class savePandetails extends HttpServlet {

    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonBody = ReqBody.getBody(req);

        String panNo = jsonBody.getString("panNo");
        String mobileNo = jsonBody.getString("mobileNo");
        String emailId = jsonBody.getString("emailId");
        int relativeCode = jsonBody.getInt("relativeCode");

        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_SAVE_PAN_INFO);

            cs.setString(1, panNo);
            cs.setString(2, mobileNo);
            cs.setString(3, emailId);
            cs.setInt(4, relativeCode);

            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);
            
            System.err.println("jsResponse: "+jsonArray);
            // You can now send the JSONArray in the response as JSON data
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

}
