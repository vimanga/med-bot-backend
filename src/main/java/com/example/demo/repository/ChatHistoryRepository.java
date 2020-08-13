package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.ChatHistory;


public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
	
	@Query(value = "SELECT * FROM chat_history u WHERE u.firebaseid = ?1", 
			  nativeQuery = true)
	List<ChatHistory> findUserMessages(String id);

}
