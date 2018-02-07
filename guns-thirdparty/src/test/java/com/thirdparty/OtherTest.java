package com.thirdparty;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by syl on 2018/1/25.
 */
@RunWith(SpringRunner.class)
public class OtherTest {

    private static String hexString = "0123456789ABCDEF";

    //@Test
    public void otherTest(){
        String date = "2017-08-12 12:03:52";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = sdf.parse(date);
            System.out.println(date2);
            Date date1 = DateUtils.parseDate(date,"yyyy-MM-dd");
            System.out.println(date1.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void otherTest1(){



    }

    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                    .indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());
    }
}
