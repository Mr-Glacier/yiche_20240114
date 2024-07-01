package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.DaoFather;
import org.example.EntityErShouChe.Bean_car_ryk;
import org.example.EntityErShouChe.Bean_dongchedi_county;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.URLEncoder;
import java.util.List;
import java.util.Random;

public class Controller_dongchedi {
    private SaveUntil saveUntil = new SaveUntil();
    private ReadUntil readUntil = new ReadUntil();

    public String Method_1_DownFirstHtml_county(String firstUrl) {
        try {
            Connection.Response response = Jsoup.connect(firstUrl).method(Connection.Method.GET)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                    .header("Referer", "https://www.dongchedi.com/usedcar/x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-371100-2-x-x-x-x-x")
                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"")
                    .header("Sec-Ch-Ua-Platform", "\"Windows\"")
                    .cookie("Cookie", "ttwid=1%7CUxb8qZxiwN1S1EHVGc1OGz9r-mm0iBBND98dOWF-Hx4%7C1716309393%7Cf4c3bf7eadf51fb732a73453588123162a792e0b750dc56508803307bf93e643; tt_webid=7371492623708784138; tt_web_version=new; s_v_web_id=verify_lwgmasu2_MIqjTdnE_Ysn8_4aRX_8FDr_gMreI0acLDbm; is_dev=false; is_boe=false; Hm_lvt_3e79ab9e4da287b5752d8048743b95e6=1716309392,1717578629; _gid=GA1.2.583508372.1717578631; city_name=%E6%97%A5%E7%85%A7; Hm_lpvt_3e79ab9e4da287b5752d8048743b95e6=1717578826; _gat_gtag_UA_138671306_1=1; _ga_YB3EWSDTGF=GS1.1.1717578630.2.1.1717578827.58.0.0; _ga=GA1.1.1014883282.1716309394")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .execute();
            System.out.println(response.body());
            System.out.println(response.statusCode());
            if (response.statusCode() == 200) {
                System.out.println("请求成功");
                return response.body();
            } else {
                System.out.println("请求失败");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void Method_2_AnalysisHtmlCounty(String Data) {
        DaoFather daoFather = new DaoFather(2, 1);
        JSONObject jsonObject = JSONObject.parseObject(Data);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("city");

            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                String county = jsonObject2.getString("city_name");
                String letter = jsonObject2.getString("initials");
                Bean_dongchedi_county bean_dongchedi_county = new Bean_dongchedi_county();
                bean_dongchedi_county.setC_county(county);
                bean_dongchedi_county.setC_letter(letter);
                bean_dongchedi_county.setC_downState("否");
                daoFather.Method_Insert(bean_dongchedi_county);
            }

        }
    }


