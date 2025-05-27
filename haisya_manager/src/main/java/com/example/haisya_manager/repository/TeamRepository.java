package com.example.haisya_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.haisya_manager.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {
	
	// 指定されたadmin_idに紐づくチームを取得する
	public Team findByAdminId(Integer adminId);

}
