package com.kob.back_end.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.back_end.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface  UserMapper extends BaseMapper<User> {
}
