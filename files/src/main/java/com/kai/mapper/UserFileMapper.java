package com.kai.mapper;

import com.kai.pojo.UserFile;

import java.util.List;

public interface UserFileMapper {

    // 根据登录用户的id获取用户的文件列表
    List<UserFile> findByUserId(Integer id);

    void saveUserFile(UserFile userFile);

    UserFile findById(String id);

    void update(UserFile file);

    void deleteById(String id);
}
