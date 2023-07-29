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
@WebServlet(name = "saveDpDetails", urlPatterns = {"/saveDpDetails"})
public class saveDpDetails extends HttpServlet {
    
    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonBody = ReqBody.getBody(req);
        
        System.out.println("json: "+jsonBody);

        String pan = jsonBody.getString("pan");
        String dpId = jsonBody.getString("dpid");
        String dpCode = jsonBody.getString("clientCode");
        String dpscheme = jsonBody.getString("dpscheme");
        String dptype = jsonBody.getString("dptype");
        String demat = jsonBody.getString("demat");

        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_SAVE_DP);

            cs.setString(1, pan);
            cs.setString(2, dpId);
            cs.setString(3, dpCode);
            cs.setString(4, dpscheme);
            cs.setString(5, dptype);
            cs.setString(6, demat);

            // Execute the stored procedure
            cs.execute();

            JSONArray jsonArray = new JSONArray();

            // You can now send the JSONArray in the response as JSON data
            resp.setContentType("application/json");
            resp.getWriter().write(jsonArray.toString());
            
            return;
            
        } catch (Exception e) {
            System.out.println("Server error: " + e);
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("An internal server error occurred.");
            return;
        }
    }
    
}
