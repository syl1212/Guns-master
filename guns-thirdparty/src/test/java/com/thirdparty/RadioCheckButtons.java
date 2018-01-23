package com.thirdparty;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by syl on 2018/1/19.
 */
public class RadioCheckButtons {
    /** The resulting PDF. */
    public static final String RESULT = "D:/Users/pdfGen/test4.pdf";
    /** Possible values of a Choice field. */
    public static final String[] FRUITS = {"苹果", "香蕉", "橘子","火龙果", "山竹"};

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // 创建文件
        Document document = new Document();
        // 建立一个书写器
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // 打开文件
        document.open();
        //显示中文字体
        BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font contentFont = new Font(bfChinese,10);

        // 添加选项内容
        Rectangle rect;
        PdfFormField field;
        PdfContentByte cb = writer.getDirectContent();
        PdfFormField radiogroup = PdfFormField.createRadioButton(writer, true);
        radiogroup.setFieldName("fruits");
        RadioCheckField radio;
        for (int i = 0; i < FRUITS.length; i++) {
            rect = new Rectangle(40, 806 - i * 20, 52, 788 - i * 20);
            radio = new RadioCheckField(writer, rect, null, FRUITS[i]);
            radio.setBorderColor(GrayColor.GRAYBLACK);
            if(FRUITS[i].equals("火龙果")){
                radio.setBackgroundColor(BaseColor.RED);
            }else{
                radio.setBackgroundColor(new GrayColor(0.8f));
            }
            field = radio.getRadioField();
            radiogroup.addKid(field);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    new Phrase(FRUITS[i], contentFont), 70, 793 - i * 20, 0);
        }
        writer.addAnnotation(radiogroup);

        // step 5
        document.close();
    }

    /**
     * Main method
     * @param args no arguments needed
     * @throws IOException
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new RadioCheckButtons().createPdf(RESULT);
    }
}
