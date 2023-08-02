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
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "saveLiveImage", urlPatterns = {"/saveLiveImage"})
public class saveLiveImage extends HttpServlet {
    
    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        JSONObject jsonBody = ReqBody.getBody(req);

        try {
            System.out.println("json: " + jsonBody);

            String image = jsonBody.getString("image");
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
            String ipadd = jsonBody.getString("ipadd");
            String location = jsonBody.getString("location");
            String state = jsonBody.getString("state");
            double latitude = jsonBody.getDouble("latitude");
            double longitude = jsonBody.getDouble("longitude");
            
            // convert base64 data to byte array
            byte []imgArray = base64StringToByteArray(image);

            try {
                conn = DBUtil.getConnection();

                cs = conn.prepareCall(DBConstraints.EKYC_SAVE_LIVE_IMAGE);

                cs.setBytes(1, imgArray);
                cs.setString(2, panno);
                cs.setInt(3, catid);
                cs.setString(4, imageName);
                cs.setInt(5, imgWidth);
                cs.setInt(6, imgHeight);
                cs.setInt(7, pageCount);
                cs.setString(8, proofName);
                cs.setInt(9, Flg);
                cs.setString(10, RejChkValue);
                cs.setString(11, RejText);
                cs.setInt(12, Count);
                cs.setString(13, RegNo);
                cs.setString(14, RegExpDate);
                cs.setInt(15, filesize);    


                // Execute the stored procedure
                cs.execute();
                
                cs = conn.prepareCall(DBConstraints.EKYC_SAVE_LIVE_IPVLOG);
                cs.setString(1, panno);
                cs.setString(2, ipadd);
                cs.setString(3, location);
                cs.setString(4, state);
                cs.setString(5, latitude+"");
                cs.setString(6, longitude+"");
                
                cs.execute();

                JSONArray jsonArray = new JSONArray();

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
        } catch (IOException | JSONException e) { 
            e.printStackTrace();
        }

    }
    
    public static byte[] base64StringToByteArray(String base64String) {
        // Decode the Base64 string to byte array
        byte[] byteArray = Base64.getDecoder().decode(base64String);

        return byteArray;
    }

    
}
