package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Doctor;
import com.example.demo.repository.DoctorsRepository;

@RestController
@RequestMapping("/doctor")
public class DoctorsController {
	
	@Autowired
	DoctorsRepository doctorsRepository;
	
	@GetMapping("/all")
	public List<Doctor> getDoctorList(){
		return doctorsRepository.findAll();
	}
	
	@PostMapping("/save")
	public void save(Doctor doctor) {
		doctorsRepository.save(doctor);
	}

}
