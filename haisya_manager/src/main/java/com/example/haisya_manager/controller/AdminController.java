package com.example.haisya_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	// ログイン後トップページ表示
	@GetMapping("/top")
	public String top() {
		return "admin/top";
	}
	
}
