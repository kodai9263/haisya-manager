package com.example.haisya_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.haisya_manager.entity.Child;

public interface ChildRepository extends JpaRepository<Child, Integer> {
	
	// member_idに紐づく子供をリストで取得する
	public List<Child> findByMemberId(Integer memberId);
	
	// admin_idに紐づく子供をリストで取得する
	public List<Child> findByAdminId(Integer adminId);

}
