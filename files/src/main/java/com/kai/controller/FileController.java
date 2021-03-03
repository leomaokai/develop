package com.kai.controller;


import com.kai.pojo.User;
import com.kai.pojo.UserFile;
import com.kai.service.IUserFileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/file")
public class FileController {

    @Resource
    private IUserFileService userFileService;

    @GetMapping("/showAll")
    public String findAll(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";
        // 从session中得到用户
        User user = (User) session.getAttribute("user");
        // 通过用户id查询其所有文件
        List<UserFile> userFiles = userFileService.findByUserId(user.getId());
        // 存入作用域中
        model.addAttribute("files", userFiles);
        return "showAll";
    }

    @PostMapping("/upload")
    public String upload(MultipartFile uploadFile, HttpSession session) {
        if (uploadFile.isEmpty()) {
            return "redirect:/file/showAll";
        }
        // 获取文件的原始名称
        String oldFileName = uploadFile.getOriginalFilename();
        // 获取文件的后缀
        String ext = "." + FilenameUtils.getExtension(oldFileName);
        // 生成新的文件名
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + UUID.randomUUID().toString().replace("-", "")
                + ext;
        // 文件的大小
        long size = uploadFile.getSize();
        // 文件的类型
        String type = uploadFile.getContentType();

        // 处理文件上传
        try {
            // 获取/static/files的绝对路径
            String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/files";
            // 获取带有时间的路径
            String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String dateDirPath = realPath + "/" + format;
            // 创建目录
            File dataDir = new File(dateDirPath);
            if (!dataDir.exists()) {
                // 如果目录不存在,则创建多级目录
                dataDir.mkdirs();
            }
            // 文件上传
            uploadFile.transferTo(new File(dataDir, newFileName));
            // 将文件信息存入数据库
            UserFile userFile = new UserFile();
            userFile.setOldFileName(oldFileName)
                    .setNewFileName(newFileName)
                    .setExt(ext)
                    .setSize(String.valueOf(size))
                    .setType(type)
                    .setPath("/files/" + format)
                    .setUserId(((User) session.getAttribute("user")).getId());
            userFileService.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/file/showAll";
    }

    @GetMapping("/download")
    public void download(@RequestParam(defaultValue = "attachment") String openStyle, String id, HttpServletResponse response) {
        // System.out.println(openStyle);
        UserFile file = userFileService.findById(id);
        // 获取文件信息及文件输入流
        FileInputStream is = null;
        ServletOutputStream os = null;
        try {
            // 获取文件路径
            String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static" + file.getPath();
            // 获取文件输入流
            is = new FileInputStream(new File(realPath, file.getNewFileName()));
            // 附件下载
            response.setHeader("content-disposition",openStyle+";fileName="+ URLEncoder.encode(file.getOldFileName(),"UTF-8"));
            // 获取响应输出流
            os = response.getOutputStream();
            // 文件拷贝
            IOUtils.copy(is, os);
            // 更新下载次数
            if(openStyle.equals("attachment")){
                file.setDownCounts(file.getDownCounts()+1);
                userFileService.update(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    @GetMapping("/delete")
    public String deleteById(String id){
        // 根据id查询信息
        UserFile userFile = userFileService.findById(id);
        // 删除文件
        try {
            String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static" + userFile.getPath();
            File file = new File(realPath, userFile.getNewFileName());
            if(file.exists())
                file.delete();
            userFileService.deleteById(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/file/showAll";
    }

    // 返回当前用户的所有文件列表--json 格式
    @GetMapping("/findAllJson")
    @ResponseBody
    public List<UserFile> findAllJson(HttpSession session) {

        // 从session中得到用户
        User user = (User) session.getAttribute("user");
        // 通过用户id查询其所有文件
        List<UserFile> userFiles = userFileService.findByUserId(user.getId());
        return userFiles;
    }
}
