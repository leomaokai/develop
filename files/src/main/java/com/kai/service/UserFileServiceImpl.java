package com.kai.service;

import com.kai.mapper.UserFileMapper;
import com.kai.pojo.UserFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class UserFileServiceImpl implements IUserFileService{

    @Resource
    private UserFileMapper userFileMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserFile> findByUserId(Integer id) {
        return userFileMapper.findByUserId(id);
    }

    @Override
    public void save(UserFile userFile) {
        // 是否是图片,当类型中还有image时说明当前类型一定为图片类型
        String image = userFile.getType().startsWith("image") ? "是" : "否";
        userFile.setIsImg(image);
        userFile.setDownCounts(0);
        // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        userFile.setUploadTime(LocalDateTime.now());
        userFileMapper.saveUserFile(userFile);
    }

    @Override
    public UserFile findById(String id) {
        return userFileMapper.findById(id);
    }

    @Override
    public void update(UserFile file) {
        userFileMapper.update(file);
    }

    @Override
    public void deleteById(String id) {
        userFileMapper.deleteById(id);
    }
}
