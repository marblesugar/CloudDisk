package com.zy.disk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zy.disk.pojo.User;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface UserMapper extends BaseMapper<User> {
    @Update("UPDATE tbl_user SET username = #{username} WHERE id = #{loginId}")
    int updateUsername(@Param("username") String username, @Param("loginId") Integer loginId);
}
