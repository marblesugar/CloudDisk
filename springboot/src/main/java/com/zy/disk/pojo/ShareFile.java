package com.zy.disk.pojo;

import lombok.Data;

@Data
public class ShareFile extends File{
	private String shareUser;
    private String url;
    
    public String getShareUser() {
        return shareUser;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
