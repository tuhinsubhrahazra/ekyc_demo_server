/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connection;

/**
 *
 * @author Tuhin
 */
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class DBUtil {

    public static String DbIP = "127.0.0.1:1433";
    public static String dataBaseName = "eKYC_ND";
    public static String dbUsername = "sa";
    public static String dbPassword = "test1234";
    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);

            String dbUrl = "jdbc:sqlserver://" + DbIP + ";databaseName=" + dataBaseName + ";encrypt=false;";

            try {
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            } catch (Exception e) {
                System.out.println("Database connection error: " + e);
            }
        } catch (Exception e) {
            System.out.println("Error"+e); 
            e.printStackTrace();
        }
        return connection;
    }
    
    public static JSONArray resultSetToJsonArray(ResultSet resultSet) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            JSONObject jsonObject = new JSONObject();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);

                // Handle null values, if needed
                if (columnValue == null) {
                    jsonObject.put(columnName, JSONObject.NULL);
                } else {
                    jsonObject.put(columnName, columnValue);
                }
            }

            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }
}
