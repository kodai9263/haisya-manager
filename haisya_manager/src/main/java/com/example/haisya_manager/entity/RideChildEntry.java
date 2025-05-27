package com.example.haisya_manager.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ride_child_entries")
@Data
public class RideChildEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "ride_id")
	private Ride ride;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "child_id")
	private Child child;
	
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;
	 
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;

}
