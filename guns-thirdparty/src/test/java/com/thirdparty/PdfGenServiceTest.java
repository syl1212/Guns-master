package com.thirdparty;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.thirdparty.genpdf.service.PdfGenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by syl on 2018/1/23.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class PdfGenServiceTest {

    @Autowired
    PdfGenService pdfGenService;

    @Test
    public void pdfGen(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("font1","测试一下aa");
        map.put("font2","来啊aaas");
        map.put("img","http://zwwlive-video.oss-cn-beijing.aliyuncs.com/guns-alioss/images/20180117/200_1516171000745000.jpg");

        byte[] outputData = null;
        OutputStream os = null;
        BufferedOutputStream bos1 = null;
        InputStream is = null;
        try {
            File file = ResourceUtils.getFile("classpath:pdf/organizational_risk_assessment_result-radio.pdf");
//            File file = new File("C:/Users/syl/Desktop/organizational_risk_assessment_result-radio.pdf");
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            outputData = pdfGenService.pdfGen(bos.toByteArray(), map);
            os = new FileOutputStream("C:/Users/syl/Desktop/organizational_risk_assessment_result-radio-1.pdf");
            is = new ByteArrayInputStream(outputData);
            byte[] buff = new byte[1024];
            int len = 0;
            while((len=is.read(buff))!=-1){
                os.write(buff, 0, len);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
