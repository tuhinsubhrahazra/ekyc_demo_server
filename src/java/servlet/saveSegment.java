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
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.*;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "saveSegment", urlPatterns = {"/saveSegment"})
public class saveSegment extends HttpServlet {
    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonBody = ReqBody.getBody(req);

        String panNo = jsonBody.getString("panNo");
        String equity = jsonBody.getString("equity");
        String fno = jsonBody.getString("fno");
        String curr = jsonBody.getString("curr");
        String commo = jsonBody.getString("commo");
        String dp = jsonBody.getString("dp");
        String mf = jsonBody.getString("mf");

        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_SAVE_SEGMENT);

            cs.setString(1, panNo);
            cs.setString(2, equity);
            cs.setString(3, fno);
            cs.setString(4, curr);
            cs.setString(5, commo);
            cs.setString(6, dp);
            cs.setString(7, mf);

            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);

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
