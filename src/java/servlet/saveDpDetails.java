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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
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

            if(demat.equals("2")){
                saveDPImageProof(jsonBody);
            }
            saveSignImageProof(jsonBody);

            saveLetestPwd(pan);
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
    
    private void saveLetestPwd(String panno) throws SQLException {
        Connection conn = DBUtil.getConnection();
        CallableStatement cs = conn.prepareCall(DBConstraints.EKYC_UPDATE_LETEST_PWD);

        cs.setString(1, panno);
        cs.setInt(2, 4);

        cs.execute();
    }

    private void saveDPImageProof(JSONObject jsonBody) throws SQLException {
        String Proof = jsonBody.getString("dpImageProof");

        String panno = jsonBody.getString("pan");
        int catid = jsonBody.getInt("dpCatid");
        String imageName = jsonBody.getString("dpImageName");
        int imgWidth = jsonBody.getInt("dpImgWidth");
        int imgHeight = jsonBody.getInt("dpImgHeight");
        int pageCount = jsonBody.getInt("pageCount");
        String proofName = jsonBody.getString("dpProofName");
        int Flg = jsonBody.getInt("Flg");
        String RejChkValue = jsonBody.getString("RejChkValue");
        String RejText = jsonBody.getString("RejText");
        int Count = jsonBody.getInt("Count");
        String RegNo = jsonBody.getString("RegNo");
        String RegExpDate = jsonBody.getString("RegExpDate");
        int filesize = jsonBody.getInt("dpFilesize");

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

    private void saveSignImageProof(JSONObject jsonBody) throws SQLException {
        String Proof = jsonBody.getString("signImageProof");

        String panno = jsonBody.getString("pan");
        int catid = jsonBody.getInt("signCatid");
        String imageName = jsonBody.getString("signImageName");
        int imgWidth = jsonBody.getInt("signImgWidth");
        int imgHeight = jsonBody.getInt("signImgHeight");
        int pageCount = jsonBody.getInt("pageCount");
        String proofName = jsonBody.getString("signProofName");
        int Flg = jsonBody.getInt("Flg");
        String RejChkValue = jsonBody.getString("RejChkValue");
        String RejText = jsonBody.getString("RejText");
        int Count = jsonBody.getInt("Count");
        String RegNo = jsonBody.getString("RegNo");
        String RegExpDate = jsonBody.getString("RegExpDate");
        int filesize = jsonBody.getInt("signFilesize");

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
    
    public static byte[] base64StringToByteArray(String base64String) {
        // Decode the Base64 string to byte array
        return Base64.getDecoder().decode(base64String.getBytes(StandardCharsets.UTF_8));
    }
    
}
