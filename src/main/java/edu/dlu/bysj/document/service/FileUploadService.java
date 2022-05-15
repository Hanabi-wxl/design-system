package edu.dlu.bysj.document.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileUploadService {
    Map<String,String> uploadFile(MultipartFile file, String url);

    boolean uploadOpenReport(Integer subjectId, String path);
}
