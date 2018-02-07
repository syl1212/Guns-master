package com.thirdparty;

import com.thirdparty.alioss.service.AliOSSFileServiceImpl;
import com.thirdparty.alioss.vo.UploadFileDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GunsThirdpartyApplicationTests {

	@Autowired
	AliOSSFileServiceImpl aliOOSFileService;

	@Test
	public void contextLoads() throws Exception{
		UploadFileDTO uploadFileDTO = new UploadFileDTO();
		uploadFileDTO.setFileName("龙珠1.jpg");
		uploadFileDTO.setFileDesc("就是测试文档");
		uploadFileDTO.setFileStorePathName("/test/");
		uploadFileDTO.setFileSuffix(".jpg");
		uploadFileDTO.setInput(readFileByBytes("D:\\other_unimportant\\龙珠1.jpg"));
		uploadFileDTO.setFileLength(Long.parseLong(String.valueOf(readFileByBytes("D:\\other_unimportant\\龙珠1.jpg").length)));
		System.out.println(aliOOSFileService.uploadFile(uploadFileDTO));
	}

	public static byte[] readFileByBytes(String fileName) {
		InputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			in = new FileInputStream(fileName);
			byte[] buf = new byte[1024];
			int length = 0;
			while ((length = in.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
		return out.toByteArray();
	}


}
