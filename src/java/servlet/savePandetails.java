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
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import java.sql.*;
import java.util.Base64;
import org.json.JSONArray;
import static servlet.saveLiveImage.base64StringToByteArray;

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

            savePanProofImage(jsonBody);

            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);

            System.err.println("jsResponse: " + jsonArray);

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

    private void savePanProofImage(JSONObject jsonBody) throws SQLException {
        String panProof = jsonBody.getString("panProof");

        String panno = jsonBody.getString("panNo");
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

        byte[] imgArray = base64StringToByteArray(panProof);

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
    
    public static byte[] base64StringToByteArray(String base64String) {
        // Decode the Base64 string to byte array
//        byte[] byteArray = Base64.getDecoder().decode(base64String);
        byte[] byteArray = Base64.getDecoder().decode(base64String.getBytes(StandardCharsets.UTF_8));
        return byteArray;
    }

}
