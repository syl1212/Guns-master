package com.thirdparty;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by syl on 2018/1/23.
 */

@RunWith(SpringRunner.class)
public class PdfTempGen {

    @Test
    public void pdfGen(){
        // 指定解析器
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        //String path = request.getSession().getServletContext().getRealPath("/upload/");
        String filename="测试1.pdf";
        String path="C:/Users/syl/Desktop/";
        OutputStream os = null;
        PdfStamper ps = null;
        PdfReader reader = null;
        try {
            os = new FileOutputStream("C:/Users/syl/Desktop/testResult1-1.pdf");
            // 2 读入pdf表单
            reader = new PdfReader(path+ "/"+filename);
            // 3 根据表单生成一个新的pdf
            ps = new PdfStamper(reader, os);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
            BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            form.addSubstitutionFont(bf);
            // 6查询数据================================================
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("name", "小帅哥");
            data.put("where", "在那");
            data.put("test", "测试");
            // 7遍历data 给pdf表单表格赋值
            for (String key : data.keySet()) {
                form.setField(key,data.get(key).toString());
            }
            ps.setFormFlattening(true);
            //-----------------------------pdf 添加图片----------------------------------
            // 通过域名获取所在页和坐标，左下角为起点
            System.out.println("pdf 添加图片");
            String imgpath="D:/other_unimportant/baby1.jpg";
            int pageNo = form.getFieldPositions("img").get(0).page;
            Rectangle signRect = form.getFieldPositions("img").get(0).position;
            float x = signRect.getLeft();
            float y = signRect.getBottom();
            // 读图片
            Image image = Image.getInstance(imgpath);
            // 获取操作的页面
            PdfContentByte under = ps.getOverContent(pageNo);
            // 根据域的大小缩放图片
            image.scaleToFit(signRect.getWidth(), signRect.getHeight());
            // 添加图片
            image.setAbsolutePosition(x, y);
            under.addImage(image);
            //-------------------------------------------------------------
            System.out.println("===============PDF导出成功=============");
        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============");
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (reader != null)
                    reader.close();
                if (os != null)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
