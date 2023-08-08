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
@WebServlet(name = "savePersonalDetails", urlPatterns = {"/savePersonalDetails"})
public class savePersonalDetails extends HttpServlet {
    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonBody = ReqBody.getBody(req);

        String panno = jsonBody.getString("panno");
        String cname = jsonBody.getString("cname");
        String fhname = jsonBody.getString("fhname");
        String mname = jsonBody.getString("mname");
        String gender = jsonBody.getString("gender");
        String maristate = jsonBody.getString("maristate");
        String address = jsonBody.getString("address");
        String pincode = jsonBody.getString("pincode");
        String kraedit = jsonBody.getString("kraedit");

        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_SAVE_PERSONAL_DETAILS);

            cs.setString(1, panno);
            cs.setString(2, cname);
            cs.setString(3, fhname);
            cs.setString(4, mname);
            cs.setString(5, gender);
            cs.setString(6, maristate);
            cs.setString(7, address);
            cs.setString(8, pincode);
            cs.setString(9, kraedit);

            saveLetestPwd(panno);
            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);
            
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonArray.toString());

        } catch (Exception e) {
            System.out.println("Server error: " + e);
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("An internal server error occurred.");
        }
    }

    private void saveLetestPwd(String panno) throws SQLException {
        Connection conn = DBUtil.getConnection();
        CallableStatement cs = conn.prepareCall(DBConstraints.EKYC_UPDATE_LETEST_PWD);

        cs.setString(1, panno);
        cs.setInt(2, 2);

        cs.execute();
    }
   
}
