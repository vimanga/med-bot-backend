package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chat_history")
public class ChatHistory {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;
	
	@Column(name = "patientId")
	private Long patientId;
	
	@Column(name = "chatBy")
	private int chatBy;
	
	@Column(name = "chatContent")
	private String chatContent;
	
	@Column(name = "firebaseid")
	private String firebaseid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public int getChatBy() {
		return chatBy;
	}

	public void setChatBy(int chatBy) {
		this.chatBy = chatBy;
	}

	public String getChatContent() {
		return chatContent;
	}

	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}
	
	

	public String getFirebaseid() {
		return firebaseid;
	}

	public void setFirebaseid(String firebaseid) {
		this.firebaseid = firebaseid;
	}

	@Override
	public String toString() {
		return "ChatHistory [id=" + id + ", patientId=" + patientId + ", chatBy=" + chatBy + ", chatContent="
				+ chatContent + "]";
	}
	
	
}
