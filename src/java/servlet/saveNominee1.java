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
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "saveNominee1", urlPatterns = {"/saveNominee1"})
public class saveNominee1 extends HttpServlet {
    
    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonBody = ReqBody.getBody(req);
        
        System.out.println("json: "+jsonBody);

        String panNo = jsonBody.getString("pan");
        String name = jsonBody.getString("name");
        String panNominee = jsonBody.getString("panNominee");
        String aadhaarNominee = jsonBody.getString("aadhaarNominee");
        String nomineeRelation = jsonBody.getString("nomineeRelation");
        String nomineeDOB = jsonBody.getString("nomineeDOB");
        String guardianName = jsonBody.getString("guardianName");
        String guardianPan = jsonBody.getString("guardianPan");
        String guardianAadhaar = jsonBody.getString("guardianAadhaar");
        String nomineeGuRel = jsonBody.getString("nomineeGuRel");
        String nomineeGuardDOB = jsonBody.getString("nomineeGuardDOB");
        String secFlag = jsonBody.getString("secFlag");
        String nomineePercent = jsonBody.getString("nomineePercent");
        String stat = jsonBody.getString("stat");
        String nomineeAddType = jsonBody.getString("nomineeAddType");
        String nomineeAdd = jsonBody.getString("nomineeAdd");
        String nomineePin = jsonBody.getString("nomineePin");


        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_SAVE_NOMINEE1);

            cs.setString(1, panNo);
            cs.setString(2, name );
            cs.setString(3, panNominee);
            cs.setString(4, aadhaarNominee);
            cs.setString(5, nomineeRelation);
            cs.setString(6, nomineeDOB);
            cs.setString(7, guardianName);
            cs.setString(8, guardianPan);
            cs.setString(9, guardianAadhaar);
            cs.setString(10, nomineeGuRel);
            cs.setString(11, nomineeGuardDOB);
            cs.setString(12, secFlag);
            cs.setString(13, nomineePercent);
            cs.setString(14, stat);
            cs.setString(15, nomineeAddType);
            cs.setString(16, nomineeAdd);
            cs.setString(17, nomineePin);

            saveLetestPwd(panNo);
            // Execute the stored procedure
            cs.execute();

            JSONArray jsonArray = new JSONArray();
            
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
    
    private void saveLetestPwd(String panno) throws SQLException {
       Connection conn = DBUtil.getConnection();
       CallableStatement cs = conn.prepareCall(DBConstraints.EKYC_UPDATE_LETEST_PWD);

        cs.setString(1, panno);
        cs.setInt(2, 7);

        cs.execute();
    }

    

}
