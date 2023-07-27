/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import connection.ReqBody;
import connection.DBConstraints;
import connection.DBUtil;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.sql.Connection;
import org.json.*;
    import java.sql.*;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "EkycLogin", urlPatterns = {"/ekyclogin"})
public class EkycLogin extends HttpServlet {

    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JSONObject jsonBody = ReqBody.getBody(req);

        String userName = jsonBody.getString("userName");
        String password = jsonBody.getString("password");
        String loginType = jsonBody.getString("loginType");

        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_LOGIN_SP);

            cs.setString(1, userName);
            cs.setString(2, password);
            cs.setString(3, "");
            cs.setString(4, loginType);
            cs.setString(5, "");

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
