package com.zjj.disk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjj.disk.pojo.File;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zjj
 * @create 2023-03-26 16:39
 */
@Repository
public interface FileMapper extends BaseMapper<File> {
    @Select("SELECT * FROM tbl_file Where id=#{id} AND is_deleted=0")
    File getById(Integer id);
}
