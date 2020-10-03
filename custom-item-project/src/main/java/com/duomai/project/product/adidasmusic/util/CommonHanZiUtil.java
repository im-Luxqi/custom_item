package com.duomai.project.product.adidasmusic.util;


/**
 * @author 多卖
 * @description 随机获取汉字 不常用类
 */
public class CommonHanZiUtil {


    //随机获取单个汉字
    public static char randomGetUnicodeHanZi(){
        return (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
    }

    //获取多个汉字并拼装了空格
    public static String getRandomHanZi(int size) {

        size = size <= 0 ? 2 : size;
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < size; i++) {
            stringBuffer.append(randomGetUnicodeHanZi() + " ");
        }
        return stringBuffer.toString();
    }

    //获取多个汉字无空格
    public static String getRandomHanZiNoSpace(int size) {

        size = size <= 0 ? 2 : size;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            stringBuffer.append(randomGetUnicodeHanZi());
        }
        return stringBuffer.toString();
    }


}
