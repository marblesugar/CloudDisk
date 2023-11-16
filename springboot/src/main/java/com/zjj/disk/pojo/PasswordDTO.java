package com.zjj.disk.pojo;

import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @author zjj
 * @create 2023-04-14 23:40
 */
@Data

public class PasswordDTO {
    @Pattern(
            regexp = "^[a-zA-Z]\\w{5,17}$",
            message = "以字母开头，长度在6~18之间，只能包含字母、数字和下划线"
    )
    private String oldPassword;
    @Pattern(
            regexp = "^[a-zA-Z]\\w{5,17}$",
            message = "以字母开头，长度在6~18之间，只能包含字母、数字和下划线"
    )
    private String newPassword;
    @Pattern(
            regexp = "^[a-zA-Z]\\w{5,17}$",
            message = "以字母开头，长度在6~18之间，只能包含字母、数字和下划线"
    )
    private String confirmPassword;
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
    
}
