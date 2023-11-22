package com.zy.disk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zy.disk.pojo.File;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileMapper extends BaseMapper<File> {
    @Select("SELECT * FROM tbl_file Where id=#{id} AND is_deleted=0")
    File getById(Integer id);
    
    @Select("SELECT * FROM tbl_file Where file_location=#{location} AND is_deleted=0")
    List<File> getByLoc(String location);
}