    public void Method_3_Down_4SList(String savePath, String url) {

        DaoFather daoFather = new DaoFather(2, 1);
        for (int i = 0; i < daoFather.Method_Find().size(); i++) {
            Bean_dongchedi_county bean_dongchedi_county = (Bean_dongchedi_county) daoFather.Method_Find().get(i);
            String county = bean_dongchedi_county.getC_county();
            String dealcounty = "";
            try {
                dealcounty = URLEncoder.encode(county, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String letter = bean_dongchedi_county.getC_letter();
            String downState = bean_dongchedi_county.getC_downState();

            int offset = 0;
            if (downState.equals("否")) {
                boolean flag = true;
                System.out.println("开始下载" + county + "========================>" + bean_dongchedi_county.getC_ID());
                for (int j = 1; j < 200; j++) {
                    String finUrl = "";
                    if (j == 1) {
                        finUrl = "__method=window.fetch&data_from=m_station&used_car_entry=wap_page_main-bottom_channel_tag&content_sort_mode=-1&" +
                                "city_name=" + dealcounty + "&limit=10&show_column=buy_car&recall_type=certifys&offset=0&business_type=";
                    } else {
                        System.out.println("第" + j + "页");
                        finUrl = "__method=window.fetch&data_from=m_station&used_car_entry=wap_page_main-bottom_channel_tag&content_sort_mode=-1&" +
                                "city_name=" + dealcounty + "&limit=10&show_column=buy_car&recall_type=certifys&offset=" + offset + "&business_type=";
                        System.out.println(offset + "  <<<<<<<<<<<<<<");
                    }
                    String content = Method_Get(url, finUrl);
                    if (content != null) {
                        saveUntil.Method_SaveFile(savePath + county + "_" + j + ".txt", content);
                        JSONObject jsonObject = JSONObject.parseObject(content).getJSONObject("data");
                        String offsetStr = jsonObject.getString("offset");
                        if (offsetStr == null) {
                            System.out.println("请求失败");
                            flag = false;
                            break;
                        }
                        if (offsetStr.equals("0")) {
                            break;
                        } else {
                            flag = false;
                            offset = Integer.parseInt(offsetStr);
                        }
                    } else {
                        System.out.println("请求失败");
                    }
                }
                if (flag) {
                    daoFather.Method_OnlyChangeState(bean_dongchedi_county.getC_ID());
                    System.out.println(county + "下载完成---->");
                }
            } else {
                System.out.println(county + "已下载");
            }
        }
    }

    public void Method_4_Analysis_4s(String savePath) {
        DaoFather daoFather = new DaoFather(2, 3);
        List<String> fileNames = readUntil.getFileNames(savePath);
        System.out.println(fileNames.size());
        for (String fileName : fileNames) {
            System.out.println("本次解析--> " + fileName);
            String content = readUntil.Method_ReadFile(savePath + fileName);

            // 使用 split 方法分割文件名，并检查分割结果的长度
            String[] partsCounty = fileName.split("_", 2);
            String county = partsCounty.length > 1 ? partsCounty[0] : "Unknown";

            // 解析 JSON 数据
            JSONObject jsonObject = JSONObject.parseObject(content).getJSONObject("data");

            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                if (jsonArray != null && !jsonArray.isEmpty()) {

                    System.out.println(jsonArray.size() + "====>");

                    for (int i = 0; i < jsonArray.size(); i++) {
                        System.out.println(jsonArray.getJSONObject(i).get("type"));
                        // 使用 .equals 方法进行字符串比较
                        if ("10001".equals(jsonArray.getJSONObject(i).getString("type"))) {
//                            System.out.println("======>" + jsonArray.getJSONObject(i));
                            JSONObject cardInfo = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("card_info");

                            if (cardInfo != null) {
                                String versionName = cardInfo.getString("title");
                                String sub_title = cardInfo.getString("sub_title");
                                String price = cardInfo.getString("price");
                                String source = "懂车帝二手车";

                                // 检查 sub_title 是否包含预期的分隔符
                                if (sub_title != null && sub_title.contains("/")) {
                                    String[] parts = sub_title.split("/",2);

//                                    if (parts.length == 2) {
                                        String licenseDate = parts[0].trim();
                                        String mileage = parts[1].trim();

                                        Bean_car_ryk bean_car_ryk = new Bean_car_ryk();
                                        bean_car_ryk.setC_name(versionName);
                                        bean_car_ryk.setC_licenseDate(licenseDate);
                                        bean_car_ryk.setC_price(price);
                                        bean_car_ryk.setC_county(county);
                                        bean_car_ryk.setC_mileage(mileage);
                                        bean_car_ryk.setC_source(source);
                                        bean_car_ryk.setC_deatileUrl("-");
                                        bean_car_ryk.setC_fileName(fileName);
                                        daoFather.Method_Insert(bean_car_ryk);
                                        System.out.println(versionName + "  " + price + "  " + licenseDate + "  " + mileage);
//                                    } else {
//                                        System.err.println("sub_title 格式不正确: " + sub_title);
//                                    }
                                } else {
                                    System.err.println("sub_title 不包含预期的分隔符: " + sub_title);
                                }
                            } else {
                                System.err.println("card_info 为空: " + jsonArray.getJSONObject(i));
                            }
                        }
                    }
                }
            }
        }


//        for (String fileName : fileNames) {
//            System.out.println("本次解析--> "+fileName);
//            String content = readUntil.Method_ReadFile(savePath + fileName);
//
//            String[] partsCounty = fileName.split("_", 2);
//            String county = partsCounty[0];
//
//            JSONObject jsonObject = JSONObject.parseObject(content).getJSONObject("data");
//
//            if (jsonObject != null) {
//                JSONArray jsonArray = jsonObject.getJSONArray("list");
//                if (jsonArray != null && jsonArray.size() > 0) {
//
//                    System.out.println(jsonArray.size()+"====>");
//
//                    for (int i = 0; i < jsonArray.size(); i++) {
//                        System.out.println(jsonArray.getJSONObject(i).get("type"));
//                        if (jsonArray.getJSONObject(i).get("type") == "10001"){
//                            System.out.println("======>"+jsonArray.getJSONObject(i));
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("card_info");
//
//                            String versionName = jsonObject1.getString("title");
//                            String sub_title = jsonObject1.getString("sub_title");
//                            String price = jsonObject1.getString("price");
//                            String source = "懂车帝二手车";
//                            String[] parts = sub_title.split("/");
//
//                            String licenseDate = parts[0];
//                            String mileage = parts[1];
//
//                            Bean_car_ryk bean_car_ryk = new Bean_car_ryk();
//                            bean_car_ryk.setC_name(versionName);
//                            bean_car_ryk.setC_licenseDate(licenseDate);
//                            bean_car_ryk.setC_price(price);
//                            bean_car_ryk.setC_county(county);
//                            bean_car_ryk.setC_mileage(mileage);
//                            bean_car_ryk.setC_source(source);
//                            bean_car_ryk.setC_deatileUrl("-");
//                            daoFather.Method_Insert(bean_car_ryk);
//                            System.out.println(versionName + "  " + price + "  " + licenseDate + "  " + mileage);
//                        }
//                    }
//                }
//            }
//        }
    }

    public void Method_5_Down_HaoCheList(String savePath, String url) {

        DaoFather daoFather = new DaoFather(2, 1);
        for (int i = 0; i < daoFather.Method_Find().size(); i++) {
            Bean_dongchedi_county bean_dongchedi_county = (Bean_dongchedi_county) daoFather.Method_Find().get(i);
            String county = bean_dongchedi_county.getC_county();
            String dealcounty = "";
            try {
                dealcounty = URLEncoder.encode(county, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String letter = bean_dongchedi_county.getC_letter();
            String downState = bean_dongchedi_county.getC_downState();

            int offset = 0;
            if (downState.equals("否")) {
                boolean flag = true;
                System.out.println("开始下载" + county + "========================>" + bean_dongchedi_county.getC_ID());
                for (int j = 1; j < 200; j++) {
                    String finUrl = "";
                    if (j == 1) {
                        System.out.println("第" + 1 + "页------------ No1");
                        finUrl = "__method=window.fetch&data_from=m_station&used_car_entry=wap_page_main-bottom_channel_tag&content_sort_mode=-1&" +
                                "city_name=" + dealcounty + "&limit=10&show_column=buy_car&recall_type=luxury_car&offset=0&business_type=";
                    } else {
                        System.out.println("第" + j + "页");
                        finUrl = "__method=window.fetch&data_from=m_station&used_car_entry=wap_page_main-bottom_channel_tag&content_sort_mode=-1&" +
                                "city_name=" + dealcounty + "&limit=10&show_column=buy_car&recall_type=luxury_car&offset=" + offset + "&business_type=";
                        System.out.println(offset + "  <<<<<<<<<<<<<<");
                    }
                    String content = Method_Get(url, finUrl);
                    if (content != null) {
                        JSONObject jsonObject = JSONObject.parseObject(content).getJSONObject("data");
                        String offsetStr = jsonObject.getString("offset");
                        if (offsetStr == null) {
                            flag = false;
                            System.out.println("请求失败");
                            break;
                        }
                        if (offsetStr.equals("0")) {
                            break;
                        } else {
                            offset = Integer.parseInt(offsetStr);
                            saveUntil.Method_SaveFile(savePath + county + "_" + j + ".txt", content);
                        }
                    } else {
                        System.out.println("请求失败");
                    }
                }
                if (flag) {
                    daoFather.Method_OnlyChangeState(bean_dongchedi_county.getC_ID());
                    System.out.println(county + "下载完成---->");
                }
            } else {
                System.out.println(county + "已下载");
            }
        }
    }
    public String Method_Get(String url, String parmStr) {
        // Url编码 url
        try {
//            String parmStrDeal = URLEncoder.encode(parmStr, "UTF-8");
//            System.out.println("开始请求" + url+parmStr);
            Connection.Response response = Jsoup.connect(url + parmStr)
//                    .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
//                    .header("Accept","*/*")
//                    .header("Accept-Encoding","gzip, deflate, br, zstd")
//                    .header("Accept-Language","zh-CN,zh;q=0.9")
//                    .header("Priority","u=1, i")
//                    .header("Sec-Fetch-Mode", "cors")
//                    .header("Sec-Fetch-Dest","empty")
//                    .header("Sec-Fetch-Site","same-origin")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .execute();

            if (response.statusCode() == 200) {

//                System.out.println(response.body());
                Random random = new Random();
                int randomInt = random.nextInt(3) + 1;
                Thread.sleep(randomInt * 1000);
                return response.body();
            } else {
                System.out.println("请求失败");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
//        String firstUrl = "https://www.dongchedi.com/motor/dealer/m/v1/get_dealer_city_list/";
        Controller_dongchedi controller_dongchedi = new Controller_dongchedi();
//        String  Data = controller_dongchedi.Method_1_DownFirstHtml_county(firstUrl);
//        controller_dongchedi.Method_2_AnalysisHtmlCounty(Data);

        String url = "https://m.dcdapp.com/motor/sh_search/api/home/card_list?";
        String savePath4s = "E:\\ZKZD2024\\二手车ryk\\DCD\\4s\\";
//        controller_dongchedi.Method_3_Down_JingXuanList(savePath4s, url);

        controller_dongchedi.Method_4_Analysis_4s(savePath4s);

        String savePathRecommend = "E:\\ZKZD2024\\二手车ryk\\DCD\\recommend\\";


//        controller_dongchedi.Method_5_Down_HaoCheList(savePathRecommend, url);


    }

}
