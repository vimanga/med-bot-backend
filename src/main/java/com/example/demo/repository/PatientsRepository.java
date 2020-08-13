package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Patient;

public interface PatientsRepository extends JpaRepository<Patient, Long>{

	
	
	@Query("SELECT u FROM Patient u WHERE u.firebaseid = ?1")
	Patient findUserById(String firebaseid);
	
	@Query("UPDATE Patient SET did = :did WHERE firebaseid = :firebaseid")
	void updatePatient(@Param("did") Long did, @Param("firebaseid") String firebaseid);
}
