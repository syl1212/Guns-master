package com.thirdparty;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by syl on 2018/1/25.
 */
@RunWith(SpringRunner.class)
public class OtherTest {

    @Test
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
}
