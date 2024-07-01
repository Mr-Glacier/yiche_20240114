package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.DaoFather;
import org.example.EntityErShouChe.Bean_car_ryk_date;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Controller_cheyipai {

    public void Method_downHtml(String url,String parmStr){
        try{
            long timestamp = System.currentTimeMillis();
            System.out.println(timestamp);
            Random random = new Random();
            int fourDigitNumber = 1000 + random.nextInt(9000);
            String timestampStr = String.valueOf(timestamp)+fourDigitNumber+"000";
            System.out.println(timestampStr);
            Thread.sleep(5000);
            // 将数字转换为字符串
            String fourDigitString = String.valueOf(fourDigitNumber);
            //171757323 0969 8551  000
            Connection.Response res =  Jsoup.connect(url).method(Connection.Method.POST)
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Accept-Encoding","gzip, deflate, br, zstd")
                    .header("Accept-Language","zh-CN,zh;q=0.9")
                    .header("App","10")
                    .header("Clienttype", "4")
                    .header("Connection", "keep-alive")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                    .header("Tid", "17177319378798585000")
                    .header("Host","shenyu.cheyipai.com")
                    .header("Referer","https://www.cheyipai.com/")
                    .header("Origin","https://www.cheyipai.com")
//                    .header("Imei", "2500400422")//261342069
                    .header("Version","8.6.5")
                    .header("Cookie", "JSESSIONID=AD0596C61A8CB049D36E687009FD71C6; acw_tc=b65cfd2c17177396994197960e6dd3f4a85b41edde571f6b13e60b55b2e3f0")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .requestBody(parmStr)
                    .execute();
            System.out.println(res.body());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String Method_PostData(String url,String parmStr,String cookie){
        try {
            Document document = Jsoup.connect(url)
                    .header("Content-Type", "application/json")
                    .header("Cookie", cookie)
                    .requestBody(parmStr)
                    .ignoreContentType(true)
                    .post();

            // 打印响应
//            System.out.println(document.body().text());
            Random random = new Random();
            int fourDigitNumber = 1000 + random.nextInt(5000);
            Thread.sleep(fourDigitNumber);
            return document.body().text();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public void Method_AnalysisData(String savePath,String date){
        DaoFather daoFather = new DaoFather(2, 4);
        ReadUntil readUntil = new ReadUntil();
        List<String> fileList = readUntil.getFileNames(savePath);
        for (String fileName : fileList) {
            String filePath = savePath + fileName;
            String data = readUntil.Method_ReadFile(filePath);
            JSONObject jsonObject = JSONObject.parseObject(data).getJSONObject("data");
            JSONArray jsonArray = jsonObject.getJSONArray("auctionGoodsRoundVOList");
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("carInfo");
                String carName = jsonObject1.getString("carName");
                String registDate = jsonObject1.getString("registDate");
                String mileage = jsonObject1.getString("mileage");
                String price = jsonArray.getJSONObject(i).getJSONObject("auctionInfo").getString("priorityOfferW");
                String locationName = jsonObject1.getString("locationName");

                Bean_car_ryk_date bean_car_ryk_date = new Bean_car_ryk_date();
                bean_car_ryk_date.setC_name(carName);
                bean_car_ryk_date.setC_licenseDate(registDate);
                bean_car_ryk_date.setC_mileage(mileage);
                bean_car_ryk_date.setC_price(price);
                bean_car_ryk_date.setC_date(date);
                bean_car_ryk_date.setC_fileName(fileName);
                bean_car_ryk_date.setC_county(locationName);
                bean_car_ryk_date.setC_deatileUrl("-");
                bean_car_ryk_date.setC_source("车易拍");
                daoFather.Method_Insert(bean_car_ryk_date);
            }


        }
    }
    public static void main(String[] args) {
        SaveUntil saveUntil = new SaveUntil();
        System.out.println("车易拍");
        // 网址接口
        String url = "https://shenyu.cheyipai.com/auction/biz/auctionListController/getAuctionCarList.json";

        String jsonData = "{\"lastAuctionId\":\"1470725242541113345\",\"pageIndex\":1,\"pageSize\":10,\"activityCode\":\"\",\"pageType\":3,\"auctionScreenListQuery\":{\"quickConditionInfoVOS\":[]}}";
        String cookie = "JSESSIONID=0CC3C6DEDCB0D99875975A6377BF47D8; acw_tc=b65cfd2c17177396994197960e6dd3f4a85b41edde571f6b13e60b55b2e3f0";

        String savePath = "E:\\ZKZD2024\\二手车ryk\\cheyipai\\";
//
//        Controller_cheyipai controller_cheyipai = new Controller_cheyipai();
//        String data = controller_cheyipai.Method_PostData(url, jsonData,cookie);
//        JSONObject jsonObject = JSONObject.parseObject(data).getJSONObject("data");
//
//        int total = jsonObject.getInteger("auctionInfoCount");
//
//        saveUntil.Method_SaveFile(savePath+"车易拍_1.txt",data);
//        // 计算商和余数
//        int quotient = total / 15;
//        int remainder = total % 15;
//
//        if (remainder != 0){
//            quotient++;
//        }
//        System.out.println("共"+quotient+"页");
//        for (int i = 2; i <= quotient; i++){
//            System.out.println("第"+i+"页");
//            String jsonDataNew = "{\"lastAuctionId\":\"1470725242541113345\",\"pageIndex\":"+i+",\"pageSize\":10,\"activityCode\":\"\",\"pageType\":3,\"auctionScreenListQuery\":{\"quickConditionInfoVOS\":[]}}";
//            String dataNew = controller_cheyipai.Method_PostData(url, jsonDataNew,cookie);
//            JSONObject jsonObjecNew = JSONObject.parseObject(dataNew).getJSONObject("data");
//            JSONArray jsonArrayNew = jsonObjecNew.getJSONArray("auctionGoodsRoundVOList");
//            if (jsonArrayNew.size() != 0){
//                saveUntil.Method_SaveFile(savePath+"车易拍_"+i+".txt",dataNew);
//            }
//        }

        Controller_cheyipai controller_cheyipai = new Controller_cheyipai();
        controller_cheyipai.Method_AnalysisData(savePath,"2024060712");


    }

}
//3ccdc14e 17177325713917963 e30c8760ac5ce6fb99374a5df706405f6af07
//17177319381855182
//17177325713917963


//  1717732570991
//  1717731937879 8585000