package com.example.haisya_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.haisya_manager.entity.RideEntry;

public interface RideEntryRepository extends JpaRepository<RideEntry, Integer> {
	
	// 指定したride_idに紐づきtrueのみの保護者をリストで取得する
	public List<RideEntry> findByRideIdAndCanDriveTrue(Integer rideId);

}
