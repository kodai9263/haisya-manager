package com.example.haisya_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.haisya_manager.entity.Child;
import com.example.haisya_manager.entity.Member;
import com.example.haisya_manager.entity.Team;
import com.example.haisya_manager.form.ChildEditForm;
import com.example.haisya_manager.form.ChildRegisterForm;
import com.example.haisya_manager.form.MemberEditForm;
import com.example.haisya_manager.form.MemberRegisterForm;
import com.example.haisya_manager.repository.ChildRepository;
import com.example.haisya_manager.repository.MemberRepository;
import com.example.haisya_manager.repository.TeamRepository;
import com.example.haisya_manager.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final ChildRepository childRepository;
	private final TeamRepository teamRepository;
	
	// ログイン中のadminIdに紐づくメンバー一覧がページングされた状態で取得する
	public Page<Member> findMembersByAdminId(Integer adminId, Pageable pageable) {
		return memberRepository.findByAdminId(adminId, pageable);
	}
	
	// ログイン中のadminIdに紐づき、指定された名前を含むmemberをページングされた状態で取得する。
	public Page<Member> findMembersByAdminIdAndNameLike(Integer adminId, String keyword, Pageable pageable) {
		return memberRepository.findByAdminIdAndNameLike(adminId, "%" + keyword + "%", pageable);
	}
	
	// 指定したidを持つ保護者を取得する
	public Optional<Member> findMemberById(Integer adminId) {
		return memberRepository.findById(adminId);
	}
	
	// member_idに紐づく子供をリストで取得する
	public List<Child> findChildrenByMemberId(Integer memberId) {
		return childRepository.findByMemberId(memberId);
	}
	
	// メンバーを登録する
	@Transactional
	public void createMember(UserDetailsImpl userDetailsImpl,
							 MemberRegisterForm memberRegisterForm,
							 ChildRegisterForm childRegisterForm) {
		Member member = new Member();
		member.setName(memberRegisterForm.getName());
		member.setAdmin(userDetailsImpl.getAdmin());

		// admin_idに紐づくチームを取得しメンバーの在籍チームを登録する
		Team team = teamRepository.findByAdminId(userDetailsImpl.getAdmin().getId());
		member.setTeam(team);
		memberRepository.save(member);
		
		// 登録された子供の数だけループする
		for (int i = 0; i < memberRegisterForm.getChildren().size(); i++) {
			String childName = memberRegisterForm.getChildren().get(i).getName();
			if (childName != null && !childName.isEmpty()) {
				Child child = new Child();
				child.setName(childName);
				child.setMember(member);
				child.setAdmin(userDetailsImpl.getAdmin());
				childRepository.save(child);
			}
		}
	}
	
	// メンバーを編集する
	@Transactional
	public void updateMember(UserDetailsImpl userDtailsImpl,
							 MemberEditForm memberEditForm,
							 ChildEditForm childEditForm,
							 Member member) {
		
		member.setName(memberEditForm.getName());
		member.setAdmin(userDtailsImpl.getAdmin());
		
		Team team = teamRepository.findByAdminId(userDtailsImpl.getAdmin().getId());
		member.setTeam(team);
		memberRepository.save(member);
		
		// 既存の子供を全て削除
		List<Child> childList = childRepository.findByMemberId(member.getId());
		for (Child child : childList) {
			childRepository.delete(child);
		}
		
		// 新しい子供及び既存の子供を入力を登録
		for (int i = 0; i < memberEditForm.getChildren().size(); i++) {
			String childName = memberEditForm.getChildren().get(i).getName();
			if (childName != null && !childName.isEmpty()) {
				Child child = new Child();
				child.setName(childName);
				child.setMember(member);
				child.setAdmin(userDtailsImpl.getAdmin());
				childRepository.save(child);
			}
		}
	}
	
	// メンバーを削除する
	@Transactional
	public void deleteMember(Member member) {
		memberRepository.delete(member);
	}

}
