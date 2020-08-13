package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "patients")
public class Patient {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pid")
	private Long pid;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "firebaseid")
	private String firebaseid;
	
//	@Column(name = "did")
	@ManyToOne
    @JoinColumn(name="did", nullable=true)
//	@JsonIgnore
	private Doctor doctor;
	
	

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

	public String getFirebaseid() {
		return firebaseid;
	}

	public void setFirebaseid(String firebaseid) {
		this.firebaseid = firebaseid;
	}

	public Doctor getDoctorId() {
		return doctor;
	}

	public void setDoctorId(Doctor doctor) {
		this.doctor = doctor;
	}

	@Override
	public String toString() {
		return "Patient [pid=" + pid + ", name=" + name + ", email=" + email + "]";
	}
	
	
}
