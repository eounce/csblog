package com.induk.csblog.util;

import com.induk.csblog.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    private String path = System.getProperty("user.dir");
    private String fileDir = path + "\\src\\main\\resources\\static\\image\\";

    public String getFullPath(String filename, String path) {
        return fileDir + path + "\\" + filename;
    }

    // 여러개의 파일 업로드
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        ArrayList<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, path));
            }
        }
        return storeFileResult;
    }

    // 하나의 파일 업로드
    public UploadFile storeFile(MultipartFile multipartFile, String path) throws IOException {
        if(multipartFile.isEmpty()) return null;

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName, path)));
        return new UploadFile(originalFilename, storeFileName);
    }

    // 저장용 파일이름 생성
    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    // 파일 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
