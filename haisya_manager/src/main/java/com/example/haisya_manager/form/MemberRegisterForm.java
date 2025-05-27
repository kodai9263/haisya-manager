package com.example.haisya_manager.form;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class MemberRegisterForm {
	@NotBlank(message = "保護者名を入力してください。")
	private String name;
	
	@Size(min = 1, message = "少なくとも1人以上の子供の名前を入力してください。")
	private List<ChildRegisterForm> children;
	
}
