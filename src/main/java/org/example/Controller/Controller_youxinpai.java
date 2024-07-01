package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.DaoFather;
import org.example.EntityErShouChe.Bean_car_ryk_date;
import org.example.Until.HelpCreateFile;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Controller_youxinpai {
    // 优信拍


    public String Method_1_DownFirstHtml(String firstUrl, String parmStr) {
        String result = "";
        try {
            Connection.Response res = Jsoup.connect(firstUrl).method(Connection.Method.POST)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Csrftoken", "VCNkjZSB-bx2H6qlFapZTqQrBCebziJPWPO4")
                    .header("Referer", "https://www.youxinpai.com/trade")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .cookie("Cookie", "xxzlclientid=7a923008-bbf8-4b78-b1f2-1717568308111; xxzlxxid=pfmxwtDfh9JN7gAwBs330bDz99xCin6xXlLaiBRy0BNp5uBTsaB9cJPDll/1KrKTlE4J; id58=CocLxWZidCqUywmePIHHAg==; xxzlbbid=pfmbM3wxMDI5M3wxLjcuMXwxNzE3NzMyMzU0ODAzfHBFeitBN3l1bFRZM0lQNFV0Z2lmOTlIRXJLNFNKQy8rd2NrZ1BTaWttR0U9fDBmOTVjZGIwNTg5YzhhMDc3ODViZGM1ZmM3MGRhMDE2XzE3MTc3MjgyOTg5ODBfYmM1N2RkYjQ4NzJlNDY5NWEzZjkxYTAxNTBhODA2MTlfMTg2MDUxODQyOHw4MDE2MTIwOWRlNTEzMDgxNzhkYTMwZDg2ZWM4NmRiNV8xNzE3NzMyMzU0NDIyXzEzOA==; csrfToken_key=n8xuvUE2i9zKPkc-RzV37tyi; csrfToken=Cs4XZ4Qm-UsJKASL7cmudrCn9UsH1mtZp68s; jwt_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiQ3M0WFo0UW0tVXNKS0FTTDdjbXVkckNuOVVzSDFtdFpwNjhzIiwiaWF0IjoxNzE3NzMyMzU4LCJleHAiOjE3MTc3MzQxNTh9.ErJOfVfMAPXoRuSWS7-WjHgX3SoZUsrOZuDwS9wcnRA")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .requestBody(parmStr)
                    .execute();
//            System.out.println(res.body());
            System.out.println(res.statusCode());
            System.out.println(res.body());
            result = res.body();
            Random random = new Random();
            int sleepTime = (random.nextInt(5) + 1) * 1000;
            Thread.sleep(sleepTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public void Method_AnalysisData(String savePath,String date){
        DaoFather daoFather = new DaoFather(2,4);
        ReadUntil readUntil = new ReadUntil();
        List<String> list = readUntil.getFileNames(savePath);
        for (String fileName : list){
            System.out.println(fileName);
            String content = readUntil.Method_ReadFile(savePath+fileName);
            JSONObject jsonObject = JSONObject.parseObject(content).getJSONObject("data").getJSONObject("entities").getJSONObject("immediately");
            JSONArray jsonArray = jsonObject.getJSONArray("auctionListEntity");
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String mileage = jsonObject1.getString("mileage");
                String auctionTitle = jsonObject1.getString("auctionTitle");
                // 找到第一个 '|' 的索引
                int index = auctionTitle.indexOf('|');
                // 如果找到了 '|'，则截取 '|' 后面的子串作为结果，否则保持原始字符串不变
                String result_auctionTitle = index != -1 ? auctionTitle.substring(index + 1).trim() : auctionTitle;
                String pricesStart = jsonObject1.getString("pricesStart");
                String licenseDate = jsonObject1.getString("year")+"年";
                String carCityName = jsonObject1.getString("carCityName");
                Bean_car_ryk_date bean_car_ryk_date = new Bean_car_ryk_date();
                bean_car_ryk_date.setC_county(carCityName);
                bean_car_ryk_date.setC_mileage(mileage);
                bean_car_ryk_date.setC_licenseDate(licenseDate);
                bean_car_ryk_date.setC_name(result_auctionTitle);
                bean_car_ryk_date.setC_price(pricesStart);
                bean_car_ryk_date.setC_deatileUrl("-");
                bean_car_ryk_date.setC_source("优信拍");
                bean_car_ryk_date.setC_fileName(fileName);
                bean_car_ryk_date.setC_date(date);
                daoFather.Method_Insert(bean_car_ryk_date);
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("优信拍");
        Controller_youxinpai controller_youxinpai = new Controller_youxinpai();

        String firstUrl = "https://www.youxinpai.com/trade/getTradeList";

        String savpath = "E:\\ZKZD2024\\二手车ryk\\youxinpai\\TimeTask\\2024060710\\";

        HelpCreateFile.Method_Creat_folder(savpath);
//        String parmStr = "{\"entities\":\"{\\\"req\\\":{\\\"cityIds\\\":[],\\\"serialIds\\\":[],\\\"appearanceGrades\\\":[],\\\"skeletonGrades\\\":[],\\\"interiorGrades\\\":[],\\\"emissionStandards\\\":[],\\\"carPriceLevel\\\":[],\\\"carYearLevel\\\":[],\\\"carGearbox\\\":[],\\\"carOwners\\\":[],\\\"carUseTypes\\\":[],\\\"fuelTypes\\\":[],\\\"conditionPriceType\\\":[],\\\"transferCounts\\\":[],\\\"startPriceType\\\":[],\\\"isNotBubbleCar\\\":false,\\\"isNotBurnCar\\\":false,\\\"isNotSmallReport\\\":false,\\\"orderFields\\\":10},\\\"page\\\":[{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"pc_circle\\\"},{\\\"page\\\":1,\\\"pageSize\\\":15,\\\"pageTab\\\":\\\"immediately\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"delay\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"fixedPrice\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"benz\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"attention\\\"}]}\"}";
//        SaveUntil saveUntil = new SaveUntil();
//        for (int i = 1; i < 1000; i++) {
//            String parmStr = "{\"entities\":\"{\\\"req\\\":{\\\"cityIds\\\":[],\\\"serialIds\\\":[],\\\"appearanceGrades\\\":[],\\\"skeletonGrades\\\":[],\\\"interiorGrades\\\":[],\\\"emissionStandards\\\":[],\\\"carPriceLevel\\\":[],\\\"carYearLevel\\\":[],\\\"carGearbox\\\":[],\\\"carOwners\\\":[],\\\"carUseTypes\\\":[],\\\"fuelTypes\\\":[],\\\"conditionPriceType\\\":[],\\\"transferCounts\\\":[],\\\"startPriceType\\\":[],\\\"isNotBubbleCar\\\":false,\\\"isNotBurnCar\\\":false,\\\"isNotSmallReport\\\":false,\\\"orderFields\\\":10},\\\"page\\\":[{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"pc_circle\\\"},{\\\"page\\\":"+i+",\\\"pageSize\\\":15,\\\"pageTab\\\":\\\"immediately\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"delay\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"fixedPrice\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"benz\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"attention\\\"}]}\"}";
//            String content = controller_youxinpai.Method_1_DownFirstHtml(firstUrl, parmStr);
//            JSONObject jsonObject = JSONObject.parseObject(content).getJSONObject("data").getJSONObject("entities").getJSONObject("immediately");
//            JSONArray jsonArray = jsonObject.getJSONArray("auctionListEntity");
//            if (jsonArray.size()==0){
//                break;
//            }
//            saveUntil.Method_SaveFile(savpath + i + ".txt", content);
//            System.out.println("第" + i + "次下载完成");
//        }
        controller_youxinpai.Method_AnalysisData(savpath,"2024060710");
    }


    /**
     * 配合 定时任务使用
     */
    public void Method_1_GetData() {
        SaveUntil saveUntil = new SaveUntil();

        // 错误和任务执行日志
        String taskLogPath = "E:\\ZKZD2024\\二手车ryk\\youxinpai\\TimeTask\\TimeTaskLog.txt";
        String errorPath = "E:\\ZKZD2024\\二手车ryk\\youxinpai\\TimeTask\\ErrorLog.txt";

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 定义时间格式化模式，精确到小时
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");

        saveUntil.Method_SaveFile_True(taskLogPath, "本次任务 开始时间：---> " + now+"\n");

        // 将当前时间格式化为字符串
        String formattedDateTime = now.format(formatter);


        String url = "https://www.youxinpai.com/trade/getTradeList";

        String savePath = "E:\\ZKZD2024\\二手车ryk\\youxinpai\\TimeTask\\";

        String main_path = savePath + formattedDateTime + "\\";

        // 创建文件夹
        HelpCreateFile.Method_Creat_folder(main_path);
        try{
            Controller_youxinpai controller_youxinpai = new Controller_youxinpai();
            for (int i = 1; i < 500; i++) {
                String parmStr = "{\"entities\":\"{\\\"req\\\":{\\\"cityIds\\\":[],\\\"serialIds\\\":[],\\\"appearanceGrades\\\":[],\\\"skeletonGrades\\\":[],\\\"interiorGrades\\\":[],\\\"emissionStandards\\\":[],\\\"carPriceLevel\\\":[],\\\"carYearLevel\\\":[],\\\"carGearbox\\\":[],\\\"carOwners\\\":[],\\\"carUseTypes\\\":[],\\\"fuelTypes\\\":[],\\\"conditionPriceType\\\":[],\\\"transferCounts\\\":[],\\\"startPriceType\\\":[],\\\"isNotBubbleCar\\\":false,\\\"isNotBurnCar\\\":false,\\\"isNotSmallReport\\\":false,\\\"orderFields\\\":10},\\\"page\\\":[{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"pc_circle\\\"},{\\\"page\\\":" + i + ",\\\"pageSize\\\":15,\\\"pageTab\\\":\\\"immediately\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"delay\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"fixedPrice\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"benz\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"attention\\\"}]}\"}";
                String content = controller_youxinpai.Method_2_PostData(url, parmStr);
                JSONObject jsonObject = JSONObject.parseObject(content).getJSONObject("data").getJSONObject("entities").getJSONObject("immediately");
                JSONArray jsonArray = jsonObject.getJSONArray("auctionListEntity");
                if (jsonArray.size()==0){
                    break;
                }
                saveUntil.Method_SaveFile(main_path + i + ".txt", content);
                System.out.println("第" + i + "次下载完成");
            }
        }catch (Exception e){
            e.printStackTrace();
            saveUntil.Method_SaveFile_True(errorPath, "本次任务 错误信息：---> " + e.getMessage()+"\n"+"==================================================\n");
        }


        LocalDateTime nowend = LocalDateTime.now();
        // 定义时间格式化模式，精确到小时
        saveUntil.Method_SaveFile_True(taskLogPath, "本次任务 结束时间：---> " + nowend+"\n");

    }

    public String Method_2_PostData(String firstUrl, String parmStr) {
        String result = "";
        try {
            long timestamp = System.currentTimeMillis();
            System.out.println("timestamp:-->"+timestamp);
//            Map<String,String> cookie = new HashMap<>();
//            cookie.put("id58", "CocACmZgAzSNXkt2NMCHAg==");
//            cookie.put("jwt_token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoibXRsZ3AwT04tUFhGQnFtc3BlamxybFU5TDd3VnEtcUVORWxJIiwiaWF0IjoxNzE3Njk3ODc2LCJleHAiOjE3MTc2OTk2NzZ9.QSDjlC4BMJmbrHkhPxCZwnoRyMkyopkG9-CdIA_ryao");
//            cookie.put("xxzlclientid", "7a923008-bbf8-4b78-b1f2-1717568308111");
//            //7a923008-bbf8-4b78-b1f2-1717568308111
//            cookie.put("csrfToken","mtlgp0ON-PXFBqmspejlrlU9L7wVq-qENElI");
//            cookie.put("csrfToken_key","ht7CKmRbzMI549mAx3gFRMwb");
//            cookie.put("xxzlbbid","pfmbM3wxMDI5M3wxLjcuMHwxNzE3Njk2MjYzOTIzfHFnMEVqUlZ0WTMrZEtobnk1cXZ1SGFKVnF4TWU5bG1pWUFwU05JWmNPZ1U9fGI3N2Q3Yzg1MjkwMDY1ZTg0MDhjMGIwYmIzMWQ3ZmQ4XzE3MTc2NTgzOTk3NDRfMjk1ODFkNmNkYWY5NGIxNzhhMmVhZGQ2ZmMwM2Y5ODFfMTg2MDUxODQyOHxjODE2M2Q2OGFhMDA3ZmM0N2UwYWVmNjUxNWIwNmIzMl8xNzE3Njk2MjYzNTM1XzEzOA");
//            cookie.put("hasShowZhongCai","");
//            cookie.put("hasShowGongGao1","");

            Connection.Response res = Jsoup.connect(firstUrl).method(Connection.Method.POST)
                    .header("Origin","https://www.youxinpai.com")
                    .header("Referer", "https://www.youxinpai.com/trade")
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Content-Type", "application/json;")
                    .header("Sec-Fetch-Site", "same-origin")
                    .header("Sec-Ch-Ua-Platform","\"Windows\"")
                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Ch-Ua-Mobile","?0")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                    .cookie("Cookie","xxzlclientid=7a923008-bbf8-4b78-b1f2-1717568308111; xxzlxxid=pfmxwtDfh9JN7gAwBs330bDz99xCin6xXlLaiBRy0BNp5uBTsaB9cJPDll/1KrKTlE4J; id58=CocLxWZidCqUywmePIHHAg==; csrfToken_key=n5NyxoiMz393lJvpgzRyFX_T; csrfToken=Y0Lg2jw3-sg-49NOg-PHGbtjibGtRjIxDDFw; jwt_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiWTBMZzJqdzMtc2ctNDlOT2ctUEhHYnRqaWJHdFJqSXhEREZ3IiwiaWF0IjoxNzE3NzMwMzI1LCJleHAiOjE3MTc3MzIxMjV9.-50qrhVAM2zJbAtiqJZVWaj1MR32a3Ttvq0zUtSpv9g; xxzlbbid=pfmbM3wxMDI5M3wxLjcuMHwxNzE3NzMwMzI2MTMzfDJKV0JCR2pHeVNXU3lkcFRucUY1NHVQYlBQazZ4L2NDSDNkRWVoaXNGd2s9fDEyOWYxMTQ3ZTkwMGMwN2E1ZTU0ZjZkODBkZWVmYjc1XzE3MTc3MzAzMjU2MzZfYzNkZGIyYTAyNWZjNGRkMzkwZWVlMzQ4NmE1MmM1YWNfMTg2MDUxODQyOHxmZjQ1ZjBlNmFiNmUzZjQzNWE1YTRmOWE3ODU0NDQzM18xNzE3NzMwMzI1Nzg0XzI1NA==")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .requestBody(parmStr)
                    .execute();
            System.out.println(res.statusCode());
            result = res.body();
            Random random = new Random();
            int sleepTime = (random.nextInt(5) + 1) * 1000;
            System.out.println(res.body());
            Thread.sleep(sleepTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
