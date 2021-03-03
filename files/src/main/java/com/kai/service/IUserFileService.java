package com.kai.service;

import com.kai.pojo.UserFile;

import java.util.List;

public interface IUserFileService {
    List<UserFile> findByUserId(Integer id);

    void save(UserFile userFile);

    UserFile findById(String id);

    void update(UserFile file);

    void deleteById(String id);
}
