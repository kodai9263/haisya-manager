package com.example.haisya_manager.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChildForm {
    @NotBlank(message = "子供の名前を入力してください。")
    private String name;
    
    private Integer grade;
} 