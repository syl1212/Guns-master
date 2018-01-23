package com.thirdparty;

import com.thirdparty.alioss.service.AliOSSFileServiceImpl;
import com.thirdparty.alioss.util.FileTypeArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by syl on 2018/1/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MulFilesUploadTest {

    @Autowired
    AliOSSFileServiceImpl aliOSSFileService;

    //@Test
    public void MulFilesUploadTestSync() throws Exception {
        File file = new File("D:\\other_unimportant\\baby3.jpg");
        InputStream in = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ".jpg", in);

        File file1 = new File("D:\\other_unimportant\\baby4.jpg");
        InputStream in1 = new FileInputStream(file1);
        MultipartFile multipartFile1 = new MockMultipartFile(file1.getName(), file1.getName(), ".jpg", in1);

        MultipartFile[] multipartFiles = new MultipartFile[]{multipartFile,multipartFile1};

        Map<String,String> map = aliOSSFileService.handleMulFilesSyn(multipartFiles, FileTypeArray.picArray);

        if(map != null){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        }

        //System.in.read();
    }

    //@Test
    public void MulFilesUploadTestAsync() throws Exception {
        File file = new File("D:\\other_unimportant\\baby3.jpg");
        InputStream in = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ".jpg", in);

        File file1 = new File("D:\\other_unimportant\\baby4.jpg");
        InputStream in1 = new FileInputStream(file1);
        MultipartFile multipartFile1 = new MockMultipartFile(file1.getName(), file1.getName(), ".jpg", in1);

        MultipartFile[] multipartFiles = new MultipartFile[]{multipartFile,multipartFile1};

        Map<String,Future<String>> map = aliOSSFileService.handleMulFilesAsyn(multipartFiles, FileTypeArray.picArray);

        if(map != null){
            for (Map.Entry<String, Future<String>> entry : map.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue().get());
            }
        }

        System.in.read();
    }

    @Test
    public void getTempUrl() throws Exception {
        String key = "images/20180122/200_1516603850457001.jpg";
        System.out.println(aliOSSFileService.downloadFileTempUrl(key));
    }
}
