package com.duomai.project.product.adidasmusic.util;

import com.duomai.project.product.general.entity.SysLuckyDrawRecord;

/**
 * @author 多卖
 * @description 随机获取汉字 不常用类
 */
public class CommonHanZiUtil {

    public static char randomGetUnicodeHanZi(){
        return (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
    }

    public static void main(String[] args) {
        for(int i = 0;i<1000;i++) {
            System.out.println(randomGetUnicodeHanZi());
        }
    }

}
