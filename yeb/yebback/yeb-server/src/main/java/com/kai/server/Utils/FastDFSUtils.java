package com.kai.server.Utils;


import com.roncoo.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

// FastDFS工具类
public class FastDFSUtils {

    private static Logger logger = LoggerFactory.getLogger(FastDFSUtils.class);

    // 初始化客户端
    static {
        try {
            // 读取配置文件并初始化对应的属性
            String absolutePath = new ClassPathResource("/config/fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(absolutePath);
        } catch (Exception e) {
            logger.error("初始化FastDFS失败", e.getMessage());
        }
    }

    // 上传文件
    public static String[] upload(MultipartFile file) {
        String name = file.getOriginalFilename();
        logger.info("文件名:", name);
        StorageClient storageClient = null;
        String[] uploadResults = null;
        try {
            // 获取storage客户端
            storageClient = getStorageClient();
            // 上传
            uploadResults = storageClient.upload_file(file.getBytes(), name.substring(name.lastIndexOf(".") + 1), null);

        } catch (Exception e) {
            logger.error("上传文件失败", e.getMessage());
        }

        if (uploadResults == null) {
            logger.error("上传失败", storageClient.getErrorCode());
        }
        return uploadResults;
    }

    // 获取文件信息
    public static FileInfo getFileInfo(String groupName, String remoteFileName) {

        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            logger.error("文件信息获取失败",e.getMessage());
        }
        return null;
    }

    // 下载文件
    public static InputStream downFile(String groupName,String remoteFileName){

        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileByte);
            return inputStream;
        } catch (Exception e) {
            logger.error("文件下载失败",e.getMessage());
        }
        return null;
    }

    // 删除文件
    public static void deleteFile(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            storageClient.delete_file(groupName,remoteFileName);
        } catch (Exception e) {
            logger.error("文件删除失败",e.getMessage());
        }
    }

    // 生成storage客户端
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    // 生成tracker服务器
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        return trackerServer;
    }

    // 获取文件路径
    public static String getTrackerUrl(){
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer=null;
        StorageServer storageServer=null;
        try {
             trackerServer= trackerClient.getTrackerServer();
             storageServer= trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            logger.error("文件路径获取失败",e.getMessage());
        }
        return "http://"+storageServer.getInetSocketAddress().getHostString()+"8888/";
    }
}
