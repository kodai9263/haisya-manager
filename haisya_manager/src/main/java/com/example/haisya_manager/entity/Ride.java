package com.example.haisya_manager.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "rides")
@Data
@ToString(exclude = {"rideEntries", "rideMemberEntries", "rideChildEntries", "drivers"})
public class Ride {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "date")
	private LocalDate date;
	
	@Column(name = "destination")
	private String destination;
	
	@Column(name = "memo")
	private String memo;
	
	@ManyToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;
	 
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;
	 
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;
	
	@OneToMany(mappedBy = "ride", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<RideEntry> rideEntries;
	
	@OneToMany(mappedBy = "ride", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<RideMemberEntry> rideMemberEntries;
	
	@OneToMany(mappedBy = "ride", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<RideChildEntry> rideChildEntries;
	
	@OneToMany(mappedBy = "ride", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Driver> drivers;
}
