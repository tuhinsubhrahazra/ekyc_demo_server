/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author Tuhin
 */
@WebServlet(urlPatterns = {"/testservletUrl"})
public class testservletUrl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String videoFilePath = "file:///D://New%20folder//2023-07-31/8113849633.mp4"; // Replace this with the actual path

//        PrintWriter printWriter = response.getWriter();
//        printWriter.print("Hello i am video player");

        File videoFile = new File(videoFilePath);
        if (!videoFile.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Set the response content type to video/mp4
        response.setContentType("video/mp4");

        // Get the video file's length (in bytes) and set it as the content length
        long fileLength = videoFile.length();
        response.setContentLengthLong(fileLength);

        // Copy the video file's data to the response output stream
        FileInputStream fileInputStream = new FileInputStream(videoFile);
        byte[] buffer = new byte[40000000];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            response.getOutputStream().write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }
   
    
    
}
