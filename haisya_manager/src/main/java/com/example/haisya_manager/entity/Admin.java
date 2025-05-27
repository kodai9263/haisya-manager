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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "admins")
@Data
@ToString(exclude = {"team", "members", "rides"})
public class Admin {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "id")
	 private Integer id;

	 @Column(name = "username")
	 private String username;
	 
	 @Column(name = "password")
	 private String password;
	 
	 @Column(name = "role")
	 private String role;
	 
	 @Column(name = "enabled")
	 private Boolean enabled;
	 
	 @Column(name = "created_at", insertable = false, updatable = false)
	 private Timestamp createdAt;
	 
	 @Column(name = "updated_at", insertable = false, updatable = false)
	 private Timestamp updatedAt;
	 
	 @OneToOne(mappedBy = "admin", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	 private Team team;
	 
	 @OneToMany(mappedBy = "admin", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	 private List<Member> members;
	 
	 @OneToMany(mappedBy = "admin", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	 private List<Ride> rides;
}
