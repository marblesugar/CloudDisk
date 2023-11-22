package com.zy.disk.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zy.disk.controller.result.Result;
import com.zy.disk.mapper.FileMapper;
import com.zy.disk.mapper.ShareMapper;
import com.zy.disk.mapper.UserMapper;
import com.zy.disk.pojo.File;
import com.zy.disk.pojo.Share;
import com.zy.disk.pojo.ShareFile;
import com.zy.disk.pojo.User;
import com.zy.disk.service.IShareService;
import com.zy.disk.utils.FileUtils;
import com.zy.disk.utils.WebUtil;
@Service
@Transactional
public class ShareServiceImpl implements IShareService {
    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileMapper fileMapper;
    
	@Override
	public Result share(Integer id, HttpServletRequest request, HttpServletResponse response) {
		
        File file = fileMapper.selectById(id);
        if (file == null) {
            return Result.fail().addMsg("文件不存在！");
        }
        String fileName = file.getFileName();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new java.io.File(file.getFileLocation()));
            response.setContentType(request.getSession().getServletContext().getMimeType(file.getFileExtention()));//获取文件的mimetype
            response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));
            
            ServletOutputStream os = response.getOutputStream();
            FileCopyUtils.copy(fileInputStream,os);
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success().addMsg("分享成功！");
	}
	
	@Override
	public String shareFile(Integer id,HttpServletRequest request) throws Exception {
		Integer loginId = WebUtil.getLoginId();
		User user = userMapper.selectById(loginId);
		String userName=user.getUsername();
        String shareUrl = FileUtils.getUrl8();
        File file=fileMapper.getById(id);
  
            Share share = new Share();
            share.setPath(file.getFileLocation());
            share.setShareUser(userName);
            share.setShareUrl(shareUrl);
            shareMapper.shareFile(share);
        
        return shareUrl;
	}
//	@Override
//	public String shareFile(Integer id,HttpServletRequest request, String currentPath) throws Exception {
//		Integer loginId = WebUtil.getLoginId();
//		User user = userMapper.selectById(loginId);
//		String userName=user.getUsername();
//        String shareUrl = FileUtils.getUrl8();
//        File file=fileMapper.getById(id);
//  
//            Share share = new Share();
//            share.setPath(currentPath + java.io.File.separator +file.getFileName()+file.getFileType() );
//            share.setShareUser(userName);
//            share.setShareUrl(shareUrl);
//            shareMapper.shareFile(share);
//        
//        return shareUrl;
//	}

	@Override
	public List<Share> findShare(HttpServletRequest request, String shareUrl) throws Exception {
		  Share share = new Share();
	        share.setShareUrl(shareUrl);
	        share.setStatus(Share.PUBLIC);
	        List<Share> shares = shareMapper.findShare(share);
	        return shares;
	        //return getShareFile(request, shares);
	}

	@Override
	public List<ShareFile> getShareFile(HttpServletRequest request, List<Share> shares) {
		//未完待续
		List<ShareFile> files = null;

        return files;
	}
	
	@Override
	public List<Share> findShareByName(HttpServletRequest request,Integer currentPage) {
		Integer loginId = WebUtil.getLoginId();
		User user = userMapper.selectById(loginId);
		String userName=user.getUsername();
		int status=1;
		try {
			List<Share>  shares = shareMapper.findShareByName(userName,status);
			System.out.println(shares);
			return shares;
			//return getShareFile(request, shares);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
        
	}

	@Override
	public String cancelShare(String url, String filePath, int status) throws Exception {
		if(Share.CANCEL == status){
			shareMapper.cancelShare(url, filePath, Share.DELETE);
			return "删除成功";
		}else{
			shareMapper.cancelShare(url, filePath, Share.CANCEL);
			return "链接已失效";
		}
	}
	
	@Override
	public boolean saveBatch(Collection<Share> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<Share> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<Share> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(Share entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Share getOne(Wrapper<Share> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<Share> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<Share> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseMapper<Share> getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<Share> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}



	

}
