package com.example.haisya_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	// トップページの表示
	@GetMapping("/")
	public String top() {
		return "top";
	}

}
