package com.zjj.disk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjj.disk.pojo.UserInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author zjj
 * @create 2023-04-11 19:50
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select("SELECT * FROM tbl_user_info where creator_id = #{loginId}")
    UserInfo getUserInfo(Integer loginId);
}
