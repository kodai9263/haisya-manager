package com.example.haisya_manager.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RideRegisterForm {
	@NotNull(message = "日付を選択してください。")
	private LocalDate date;

	@NotBlank(message = "行き先を入力してください。")
	private String destination;
	
	private String memo;
}
