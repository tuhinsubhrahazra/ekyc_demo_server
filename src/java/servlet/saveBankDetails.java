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
import static servlet.saveDpDetails.base64StringToByteArray;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "saveBankDetails", urlPatterns = {"/saveBankDetails"})
public class saveBankDetails extends HttpServlet {

    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonBody = ReqBody.getBody(req);

        String panno = jsonBody.getString("panno");
        String bankactno = jsonBody.getString("bankactno");
        String ifsccode = jsonBody.getString("ifsccode");
        String micr = jsonBody.getString("micr");
        String bankactype = jsonBody.getString("bankactype");

        try {
            conn = DBUtil.getConnection();

            cs = conn.prepareCall(DBConstraints.EKYC_SAVE_BANK_DETAILS);

            cs.setString(1, panno);
            cs.setString(2, bankactno);
            cs.setString(3, ifsccode);
            cs.setString(4, micr);
            cs.setString(5, bankactype);

            saveBankDetImageProof(jsonBody);

            saveLetestPwd(panno);
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
    
    private void saveLetestPwd(String panno) throws SQLException {
        Connection conn = DBUtil.getConnection();
        CallableStatement cs = conn.prepareCall(DBConstraints.EKYC_UPDATE_LETEST_PWD);

        cs.setString(1, panno);
        cs.setInt(2, 5);

        cs.execute();
    }

    private void saveBankDetImageProof(JSONObject jsonBody) throws SQLException {
        String Proof = jsonBody.getString("bankProofImage");

        String panno = jsonBody.getString("panno");
        int catid = jsonBody.getInt("catid");
        String imageName = jsonBody.getString("imageName");
        int imgWidth = jsonBody.getInt("imgWidth");
        int imgHeight = jsonBody.getInt("imgHeight");
        int pageCount = jsonBody.getInt("pageCount");
        String proofName = jsonBody.getString("proofName");
        int Flg = jsonBody.getInt("Flg");
        String RejChkValue = jsonBody.getString("RejChkValue");
        String RejText = jsonBody.getString("RejText");
        int Count = jsonBody.getInt("Count");
        String RegNo = jsonBody.getString("RegNo");
        String RegExpDate = jsonBody.getString("RegExpDate");
        int filesize = jsonBody.getInt("filesize");

        byte[] imgArray = base64StringToByteArray(Proof);

        Connection conn2 = DBUtil.getConnection();

        CallableStatement cs2 = conn2.prepareCall(DBConstraints.EKYC_SAVE_LIVE_IMAGE);

        cs2.setBytes(1, imgArray);
        cs2.setString(2, panno);
        cs2.setInt(3, catid);
        cs2.setString(4, imageName);
        cs2.setInt(5, imgWidth);
        cs2.setInt(6, imgHeight);
        cs2.setInt(7, pageCount);
        cs2.setString(8, proofName);
        cs2.setInt(9, Flg);
        cs2.setString(10, RejChkValue);
        cs2.setString(11, RejText);
        cs2.setInt(12, Count);
        cs2.setString(13, RegNo);
        cs2.setString(14, RegExpDate);
        cs2.setInt(15, filesize);

        // Execute the stored procedure
        cs2.execute();
    }
    
}
