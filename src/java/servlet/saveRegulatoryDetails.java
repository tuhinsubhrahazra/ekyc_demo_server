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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Tuhin
 */
@WebServlet(name = "saveRegulatoryDetails", urlPatterns = {"/saveRegulatoryDetails"})
public class saveRegulatoryDetails extends HttpServlet {

    CallableStatement cs = null;
    ResultSet rs = null;
    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonBody = ReqBody.getBody(req);

        try {
            System.out.println("json: " + jsonBody);

            String pan = jsonBody.getString("pan");
            int occCode = jsonBody.getInt("occCode");
            int annincCode = jsonBody.getInt("annincCode");
            int pepStatus = jsonBody.getInt("pepStatus");
            int edu = jsonBody.getInt("edu");
            String networth = jsonBody.getString("networth");
            String networthdt = jsonBody.getString("networthdt");
            String occdesc = jsonBody.getString("occdesc");
            String ddpi = jsonBody.getString("ddpi");
            int bsda = jsonBody.getInt("bsda");
            int dis = jsonBody.getInt("dis");

            try {
                conn = DBUtil.getConnection();

                cs = conn.prepareCall(DBConstraints.EKYC_SAVE_REGULATORY_DETAILS);

                cs.setString(1, pan);
                cs.setInt(2, occCode);
                cs.setInt(3, annincCode);
                cs.setInt(4, pepStatus);
                cs.setInt(5, edu);
                cs.setString(6, networth);
                cs.setString(7, networthdt);
                cs.setString(8, occdesc);
                cs.setString(9, ddpi);
                cs.setInt(10, bsda);
                cs.setInt(11, dis);

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
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("An internal server error occurred.");
                return;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveLetestPwd(String panno) throws SQLException {
        Connection conn = DBUtil.getConnection();
        CallableStatement cs = conn.prepareCall(DBConstraints.EKYC_UPDATE_LETEST_PWD);

        cs.setString(1, panno);
        cs.setInt(2, 6);

        cs.execute();
    }
}
