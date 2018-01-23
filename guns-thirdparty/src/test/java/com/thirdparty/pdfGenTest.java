package com.thirdparty;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.net.URL;

/**
 * Created by syl on 2018/1/19.
 */
@RunWith(SpringRunner.class)
public class pdfGenTest {

    public static final String[] LANGUAGES = { "English", "German", "French", "Spanish", "Dutch" };

    //@Test
    public void TestPDFDemo1() {
        try {
            // 1.新建document对象
            Document document = new Document();
            // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
            // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/Users/pdfGen/test.pdf"));
            // 3.打开文档
            document.open();
            // 4.添加一个内容段落
            document.add(new Paragraph("Hello World!"));
            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void TestPDFDemo2() {
        try {
            //创建文件
            Document document = new Document();
            //建立一个书写器
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/Users/pdfGen/test2.pdf"));
            //打开文件
            document.open();
            //添加内容
            document.add(new Paragraph("Some content here"));
            //设置属性
            //标题
            document.addTitle("this is a title");
            //作者
            document.addAuthor("syl");
            //主题
            document.addSubject("this is subject");
            //关键字
            document.addKeywords("Keywords");
            //创建时间
            document.addCreationDate();
            //应用程序
            document.addCreator("hd.com");
            //关闭文档
            document.close();
            //关闭书写器
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void TestPDFDemo3() {
        try {
            //创建文件
            Document document = new Document();
            //建立一个书写器
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/Users/pdfGen/test3.pdf"));
            //打开文件
            document.open();
            //添加内容
            document.add(new Paragraph("HD content here"));
            //图片1
            Image image1 = Image.getInstance("D:/other_unimportant/aaas.jpg");
            //设置图片位置的x轴和y周
            image1.setAbsolutePosition(100f, 550f);
            //设置图片的宽度和高度
            image1.scaleAbsolute(200, 200);
            //将图片1添加到pdf文件中
            document.add(image1);
            //图片2
            Image image2 = Image.getInstance(new URL("http://static.cnblogs.com/images/adminlogo.gif"));
            //将图片2添加到pdf文件中
            document.add(image2);
            //关闭文档
            document.close();
            //关闭书写器
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
