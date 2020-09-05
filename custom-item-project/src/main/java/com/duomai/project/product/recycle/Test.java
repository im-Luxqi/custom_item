package com.duomai.project.product.recycle;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.springframework.util.DigestUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @description
 * @create by 王星齐
 * @date 2020-09-04 14:20
 */



public class Test {

    public static void main(String[] args) {
        String format = "appId=adidas&appSecret=4usEfQ3B5G9TEj*g&endTime=1599926399999&openid=AAHimzDHAJzKaWWJ9papdbPs&startTime=1598976000000&timestamp=1599201130042";
        DigestUtils.md5DigestAsHex(format.getBytes(Consts.UTF_8));
        Map<String,Object> map = new HashMap<>();
        map.put("appId", "a");
        map.put("timestamp", "ccc");
        map.put("openid", "bb");
        map.put("sign", 5);
        map.put("startTime", 5);
        map.put("endTime", 5);
        System.out.println(JSON.toJSONString(sortMapByKey(map)));
    }

    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

}

class MapKeyComparator implements Comparator<String> {
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
