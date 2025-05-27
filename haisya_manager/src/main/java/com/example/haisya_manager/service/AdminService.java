package com.example.haisya_manager.service;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.haisya_manager.entity.Admin;
import com.example.haisya_manager.entity.Team;
import com.example.haisya_manager.form.RegisterForm;
import com.example.haisya_manager.repository.AdminRepository;
import com.example.haisya_manager.repository.TeamRepository;

@Service
public class AdminService {
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	private final TeamRepository teamRepository;
	
	public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, TeamRepository teamRepository) {
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
		this.teamRepository = teamRepository;
	}
	
	@Transactional
	public Team createAdminAndTeam(RegisterForm registerForm) {
		// ユーザーを先に保存
		Admin admin = new Admin();
		admin.setUsername(registerForm.getUsername());
		admin.setPassword(passwordEncoder.encode(registerForm.getPassword()));
		admin.setRole("ROLE_ADMIN");
		admin.setEnabled(true);
		admin = adminRepository.save(admin);
		
		// チームを保存
		Team team = new Team();
		team.setName(registerForm.getTeamName());
		team.setAdmin(admin);
		return teamRepository.save(team);
	}
	
	// ユーザー名が登録済みかチェックする
	public boolean isUsernameRegistered(String username) {
		Admin admin = adminRepository.findByUsername(username);
		return admin != null;
	}
	
	// パスワードとパスワード（確認用）の入力が一致するかチェックする
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}

}
