package edu.dlu.bysj.document.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import edu.dlu.bysj.base.model.entity.OpenReport;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.document.service.FileUploadService;
import edu.dlu.bysj.paper.service.FileInformationService;
import edu.dlu.bysj.paper.service.OpenReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author sinre
 * @create 05 14, 2022
 * @since 1.0.0
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    /**
     * 项目端口
     */
    @Value("${server.port}")
    public String port;

    @Value("${server.servlet.context-path}")
    public String contextPath;

    @Autowired
    private OpenReportService reportService;

    @Override
    public Map<String,String> uploadFile(MultipartFile file, String url) {
        try {
            // 获取文件的名称
            String originalFilename = file.getOriginalFilename();
            // 文件后缀 例如：.pdf
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // uuid 生成文件名
            String uuid = String.valueOf(UUID.randomUUID());
            // 根路径，在 resources/upload
            String basePath = ResourceUtils.getURL("classpath:").getPath() + "static/upload/" + (StringUtils.isNotBlank(url) ? (url + "/") : "");
            // 新的文件名，使用uuid生成文件名
            String fileName = uuid + fileSuffix;
            // 创建新的文件
            File fileExist = new File(basePath);
            // 文件夹不存在，则新建
            if (!fileExist.exists()) {
                fileExist.mkdirs();
            }
            // 获取文件对象
            File fileObj = new File(basePath, fileName);
            // 完成文件的上传
            file.transferTo(fileObj);
            // 返回绝对路径
            Map<String,String> res = new HashMap<>();

            res.put("url", "http://" + InetAddress.getLocalHost().getHostAddress()
                    + ":" + port + (contextPath.equals("/") ? "" : contextPath)
                    + "/upload/" + url + "/" + fileName);

            res.put("dir", "static/upload/"+ url + "/" + fileName);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean uploadOpenReport(Integer subjectId, String path) {

        return false;
    }
}
