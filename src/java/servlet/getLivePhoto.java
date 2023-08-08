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
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "getLivePhoto", urlPatterns = {"/getLivePhoto"})
public class getLivePhoto extends HttpServlet {

    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JSONObject jsonBody = ReqBody.getBody(req);

        System.out.println("json: " + jsonBody);

        String pan = jsonBody.getString("pan");

        try {
            conn = DBUtil.getConnection();
            cs = conn.prepareCall(DBConstraints.EKYC_GET_LIVE_PHOTO);

            cs.setString(1, pan);

            // Execute the stored procedure
            rs = cs.executeQuery();

            JSONArray jsonArray = DBUtil.resultSetToJsonArray(rs);
            Object object = jsonArray.getJSONObject(0).get("image");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            // Write the object to the output stream
            objectOutputStream.writeObject(object);

            // Get the byte array representing the serialized object
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64String = java.util.Base64.getEncoder().encodeToString(byteArray);
            
            jsonArray.getJSONObject(0).remove("image");
            jsonArray.getJSONObject(0).put("image", base64String);
            
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
