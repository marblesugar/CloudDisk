package com.zy.disk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.zy.disk.pojo.Share;


@Repository
public interface ShareMapper {
	
	//分享
	@Select("insert into tbl_share(shareUrl, path, shareUser) values(#{shareUrl},#{path},#{shareUser})")
    void shareFile(Share share) throws Exception;

    //查找所有分享，返回链表
	@Select("select * from tbl_share where shareUrl = #{shareUrl} and status = #{status}")
    List<Share> findShare(Share share) throws Exception;


    //用于从share表中查询出该用户的已分享信息
	@Select("select * from tbl_share where shareUser = #{username, jdbcType=VARCHAR} and status = #{status, jdbcType=TINYINT}")
    List<Share> findShareByName(@Param("username") String username, @Param("status")  int status) throws Exception;

    //取消分享
	@Select("update tbl_share set status = #{status} where shareUrl = #{url} and path=#{filePath}")
    void cancelShare(@Param("url") String url, @Param("filePath")  String filePath, @Param("status") int status) throws Exception;

}
