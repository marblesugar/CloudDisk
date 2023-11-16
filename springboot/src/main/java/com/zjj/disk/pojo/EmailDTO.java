package com.zjj.disk.pojo;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author zjj
 * @create 2023-04-15 19:54
 */
@Data
public class EmailDTO {
    @Email
    private String email;
    @NotEmpty
    @NotNull
    private String validationCode;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getValidationCode() {
		return validationCode;
	}
	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}
    
}
