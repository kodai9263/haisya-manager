package com.example.haisya_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.haisya_manager.form.RegisterForm;
import com.example.haisya_manager.service.AdminService;

@Controller
public class AuthController {
	private final AdminService adminService;
	
	public AuthController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	// ログインページ表示
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	// チーム登録表示
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("registerForm", new RegisterForm());
		return "register";
	}
	
	// チーム登録をする
	@PostMapping("/register")
	public String register(@ModelAttribute @Validated RegisterForm registerForm,
						   BindingResult bindingResult,
						   RedirectAttributes redirectAttributes,
						   Model model)
	{
		// ユーザー名が登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
		if (adminService.isUsernameRegistered(registerForm.getUsername())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "username", "すでに登録済みのユーザーです。");
			bindingResult.addError(fieldError);
		}
		// パスワードとパスワード（確認用）の入力が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
		if (!adminService.isSamePassword(registerForm.getPassword(), registerForm.getPasswordConfirmation())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
			bindingResult.addError(fieldError);
		}
		// エラーがあれば、チーム登録画面に戻る
		if (bindingResult.hasErrors()) {
			return "register";
		}
		adminService.createAdminAndTeam(registerForm);
		redirectAttributes.addFlashAttribute("successMessage", "チーム登録が完了しました。");
		return  "redirect:/";
	}

}
