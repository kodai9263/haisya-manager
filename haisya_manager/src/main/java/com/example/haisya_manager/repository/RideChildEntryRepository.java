package com.example.haisya_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.haisya_manager.entity.Ride;
import com.example.haisya_manager.entity.RideChildEntry;

public interface RideChildEntryRepository extends JpaRepository<RideChildEntry, Integer> {
	
	// 指定したride_idに紐づく乗員する子供をリストで取得する
	public List<RideChildEntry> findByRideId(Integer rideId);
	
	// 指定したrideに紐づく保護者を削除する
	void deleteByRide(Ride ride);
}
