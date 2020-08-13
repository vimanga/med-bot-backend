package com.example.demo.service;


import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.mail.support.DefaultMailHeaderMapper;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;


//@Configuration
public class MailRecieverServiceBK {
	
//	@Bean
//    public HeaderMapper<MimeMessage> mailHeaderMapper() {
//        return new DefaultMailHeaderMapper();
//    }
//
//    @Bean
//    public IntegrationFlow imapMailFlow() {    
//        IntegrationFlow flow =  IntegrationFlows
//                .from(Mail.imapInboundAdapter("imap://diabipal@gmail.com:diabipal123@imap.gmail.com:993/INBOX")
//                                .userFlag("testSIUserFlag")
//                                .javaMailProperties(p -> p.put("mail.debug", "false")
//                                        .put("mail.imap.connectionpoolsize", "5").put("mail.smtp.auth", "true").put("mail.smtp.port", "25")),
//                        e -> e.autoStartup(true)
//                                .poller(p -> p.fixedDelay(5000)))
//                .transform(Mail.toStringTransformer())
//                .channel(MessageChannels.queue("imapChannel"))
//                .get();
//        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx"+flow.toString());
//        return flow;
//     }
//
//    @Bean(name = PollerMetadata.DEFAULT_POLLER)
//    public PollerMetadata defaultPoller() {
//
//        PollerMetadata pollerMetadata = new PollerMetadata();
//        pollerMetadata.setTrigger(new PeriodicTrigger(1000));
//        return pollerMetadata;
//    }
	
	public void sendmail() {
	Properties props = System.getProperties();
	props.setProperty("mail.store.protocol", "imaps");
	try {
	  Session session = Session.getDefaultInstance(props, null);
	  Store store = session.getStore("imaps");
	  store.connect("imap.gmail.com", "diabipal@gmail.com", "diabipal123");
	  
	  Folder emailFolder = store.getFolder("INBOX");
      emailFolder.open(Folder.READ_ONLY);

      // retrieve the messages from the folder in an array and print it
      Message[] messages = emailFolder.getMessages();
      System.out.println("messages.length---" + messages.length);

      for (int i = 0, n = messages.length; i < n; i++) {
         Message message = messages[i];
         System.out.println("---------------------------------");
         System.out.println("Email Number " + (i + 1));
         System.out.println("Subject: " + message.getSubject());
         System.out.println("From: " + message.getFrom()[0]);
         System.out.println("Text: " + message.getContent().toString());

      }
      //////////////////////////////////
      
      Message message1 = messages[5];
      String contentType = message1.getContentType();
      String messageContent="";
      

      if (contentType.contains("multipart")) {
    	  System.out.println("A");
           Multipart multiPart = (Multipart) message1.getContent();
           int numberOfParts = multiPart.getCount();
           for (int partCount = 0; partCount < numberOfParts; partCount++) {
               MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                   messageContent = part.getContent().toString();
                   System.out.println(messageContent);
           }
       }
       else if (contentType.contains("text/plain")
               || contentType.contains("text/html")) {
    	   System.out.println("B");
           Object content = message1.getContent();
           if (content != null) {
               messageContent = content.toString();
               System.out.println("C");
           }
       }
        //System.out.println(" Message: " + messageContent);
        
        ////////////////////////////////////

      //close the store and folder objects
      emailFolder.close(false);
      store.close();
      
	} catch (NoSuchProviderException e) {
        e.printStackTrace();
     } catch (MessagingException e) {
        e.printStackTrace();
     } catch (Exception e) {
        e.printStackTrace();
     }
  }
	  
//	} catch (NoSuchProviderException e) {
//	  e.printStackTrace();
//	  System.exit(1);
//	} catch (MessagingException e) {
//	  e.printStackTrace();
//	  System.exit(2);		
//	}
	
//	}
	
	
    
     
	
}
