package com.zy.disk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class Share {
	 	@TableId(type = IdType.AUTO)
	 	private String shareId;
	    private String shareUrl;
	    private String shareUser;
	    private String path;
	    private String command;
	    private int status;

	    public static final int DELETE = -1;
	    public static final int CANCEL = 0;
	    public static final int PUBLIC = 1;
		public static final int PRIVATE = 2;
		
		
	    public static int getPUBLIC() {
	        return PUBLIC;
	    }

	    public static int getPRIVATE() {
	        return PRIVATE;
	    }

	    public static int getCANCEL() {
	        return CANCEL;
	    }

	    public static int getDELETE() {
	        return DELETE;
	    }

	    public String getShareUrl() {
	        return shareUrl;
	    }

	    public void setShareUrl(String shareUrl) {
	        this.shareUrl = shareUrl;
	    }

	    public String getShareId() {
	        return shareId;
	    }

	    public void setShareId(String shareId) {
	        this.shareId = shareId;
	    }

	    public String getShareUser() {
	        return shareUser;
	    }

	    public void setShareUser(String shareUser) {
	        this.shareUser = shareUser;
	    }

	    public String getPath() {
	        return path;
	    }

	    public void setPath(String path) {
	        this.path = path;
	    }

	    public String getCommand() {
	        return command;
	    }

	    public void setCommand(String command) {
	        this.command = command;
	    }

	    public int getStatus() {
	        return status;
	    }

	    public void setStatus(int status) {
	        this.status = status;
	    }
}
