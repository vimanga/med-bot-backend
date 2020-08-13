package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Doctor;

public interface DoctorsRepository extends JpaRepository<Doctor, Long>{

	@Query("SELECT u FROM Doctor u WHERE u.id = ?1")
	Doctor findDoctorById(Long id);
}
