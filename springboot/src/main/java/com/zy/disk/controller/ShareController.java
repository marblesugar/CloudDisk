package com.zy.disk.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zy.disk.controller.result.Result;
import com.zy.disk.pojo.File;
import com.zy.disk.pojo.Share;
import com.zy.disk.pojo.ShareFile;
import com.zy.disk.service.IFileService;
import com.zy.disk.service.IShareService;
import com.zy.disk.service.IUserService;

@RestController
public class ShareController {

	@Autowired
	private IShareService shareService;
	
	@Autowired
	private IFileService fileService;
	/**
	 * 生成分享链接
	 * 
	 * @param request
	 * @param currentPath
	 * @param shareFile
	 * @return
	 * @throws Exception
	 */
//    @RequestMapping("/shareFile")
//    public @ResponseBody Result shareFile(HttpServletRequest request, String currentPath, String[] shareFile){
//        try {
//            String shareUrl = shareService.shareFile(request, currentPath, shareFile);
//            return Result.success().addMsg("分享成功").addData("shareUrl", shareUrl);
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.fail().addMsg("分享失败"); 
//        }
//    }
	@GetMapping("/shareFile/{id}")
	public Result share(@PathVariable Integer id, HttpServletRequest request) throws Exception {
		try {
			String shareUrl = shareService.shareFile(id, request);
			return Result.success().addMsg("分享成功").addData("shareUrl", shareUrl);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.fail().addMsg("分享失败");
		}

	}

	/**
	 * 接收分享访问的share方法
	 * 
	 * @param request
	 * @param shareUrl
	 * @return
	 */
	@SuppressWarnings("null")
	@RequestMapping("/share/{shareUrl}")
	public Result share(HttpServletRequest request, @PathVariable String shareUrl) {
		try {
			List<Share> files = shareService.findShare(request, shareUrl);
			System.out.println("shareing 链表");
			System.out.println(files.get(0).getPath());
			List<File> file=fileService.getFileByLoc(files.get(0).getPath());
			//request.setAttribute("files", files);
			//Page<File> filePage = service.getFileByPage(currentPage, pageSize, curDir, op);
	        return Result.success().addMsg("查询成功！").addData("files",files).addData("file", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.fail().addMsg("获取失败！");
	}
	
	
	/**
	 * 处理查询该用户已分享文件的请求
	 * 
	 * @param request
	 * @param status
	 * @return
	 */
	@RequestMapping("/searchShare/{currentPage}")
	public Result searchShare(HttpServletRequest request,@PathVariable Integer currentPage) {
		try {
			List<Share> files = shareService.findShareByName(request, currentPage);
			System.out.println(files);
			return Result.success().addMsg("获取成功").addData("files", files);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.fail().addMsg("分享失败");
		}
	}

	/**
	 * 取消该分享链接
	 * 
	 * @param url
	 * @param filePath
	 * @param status
	 * @return
	 */
	@RequestMapping("/cancelShare")
	public @ResponseBody Result cancelShare(String url, String filePath, int status) {
		try {
			String msg = shareService.cancelShare(url, filePath, status);
			return Result.success().addMsg(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.success().addMsg("删除失败");
		}
	}

}
