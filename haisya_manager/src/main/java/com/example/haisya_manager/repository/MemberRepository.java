package com.example.haisya_manager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.haisya_manager.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	public Member findByName(String name);
	
	// 指定されたadmin_idに紐づくmemberページングされた状態で取得する
	public Page<Member> findByAdminId(Integer adminId, Pageable pageable);
	
	// 指定されたキーワードを名前に含むmemberをページングされた状態で取得する
	public Page<Member> findByAdminIdAndNameLike(Integer adminId, String keyword, Pageable pageable);
	
	// admin_idに紐づく保護者をリストで取得する
	public List<Member> findByAdminId(Integer adminId);
}
