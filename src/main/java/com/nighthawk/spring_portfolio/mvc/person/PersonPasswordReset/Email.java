package com.nighthawk.spring_portfolio.mvc.person.PersonPasswordReset;


// Java program to send email 
  
import java.util.*;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
  
  
public class Email  
{ 
  
   public static void sendEmail(String recipient,String code){
      // email ID of Recipient. 
  
      // email ID of  Sender. 
      String sender = "sender@gmail.com";
  
      // Getting system properties 
      Properties properties = System.getProperties(); 
  
      // Setting up mail server 
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.starttls.enable", "true");
      properties.put("mail.smtp.host", "smtp.gmail.com");
      properties.put("mail.smtp.port", 587);
      properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
  
      // creating session object to get properties 
      Session session = Session.getDefaultInstance(properties,new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("trystan.yt.2@gmail.com","mlneyqihivhipuce"); // email and password, see this for app passwords https://support.google.com/accounts/answer/185833?visit_id=638748419667916449-2613033234&p=InvalidSecondFactor&rd=1
        }
    }); 
  
      try 
      { 
         // MimeMessage object. 
         MimeMessage message = new MimeMessage(session); 
  
         // Set From Field: adding senders email to from field. 
         message.setFrom(new InternetAddress(sender)); 
  
         // Set To Field: adding recipient's email to from field. 
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); 
  
         // Set Subject: subject of the email 
         message.setSubject("Password Reset"); 
  
         MimeMultipart emailContent = new MimeMultipart();
         
         MimeBodyPart body1 = new MimeBodyPart();
         body1.setContent("<h1>To reset your email use the following code:</h1>","text/html");
         MimeBodyPart body2 = new MimeBodyPart();
         body2.setContent("<code style=\"background-color: lightblue; font-size: 50px; border-radius: 15px;\">"+code+"</code>","text/html");

         emailContent.addBodyPart(body1);
         emailContent.addBodyPart(body2);

         message.setContent(emailContent);

  
         // Send email. 
         Transport.send(message); 
         System.out.println("Mail successfully sent"); 
      } 
      catch (MessagingException mex)  
      { 
         mex.printStackTrace(); 
      } 
   }
} 