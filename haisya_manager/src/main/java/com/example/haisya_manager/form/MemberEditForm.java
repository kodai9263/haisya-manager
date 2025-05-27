package com.example.haisya_manager.form;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class MemberEditForm {
	@NotBlank(message = "保護者名を入力してください。")
	private String name;
	
	@Size(min = 1, message = "少なくとも1人以上の子供の名前を入力してください。")
	private List<ChildEditForm> children = new ArrayList<>();
}
