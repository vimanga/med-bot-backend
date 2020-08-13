package com.example.demo.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.ChatHistory;
import com.example.demo.entity.Patient;
import com.example.demo.repository.ChatHistoryRepository;
import com.example.demo.repository.DoctorsRepository;
import com.example.demo.repository.PatientsRepository;
import com.example.demo.service.ChatService;
import com.example.demo.service.EmailService;
import com.example.demo.service.MailListning;


@RestController
@RequestMapping("/chat")
public class ChatHistoryController {
	
	@Autowired
	ChatHistoryRepository chatHistoryRepository;
	@Autowired
	PatientsRepository PatientsRepository;
	@Autowired
	DoctorsRepository DoctorsRepository;
	
	@CrossOrigin(origins="*")
	@GetMapping("/getuserchat/{id}")
	public List<ChatHistory> getUserChat(@PathVariable("id") String id) {
		
		
		return chatHistoryRepository.findUserMessages(id);
	}
	
	 @CrossOrigin(origins="*")
	 @GetMapping("/all")
	  List<ChatHistory> all() {
		 
		 //MailListning mailListning = new MailListning();
		 //mailListning.sendmail();

		 System.out.println("x");
	    return chatHistoryRepository.findAll();
	    
	  }
	
	@PostMapping("/addchat")
	public void addChat(@RequestBody ChatHistory chatHistory) {
		
		chatHistoryRepository.save(chatHistory);
		
	}
	@CrossOrigin(origins="*")
	@PostMapping("/sendmail")
	public void sendMail(@RequestBody ChatHistory chatHistory) throws AddressException, MessagingException, IOException {
		
		System.out.println("mail sending.......");
				
		EmailService emailService = new EmailService();
		
		Patient patient = new Patient();
		//patient = PatientsRepository.getOne(chatHistory.getPatientId());
		patient = PatientsRepository.findUserById(chatHistory.getFirebaseid());
		String doctorEmail = patient.getDoctorId().getEmail();
		
		ChatHistory newchatReply = new ChatHistory();
		newchatReply.setPatientId(chatHistory.getPatientId());
		newchatReply.setChatBy(2);
		newchatReply.setChatContent(" your message will be redirected to a doctor,");
		newchatReply.setFirebaseid(chatHistory.getFirebaseid());
		
		chatHistoryRepository.save(newchatReply);
		
		emailService.sendmail(chatHistory.getChatContent(), chatHistory.getFirebaseid(), doctorEmail );
		
	}
	
	@RequestMapping("/saveemails")
	public void save() {
		
		MailListning mailListning = new MailListning();
		List<ChatHistory> chatHistory = mailListning.sendmail();
			
		for(ChatHistory chat:chatHistory)  
			chatHistoryRepository.save(chat);
			
		}
	
	@CrossOrigin(origins="*")
	@PostMapping("/getreply")
	public ChatHistory getReply(@Valid @RequestBody ChatHistory chatHistory) throws FileNotFoundException, IOException, InterruptedException {
		

		
		ChatHistory newchatHistory = new ChatHistory();
		ChatHistory newchatReply = new ChatHistory();
		
		newchatHistory.setId(chatHistory.getId());
		newchatHistory.setPatientId(chatHistory.getPatientId());
		newchatHistory.setChatBy(chatHistory.getChatBy());
		newchatHistory.setChatContent(chatHistory.getChatContent());
		newchatHistory.setFirebaseid(chatHistory.getFirebaseid());
		
		//get doctor email
		Patient patient = new Patient();
		//patient = PatientsRepository.getOne(chatHistory.getPatientId());
		patient = PatientsRepository.findUserById(chatHistory.getFirebaseid());
		String doctorEmail = patient.getDoctorId().getEmail();
		System.out.println("&&&&&&&&&&&&&&&&&&&& "+patient.getDoctorId().getEmail());
		
		
		String text = chatHistory.getChatContent();
		ChatService chatService = new ChatService();
		System.out.println("###########"+chatHistory.getId());
		System.out.println("###########"+chatHistory.getPatientId());
		System.out.println("###########"+chatHistory.getChatBy());
		System.out.println("###########"+chatHistory.getChatContent());
		String reply = chatService.chatOutput(text, chatHistory.getPatientId(), doctorEmail);
		System.out.println("@@@@@@@@@@ "+reply);
//		TimeUnit.SECONDS.sleep(2);
		chatHistoryRepository.save(newchatHistory);
		
//		newchatReply.setId(chatHistory.getId());
		newchatReply.setPatientId(chatHistory.getPatientId());
		newchatReply.setChatBy(2);
		newchatReply.setChatContent(reply);
		newchatReply.setFirebaseid(chatHistory.getFirebaseid());
		System.out.println("************************");
		System.out.println(reply);
		
		if(!reply.equals(" your message will be redirected to a doctor,")) {
		chatHistoryRepository.save(newchatReply);
		}
		
//		ChatHistory chatHistoryReply = chatHistory;
//		chatHistoryReply.setChatContent(reply);
		

		
		return newchatReply;
	}
	
//	@RequestMapping(value = "/sendemail")
//	public String sendEmail() throws AddressException, MessagingException, IOException {
//		EmailService emailService = new EmailService();
//		//emailService.sendmail();
//	   return "Email sent successfully";   
//	}
}
