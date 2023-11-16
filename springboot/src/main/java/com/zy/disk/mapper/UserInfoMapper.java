package com.zy.disk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zy.disk.pojo.UserInfo;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select("SELECT * FROM tbl_user_info where creator_id = #{loginId}")
    UserInfo getUserInfo(Integer loginId);
}
