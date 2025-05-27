package com.example.haisya_manager.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.haisya_manager.entity.Admin;

public class UserDetailsImpl implements UserDetails {
    private final Admin admin;
    private final Collection<GrantedAuthority> authorities;

    public UserDetailsImpl(Admin admin, Collection<GrantedAuthority> authorities) {
        this.admin = admin;
        this.authorities = authorities;
    }

    public Admin getAdmin() {
        return admin;
    }

    // ハッシュ化済みのパスワードを返す
    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    // ログイン時に利用するユーザー名を返す
    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    // ロールのコレクションを返す
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // アカウントが期限切れでなければtrueを返す
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ユーザーがロックされていなければtrueを返す
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ユーザーのパスワードが期限切れでなければtrueを返す
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ユーザーが有効であればtrueを返す
    @Override
    public boolean isEnabled() {
        return admin.getEnabled();
    }
}
