package com.example.demo.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.demo.controllers.ChatHistoryController;
import com.example.demo.entity.ChatHistory;
import com.example.demo.repository.ChatHistoryRepository;


//@Configuration
public class MailListning {
	
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
	
//	@Autowired
//	ChatHistoryRepository chatHistoryRepository;
	
	public List<ChatHistory> sendmail() {
	List<ChatHistory> list = new ArrayList<ChatHistory>();
	Properties props = System.getProperties();
	props.setProperty("mail.store.protocol", "imaps");
	try {
	  Session session = Session.getDefaultInstance(props, null);
	  Store store = session.getStore("imaps");
	  store.connect("imap.gmail.com", "diabipal@gmail.com", "diabipal1997");
	  
	  Folder emailFolder = store.getFolder("INBOX");
      emailFolder.open(Folder.READ_WRITE);
      
//      Folder[] f = store.getDefaultFolder().list();
//      for(Folder fd:f)
//        System.out.println(">> "+fd.getName());

      //retrieve the messages from the folder in an array and print it
      //Message[] messages = emailFolder.getMessages();
      Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
      System.out.println("messages.length---" + messages.length);

      for (int i = 0, n = messages.length; i < n; i++) {
         Message message = messages[i];
         System.out.println("---------------------------------");
         System.out.println("Email Number " + (i + 1));
         System.out.println("Subject: " + message.getSubject());
         System.out.println("From: " + message.getFrom()[0]);
         System.out.println("Text: " + message.getContent().toString());
         
         String contentType = message.getContentType();
         String messageContent="";
         
         System.out.println("XXXXXXXXX - "+message.getSubject().toString());
         
         String patientId[] = message.getSubject().toString().split("\\#");
         
         if(patientId[0].equals("Re: Diabipal email ")) {
        	 
	         
	         
	         System.out.println("######## "+patientId[1]);
	         
	         ChatHistory chatHistory = new ChatHistory();
	         String lines[] = null;
	
	         if (contentType.contains("multipart")) {
	       	  System.out.println("A");
	              Multipart multiPart = (Multipart) message.getContent();
	              int numberOfParts = multiPart.getCount();
	              System.out.println("numberOfParts"+numberOfParts);
	              for (int partCount = 0; partCount < 1; partCount++) {
	                  MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
	                      messageContent = part.getContent().toString();
	                      System.out.println(messageContent);  
	//                      if(partCount == 0) {
	                      lines = messageContent.split("\\r?\\n");
	                      System.out.println(lines[0]);
	//                      }
	              }
	              
	          }
	          else if (contentType.contains("text/plain")
	                  || contentType.contains("text/html")) {
	       	   System.out.println("B");
	              Object content = message.getContent();
	              if (content != null) {
	                  messageContent = content.toString();
	                  System.out.println("C");
	              }
	          }
	           //System.out.println(" Message: " + messageContent);
	         
	         System.out.println("XXX patient id : "+patientId[1]);
	         System.out.println("XXX Message : "+lines[0]);
	         
	        // chatHistory.setPatientId(Long. parseLong(patientId[1]));
	         chatHistory.setFirebaseid(patientId[1]);
	         chatHistory.setChatBy(3);
	         chatHistory.setChatContent(lines[0]);
	         //chatHistory.setId((long) 2001);
	         
	         
	//         chatHistoryRepository.save(chatHistory);
	//         ChatHistoryController chatHistoryController = new ChatHistoryController();
	//         
	//         chatHistoryController.save(chatHistory);
	         
	         list.add(chatHistory);
         
         }
           
           ////////////////////////////////////

         //close the store and folder objects
         message.setFlag(Flags.Flag.DELETED, true);
         

      }
      
      emailFolder.close(false);
      store.close();
      //////////////////////////////////
      
     // Message message1 = messages[0];
      
      
	} catch (NoSuchProviderException e) {
        e.printStackTrace();
     } catch (MessagingException e) {
        e.printStackTrace();
     } catch (Exception e) {
        e.printStackTrace();
     }
	return list;
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
