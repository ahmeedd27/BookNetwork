package com.ahmed.Spring_Security.file;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.*;

@Service
@Slf4j // to log somethings
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload.photos-output-path}")
    private String fileUploadPath; // this tha main folder that i will create in the application root folder

    private String getFileExtension(String filename) {
        if(filename==null || filename.isEmpty()){
            return "";
        }
        // something.jpg
        int lastDotIndex=filename.lastIndexOf("."); // will give the exact index of the dot
        if(lastDotIndex==-1){ // means that the file does not have the extension
            return "";
        }
        // in case it like this JPG
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }


    private String uploadFile(
            @NonNull MultipartFile sourceFile
            ,@NonNull String fileUploadSubFolder)
    {
        final String finalUploadPath=fileUploadPath+separator+fileUploadSubFolder;
        // we need to make sure that we have the target folder
        File targetFolder=new File(finalUploadPath);
        if(!targetFolder.exists()){
            boolean folderCreated=targetFolder.mkdirs(); // it will create it and it's all sub dirs
            if(!folderCreated){ // if it is not created for any reason
                log.warn("failed to create something");
                return null;
            }
        }
        // from the source file I want to extract the file extension
        final String fileExtension=getFileExtension(sourceFile.getOriginalFilename());
        /*
         I want to make thing like this ./upload/user/1/23576534.jpg
         using the current milli secondes to avoid the redundantly
        */
        final String targetFilePath=finalUploadPath+separator+currentTimeMillis()+"."+fileExtension;
        Path targetPath= Paths.get(targetFilePath);
        try{
            Files.write(targetPath , sourceFile.getBytes());
            log.info("File Saved Successfully to the target location : "+targetFilePath);
            return targetFilePath;
        }catch(IOException e){
            log.error("File was not saved" , e);
        }
        return null;
    }


    // that save method will return a path where we save the file
    public String saveFile(
            @NonNull MultipartFile sourceFile
            ,@NonNull Integer userId
            ) {
        final String fileUploadSubFolder="users"+ separator + userId;
        return uploadFile(sourceFile , fileUploadSubFolder);
    }


}
