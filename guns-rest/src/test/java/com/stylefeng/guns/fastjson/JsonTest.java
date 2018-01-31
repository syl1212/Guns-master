package com.stylefeng.guns.fastjson;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.GunsRestApplication;
import com.stylefeng.guns.rest.common.SimpleObject;
import com.stylefeng.guns.rest.modular.auth.converter.BaseTransferEntity;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

/**
 * json测试
 *
 * @author fengshuonan
 * @date 2017-08-25 16:11
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GunsRestApplication.class)
public class JsonTest {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    public void testMd5(){
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJyYW5kb21LZXkiOiJ4MGpoYm4iLCJzdWIiOiJhZG1pbiIsImV4cCI6MTUxNzk5NTYxNywiaWF0IjoxNTE3MzkwODE3fQ.nFNVhrvZqgHGQf6KmDzzvq-m3hjfi9eFDOIMfwobfW0jWxQQbk_hS7dnkgqxft-KXlve0_R319skrda_B4xjEQ";
        String md5KeyFromToken = jwtTokenUtil.getMd5KeyFromToken(token);


        BaseTransferEntity baseTransferEntity = new BaseTransferEntity();
        SimpleObject simpleObject = new SimpleObject();
        simpleObject.setUser("fsn");

        String json = JSON.toJSONString(simpleObject);
        String object = Base64Utils.encodeToString(json.getBytes());

        baseTransferEntity.setObject(object);

        //md5签名
        String encrypt = MD5Util.encrypt(object + md5KeyFromToken);
        baseTransferEntity.setSign(encrypt);

        System.out.println(JSON.toJSONString(baseTransferEntity));
    }
}
