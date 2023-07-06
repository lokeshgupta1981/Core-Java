package com.howtodoinjava.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class EmailSenderWithSSL {

  private static final Properties PROPERTIES = new Properties();
  private static final String USERNAME = "admin@eprindia.in";
  private static final String PASSWORD = "qyuuiyuzjphpoaay";
  private static final String HOST = "smtp.gmail.com";

  static {
    PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
    PROPERTIES.put("mail.smtp.port", "465");
    PROPERTIES.put("mail.smtp.auth", "true");
    PROPERTIES.put("mail.smtp.starttls.enable", "true");
    PROPERTIES.put("mail.smtp.socketFactory.port", "465");
    PROPERTIES.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
  }

  public static void sendPlainTextEmail(String from, String to, String subject, List<String> messages, boolean debug) {

    Authenticator authenticator = new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(USERNAME, PASSWORD);
      }
    };

    Session session = Session.getInstance(PROPERTIES, authenticator);
    session.setDebug(debug);

    try {
      // create a message
      MimeMessage msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(from));
      InternetAddress[] address = {new InternetAddress(to)};
      msg.setRecipients(Message.RecipientType.TO, address);
      msg.setSubject(subject);
      msg.setSentDate(new Date());

      // create the Multipart and its parts to it
      Multipart mp = new MimeMultipart();
      for (String message : messages) {
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setText(message, "us-ascii");
        mp.addBodyPart(mbp);
      }
      msg.setContent(mp);

      // send the message
      Transport.send(msg);
    } catch (MessagingException mex) {
      mex.printStackTrace();
      Exception ex = null;
      if ((ex = mex.getNextException()) != null) {
        ex.printStackTrace();
      }
    }
  }
}
