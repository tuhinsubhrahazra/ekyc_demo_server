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
@WebServlet(name = "getReviewDetails", urlPatterns = {"/getReviewDetails"})
public class getReviewDetails extends HttpServlet {
    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JSONObject jsonBody = ReqBody.getBody(req);

        String mobile = jsonBody.getString("mobile");
        String relation = jsonBody.getString("relation");
        
        System.out.println("js: "+ jsonBody);

        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_GET_CLIENT_REVIEW_DET);

            cs.setString(1, mobile);
            cs.setString(2, relation);

            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);

            // You can now send the JSONArray in the response as JSON data
            resp.setContentType("application/json");
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
