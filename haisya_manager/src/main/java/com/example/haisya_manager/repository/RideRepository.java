package com.example.haisya_manager.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.haisya_manager.entity.Ride;

public interface RideRepository extends JpaRepository<Ride, Integer> {
	
	// ログイン中のadminIdに紐づくすべての配車状況日付が新しい順にページングされた状態で取得する
	public Page<Ride> findByAdminIdOrderByDateDesc(Integer adminId, Pageable pageable);
	
	// 指定された日付をログイン中のadminIdに紐づく配車状況がページングされた状態で取得する
	public Page<Ride> findByAdminIdAndDate(Integer adminId, LocalDate date, Pageable pageable);
	
}
