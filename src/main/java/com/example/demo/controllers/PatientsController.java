package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.ChatHistory;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.repository.DoctorsRepository;
import com.example.demo.repository.PatientsRepository;
import com.example.demo.service.MailListning;

@RestController
@RequestMapping("/patient")
public class PatientsController {

	@Autowired
	PatientsRepository patientsRepository;
	
	@Autowired
	DoctorsRepository doctorsRepository;
	
	@GetMapping("/getpatient/{id}")
	public Optional<Patient> getUserChat(@PathVariable("id") long id) {
		
		
		return patientsRepository.findById(id);
	}
	
	@GetMapping("/getpatientfromid/{id}")
	public Patient getUserFromFirebase(@PathVariable("id") String id) {
		
		return patientsRepository.findUserById(id);
	}
	
	
	@PostMapping("/savepatient")
	public void save(@RequestBody Patient patient) {
		
		patientsRepository.save(patient);
			
		}
	
	@PostMapping("/updatepatient")
	public void update(@RequestBody Patient patient) {
		System.out.println("*****");
		System.out.println(patient);
		System.out.println(patient.getDoctorId().getDid());
		
		Patient newpatient = new Patient();
		Doctor newDoctor = new Doctor();
		
		newpatient = patientsRepository.findUserById(patient.getFirebaseid());
		newDoctor = doctorsRepository.findDoctorById(patient.getDoctorId().getDid());
		newpatient.setDoctorId(newDoctor);
		patientsRepository.save(newpatient);
		//patientsRepository.updatePatient(patient.getDoctorId().getDid(), patient.getFirebaseid());
		}
}
