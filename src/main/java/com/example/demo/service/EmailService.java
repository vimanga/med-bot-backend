package com.example.demo.service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.repository.PatientsRepository;
import com.example.demo.repository.DoctorsRepository;

public class EmailService {
	
	@Autowired
	PatientsRepository PatientsRepository;
	
	public void sendmail(String sentence, String patientId, String doctorEmail) throws AddressException, MessagingException, IOException {
		
		//getting doctor email
		
		//Patient patient = new Patient();
//		PatientsRepository patientsRepository = null;
//		DoctorsRepository doctorsRepository = null;
//		//patient = patientsRepository.findById(Long.parseLong(patientId));
//		
//		System.out.println("**************** "+Long.parseLong(patientId));
//		
//		@SuppressWarnings("null")
//		Optional<Patient> optinalEntity =  patientsRepository.findById(Long.parseLong(patientId));
//		Patient patient = optinalEntity.get();
//		
//		@SuppressWarnings("null")
//		Optional<Doctor> optinalEntityD =  doctorsRepository.findById(patient.getDoctorId());
//		Doctor doctor = optinalEntityD.get();
		
//		Patient patient = new Patient();
//		System.out.println("))))))))))))))))  "+patientId);
//		patient = PatientsRepository.getOne((long) 1001);
//		System.out.println("&&&&&&&&&&&&&&&&&&&& "+patient.getDoctorId().getEmail());
		
		
		
		   Properties props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtp.gmail.com");
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication("diabipal@gmail.com", "diabipal1997");
		      }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress("diabipal@gmail.com", false));

		   //msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("vimanga97@gmail.com"));
		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(doctorEmail));
		   msg.setSubject("Diabipal email #"+patientId);
		   msg.setContent(sentence, "text/html");
		   msg.setSentDate(new Date());

		   MimeBodyPart messageBodyPart = new MimeBodyPart();
		   messageBodyPart.setContent(sentence, "text/html");

		   Multipart multipart = new MimeMultipart();
		   multipart.addBodyPart(messageBodyPart);
//		   MimeBodyPart attachPart = new MimeBodyPart();

//		   attachPart.attachFile("/var/tmp/image19.png");
//		   multipart.addBodyPart(attachPart);
//		   msg.setContent(multipart);
		   Transport.send(msg);   
		}

}
