package com.duomai.project.product;

import com.duomai.common.util.EncryptUtil;
import com.duomai.common.util.MD5Utils;
import com.duomai.project.product.adidasmusic.util.CommonHanZiUtil;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException {

        //生成sign
//        String a = "{\"method\":\"wx.dz.tools.browse.baby\",\"sysCommodity\":[{\"name\":\"NITE JOGGER EF5402,男女经典运动鞋\",\"numId\":611446977762,\"type\":\"GOODS\",\"img\":\"https://img.alicdn.com/imgextra/i1/446338500/O1CN01okCPJC2Cf3AOCXFUv-446338500.jpg_430x430q90.jpg\",\"price\":1299,\"createTime\":\"2020-10-13 00:00:00\",\"commoditySort\":\"total\"},{\"name\":\"MICROPACER_R1 G27934,男鞋经典运动鞋\",\"numId\":591916480352,\"type\":\"GOODS\",\"img\":\"https://img.alicdn.com/imgextra/i4/6000000000093/O1CN01tByuOx1CYdPNrhOU6_!!6000000000093-0-tbvideo.jpg_600x600.jpg\",\"price\":1799,\"createTime\":\"2020-10-13 00:00:00\",\"commoditySort\":\"total\"},{\"name\":\"PW SOLAR HU NMD,男女经典运动鞋\",\"numId\":579198018067,\"type\":\"GOODS\",\"img\":\"https://img.alicdn.com/imgextra/i2/6000000005917/O1CN01SLElwj1ta26jnhBrc_!!6000000005917-0-tbvideo.jpg_600x600.jpg\",\"price\":1899,\"createTime\":\"2020-10-13 00:00:00\",\"commoditySort\":\"total\"}]}";
        
//        String b = "{\"method\": \"wx.dz.page.load\"}";
//        String c = b.replaceAll(" ","");
//        String zhString = "20ee7d6400dda1c9622699123af2c2c8"+"admjson"+
//                EncryptUtil.adjustURLEncoderForJsEncodeURIComponent(c)
//                +"appkey"+"21699045"+"m"+"wx.dz.page.load"+"timestamp"+"1602640509448"+"20ee7d6400dda1c9622699123af2c2c8";
//        String newString = MD5Utils.getMD5(zhString.toLowerCase().getBytes("UTF-8"));
//        System.out.println(newString);

//        String sss = "丶01DLM4+FrywmJJjX4wgw0DDqkdTy6q/xxESifFGrAquA0=";
//        for(int i = 0;i<1000;i++) {
//            System.out.println(sss.replace('丶', CommonHanZiUtil.randomGetUnicodeHanZi()));
//        }

        String aaa = "qwqweqweqweqwe";
        for(int i =0;i<1000;i++){
            Double ran = Math.random()*1000000;
            Integer rand = ran.intValue();
            System.out.println(aaa+rand);
        }

    }

}
