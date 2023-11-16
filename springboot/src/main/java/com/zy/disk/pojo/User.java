package com.zy.disk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Length(min = 2, max = 9)
    private String username;
    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z]\\w{5,17}$",
            message = "以字母开头，长度在6~18之间，只能包含字母、数字和下划线"
    )
    private String password;
    @Email
    private String email;
    private Integer role;
    private String salt;
    private String activationCode;
    private LocalDateTime activationTime;
    private Integer isVaild;
    @TableLogic
    private Integer isDeleted;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public LocalDateTime getActivationTime() {
		return activationTime;
	}
	public void setActivationTime(LocalDateTime activationTime) {
		this.activationTime = activationTime;
	}
	public Integer getIsVaild() {
		return isVaild;
	}
	public void setIsVaild(Integer isVaild) {
		this.isVaild = isVaild;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
    
}
