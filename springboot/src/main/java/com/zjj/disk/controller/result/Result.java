package com.zjj.disk.controller.result;

import lombok.Data;

import java.util.HashMap;

/**
 * @author zjj
 * @create 2023-03-25 20:54
 */
@Data
public class Result {
    private Integer code;
    private String message;
    private HashMap<String, Object> data = new HashMap<>();

    public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

	public static Result success(){
        Result result = new Result();
        result.setCode(200);
        return result;
    }

    public static Result fail(){
        Result result = new Result();
        result.setCode(400);
        return result;
    }

    public Result addMsg(String message){
        this.setMessage(message);
        return this;
    }

    public Result addData(String key, Object value){
        this.getData().put(key, value);
        return this;
    }
}
