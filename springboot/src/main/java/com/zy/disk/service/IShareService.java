package com.zy.disk.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zy.disk.controller.result.Result;
import com.zy.disk.pojo.Share;
import com.zy.disk.pojo.ShareFile;
import com.zy.disk.pojo.User;



public interface IShareService extends IService<Share> {

	
	  

	
    /**
     * 处理分享文件
     * @param request 请求
     * @param currentPath 当前位置
     * @param shareFile 分享的文档
     * @return
     */
	 public String shareFile(Integer id,HttpServletRequest request) throws Exception ;
	 
	 public Result share(Integer id, HttpServletRequest request, HttpServletResponse response);   
//    public String shareFile(Integer id,HttpServletRequest request, String currentPath) throws Exception ;
//    
    /**
     * 获得分享文件
     * @param request 请求
     * @param shareUrl 分享链接
     * @return 共享文件链表
     */
    public List<Share> findShare(HttpServletRequest request, String shareUrl) throws Exception;
   
    /**
     * 获得分享文件
     * @param request 请求
     * @param shares 分享的文档链表
     * @return 共享文件链表
     */
    public List<ShareFile> getShareFile(HttpServletRequest request, List<Share> shares);
    
    /**
     * 查询已分享的文件信息
     * @param request 请求
     * @param currentPage
     * @param pageSize
     * @param curDir
     * @param op
     * @return
     */
    //public List<ShareFile> findShareByName(HttpServletRequest request,Integer currentPage);
    public List<Share> findShareByName(HttpServletRequest request,Integer currentPage);
    /**
     * 取消/删除分享链接操作
     * @param url 分享链接
     * @param filePath 文件位置
     * @param status 状态
     * @return
     */
    public String cancelShare(String url, String filePath, int status) throws Exception;
}
