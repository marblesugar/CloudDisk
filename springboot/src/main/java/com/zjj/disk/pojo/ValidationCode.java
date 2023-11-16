package com.zjj.disk.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zjj
 * @create 2023-04-15 16:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationCode {
    private String email;
    private String code;
    private Long expiredTime;
    private Integer status;
    
	public ValidationCode(String sendEmail, String code2, Long expiredTime2, Integer status2) {
		// TODO Auto-generated constructor stub
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
    
}
