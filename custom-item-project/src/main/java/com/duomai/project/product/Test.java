package com.duomai.project.product;

import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Test {

    static  Object A1 = new Object();
    static  Object A2 = new Object();

    public static void main(String[] args) throws Exception {




        new Thread(()->{

                try {
                    A1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread()+"A1");

        },"线程1").start();


        new Thread(()->{
            synchronized(A2){
                System.out.println("持有A2");

                try {
                   A2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                synchronized(A1){
                    System.out.println(Thread.currentThread() + "持有A1");
                }


            }

        },"线程2").start();




        new Thread(()->{

            A1.notify();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            A2.notify();
        },"线程3").start();





//        for(int =0;i<1000;i++){
//            AwardUseWayEnum awardUseWayEnum = AwardUseWayEnum.randomType(AwardUseWayEnum.values());
//            if(awardUseWayEnum.equals(AwardUseWayEnum.POOL)){
//                System.out.println("----------------");
//                System.out.println("----------------");
//                System.out.println("----------------");
//                System.out.println("----------------");
//                System.out.println("----------------");
//            }
//
//            System.out.println(awardUseWayEnum.getValue());
//        }



//        Pattern.matches("^[A-Za-z0-9]{15,18}$","11111111111111");
//
//        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "21699045", "20ee7d6400dda1c9670e7f997af2c2c8");
//        TradesSoldGetRequest req = new TradesSoldGetRequest();
//        req.setFields("tid,payment,total_fee,created,status,orders,buyer_rate");
//        req.setBuyerNick("t01k3OzV5DSJfBiChmIa9rZ2Z/HkuUlU16/T/y6FH7lLiM=");
//        req.setType("guarantee_trade,auto_delivery,ec,cod,step,tmall_i18n");
//        req.putOtherTextParam("top_mix_params", "buyer_nick");
//        TradesSoldGetResponse res = client.execute(req, "6202914b8c0b9bb920d5bceg6b00afbf86fba14a2fadc7c407957498");
//        if(res.isSuccess()){
//            List<Trade> list = res.getTrades();
//            for(Trade trade:list){
//                if(trade.getTid().toString().equals("1393007256636743014")){
//                    System.out.println(11111);
//                }
//            }
//        }




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

//        String aaa = "qwqweqweqweqwe";
//        for(int i =0;i<1000;i++){
//            Double ran = Math.random()*1000000;
//            Integer rand = ran.intValue();
//            System.out.println(aaa+rand);
//        }

//        List<SysLuckyDrawRecord> commonBattles = new ArrayList<>();
//        commonBattles.add(new SysLuckyDrawRecord("1004"));
//        commonBattles.add(new SysLuckyDrawRecord("1002"));
//        commonBattles.add(new SysLuckyDrawRecord("1003"));
//        commonBattles.add(new SysLuckyDrawRecord("1004"));
//        commonBattles.add(new SysLuckyDrawRecord("1001"));
//        commonBattles.add(new SysLuckyDrawRecord("1002"));
//        commonBattles.add(new SysLuckyDrawRecord("1003"));
//
//
//        List<SysLuckyDrawRecord> shouldCost = new ArrayList<SysLuckyDrawRecord>();
//        Map<String, List<SysLuckyDrawRecord>> collect = commonBattles.stream()
//                .collect(Collectors.groupingBy(SysLuckyDrawRecord::getAwardId));
//
//        Map<String, List<SysLuckyDrawRecord>> sortMap = new TreeMap<>(String::compareTo);
//        sortMap.putAll(collect);
//
//
//
//        boolean mulValue;
//        while (shouldCost.size() < 3) {
//            mulValue = false;
//            for (String key : sortMap.keySet()) {
//                if (sortMap.get(key).size() > 1) {
//                    mulValue = true;
//                }
//            }
//
//            for (String key : sortMap.keySet()) {
//                if (shouldCost.size() >= 3) {
//                    break;
//                }
//                List<SysLuckyDrawRecord> sysLuckyDrawRecords = sortMap.get(key);
//                if (mulValue && sysLuckyDrawRecords.size() > 1) {
//                    shouldCost.add(sysLuckyDrawRecords.get(0));
//                    sysLuckyDrawRecords.remove(0);
//                } else if (!mulValue) {
//                    shouldCost.add(sysLuckyDrawRecords.get(0));
//                    sysLuckyDrawRecords.remove(0);
//                }
//            }
//        }
//
//        shouldCost.forEach(x -> {
//            System.out.println(x.getAwardId());
//        });

    }

}
