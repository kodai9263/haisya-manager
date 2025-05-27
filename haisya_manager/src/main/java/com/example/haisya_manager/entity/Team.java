package com.example.haisya_manager.entity;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "teams")
@Data
@ToString(exclude = "members")
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;
	
	@OneToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;
	 
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;
	 
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;

	@OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Member> members;
}
