package org.example;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.File;

public class SendFile extends GetParams {

   static private final String userName = GetParams.ReadFile().get("mailSender");
   static private final String password = GetParams.ReadFile().get("mailSenderPw");

        public static void sendEmailWithAttachment(String to, String subject, String body, String filePath)  {



            // Set up the mail server properties
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Create the session
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            });

            try {

                // Create a default MimeMessage object
                Message message = new MimeMessage(session);

                // Set From: header field
                message.setFrom(new InternetAddress(userName));

                // Set To: header field
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

                // Set Subject: header field
                message.setSubject(subject);

                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(body);

                // Create a multipart message
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filePath);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(new File(filePath).getName());
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                message.setContent(multipart);

                // Send message
                Transport.send(message);

                System.out.println("Sent message successfully with attachment....");

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }}



