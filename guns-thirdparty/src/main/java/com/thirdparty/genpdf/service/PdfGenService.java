package com.thirdparty.genpdf.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author syl
 */
@Slf4j
@Service
public class PdfGenService {

    /**
     * 根据pdf模板生成对应pdf
     * @param inputSource
     * @param map
     * @return
     * @throws Exception
     */
    public byte[] pdfGen(byte[] inputSource, Map<String, Object> map) throws Exception {
        // 指定解析器
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        PdfStamper ps = null;
        PdfReader reader = null;
        ByteArrayOutputStream os = null;
        byte[] outputData = null;
        try {
            os = new ByteArrayOutputStream();
            // 2 读入pdf表单
            reader = new PdfReader(inputSource);
            // 3 根据表单生成一个新的pdf
            ps = new PdfStamper(reader, os);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
            BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            form.addSubstitutionFont(bf);
            // 6查询数据================================================
            // 7遍历data 给pdf表单表格赋值
            for (String key : map.keySet()) {
                if(key.contains("img")){
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    // 读图片
                    Image image = Image.getInstance(map.get(key).toString());
                    // 获取操作的页面
                    PdfContentByte under = ps.getOverContent(pageNo);
                    // 根据域的大小缩放图片
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    // 添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }else{
                    form.setField(key,map.get(key).toString());
                }
            }
            ps.setFormFlattening(true);
            ps.close();
            reader.close();
            outputData = os.toByteArray();
            log.info("===============PDF生成成功=============");
        } catch (Exception e) {
            log.info("===============PDF生成失败=============" + e.getMessage());
            outputData = null;
            throw e;
        } finally {
            if(os != null){
                os.close();
            }
        }
        return outputData;
    }


    public static void main(String[] args) throws FileNotFoundException, DocumentException {
        // 1.新建document对象
        Document document = new Document();
        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/Users/syl/genPdf/test.pdf"));
        // 3.打开文档
        document.open();
        // 4.添加一个内容段落
        document.add(new Paragraph("Hello World!"));
        // 5.关闭文档
        document.close();
    }
}
