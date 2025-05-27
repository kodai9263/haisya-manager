package com.example.haisya_manager.form;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class RegisterForm {
	@NotBlank(message = "ユーザー名を入力してください。")
	private String username;
	
	@NotBlank(message = "パスワードを入力してください。")
	@Length(min = 8, message = "パスワードは8文字以上で入力してください。")
	private String password;
	
	@NotBlank(message = "パスワード（確認用）を入力してください。")
	private String passwordConfirmation;

	@NotBlank(message = "チーム名を入力してください。")
	private String teamName;
}
