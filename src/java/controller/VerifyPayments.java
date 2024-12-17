/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.PayHere;

/**
 *
 * @author danir
 */
@WebServlet(name = "VerifyPayments", urlPatterns = {"/VerifyPayments"})
public class VerifyPayments extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
        String merchant_id = req.getParameter("merchant_id");
        String order_id  = req.getParameter("order_id");
        String payhere_amount = req.getParameter("payhere_amount");
        String payhere_currency = req.getParameter("payhere_currency");
        String status_code = req.getParameter("status_code");
        String md5sig = req.getParameter("md5sig");
        
        String merchant_secret ="Mjg3MjQ1OTUzMDQwMzQ0Njc3NTU0NDMzNDE2MDkxNjczNzEyNTI=";
        String merchant_secret_md5hash = PayHere.generateMD5(merchant_secret);
        
        
        
        String generateMD5Hash = PayHere.generateMD5(merchant_id+order_id+payhere_amount+payhere_currency+status_code+merchant_secret_md5hash);
        
        if(generateMD5Hash.equals(md5sig)&&status_code.equals("2")){
            System.out.println("Payment Compleated of "+order_id );
        }
        
        
        
    }

   
    
    
    
}
