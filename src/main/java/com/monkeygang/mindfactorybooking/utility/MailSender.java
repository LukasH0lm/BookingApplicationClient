package com.monkeygang.mindfactorybooking.utility;


import com.monkeygang.mindfactorybooking.BookingApplication;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class MailSender {
//Lukas er sej


    public static void sendReceiptToCustomer() throws IOException {

        String to = CurrentBookingSingleton.getInstance().getCustomer().getEmail();

        String from = "MindFactory@ecco.dk";

        String host = "smtp-relay.sendinblue.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.enable", "no");
        properties.put("mail.smtp.starttls.enable", "yes");
        properties.put("mail.smtp.auth", "true");

        File file = new File(BookingApplication.class.getResource("").getPath() + "SendinBluePassword");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String password = br.readLine();
        System.out.println(password);


        Session session = Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("mindfactorybyecco.noreply@gmail.com", password);

            }

        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("MindFactory Booking kode");

            // Now set the actual message
            message.setText("Din kode er: " + CurrentBookingSingleton.getInstance().getBooking().hashCode());


            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText("hvis du vil ændre i din booking, skal du bruge denne kode: ");

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart);

            /*
            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = String.valueOf(BookingApplication.class.getResource("HelloWorld.pdf"));
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);*/

            message.setContent(multipart);


            System.out.println("sending...");
            // Sender beskeden
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

    public static void sendNotificationToAdmin() throws IOException {


        //TODO: send mail to ASS

    }
}



