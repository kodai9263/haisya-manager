package com.example.haisya_manager.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.haisya_manager.entity.Admin;
import com.example.haisya_manager.repository.AdminRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final AdminRepository adminRepository;
	
	public UserDetailsServiceImpl(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Admin admin = adminRepository.findByUsername(username);
			String adminRoleName = admin.getRole();
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(adminRoleName));
			return new UserDetailsImpl(admin, authorities);
		} catch (Exception e) {
			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
		}
	}

}
