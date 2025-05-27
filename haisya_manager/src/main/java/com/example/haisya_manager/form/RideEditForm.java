package com.example.haisya_manager.form;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideEditForm {
	@NotNull(message = "日付を選択してください。")
	private LocalDate date;

	@NotBlank(message = "行き先を入力してください。")
	private String destination;
	
	private String memo;

	private List<DriverForm> drivers;
	
	private List<RideMemberEntryForm> rideMemberEntries;
	
	private List<RideChildEntryForm> rideChildEntries;
}
