package org.example.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.DaoFather;
import org.example.Dao.Dao_version;
import org.example.Entity.*;
import org.example.Until.MD5Until;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller_yiche {
    private SaveUntil saveUntil = new SaveUntil();
    private ReadUntil readUntil = new ReadUntil();

    public String Method_DownHTML(String url) {
        String connent = "ERROR";
        try {
            Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            Thread.sleep(550);
            return document.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connent;
    }

    public String Method_RequestAPI(String main_Url, String param) {
        String resultJson = "Error";
        String timestamp = String.valueOf(System.currentTimeMillis());
//        String param = "{\"carIds\":\"" + versionID + "\",\"cityId\":\"201\"}";

        String o = "19DDD1FBDFF065D3A4DA777D2D7A81EC";
        String s = "cid=" + 508 + "&param=" + param + o + timestamp;
        String md5_Str = MD5Until.Method_Make_MD5(s);
        //System.out.println(md5_Str);
        String Cookie = "CIGUID=849ec451-0627-4ee7-8139-7d0a7233d10a; auto_id=6618b3b2d19037859fee9321a2165348; UserGuid=849ec451-0627-4ee7-8139-7d0a7233d10a; CIGDCID=1f1c18bfd1a13ef3a7c0b2edee9ef3ca; G_CIGDCID=1f1c18bfd1a13ef3a7c0b2edee9ef3ca; selectcity=110100; selectcityid=201; selectcityName=%E5%8C%97%E4%BA%AC; Hm_lvt_610fee5a506c80c9e1a46aa9a2de2e44=1702474832,1703750079; isWebP=true; locatecity=110100; bitauto_ipregion=101.27.236.186%3A%E6%B2%B3%E5%8C%97%E7%9C%81%E4%BF%9D%E5%AE%9A%E5%B8%82%3B201%2C%E5%8C%97%E4%BA%AC%2Cbeijing; yiche_did=5e58467469f6b2c8732f3492175f2a13_._1000_._0_._847319_._905548_._W2311281141108493357; csids=8014_2556_2855_5476_10188_6435_6209_2573_3750_5786; Hm_lpvt_610fee5a506c80c9e1a46aa9a2de2e44=1703842561";
        try {
            String param_url = URLEncoder.encode(param, "UTF-8");
            String main_url = main_Url + "cid=508&param=" + param_url;
//            System.out.println(main_url);
            Connection.Response res = Jsoup.connect(main_url).method(Connection.Method.GET)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("X-City-Id", "201")
                    .header("X-Ip-Address", "101.27.236.186")
                    .header("X-Platform", "pc")
                    .header("X-Sign", md5_Str)
                    .header("X-User-Guid", "849ec451-0627-4ee7-8139-7d0a7233d10a")
                    .header("Cookie", Cookie)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("Cid", "508")
                    .header("Sec-Ch-Ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Ch-Ua-Platform", "\"Windows\"")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Site", "same-site")
                    .header("X-Timestamp", timestamp)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true).execute();
            resultJson = res.body();
            Thread.sleep(480);
//            System.out.println(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

//https://mapi.yiche.com/web_api/car_model_api/api/v1/master/get_master_list?cid=508&param=%7B%7D
    public void Method_1_RequestYiche_Brand(String savePath, String main_url, String fileName) {
        try {
            String content = Method_RequestAPI(main_url,"{}");
            if (!content.equals("Error")){
                saveUntil.Method_SaveFile(savePath + fileName,content);
                System.out.println("Method_1------>下载完成");
            }
//            Document document = Jsoup.parse(new URL(main_url).openStream(), "UTF-8", main_url);
//            System.out.println(document);
//            saveUntil.Method_SaveFile(savePath + fileName, document.toString());
//            System.out.println("Method_1------>下载完成");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Method_2_Analysis_Yiche_Brand(String path, String fileName) {
        DaoFather dao_brand = new DaoFather(0, 0);

        String content = readUntil.Method_ReadFile(path + fileName);
//        System.out.println(content);

        JSONObject jsonObject = JSONObject.parseObject(content);
        JSONArray BrandList = jsonObject.getJSONArray("data");

        for (int i = 0; i < BrandList.size(); i++) {
            JSONObject oneBrand = BrandList.getJSONObject(i);
            Bean_brand_json bean_brand = new Bean_brand_json();
            bean_brand.setC_brandList(oneBrand.getString("brandList"));
            bean_brand.setC_advertFlag(oneBrand.getString("advertFlag"));
            bean_brand.setC_advertInfo(oneBrand.getString("advertInfo"));
            bean_brand.setC_brand_id(oneBrand.getString("id"));
            bean_brand.setC_name(oneBrand.getString("name"));
            bean_brand.setC_logoUrl(oneBrand.getString("logoUrl"));
            bean_brand.setC_logoUrlWp(oneBrand.getString("logoUrlWp"));
            bean_brand.setC_initial(oneBrand.getString("initial"));
            bean_brand.setC_logoStory(oneBrand.getString("logoStory"));
            bean_brand.setC_allSpell(oneBrand.getString("allSpell"));
            bean_brand.setC_saleStatus(oneBrand.getString("saleStatus"));
            bean_brand.setC_yiCheHuiTag(oneBrand.getString("yiCheHuiTag"));
            bean_brand.setC_photoCount(oneBrand.getString("photoCount"));
            bean_brand.setC_DownState("否");
            bean_brand.setC_DownTime("--");
            dao_brand.MethodInsert(bean_brand);
        }
//        Document document = Jsoup.parse(content);
//
//        Elements Item = document.select(".brand-list");
//
//        Elements Item_zimu = Item.select(".brand-list-item");
//
//        for (int i = 0; i < Item_zimu.size(); i++) {
//            Element Item_letter = Item_zimu.get(i).selectFirst(".item-letter");
//            String Brand_letter = Item_letter.text();
//
//            Elements Item_Brands = Item_zimu.get(i).select(".item-brand");
//            for (int j = 0; j < Item_Brands.size(); j++) {
//                Element Item_One_Brand = Item_Brands.get(j);
//
//                String C_brand_id = Item_One_Brand.attr("data-id");
//                String C_brand_name = Item_One_Brand.attr("data-name");
//                String brand_url = "https://car.yiche.com" + Item_One_Brand.select("a").attr("href");
//                String brand_pictureurl = "https:" + Item_One_Brand.select(".brand-icon.lazyload").attr("data-original");
//                Bean_brand bean_brand = new Bean_brand();
//                bean_brand.setC_brand_id(C_brand_id);
//                bean_brand.setC_brand_name(C_brand_name);
//                bean_brand.setC_brand_url(brand_url);
//                bean_brand.setC_brand_picurl(brand_pictureurl);
//                bean_brand.setC_letter(Brand_letter);
//                bean_brand.setC_DownState("否");
//                bean_brand.setC_DownTime("--");
//                bean_brand.setC_备注("");
//                bean_brand.setC_备注时间("");
//                dao_brand.Method_Insert(bean_brand);
//            }
//        }
        System.out.println("Method_2 ---->品牌完成入库");
    }

    public void Method_3_Down_FactoryANDModel(String savePath, String main_url, String fileName) {
        DaoFather dao_brand = new DaoFather(0, 0);

        ArrayList<Object> BeanList = dao_brand.Method_Find();

        for (Object bean : BeanList) {
            Bean_brand_json bean_brand = (Bean_brand_json) bean;
            int C_ID = bean_brand.getC_ID();
            String brand_id = bean_brand.getC_brand_id();
            String DownState = bean_brand.getC_DownState();
            if (DownState.equals("否")) {
                String param = "{\"masterId\":\"" + brand_id + "\"}";
                String connect = Method_RequestAPI(main_url, param);
                if (!connect.equals("Error")) {
                    String status = JSON.parseObject(connect).getString("status");
                    if (status.equals("1")) {
                        saveUntil.Method_SaveFile(savePath + brand_id + fileName, connect);
                        dao_brand.Method_ChangeState(C_ID);
                        System.out.println(C_ID);
                    }
                }
            }
        }
    }


    public void Method_4_Analysis_models(String savePath, String fileName) {
        DaoFather dao_brand = new DaoFather(0, 0);

        DaoFather dao_model = new DaoFather(0, 1);


        ArrayList<Object> BeanList = dao_brand.Method_Find();

        for (Object bean : BeanList) {
            Bean_brand_json bean_brand = (Bean_brand_json) bean;
            String brand_id = bean_brand.getC_brand_id();
            String brand_name = bean_brand.getC_name();
            String connect = readUntil.Method_ReadFile(savePath + brand_id + fileName);
            System.out.println(brand_id);
            JSONArray data = JSON.parseObject(connect).getJSONArray("data");
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    String factory_id = jsonObject.getString("id");
                    String factory_name = jsonObject.getString("name");
                    JSONArray modelList = jsonObject.getJSONArray("serialList");
                    if (modelList != null) {
                        for (int j = 0; j < modelList.size(); j++) {
                            JSONObject one_model = modelList.getJSONObject(j);
                            String C_advertFlag = one_model.getString("advertFlag");
                            String C_advertInfo = one_model.getString("advertInfo");
                            String C_id = one_model.getString("id");
//                            System.out.println(C_id);
                            String C_name = one_model.getString("name");
                            String C_subsidizedReferPrice = one_model.getString("subsidizedReferPrice");
                            String C_referPrice = one_model.getString("referPrice");
                            String C_allSpell = one_model.getString("allSpell");
                            String C_usedCarPrice = one_model.getString("usedCarPrice");
                            String C_desc = one_model.getString("desc");
                            String C_localDealPrice = one_model.getString("localDealPrice");
                            String C_minDealPriceCarId = one_model.getString("minDealPriceCarId");
                            String C_minDealPriceCarStatus = one_model.getString("minDealPriceCarStatus");
                            String C_entireDealPrice = one_model.getString("entireDealPrice");
                            String C_cityReferencePrice = one_model.getString("cityReferencePrice");
                            String C_subscribeStatus = one_model.getString("subscribeStatus");
                            String C_dropPrice = one_model.getString("dropPrice");
                            String C_maintainValue = one_model.getString("maintainValue");
                            String C_saleStatus = one_model.getString("saleStatus");
                            String C_isNewEnergy = one_model.getString("isNewEnergy");
                            String C_brandId = one_model.getString("brandId");
                            String C_brandName = one_model.getString("brandName");
                            String C_serialFirstLevel = one_model.getString("serialFirstLevel");
                            String C_serialSecondLevel = one_model.getString("serialSecondLevel");
                            String C_exteriorPhotoVo = one_model.getString("exteriorPhotoVo");
                            String C_frontRowPhotoVo = one_model.getString("frontRowPhotoVo");
                            String C_backRowPhotoVo = one_model.getString("backRowPhotoVo");
                            String C_officialPhotoVo = one_model.getString("officialPhotoVo");
                            String C_autoShowPhotoVo = one_model.getString("autoShowPhotoVo");
                            String C_officialPhotoVoList = one_model.getString("officialPhotoVoList");
                            String C_autoShowPhotoVoList = one_model.getString("autoShowPhotoVoList");
                            String C_photoCount = one_model.getString("photoCount");
                            String C_pictureType = one_model.getString("pictureType");
                            String C_photoId = one_model.getString("photoId");
                            String C_videoInfo = one_model.getString("videoInfo");
                            String C_loanVo = one_model.getString("loanVo");
                            String C_forumSpell = one_model.getString("forumSpell");
                            String C_forumId = one_model.getString("forumId");
                            String C_newEnergyCarList = one_model.getString("newEnergyCarList");
                            String C_businessCardList = one_model.getString("businessCardList");
                            String C_carNum = one_model.getString("carNum");
                            String C_carIdList = one_model.getString("carIdList");
                            String C_modelTagsStatic = one_model.getString("modelTagsStatic");
                            String C_imageUrl = one_model.getString("imageUrl");
                            String C_imageUrlWp = one_model.getString("imageUrlWp");
                            String C_carMarket = one_model.getString("carMarket");
                            String C_electricRechargeMileage = one_model.getString("electricRechargeMileage");
                            String C_mouthDay = one_model.getString("mouthDay");
                            String C_yiCheHuiTag = one_model.getString("yiCheHuiTag");
                            String C_masterId = one_model.getString("masterId");
                            String C_masterName = one_model.getString("masterName");
                            String C_masterAllSpell = one_model.getString("masterAllSpell");
                            String C_logoUrl = one_model.getString("logoUrl");
                            String C_logoUrlWp = one_model.getString("logoUrlWp");
                            String C_yiCheHuiFlashSale = one_model.getString("yiCheHuiFlashSale");
                            String C_clueControlType = one_model.getString("clueControlType");
                            String C_filterMasterType = one_model.getString("filterMasterType");
                            String C_marketDate = one_model.getString("marketDate");
                            String C_vrPhotoCarId = one_model.getString("vrPhotoCarId");
                            String C_vrAlbumId = one_model.getString("vrAlbumId");
                            String C_wxWorkChatFlag = one_model.getString("wxWorkChatFlag");
                            String C_tag = one_model.getString("tag");
                            String C_labels = one_model.getString("labels");
                            String C_modelTagsDynamicLinkeds = one_model.getString("modelTagsDynamicLinkeds");
                            String C_vrInfo = one_model.getString("vrInfo");
                            String C_evaluationInfo = one_model.getString("evaluationInfo");
                            String C_rankInfo = one_model.getString("rankInfo");
                            String C_dealFinalPrice = one_model.getString("dealFinalPrice");
                            String C_videoInstructionCount = one_model.getString("videoInstructionCount");

                            Bean_model bean_model = new Bean_model();
                            System.out.println(brand_id);
                            bean_model.setC_factory_id(factory_id);
                            bean_model.setC_factory_name(factory_name);
                            bean_model.setC_advertFlag(C_advertFlag);
                            bean_model.setC_advertInfo(C_advertInfo);
                            bean_model.setC_model_id(C_id);
                            bean_model.setC_name(C_name);
                            bean_model.setC_subsidizedReferPrice(C_subsidizedReferPrice);
                            bean_model.setC_referPrice(C_referPrice);
                            bean_model.setC_allSpell(C_allSpell);
                            bean_model.setC_usedCarPrice(C_usedCarPrice);
                            bean_model.setC_desc(C_desc);
                            bean_model.setC_localDealPrice(C_localDealPrice);
                            bean_model.setC_minDealPriceCarId(C_minDealPriceCarId);
                            bean_model.setC_minDealPriceCarStatus(C_minDealPriceCarStatus);
                            bean_model.setC_entireDealPrice(C_entireDealPrice);
                            bean_model.setC_cityReferencePrice(C_cityReferencePrice);
                            bean_model.setC_subscribeStatus(C_subscribeStatus);
                            bean_model.setC_dropPrice(C_dropPrice);
                            bean_model.setC_maintainValue(C_maintainValue);
                            bean_model.setC_saleStatus(C_saleStatus);
                            bean_model.setC_isNewEnergy(C_isNewEnergy);
                            bean_model.setC_brandId(brand_id);
                            bean_model.setC_brandName(brand_name);
                            bean_model.setC_serialFirstLevel(C_serialFirstLevel);
                            bean_model.setC_serialSecondLevel(C_serialSecondLevel);
                            bean_model.setC_exteriorPhotoVo(C_exteriorPhotoVo);
                            bean_model.setC_frontRowPhotoVo(C_frontRowPhotoVo);
                            bean_model.setC_backRowPhotoVo(C_backRowPhotoVo);
                            bean_model.setC_officialPhotoVo(C_officialPhotoVo);
                            bean_model.setC_autoShowPhotoVo(C_autoShowPhotoVo);
                            bean_model.setC_officialPhotoVoList(C_officialPhotoVoList);
                            bean_model.setC_autoShowPhotoVoList(C_autoShowPhotoVoList);
                            bean_model.setC_photoCount(C_photoCount);
                            bean_model.setC_pictureType(C_pictureType);
                            bean_model.setC_photoId(C_photoId);
                            bean_model.setC_videoInfo(C_videoInfo);
                            bean_model.setC_loanVo(C_loanVo);
                            bean_model.setC_forumSpell(C_forumSpell);
                            bean_model.setC_forumId(C_forumId);
                            bean_model.setC_newEnergyCarList(C_newEnergyCarList);
                            bean_model.setC_businessCardList(C_businessCardList);
                            bean_model.setC_carNum(C_carNum);
                            bean_model.setC_carIdList(C_carIdList);
                            bean_model.setC_modelTagsStatic(C_modelTagsStatic);
                            bean_model.setC_imageUrl(C_imageUrl);
                            bean_model.setC_imageUrlWp(C_imageUrlWp);
                            bean_model.setC_carMarket(C_carMarket);
                            bean_model.setC_electricRechargeMileage(C_electricRechargeMileage);
                            bean_model.setC_mouthDay(C_mouthDay);
                            bean_model.setC_yiCheHuiTag(C_yiCheHuiTag);
                            bean_model.setC_masterId(C_masterId);
                            bean_model.setC_masterName(C_masterName);
                            bean_model.setC_masterAllSpell(C_masterAllSpell);
                            bean_model.setC_logoUrl(C_logoUrl);
                            bean_model.setC_logoUrlWp(C_logoUrlWp);
                            bean_model.setC_yiCheHuiFlashSale(C_yiCheHuiFlashSale);
                            bean_model.setC_clueControlType(C_clueControlType);
                            bean_model.setC_filterMasterType(C_filterMasterType);
                            bean_model.setC_marketDate(C_marketDate);
                            bean_model.setC_vrPhotoCarId(C_vrPhotoCarId);
                            bean_model.setC_vrAlbumId(C_vrAlbumId);
                            bean_model.setC_wxWorkChatFlag(C_wxWorkChatFlag);
                            bean_model.setC_tag(C_tag);
                            bean_model.setC_labels(C_labels);
                            bean_model.setC_modelTagsDynamicLinkeds(C_modelTagsDynamicLinkeds);
                            bean_model.setC_vrInfo(C_vrInfo);
                            bean_model.setC_evaluationInfo(C_evaluationInfo);
                            bean_model.setC_rankInfo(C_rankInfo);
                            bean_model.setC_dealFinalPrice(C_dealFinalPrice);
                            bean_model.setC_videoInstructionCount(C_videoInstructionCount);
                            bean_model.setC_DownState("否");
                            bean_model.setC_DownTime("--");
                            dao_model.MethodInsert(bean_model);
                        }
                    }
                }
            }
        }
    }

    public void Method_5_Down_version(String savePath, String main_url, String fileName) {
        DaoFather dao_model = new DaoFather(0, 1);
        ArrayList<Object> BeanList = dao_model.Method_Find();

        for (Object bean : BeanList) {
            Bean_model bean_model = (Bean_model) bean;
            String model_id = bean_model.getC_model_id();
            String DownState = bean_model.getC_DownState();
            int C_ID = bean_model.getC_ID();

            if (DownState.equals("否")) {
                String param = "{\"serialId\":\"" + model_id + "\"}";
                String content = Method_RequestAPI(main_url, param);
                if (!content.equals("Error")) {
                    System.out.println(C_ID);
//                    System.out.println(content);
                    String status = JSON.parseObject(content).getString("status");
                    if (status.equals("15501")) {
                        saveUntil.Method_SaveFile(savePath + "Error_" + model_id + fileName, content);
                        dao_model.Method_ChangeState2(C_ID);
                    }
                    if (status.equals("1")) {
                        saveUntil.Method_SaveFile(savePath + model_id + fileName, content);
                        dao_model.Method_ChangeState(C_ID);
                        System.out.println("成功保存数据 :" + C_ID);
                    } else {
                        System.out.println(content);
                    }
                }
            }
        }
    }

    public void Method_6_Analysis_version(String savePath, String fileName) {
        DaoFather dao_model = new DaoFather(0, 1);
        DaoFather dao_version = new DaoFather(0, 2);
        ArrayList<Object> BeanList = dao_model.Method_Find();

        for (Object bean : BeanList) {
            Bean_model bean_model = (Bean_model) bean;
            String model_id = bean_model.getC_model_id();
            String DownState = bean_model.getC_DownState();
            if (DownState.equals("是")) {
                String content = readUntil.Method_ReadFile(savePath + model_id + fileName);
                System.out.println(model_id);
                JSONObject jsonObject = JSON.parseObject(content).getJSONObject("data");
                JSONArray notSaleCarList = jsonObject.getJSONArray("notSaleCarList");
                if (notSaleCarList != null) {
                    for (int i = 0; i < notSaleCarList.size(); i++) {
                        JSONObject Item1_notSale = notSaleCarList.getJSONObject(i);
                        String year_carList = Item1_notSale.getString("year");
                        String saleStatusList = Item1_notSale.getString("saleStatusList");

                        JSONArray powerList_notSale = Item1_notSale.getJSONArray("powerList");
                        if (powerList_notSale.size() != 0) {
                            for (int j = 0; j < powerList_notSale.size(); j++) {
                                JSONObject powerList_notSale_item = powerList_notSale.getJSONObject(j);
                                String powerName = powerList_notSale_item.getString("powerName");
                                JSONArray carList = powerList_notSale_item.getJSONArray("carList");
                                for (int k = 0; k < carList.size(); k++) {
                                    JSONObject carList_item = carList.getJSONObject(k);
                                    String C_id = carList_item.getString("id");
                                    String C_name = carList_item.getString("name");
                                    String C_year = carList_item.getString("year");
                                    String C_saleStatus = carList_item.getString("saleStatus");
                                    String C_localDealPrice = carList_item.getString("localDealPrice");
                                    String C_locaLatestDealPrice = carList_item.getString("locaLatestDealPrice");
                                    String C_imageUrl = carList_item.getString("imageUrl");
                                    String C_entireDealPrice = carList_item.getString("entireDealPrice");
                                    String C_dealFinalPriceInfo = carList_item.getString("dealFinalPriceInfo");
                                    String C_serialId = carList_item.getString("serialId");
                                    String C_serialName = carList_item.getString("serialName");
                                    String C_serialSecondLevel = carList_item.getString("serialSecondLevel");
                                    String C_usedCarPrice = carList_item.getString("usedCarPrice");
                                    String C_dropPrice = carList_item.getString("dropPrice");
                                    String C_dealerDropAfterPrice = carList_item.getString("dealerDropAfterPrice");
                                    String C_maintainValue = carList_item.getString("maintainValue");
                                    String C_cityReferencePrice = carList_item.getString("cityReferencePrice");
                                    String C_subscribeStatus = carList_item.getString("subscribeStatus");
                                    String C_referPrice = carList_item.getString("referPrice");
                                    String C_discountPrice = carList_item.getString("discountPrice");
                                    String C_subsidizedReferPrice = carList_item.getString("subsidizedReferPrice");
                                    String C_greenStandards = carList_item.getString("greenStandards");
                                    String C_haveParam = carList_item.getString("haveParam");
                                    String C_pvPercent = carList_item.getString("pvPercent");
                                    String C_hasImageFlag = carList_item.getString("hasImageFlag");
                                    String C_marketDate = carList_item.getString("marketDate");
                                    String C_params = carList_item.getString("params");
                                    String C_photoInfo = carList_item.getString("photoInfo");
                                    String C_yiCheHuiTag = carList_item.getString("yiCheHuiTag");
                                    String C_loanVo = carList_item.getString("loanVo");
                                    String C_businessCardList = carList_item.getString("businessCardList");
                                    String C_oilFuelTypeInt = carList_item.getString("oilFuelTypeInt");
                                    String C_fuelUnitType = carList_item.getString("fuelUnitType");
                                    String C_electricRechargeMileage = carList_item.getString("electricRechargeMileage");
                                    String C_oilWear = carList_item.getString("oilWear");
                                    String C_masterId = carList_item.getString("masterId");
                                    String C_masterName = carList_item.getString("masterName");
                                    String C_logoUrl = carList_item.getString("logoUrl");
                                    String C_logoUrlWp = carList_item.getString("logoUrlWp");
                                    String C_masterAllSpell = carList_item.getString("masterAllSpell");
                                    String C_minDealPrice = carList_item.getString("minDealPrice");
                                    String C_invoiceCount = carList_item.getString("invoiceCount");
                                    String C_brandId = carList_item.getString("brandId");
                                    String C_brandName = carList_item.getString("brandName");
                                    String C_imageUrlWp = carList_item.getString("imageUrlWp");
                                    String C_tranAndGearNum = carList_item.getString("tranAndGearNum");


                                    JSONArray tagList = carList_item.getJSONArray("tagList");

                                    String C_styleType = "无";
                                    String C_styleType_value = "无";
                                    if (tagList.size() != 0) {
                                        C_styleType = tagList.getJSONObject(0).getString("styleType");
                                        C_styleType_value = tagList.getJSONObject(0).getString("value");
                                    }


                                    Bean_version bean_version = new Bean_version();

                                    bean_version.setC_model_id(model_id);
                                    bean_version.setC_sourceList("notSaleCarList");
                                    bean_version.setC_year_carList(year_carList);
                                    bean_version.setC_saleStatusList(saleStatusList);
                                    bean_version.setC_powerName(powerName);
                                    bean_version.setC_DownState("否");

                                    bean_version.setC_version_id(C_id);
                                    bean_version.setC_name(C_name);
                                    bean_version.setC_year(C_year);
                                    bean_version.setC_saleStatus(C_saleStatus);
                                    bean_version.setC_localDealPrice(C_localDealPrice);
                                    bean_version.setC_locaLatestDealPrice(C_locaLatestDealPrice);
                                    bean_version.setC_imageUrl(C_imageUrl);
                                    bean_version.setC_entireDealPrice(C_entireDealPrice);
                                    bean_version.setC_dealFinalPriceInfo(C_dealFinalPriceInfo);
                                    bean_version.setC_serialId(C_serialId);
                                    bean_version.setC_serialName(C_serialName);
                                    bean_version.setC_serialSecondLevel(C_serialSecondLevel);
                                    bean_version.setC_usedCarPrice(C_usedCarPrice);
                                    bean_version.setC_dropPrice(C_dropPrice);
                                    bean_version.setC_dealerDropAfterPrice(C_dealerDropAfterPrice);
                                    bean_version.setC_maintainValue(C_maintainValue);
                                    bean_version.setC_cityReferencePrice(C_cityReferencePrice);
                                    bean_version.setC_subscribeStatus(C_subscribeStatus);
                                    bean_version.setC_referPrice(C_referPrice);
                                    bean_version.setC_discountPrice(C_discountPrice);
                                    bean_version.setC_subsidizedReferPrice(C_subsidizedReferPrice);
                                    bean_version.setC_greenStandards(C_greenStandards);
                                    bean_version.setC_haveParam(C_haveParam);
                                    bean_version.setC_pvPercent(C_pvPercent);
                                    bean_version.setC_hasImageFlag(C_hasImageFlag);
                                    bean_version.setC_marketDate(C_marketDate);
                                    bean_version.setC_params(C_params);
                                    bean_version.setC_photoInfo(C_photoInfo);
                                    bean_version.setC_yiCheHuiTag(C_yiCheHuiTag);
                                    bean_version.setC_loanVo(C_loanVo);
                                    bean_version.setC_businessCardList(C_businessCardList);
                                    bean_version.setC_oilFuelTypeInt(C_oilFuelTypeInt);
                                    bean_version.setC_fuelUnitType(C_fuelUnitType);
                                    bean_version.setC_electricRechargeMileage(C_electricRechargeMileage);
                                    bean_version.setC_oilWear(C_oilWear);
                                    bean_version.setC_masterId(C_masterId);
                                    bean_version.setC_masterName(C_masterName);
                                    bean_version.setC_logoUrl(C_logoUrl);
                                    bean_version.setC_logoUrlWp(C_logoUrlWp);
                                    bean_version.setC_masterAllSpell(C_masterAllSpell);
                                    bean_version.setC_minDealPrice(C_minDealPrice);
                                    bean_version.setC_invoiceCount(C_invoiceCount);
                                    bean_version.setC_brandId(C_brandId);
                                    bean_version.setC_brandName(C_brandName);
                                    bean_version.setC_imageUrlWp(C_imageUrlWp);
                                    bean_version.setC_tranAndGearNum(C_tranAndGearNum);
                                    bean_version.setC_styleType(C_styleType);
                                    bean_version.setC_styleType_value(C_styleType_value);
                                    dao_version.MethodInsert(bean_version);
                                }
                            }
                        }
                    }
                }
                JSONArray waitSaleCarList = jsonObject.getJSONArray("waitSaleCarList");
                if (waitSaleCarList != null) {
                    for (int i = 0; i < waitSaleCarList.size(); i++) {
                        JSONObject Item1_notSale = waitSaleCarList.getJSONObject(i);
                        String year_carList = Item1_notSale.getString("year");
                        String saleStatusList = Item1_notSale.getString("saleStatusList");

                        JSONArray powerList_notSale = Item1_notSale.getJSONArray("powerList");
                        if (powerList_notSale.size() != 0) {
                            for (int j = 0; j < powerList_notSale.size(); j++) {
                                JSONObject powerList_notSale_item = powerList_notSale.getJSONObject(j);
                                String powerName = powerList_notSale_item.getString("powerName");
                                JSONArray carList = powerList_notSale_item.getJSONArray("carList");
                                for (int k = 0; k < carList.size(); k++) {
                                    JSONObject carList_item = carList.getJSONObject(k);
                                    String C_id = carList_item.getString("id");
                                    String C_name = carList_item.getString("name");
                                    String C_year = carList_item.getString("year");
                                    String C_saleStatus = carList_item.getString("saleStatus");
                                    String C_localDealPrice = carList_item.getString("localDealPrice");
                                    String C_locaLatestDealPrice = carList_item.getString("locaLatestDealPrice");
                                    String C_imageUrl = carList_item.getString("imageUrl");
                                    String C_entireDealPrice = carList_item.getString("entireDealPrice");
                                    String C_dealFinalPriceInfo = carList_item.getString("dealFinalPriceInfo");
                                    String C_serialId = carList_item.getString("serialId");
                                    String C_serialName = carList_item.getString("serialName");
                                    String C_serialSecondLevel = carList_item.getString("serialSecondLevel");
                                    String C_usedCarPrice = carList_item.getString("usedCarPrice");
                                    String C_dropPrice = carList_item.getString("dropPrice");
                                    String C_dealerDropAfterPrice = carList_item.getString("dealerDropAfterPrice");
                                    String C_maintainValue = carList_item.getString("maintainValue");
                                    String C_cityReferencePrice = carList_item.getString("cityReferencePrice");
                                    String C_subscribeStatus = carList_item.getString("subscribeStatus");
                                    String C_referPrice = carList_item.getString("referPrice");
                                    String C_discountPrice = carList_item.getString("discountPrice");
                                    String C_subsidizedReferPrice = carList_item.getString("subsidizedReferPrice");
                                    String C_greenStandards = carList_item.getString("greenStandards");
                                    String C_haveParam = carList_item.getString("haveParam");
                                    String C_pvPercent = carList_item.getString("pvPercent");
                                    String C_hasImageFlag = carList_item.getString("hasImageFlag");
                                    String C_marketDate = carList_item.getString("marketDate");
                                    String C_params = carList_item.getString("params");
                                    String C_photoInfo = carList_item.getString("photoInfo");
                                    String C_yiCheHuiTag = carList_item.getString("yiCheHuiTag");
                                    String C_loanVo = carList_item.getString("loanVo");
                                    String C_businessCardList = carList_item.getString("businessCardList");
                                    String C_oilFuelTypeInt = carList_item.getString("oilFuelTypeInt");
                                    String C_fuelUnitType = carList_item.getString("fuelUnitType");
                                    String C_electricRechargeMileage = carList_item.getString("electricRechargeMileage");
                                    String C_oilWear = carList_item.getString("oilWear");
                                    String C_masterId = carList_item.getString("masterId");
                                    String C_masterName = carList_item.getString("masterName");
                                    String C_logoUrl = carList_item.getString("logoUrl");
                                    String C_logoUrlWp = carList_item.getString("logoUrlWp");
                                    String C_masterAllSpell = carList_item.getString("masterAllSpell");
                                    String C_minDealPrice = carList_item.getString("minDealPrice");
                                    String C_invoiceCount = carList_item.getString("invoiceCount");
                                    String C_brandId = carList_item.getString("brandId");
                                    String C_brandName = carList_item.getString("brandName");
                                    String C_imageUrlWp = carList_item.getString("imageUrlWp");
                                    String C_tranAndGearNum = carList_item.getString("tranAndGearNum");


                                    JSONArray tagList = carList_item.getJSONArray("tagList");

                                    String C_styleType = "无";
                                    String C_styleType_value = "无";
                                    if (tagList.size() != 0) {
                                        C_styleType = tagList.getJSONObject(0).getString("styleType");
                                        C_styleType_value = tagList.getJSONObject(0).getString("value");
                                    }


                                    Bean_version bean_version = new Bean_version();

                                    bean_version.setC_model_id(model_id);
                                    bean_version.setC_sourceList("waitSaleCarList");
                                    bean_version.setC_year_carList(year_carList);
                                    bean_version.setC_saleStatusList(saleStatusList);
                                    bean_version.setC_powerName(powerName);
                                    bean_version.setC_DownState("否");


                                    bean_version.setC_version_id(C_id);
                                    bean_version.setC_name(C_name);
                                    bean_version.setC_year(C_year);
                                    bean_version.setC_saleStatus(C_saleStatus);
                                    bean_version.setC_localDealPrice(C_localDealPrice);
                                    bean_version.setC_locaLatestDealPrice(C_locaLatestDealPrice);
                                    bean_version.setC_imageUrl(C_imageUrl);
                                    bean_version.setC_entireDealPrice(C_entireDealPrice);
                                    bean_version.setC_dealFinalPriceInfo(C_dealFinalPriceInfo);
                                    bean_version.setC_serialId(C_serialId);
                                    bean_version.setC_serialName(C_serialName);
                                    bean_version.setC_serialSecondLevel(C_serialSecondLevel);
                                    bean_version.setC_usedCarPrice(C_usedCarPrice);
                                    bean_version.setC_dropPrice(C_dropPrice);
                                    bean_version.setC_dealerDropAfterPrice(C_dealerDropAfterPrice);
                                    bean_version.setC_maintainValue(C_maintainValue);
                                    bean_version.setC_cityReferencePrice(C_cityReferencePrice);
                                    bean_version.setC_subscribeStatus(C_subscribeStatus);
                                    bean_version.setC_referPrice(C_referPrice);
                                    bean_version.setC_discountPrice(C_discountPrice);
                                    bean_version.setC_subsidizedReferPrice(C_subsidizedReferPrice);
                                    bean_version.setC_greenStandards(C_greenStandards);
                                    bean_version.setC_haveParam(C_haveParam);
                                    bean_version.setC_pvPercent(C_pvPercent);
                                    bean_version.setC_hasImageFlag(C_hasImageFlag);
                                    bean_version.setC_marketDate(C_marketDate);
                                    bean_version.setC_params(C_params);
                                    bean_version.setC_photoInfo(C_photoInfo);
                                    bean_version.setC_yiCheHuiTag(C_yiCheHuiTag);
                                    bean_version.setC_loanVo(C_loanVo);
                                    bean_version.setC_businessCardList(C_businessCardList);
                                    bean_version.setC_oilFuelTypeInt(C_oilFuelTypeInt);
                                    bean_version.setC_fuelUnitType(C_fuelUnitType);
                                    bean_version.setC_electricRechargeMileage(C_electricRechargeMileage);
                                    bean_version.setC_oilWear(C_oilWear);
                                    bean_version.setC_masterId(C_masterId);
                                    bean_version.setC_masterName(C_masterName);
                                    bean_version.setC_logoUrl(C_logoUrl);
                                    bean_version.setC_logoUrlWp(C_logoUrlWp);
                                    bean_version.setC_masterAllSpell(C_masterAllSpell);
                                    bean_version.setC_minDealPrice(C_minDealPrice);
                                    bean_version.setC_invoiceCount(C_invoiceCount);
                                    bean_version.setC_brandId(C_brandId);
                                    bean_version.setC_brandName(C_brandName);
                                    bean_version.setC_imageUrlWp(C_imageUrlWp);
                                    bean_version.setC_tranAndGearNum(C_tranAndGearNum);
                                    bean_version.setC_styleType(C_styleType);
                                    bean_version.setC_styleType_value(C_styleType_value);

                                    dao_version.MethodInsert(bean_version);
                                }
                            }
                        }
                    }
                }
                JSONArray onSaleCarList = jsonObject.getJSONArray("onSaleCarList");
                if (onSaleCarList != null) {
                    for (int i = 0; i < onSaleCarList.size(); i++) {
//                            System.out.println(onSaleCarList);
                        JSONObject Item1_notSale = onSaleCarList.getJSONObject(i);
                        String year_carList = Item1_notSale.getString("year");
                        String saleStatusList = Item1_notSale.getString("saleStatusList");

                        JSONArray powerList_notSale = Item1_notSale.getJSONArray("powerList");
                        if (powerList_notSale.size() != 0) {
                            for (int j = 0; j < powerList_notSale.size(); j++) {
                                JSONObject powerList_notSale_item = powerList_notSale.getJSONObject(j);
                                String powerName = powerList_notSale_item.getString("powerName");
                                JSONArray carList = powerList_notSale_item.getJSONArray("carList");
                                for (int k = 0; k < carList.size(); k++) {
                                    JSONObject carList_item = carList.getJSONObject(k);
                                    String C_id = carList_item.getString("id");
                                    String C_name = carList_item.getString("name");
                                    String C_year = carList_item.getString("year");
                                    String C_saleStatus = carList_item.getString("saleStatus");
                                    String C_localDealPrice = carList_item.getString("localDealPrice");
                                    String C_locaLatestDealPrice = carList_item.getString("locaLatestDealPrice");
                                    String C_imageUrl = carList_item.getString("imageUrl");
                                    String C_entireDealPrice = carList_item.getString("entireDealPrice");
                                    String C_dealFinalPriceInfo = carList_item.getString("dealFinalPriceInfo");
                                    String C_serialId = carList_item.getString("serialId");
                                    String C_serialName = carList_item.getString("serialName");
                                    String C_serialSecondLevel = carList_item.getString("serialSecondLevel");
                                    String C_usedCarPrice = carList_item.getString("usedCarPrice");
                                    String C_dropPrice = carList_item.getString("dropPrice");
                                    String C_dealerDropAfterPrice = carList_item.getString("dealerDropAfterPrice");
                                    String C_maintainValue = carList_item.getString("maintainValue");
                                    String C_cityReferencePrice = carList_item.getString("cityReferencePrice");
                                    String C_subscribeStatus = carList_item.getString("subscribeStatus");
                                    String C_referPrice = carList_item.getString("referPrice");
                                    String C_discountPrice = carList_item.getString("discountPrice");
                                    String C_subsidizedReferPrice = carList_item.getString("subsidizedReferPrice");
                                    String C_greenStandards = carList_item.getString("greenStandards");
                                    String C_haveParam = carList_item.getString("haveParam");
                                    String C_pvPercent = carList_item.getString("pvPercent");
                                    String C_hasImageFlag = carList_item.getString("hasImageFlag");
                                    String C_marketDate = carList_item.getString("marketDate");
                                    String C_params = carList_item.getString("params");
                                    String C_photoInfo = carList_item.getString("photoInfo");
                                    String C_yiCheHuiTag = carList_item.getString("yiCheHuiTag");
                                    String C_loanVo = carList_item.getString("loanVo");
                                    String C_businessCardList = carList_item.getString("businessCardList");
                                    String C_oilFuelTypeInt = carList_item.getString("oilFuelTypeInt");
                                    String C_fuelUnitType = carList_item.getString("fuelUnitType");
                                    String C_electricRechargeMileage = carList_item.getString("electricRechargeMileage");
                                    String C_oilWear = carList_item.getString("oilWear");
                                    String C_masterId = carList_item.getString("masterId");
                                    String C_masterName = carList_item.getString("masterName");
                                    String C_logoUrl = carList_item.getString("logoUrl");
                                    String C_logoUrlWp = carList_item.getString("logoUrlWp");
                                    String C_masterAllSpell = carList_item.getString("masterAllSpell");
                                    String C_minDealPrice = carList_item.getString("minDealPrice");
                                    String C_invoiceCount = carList_item.getString("invoiceCount");
                                    String C_brandId = carList_item.getString("brandId");
                                    String C_brandName = carList_item.getString("brandName");
                                    String C_imageUrlWp = carList_item.getString("imageUrlWp");
                                    String C_tranAndGearNum = carList_item.getString("tranAndGearNum");


                                    JSONArray tagList = carList_item.getJSONArray("tagList");

                                    String C_styleType = "无";
                                    String C_styleType_value = "无";
                                    if (tagList.size() != 0) {
                                        C_styleType = tagList.getJSONObject(0).getString("styleType");
                                        C_styleType_value = tagList.getJSONObject(0).getString("value");
                                    }

                                    Bean_version bean_version = new Bean_version();

                                    bean_version.setC_model_id(model_id);
                                    bean_version.setC_sourceList("onSaleCarList");
                                    bean_version.setC_year_carList(year_carList);
                                    bean_version.setC_saleStatusList(saleStatusList);
                                    bean_version.setC_powerName(powerName);
                                    bean_version.setC_DownState("否");

                                    bean_version.setC_version_id(C_id);
                                    bean_version.setC_name(C_name);
                                    bean_version.setC_year(C_year);
                                    bean_version.setC_saleStatus(C_saleStatus);
                                    bean_version.setC_localDealPrice(C_localDealPrice);
                                    bean_version.setC_locaLatestDealPrice(C_locaLatestDealPrice);
                                    bean_version.setC_imageUrl(C_imageUrl);
                                    bean_version.setC_entireDealPrice(C_entireDealPrice);
                                    bean_version.setC_dealFinalPriceInfo(C_dealFinalPriceInfo);
                                    bean_version.setC_serialId(C_serialId);
                                    bean_version.setC_serialName(C_serialName);
                                    bean_version.setC_serialSecondLevel(C_serialSecondLevel);
                                    bean_version.setC_usedCarPrice(C_usedCarPrice);
                                    bean_version.setC_dropPrice(C_dropPrice);
                                    bean_version.setC_dealerDropAfterPrice(C_dealerDropAfterPrice);
                                    bean_version.setC_maintainValue(C_maintainValue);
                                    bean_version.setC_cityReferencePrice(C_cityReferencePrice);
                                    bean_version.setC_subscribeStatus(C_subscribeStatus);
                                    bean_version.setC_referPrice(C_referPrice);
                                    bean_version.setC_discountPrice(C_discountPrice);
                                    bean_version.setC_subsidizedReferPrice(C_subsidizedReferPrice);
                                    bean_version.setC_greenStandards(C_greenStandards);
                                    bean_version.setC_haveParam(C_haveParam);
                                    bean_version.setC_pvPercent(C_pvPercent);
                                    bean_version.setC_hasImageFlag(C_hasImageFlag);
                                    bean_version.setC_marketDate(C_marketDate);
                                    bean_version.setC_params(C_params);
                                    bean_version.setC_photoInfo(C_photoInfo);
                                    bean_version.setC_yiCheHuiTag(C_yiCheHuiTag);
                                    bean_version.setC_loanVo(C_loanVo);
                                    bean_version.setC_businessCardList(C_businessCardList);
                                    bean_version.setC_oilFuelTypeInt(C_oilFuelTypeInt);
                                    bean_version.setC_fuelUnitType(C_fuelUnitType);
                                    bean_version.setC_electricRechargeMileage(C_electricRechargeMileage);
                                    bean_version.setC_oilWear(C_oilWear);
                                    bean_version.setC_masterId(C_masterId);
                                    bean_version.setC_masterName(C_masterName);
                                    bean_version.setC_logoUrl(C_logoUrl);
                                    bean_version.setC_logoUrlWp(C_logoUrlWp);
                                    bean_version.setC_masterAllSpell(C_masterAllSpell);
                                    bean_version.setC_minDealPrice(C_minDealPrice);
                                    bean_version.setC_invoiceCount(C_invoiceCount);
                                    bean_version.setC_brandId(C_brandId);
                                    bean_version.setC_brandName(C_brandName);
                                    bean_version.setC_imageUrlWp(C_imageUrlWp);
                                    bean_version.setC_tranAndGearNum(C_tranAndGearNum);
                                    bean_version.setC_styleType(C_styleType);
                                    bean_version.setC_styleType_value(C_styleType_value);
                                    dao_version.MethodInsert(bean_version);
                                }
                            }
                        }
                    }
                }
                JSONArray stopSaleCarList = jsonObject.getJSONArray("stopSaleCarList");
                if (stopSaleCarList != null) {
                    for (int i = 0; i < stopSaleCarList.size(); i++) {
                        JSONObject Item1_notSale = stopSaleCarList.getJSONObject(i);
                        String year_carList = Item1_notSale.getString("year");
                        String saleStatusList = Item1_notSale.getString("saleStatusList");

                        JSONArray powerList_notSale = Item1_notSale.getJSONArray("powerList");
                        if (powerList_notSale.size() != 0) {
                            for (int j = 0; j < powerList_notSale.size(); j++) {
                                JSONObject powerList_notSale_item = powerList_notSale.getJSONObject(j);
                                String powerName = powerList_notSale_item.getString("powerName");
                                JSONArray carList = powerList_notSale_item.getJSONArray("carList");
                                for (int k = 0; k < carList.size(); k++) {
                                    JSONObject carList_item = carList.getJSONObject(k);
                                    String C_id = carList_item.getString("id");
                                    String C_name = carList_item.getString("name");
                                    String C_year = carList_item.getString("year");
                                    String C_saleStatus = carList_item.getString("saleStatus");
                                    String C_localDealPrice = carList_item.getString("localDealPrice");
                                    String C_locaLatestDealPrice = carList_item.getString("locaLatestDealPrice");
                                    String C_imageUrl = carList_item.getString("imageUrl");
                                    String C_entireDealPrice = carList_item.getString("entireDealPrice");
                                    String C_dealFinalPriceInfo = carList_item.getString("dealFinalPriceInfo");
                                    String C_serialId = carList_item.getString("serialId");
                                    String C_serialName = carList_item.getString("serialName");
                                    String C_serialSecondLevel = carList_item.getString("serialSecondLevel");
                                    String C_usedCarPrice = carList_item.getString("usedCarPrice");
                                    String C_dropPrice = carList_item.getString("dropPrice");
                                    String C_dealerDropAfterPrice = carList_item.getString("dealerDropAfterPrice");
                                    String C_maintainValue = carList_item.getString("maintainValue");
                                    String C_cityReferencePrice = carList_item.getString("cityReferencePrice");
                                    String C_subscribeStatus = carList_item.getString("subscribeStatus");
                                    String C_referPrice = carList_item.getString("referPrice");
                                    String C_discountPrice = carList_item.getString("discountPrice");
                                    String C_subsidizedReferPrice = carList_item.getString("subsidizedReferPrice");
                                    String C_greenStandards = carList_item.getString("greenStandards");
                                    String C_haveParam = carList_item.getString("haveParam");
                                    String C_pvPercent = carList_item.getString("pvPercent");
                                    String C_hasImageFlag = carList_item.getString("hasImageFlag");
                                    String C_marketDate = carList_item.getString("marketDate");
                                    String C_params = carList_item.getString("params");
                                    String C_photoInfo = carList_item.getString("photoInfo");
                                    String C_yiCheHuiTag = carList_item.getString("yiCheHuiTag");
                                    String C_loanVo = carList_item.getString("loanVo");
                                    String C_businessCardList = carList_item.getString("businessCardList");
                                    String C_oilFuelTypeInt = carList_item.getString("oilFuelTypeInt");
                                    String C_fuelUnitType = carList_item.getString("fuelUnitType");
                                    String C_electricRechargeMileage = carList_item.getString("electricRechargeMileage");
                                    String C_oilWear = carList_item.getString("oilWear");
                                    String C_masterId = carList_item.getString("masterId");
                                    String C_masterName = carList_item.getString("masterName");
                                    String C_logoUrl = carList_item.getString("logoUrl");
                                    String C_logoUrlWp = carList_item.getString("logoUrlWp");
                                    String C_masterAllSpell = carList_item.getString("masterAllSpell");
                                    String C_minDealPrice = carList_item.getString("minDealPrice");
                                    String C_invoiceCount = carList_item.getString("invoiceCount");
                                    String C_brandId = carList_item.getString("brandId");
                                    String C_brandName = carList_item.getString("brandName");
                                    String C_imageUrlWp = carList_item.getString("imageUrlWp");
                                    String C_tranAndGearNum = carList_item.getString("tranAndGearNum");


                                    JSONArray tagList = carList_item.getJSONArray("tagList");
                                    String C_styleType = "无";
                                    String C_styleType_value = "无";
                                    if (tagList.size() != 0) {
                                        C_styleType = tagList.getJSONObject(0).getString("styleType");
                                        C_styleType_value = tagList.getJSONObject(0).getString("value");
                                    }


                                    Bean_version bean_version = new Bean_version();
                                    bean_version.setC_DownState("否");
                                    bean_version.setC_model_id(model_id);
                                    bean_version.setC_sourceList("stopSaleCarList");
                                    bean_version.setC_year_carList(year_carList);
                                    bean_version.setC_saleStatusList(saleStatusList);
                                    bean_version.setC_powerName(powerName);

                                    bean_version.setC_version_id(C_id);
                                    bean_version.setC_name(C_name);
                                    bean_version.setC_year(C_year);
                                    bean_version.setC_saleStatus(C_saleStatus);
                                    bean_version.setC_localDealPrice(C_localDealPrice);
                                    bean_version.setC_locaLatestDealPrice(C_locaLatestDealPrice);
                                    bean_version.setC_imageUrl(C_imageUrl);
                                    bean_version.setC_entireDealPrice(C_entireDealPrice);
                                    bean_version.setC_dealFinalPriceInfo(C_dealFinalPriceInfo);
                                    bean_version.setC_serialId(C_serialId);
                                    bean_version.setC_serialName(C_serialName);
                                    bean_version.setC_serialSecondLevel(C_serialSecondLevel);
                                    bean_version.setC_usedCarPrice(C_usedCarPrice);
                                    bean_version.setC_dropPrice(C_dropPrice);
                                    bean_version.setC_dealerDropAfterPrice(C_dealerDropAfterPrice);
                                    bean_version.setC_maintainValue(C_maintainValue);
                                    bean_version.setC_cityReferencePrice(C_cityReferencePrice);
                                    bean_version.setC_subscribeStatus(C_subscribeStatus);
                                    bean_version.setC_referPrice(C_referPrice);
                                    bean_version.setC_discountPrice(C_discountPrice);
                                    bean_version.setC_subsidizedReferPrice(C_subsidizedReferPrice);
                                    bean_version.setC_greenStandards(C_greenStandards);
                                    bean_version.setC_haveParam(C_haveParam);
                                    bean_version.setC_pvPercent(C_pvPercent);
                                    bean_version.setC_hasImageFlag(C_hasImageFlag);
                                    bean_version.setC_marketDate(C_marketDate);
                                    bean_version.setC_params(C_params);
                                    bean_version.setC_photoInfo(C_photoInfo);
                                    bean_version.setC_yiCheHuiTag(C_yiCheHuiTag);
                                    bean_version.setC_loanVo(C_loanVo);
                                    bean_version.setC_businessCardList(C_businessCardList);
                                    bean_version.setC_oilFuelTypeInt(C_oilFuelTypeInt);
                                    bean_version.setC_fuelUnitType(C_fuelUnitType);
                                    bean_version.setC_electricRechargeMileage(C_electricRechargeMileage);
                                    bean_version.setC_oilWear(C_oilWear);
                                    bean_version.setC_masterId(C_masterId);
                                    bean_version.setC_masterName(C_masterName);
                                    bean_version.setC_logoUrl(C_logoUrl);
                                    bean_version.setC_logoUrlWp(C_logoUrlWp);
                                    bean_version.setC_masterAllSpell(C_masterAllSpell);
                                    bean_version.setC_minDealPrice(C_minDealPrice);
                                    bean_version.setC_invoiceCount(C_invoiceCount);
                                    bean_version.setC_brandId(C_brandId);
                                    bean_version.setC_brandName(C_brandName);
                                    bean_version.setC_imageUrlWp(C_imageUrlWp);
                                    bean_version.setC_tranAndGearNum(C_tranAndGearNum);
                                    bean_version.setC_styleType(C_styleType);
                                    bean_version.setC_styleType_value(C_styleType_value);

                                    dao_version.MethodInsert(bean_version);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void Method_7_Down_versionConfig(String savePath, String main_url, String fileName) {

        Dao_version dao_version = new Dao_version(0, 2);

        dao_version.Method_分组();

        ArrayList<Object> BeanList = dao_version.Method_Find();

        int All_GroupNumber = ((Bean_version) BeanList.get(BeanList.size() - 1)).getGroupNumber();

        for (int i = 1; i <= All_GroupNumber; i++) {

            ArrayList<Object> BeanList_FenZu = dao_version.Method_Find_ByGroup(i);

            StringBuilder VersionIDs = new StringBuilder();
            if (BeanList_FenZu.size() != 0) {
                for (int j = 0; j < BeanList_FenZu.size(); j++) {
                    Bean_version bean_version_fenzu = (Bean_version) BeanList_FenZu.get(j);
                    VersionIDs.append(",").append(bean_version_fenzu.getC_version_id());
                }
                String finall_Versions = VersionIDs.substring(1);

                String DownState = ((Bean_version) BeanList_FenZu.get(0)).getC_DownState();

                if (DownState.equals("否")) {
                    System.out.println("第几组 \t" + i + " \t组内个数: " + BeanList_FenZu.size());
                    System.out.println(finall_Versions);

                    String param = "{\"carIds\":\"" + finall_Versions + "\",\"cityId\":\"201\"}";
                    String connect = Method_RequestAPI(main_url, param);
                    if (!connect.equals("Error")) {
                        String status = JSON.parseObject(connect).getString("status");
                        if (status.equals("1")) {
                            saveUntil.Method_SaveFile(savePath + i + fileName, connect);
                            dao_version.Method_ChangeState(i);
                        } else {
                            System.out.println(connect);
                        }
                    }
                }
            }
            BeanList_FenZu.clear();
        }
    }

    public void Method_8_Analysis_Config(String savePath, String fileName) {
        Dao_version dao_version = new Dao_version(0, 2);
        ArrayList<Object> BeanList = dao_version.Method_Find();

        int All_GroupNumber = ((Bean_version) BeanList.get(BeanList.size() - 1)).getGroupNumber();

        for (int i = 1; i <= All_GroupNumber; i++) {
            String connect = readUntil.Method_ReadFile(savePath + i + fileName);
            System.out.println(i + fileName);
            Method_Analysis_baseConfig(connect, i + fileName);
//            Method_9_Analysis_Config_GetCoumns(connect);
//            Method_Analysis_choseConfig(connect, i + fileName);
        }
    }

    public void Method_9_Analysis_Config_GetCoumns(String content) {
        DaoFather dao_configcolumn = new DaoFather(0, 3);
        JSONObject jsonObject = JSON.parseObject(content).getJSONObject("data");
        JSONArray Items = jsonObject.getJSONArray("list");
        boolean b = (Items == null);
        if (!b) {
            if (Items.size() != 0) {
                for (int i = 0; i < Items.size(); i++) {
                    JSONObject Item_One_Title = Items.getJSONObject(i);

//            一级标题
                    String title_1 = Item_One_Title.getString("name");


                    JSONArray Items2 = Item_One_Title.getJSONArray("items");

                    for (int j = 0; j < Items2.size(); j++) {
                        JSONObject Item_Two_Title = Items2.getJSONObject(j);
                        String title_2 = title_1 + "_" + Item_Two_Title.getString("name");
//                        System.out.println(title_2);

                        Bean_configcolumn bean_configcolumn = new Bean_configcolumn();
                        bean_configcolumn.setC_column_one(title_1);
                        bean_configcolumn.setC_column_two(title_2);
                        dao_configcolumn.Method_Insert(bean_configcolumn);

                    }
                }
            } else {
//            saveUntil.Method_SaveFile_True("E:\\ZKZD2023\\易车网\\Erroe.txt", file+"\t");
            }
        }
    }


    public static void Method_Analysis_baseConfig(String contentJSON, String filename) {

//        1.构建哈希Map
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("电池/补能_补能速度", "C_电池补能_补能速度");
        columnMap.put("被动安全_被动行人保护", "C_被动安全_被动行人保护");
        columnMap.put("被动安全_侧安全气帘", "C_被动安全_侧安全气帘");
        columnMap.put("被动安全_第二排侧气囊", "C_被动安全_第二排侧气囊");
        columnMap.put("被动安全_第二排正向气囊", "C_被动安全_第二排正向气囊");
        columnMap.put("被动安全_第一排侧气囊", "C_被动安全_第一排侧气囊");
        columnMap.put("被动安全_儿童座椅接口", "C_被动安全_儿童座椅接口");
        columnMap.put("被动安全_副驾驶安全气囊", "C_被动安全_副驾驶安全气囊");
        columnMap.put("被动安全_副驾驶座垫式气囊", "C_被动安全_副驾驶座垫式气囊");
        columnMap.put("被动安全_后排安全带式气囊", "C_被动安全_后排安全带式气囊");
        columnMap.put("被动安全_后排座椅防下滑气囊", "C_被动安全_后排座椅防下滑气囊");
        columnMap.put("被动安全_缺气保用轮胎", "C_被动安全_缺气保用轮胎");
        columnMap.put("被动安全_膝部气囊", "C_被动安全_膝部气囊");
        columnMap.put("被动安全_中央安全气囊", "C_被动安全_中央安全气囊");
        columnMap.put("被动安全_主驾驶安全气囊", "C_被动安全_主驾驶安全气囊");
        columnMap.put("变速箱_变速箱类型", "C_变速箱_变速箱类型");
        columnMap.put("变速箱_变速箱描述", "C_变速箱_变速箱描述");
        columnMap.put("变速箱_挡位数", "C_变速箱_挡位数");
        columnMap.put("变速箱_卡车倒退挡位数", "C_变速箱_卡车倒退挡位数");
        columnMap.put("变速箱_卡车前进挡位数", "C_变速箱_卡车前进挡位数");
        columnMap.put("玻璃/后视镜_车窗防夹手功能", "C_玻璃后视镜_车窗防夹手功能");
        columnMap.put("玻璃/后视镜_车窗一键升降", "C_玻璃后视镜_车窗一键升降");
        columnMap.put("玻璃/后视镜_车内化妆镜", "C_玻璃后视镜_车内化妆镜");
        columnMap.put("玻璃/后视镜_第二排电动车窗", "C_玻璃后视镜_第二排电动车窗");
        columnMap.put("玻璃/后视镜_第一排电动车窗", "C_玻璃后视镜_第一排电动车窗");
        columnMap.put("玻璃/后视镜_多层隔音玻璃", "C_玻璃后视镜_多层隔音玻璃");
        columnMap.put("玻璃/后视镜_感应雨刷功能", "C_玻璃后视镜_感应雨刷功能");
        columnMap.put("玻璃/后视镜_后风挡遮阳帘", "C_玻璃后视镜_后风挡遮阳帘");
        columnMap.put("玻璃/后视镜_后排侧隐私玻璃", "C_玻璃后视镜_后排侧隐私玻璃");
        columnMap.put("玻璃/后视镜_后排侧遮阳帘", "C_玻璃后视镜_后排侧遮阳帘");
        columnMap.put("玻璃/后视镜_后雨刷", "C_玻璃后视镜_后雨刷");
        columnMap.put("玻璃/后视镜_可加热喷水嘴", "C_玻璃后视镜_可加热喷水嘴");
        columnMap.put("玻璃/后视镜_流媒体外后视镜显示屏", "C_玻璃后视镜_流媒体外后视镜显示屏");
        columnMap.put("玻璃/后视镜_内后视镜功能", "C_玻璃后视镜_内后视镜功能");
        columnMap.put("玻璃/后视镜_外后视镜功能", "C_玻璃后视镜_外后视镜功能");
        columnMap.put("车机/互联_AR实景导航", "C_车机互联_AR实景导航");
        columnMap.put("车机/互联_OTA升级", "C_车机互联_OTA升级");
        columnMap.put("车机/互联_V2X通讯", "C_车机互联_V2X通讯");
        columnMap.put("车机/互联_车机WiFi功能", "C_车机互联_车机WiFi功能");
        columnMap.put("车机/互联_车机网络", "C_车机互联_车机网络");
        columnMap.put("车机/互联_车机系统", "C_车机互联_车机系统");
        columnMap.put("车机/互联_车机系统存储[GB]", "C_车机互联_车机系统存储GB");
        columnMap.put("车机/互联_车机系统内存[GB]", "C_车机互联_车机系统内存GB");
        columnMap.put("车机/互联_车机芯片", "C_车机互联_车机芯片");
        columnMap.put("车机/互联_车载导航地图", "C_车机互联_车载导航地图");
        columnMap.put("车机/互联_道路救援呼叫", "C_车机互联_道路救援呼叫");
        columnMap.put("车机/互联_多指飞屏操控", "C_车机互联_多指飞屏操控");
        columnMap.put("车机/互联_副驾屏幕", "C_车机互联_副驾屏幕");
        columnMap.put("车机/互联_副驾屏幕材质", "C_车机互联_副驾屏幕材质");
        columnMap.put("车机/互联_副驾屏幕尺寸[英寸]", "C_车机互联_副驾屏幕尺寸英寸");
        columnMap.put("车机/互联_副驾屏幕分辨率[px]", "C_车机互联_副驾屏幕分辨率px");
        columnMap.put("车机/互联_副驾屏幕刷新率[Hz]", "C_车机互联_副驾屏幕刷新率Hz");
        columnMap.put("车机/互联_副驾屏幕像素密度[PPI]", "C_车机互联_副驾屏幕像素密度PPI");
        columnMap.put("车机/互联_高精地图", "C_车机互联_高精地图");
        columnMap.put("车机/互联_蓝牙/车载电话", "C_车机互联_蓝牙车载电话");
        columnMap.put("车机/互联_面部识别", "C_车机互联_面部识别");
        columnMap.put("车机/互联_手机互联/映射", "C_车机互联_手机互联映射");
        columnMap.put("车机/互联_手势控制", "C_车机互联_手势控制");
        columnMap.put("车机/互联_语音分区唤醒", "C_车机互联_语音分区唤醒");
        columnMap.put("车机/互联_语音连续识别", "C_车机互联_语音连续识别");
        columnMap.put("车机/互联_语音免唤醒", "C_车机互联_语音免唤醒");
        columnMap.put("车机/互联_语音识别控制功能", "C_车机互联_语音识别控制功能");
        columnMap.put("车机/互联_语音助手唤醒词", "C_车机互联_语音助手唤醒词");
        columnMap.put("车机/互联_远程控制功能", "C_车机互联_远程控制功能");
        columnMap.put("车机/互联_中控彩色屏幕", "C_车机互联_中控彩色屏幕");
        columnMap.put("车机/互联_中控屏幕材质", "C_车机互联_中控屏幕材质");
        columnMap.put("车机/互联_中控屏幕尺寸[英寸]", "C_车机互联_中控屏幕尺寸英寸");
        columnMap.put("车机/互联_中控屏幕分辨率[px]", "C_车机互联_中控屏幕分辨率px");
        columnMap.put("车机/互联_中控屏幕亮度[nit]", "C_车机互联_中控屏幕亮度nit");
        columnMap.put("车机/互联_中控屏幕色彩[万色]", "C_车机互联_中控屏幕色彩万色");
        columnMap.put("车机/互联_中控屏幕刷新率[Hz]", "C_车机互联_中控屏幕刷新率Hz");
        columnMap.put("车机/互联_中控屏幕像素密度[PPI]", "C_车机互联_中控屏幕像素密度PPI");
        columnMap.put("车机/互联_中控下屏幕材质", "C_车机互联_中控下屏幕材质");
        columnMap.put("车机/互联_中控下屏幕尺寸[英寸]", "C_车机互联_中控下屏幕尺寸英寸");
        columnMap.put("车机/互联_中控下屏幕分辨率[px]", "C_车机互联_中控下屏幕分辨率px");
        columnMap.put("车机/互联_中控下屏幕像素密度[PPI]", "C_车机互联_中控下屏幕像素密度PPI");
        columnMap.put("车轮制动_备胎", "C_车轮制动_备胎");
        columnMap.put("车轮制动_备胎放置方式", "C_车轮制动_备胎放置方式");
        columnMap.put("车轮制动_后轮胎规格", "C_车轮制动_后轮胎规格");
        columnMap.put("车轮制动_后制动器类型", "C_车轮制动_后制动器类型");
        columnMap.put("车轮制动_轮胎规格", "C_车轮制动_轮胎规格");
        columnMap.put("车轮制动_轮胎数", "C_车轮制动_轮胎数");
        columnMap.put("车轮制动_前轮胎规格", "C_车轮制动_前轮胎规格");
        columnMap.put("车轮制动_前制动器类型", "C_车轮制动_前制动器类型");
        columnMap.put("车轮制动_驻车制动类型", "C_车轮制动_驻车制动类型");
        columnMap.put("车身_车门开启方式", "C_车身_车门开启方式");
        columnMap.put("车身_车门数", "C_车身_车门数");
        columnMap.put("车身_车型种类", "C_车身_车型种类");
        columnMap.put("车身_风阻系数[Cd]", "C_车身_风阻系数Cd");
        columnMap.put("车身_高度[mm]", "C_车身_高度mm");
        columnMap.put("车身_官方后备厢容积[L]", "C_车身_官方后备厢容积L");
        columnMap.put("车身_官方前备厢容积[L]", "C_车身_官方前备厢容积L");
        columnMap.put("车身_后轮距[mm]", "C_车身_后轮距mm");
        columnMap.put("车身_货箱高度[mm]", "C_车身_货箱高度mm");
        columnMap.put("车身_货箱宽度[mm]", "C_车身_货箱宽度mm");
        columnMap.put("车身_货箱长度[mm]", "C_车身_货箱长度mm");
        columnMap.put("车身_接近角[°]", "C_车身_接近角");
        columnMap.put("车身_空载最小离地间隙[mm]", "C_车身_空载最小离地间隙mm");
        columnMap.put("车身_宽度[mm]", "C_车身_宽度mm");
        columnMap.put("车身_离去角[°]", "C_车身_离去角");
        columnMap.put("车身_满载质量[kg]", "C_车身_满载质量kg");
        columnMap.put("车身_满载最小离地间隙[mm]", "C_车身_满载最小离地间隙mm");
        columnMap.put("车身_气罐容积[L]", "C_车身_气罐容积L");
        columnMap.put("车身_牵引质量[kg]", "C_车身_牵引质量kg");
        columnMap.put("车身_前轮距[mm]", "C_车身_前轮距mm");
        columnMap.put("车身_实测后备厢容积[L]", "C_车身_实测后备厢容积L");
        columnMap.put("车身_实测前备厢容积[L]", "C_车身_实测前备厢容积L");
        columnMap.put("车身_通过角[°]", "C_车身_通过角");
        columnMap.put("车身_油箱容积[L]", "C_车身_油箱容积L");
        columnMap.put("车身_载重质量[kg]", "C_车身_载重质量kg");
        columnMap.put("车身_长度[mm]", "C_车身_长度mm");
        columnMap.put("车身_整备质量[kg]", "C_车身_整备质量kg");
        columnMap.put("车身_轴距[mm]", "C_车身_轴距mm");
        columnMap.put("车身_最大爬坡度[%]", "C_车身_最大爬坡度");
        columnMap.put("车身_最小转弯半径[m]", "C_车身_最小转弯半径m");
        columnMap.put("车身_座位数", "C_车身_座位数");
        columnMap.put("灯光功能_LED日间行车灯", "C_灯光功能_LED日间行车灯");
        columnMap.put("灯光功能_车内氛围灯", "C_灯光功能_车内氛围灯");
        columnMap.put("灯光功能_大灯功能", "C_灯光功能_大灯功能");
        columnMap.put("灯光功能_大灯清洗装置", "C_灯光功能_大灯清洗装置");
        columnMap.put("灯光功能_灯光特色功能", "C_灯光功能_灯光特色功能");
        columnMap.put("灯光功能_近光灯光源", "C_灯光功能_近光灯光源");
        columnMap.put("灯光功能_前雾灯", "C_灯光功能_前雾灯");
        columnMap.put("灯光功能_远光灯光源", "C_灯光功能_远光灯光源");
        columnMap.put("灯光功能_主动式环境氛围灯", "C_灯光功能_主动式环境氛围灯");
        columnMap.put("灯光功能_转向辅助灯", "C_灯光功能_转向辅助灯");
        columnMap.put("底盘转向_差速锁", "C_底盘转向_差速锁");
        columnMap.put("底盘转向_车体结构", "C_底盘转向_车体结构");
        columnMap.put("底盘转向_后桥描述", "C_底盘转向_后桥描述");
        columnMap.put("底盘转向_后桥速比", "C_底盘转向_后桥速比");
        columnMap.put("底盘转向_后桥允许载荷[kg]", "C_底盘转向_后桥允许载荷kg");
        columnMap.put("底盘转向_后悬架类型", "C_底盘转向_后悬架类型");
        columnMap.put("底盘转向_缓速器类型", "C_底盘转向_缓速器类型");
        columnMap.put("底盘转向_卡车驱动形式", "C_底盘转向_卡车驱动形式");
        columnMap.put("底盘转向_可调悬架功能", "C_底盘转向_可调悬架功能");
        columnMap.put("底盘转向_可调悬架种类", "C_底盘转向_可调悬架种类");
        columnMap.put("底盘转向_客车后悬架类型", "C_底盘转向_客车后悬架类型");
        columnMap.put("底盘转向_客车前悬架类型", "C_底盘转向_客车前悬架类型");
        columnMap.put("底盘转向_前桥描述", "C_底盘转向_前桥描述");
        columnMap.put("底盘转向_前桥允许载荷[kg]", "C_底盘转向_前桥允许载荷kg");
        columnMap.put("底盘转向_前悬架类型", "C_底盘转向_前悬架类型");
        columnMap.put("底盘转向_驱动形式", "C_底盘转向_驱动形式");
        columnMap.put("底盘转向_四驱形式", "C_底盘转向_四驱形式");
        columnMap.put("底盘转向_限滑差速器", "C_底盘转向_限滑差速器");
        columnMap.put("底盘转向_中央差速器结构", "C_底盘转向_中央差速器结构");
        columnMap.put("底盘转向_助力类型", "C_底盘转向_助力类型");
        columnMap.put("底盘转向_最大涉水深度[mm]", "C_底盘转向_最大涉水深度mm");
        columnMap.put("电池/补能_CLTC纯电续航[km]", "C_电池补能_CLTC纯电续航km");
        columnMap.put("电池/补能_CLTC综合续航[km]", "C_电池补能_CLTC综合续航km");
        columnMap.put("电池/补能_EPA纯电续航[km]", "C_电池补能_EPA纯电续航km");
        columnMap.put("电池/补能_NEDC纯电续航[km]", "C_电池补能_NEDC纯电续航km");
        columnMap.put("电池/补能_NEDC综合续航[km]", "C_电池补能_NEDC综合续航km");
        columnMap.put("电池/补能_WLTC纯电续航[km]", "C_电池补能_WLTC纯电续航km");
        columnMap.put("电池/补能_WLTC综合续航[km]", "C_电池补能_WLTC综合续航km");
        columnMap.put("电池/补能_百公里耗电量[kWh/100km]", "C_电池补能_百公里耗电量kWh100km");
        columnMap.put("电池/补能_车辆充电口", "C_电池补能_车辆充电口");
        columnMap.put("电池/补能_车辆换电", "C_电池补能_车辆换电");
        columnMap.put("电池/补能_电池电量[kWh]", "C_电池补能_电池电量kWh");
        columnMap.put("电池/补能_电池类型", "C_电池补能_电池类型");
        columnMap.put("电池/补能_电池能量密度[Wh/kg]", "C_电池补能_电池能量密度Whkg");
        columnMap.put("电池/补能_电池温度管理", "C_电池补能_电池温度管理");
        columnMap.put("电池/补能_电池组质保", "C_电池补能_电池组质保");
        columnMap.put("电池/补能_电芯品牌", "C_电池补能_电芯品牌");
        columnMap.put("电池/补能_对外放电功率", "C_电池补能_对外放电功率");
        columnMap.put("电池/补能_对外放电功能", "C_电池补能_对外放电功能");
        columnMap.put("电池/补能_高压快充平台", "C_电池补能_高压快充平台");
        columnMap.put("电池/补能_换电时间[min]", "C_电池补能_换电时间min");
        columnMap.put("电池/补能_家用充电桩", "C_电池补能_家用充电桩");
        columnMap.put("电池/补能_快充电量[%]", "C_电池补能_快充电量");
        columnMap.put("电池/补能_快充功率[kW]", "C_电池补能_快充功率kW");
        columnMap.put("电池/补能_快充口位置", "C_电池补能_快充口位置");
        columnMap.put("电池/补能_快充时间[h]", "C_电池补能_快充时间h");
        columnMap.put("电池/补能_快充时间[min]", "C_电池补能_快充时间min");
        columnMap.put("电池/补能_慢充电量[%]", "C_电池补能_慢充电量");
        columnMap.put("电池/补能_慢充功率[kW]", "C_电池补能_慢充功率kW");
        columnMap.put("电池/补能_慢充口位置", "C_电池补能_慢充口位置");
        columnMap.put("电池/补能_慢充时间[h]", "C_电池补能_慢充时间h");
        columnMap.put("电池/补能_首任车主电池组质保", "C_电池补能_首任车主电池组质保");
        columnMap.put("电池/补能_随车充电枪", "C_电池补能_随车充电枪");
        columnMap.put("电动机_电动机品牌", "C_电动机_电动机品牌");
        columnMap.put("电动机_电动机型号", "C_电动机_电动机型号");
        columnMap.put("电动机_电动机总功率[kW]", "C_电动机_电动机总功率kW");
        columnMap.put("电动机_电动机总马力[Ps]", "C_电动机_电动机总马力Ps");
        columnMap.put("电动机_电动机总扭矩[N·m]", "C_电动机_电动机总扭矩Nm");
        columnMap.put("电动机_电机布局", "C_电动机_电机布局");
        columnMap.put("电动机_电机类型", "C_电动机_电机类型");
        columnMap.put("电动机_后电动机最大功率[kW]", "C_电动机_后电动机最大功率kW");
        columnMap.put("电动机_后电动机最大扭矩[N·m]", "C_电动机_后电动机最大扭矩Nm");
        columnMap.put("电动机_前电动机最大功率[kW]", "C_电动机_前电动机最大功率kW");
        columnMap.put("电动机_前电动机最大扭矩[N·m]", "C_电动机_前电动机最大扭矩Nm");
        columnMap.put("电动机_驱动电机数", "C_电动机_驱动电机数");
        columnMap.put("电动机_系统综合功率[kW]", "C_电动机_系统综合功率kW");
        columnMap.put("电动机_系统综合马力[Ps]", "C_电动机_系统综合马力Ps");
        columnMap.put("电动机_系统综合扭矩[N·m]", "C_电动机_系统综合扭矩Nm");
        columnMap.put("发动机_发动机布局", "C_发动机_发动机布局");
        columnMap.put("发动机_发动机型号", "C_发动机_发动机型号");
        columnMap.put("发动机_缸盖材料", "C_发动机_缸盖材料");
        columnMap.put("发动机_缸体材料", "C_发动机_缸体材料");
        columnMap.put("发动机_供油方式", "C_发动机_供油方式");
        columnMap.put("发动机_环保标准", "C_发动机_环保标准");
        columnMap.put("发动机_进气形式", "C_发动机_进气形式");
        columnMap.put("发动机_每缸气门数[个]", "C_发动机_每缸气门数个");
        columnMap.put("发动机_排量[L]", "C_发动机_排量L");
        columnMap.put("发动机_排量[mL]", "C_发动机_排量mL");
        columnMap.put("发动机_配气机构", "C_发动机_配气机构");
        columnMap.put("发动机_气缸排列形式", "C_发动机_气缸排列形式");
        columnMap.put("发动机_气缸数[个]", "C_发动机_气缸数个");
        columnMap.put("发动机_燃油标号", "C_发动机_燃油标号");
        columnMap.put("发动机_压缩比", "C_发动机_压缩比");
        columnMap.put("发动机_最大功率[kW]", "C_发动机_最大功率kW");
        columnMap.put("发动机_最大功率转速[rpm]", "C_发动机_最大功率转速rpm");
        columnMap.put("发动机_最大净功率[kW]", "C_发动机_最大净功率kW");
        columnMap.put("发动机_最大马力[Ps]", "C_发动机_最大马力Ps");
        columnMap.put("发动机_最大扭矩[N·m]", "C_发动机_最大扭矩Nm");
        columnMap.put("发动机_最大扭矩转速[rpm]", "C_发动机_最大扭矩转速rpm");
        columnMap.put("辅助/操控配置_倒车车侧预警系统", "C_辅助操控配置_倒车车侧预警系统");
        columnMap.put("辅助/操控配置_低速四驱", "C_辅助操控配置_低速四驱");
        columnMap.put("辅助/操控配置_底盘透视", "C_辅助操控配置_底盘透视");
        columnMap.put("辅助/操控配置_陡坡缓降", "C_辅助操控配置_陡坡缓降");
        columnMap.put("辅助/操控配置_发动机启停", "C_辅助操控配置_发动机启停");
        columnMap.put("辅助/操控配置_防侧翻系统", "C_辅助操控配置_防侧翻系统");
        columnMap.put("辅助/操控配置_后驻车雷达", "C_辅助操控配置_后驻车雷达");
        columnMap.put("辅助/操控配置_驾驶辅助影像", "C_辅助操控配置_驾驶辅助影像");
        columnMap.put("辅助/操控配置_驾驶模式选择", "C_辅助操控配置_驾驶模式选择");
        columnMap.put("辅助/操控配置_开门碰撞预警", "C_辅助操控配置_开门碰撞预警");
        columnMap.put("辅助/操控配置_可变转向比", "C_辅助操控配置_可变转向比");
        columnMap.put("辅助/操控配置_疲劳提醒", "C_辅助操控配置_疲劳提醒");
        columnMap.put("辅助/操控配置_前驻车雷达", "C_辅助操控配置_前驻车雷达");
        columnMap.put("辅助/操控配置_蠕行模式", "C_辅助操控配置_蠕行模式");
        columnMap.put("辅助/操控配置_上坡辅助", "C_辅助操控配置_上坡辅助");
        columnMap.put("辅助/操控配置_涉水感应系统", "C_辅助操控配置_涉水感应系统");
        columnMap.put("辅助/操控配置_坦克转弯", "C_辅助操控配置_坦克转弯");
        columnMap.put("辅助/操控配置_巡航系统", "C_辅助操控配置_巡航系统");
        columnMap.put("辅助/操控配置_遥控泊车", "C_辅助操控配置_遥控泊车");
        columnMap.put("辅助/操控配置_夜视系统", "C_辅助操控配置_夜视系统");
        columnMap.put("辅助/操控配置_远程召唤", "C_辅助操控配置_远程召唤");
        columnMap.put("辅助/操控配置_整体主动转向系统", "C_辅助操控配置_整体主动转向系统");
        columnMap.put("辅助/操控配置_自动泊车", "C_辅助操控配置_自动泊车");
        columnMap.put("辅助/操控配置_自动驻车", "C_辅助操控配置_自动驻车");
        columnMap.put("辅助驾驶功能_并线辅助(BSM/BSD)", "C_辅助驾驶功能_并线辅助BSMBSD");
        columnMap.put("辅助驾驶功能_车道保持(LKAS)", "C_辅助驾驶功能_车道保持LKAS");
        columnMap.put("辅助驾驶功能_车道居中保持", "C_辅助驾驶功能_车道居中保持");
        columnMap.put("辅助驾驶功能_车道偏离预警(LDWS)", "C_辅助驾驶功能_车道偏离预警LDWS");
        columnMap.put("辅助驾驶功能_城市辅助驾驶", "C_辅助驾驶功能_城市辅助驾驶");
        columnMap.put("辅助驾驶功能_倒车循迹", "C_辅助驾驶功能_倒车循迹");
        columnMap.put("辅助驾驶功能_道路交通标识识别", "C_辅助驾驶功能_道路交通标识识别");
        columnMap.put("辅助驾驶功能_高速辅助驾驶", "C_辅助驾驶功能_高速辅助驾驶");
        columnMap.put("辅助驾驶功能_后方碰撞预警", "C_辅助驾驶功能_后方碰撞预警");
        columnMap.put("辅助驾驶功能_前方碰撞预警", "C_辅助驾驶功能_前方碰撞预警");
        columnMap.put("辅助驾驶功能_匝道自动驶出/入", "C_辅助驾驶功能_匝道自动驶出入");
        columnMap.put("辅助驾驶功能_主动刹车", "C_辅助驾驶功能_主动刹车");
        columnMap.put("辅助驾驶功能_自动变道辅助", "C_辅助驾驶功能_自动变道辅助");
        columnMap.put("辅助驾驶硬件_超声波雷达", "C_辅助驾驶硬件_超声波雷达");
        columnMap.put("辅助驾驶硬件_车内摄像头", "C_辅助驾驶硬件_车内摄像头");
        columnMap.put("辅助驾驶硬件_辅助驾驶系统", "C_辅助驾驶硬件_辅助驾驶系统");
        columnMap.put("辅助驾驶硬件_辅助驾驶芯片", "C_辅助驾驶硬件_辅助驾驶芯片");
        columnMap.put("辅助驾驶硬件_毫米波雷达", "C_辅助驾驶硬件_毫米波雷达");
        columnMap.put("辅助驾驶硬件_环境感知摄像头", "C_辅助驾驶硬件_环境感知摄像头");
        columnMap.put("辅助驾驶硬件_环境感知摄像头像素[万]", "C_辅助驾驶硬件_环境感知摄像头像素万");
        columnMap.put("辅助驾驶硬件_环视摄像头", "C_辅助驾驶硬件_环视摄像头");
        columnMap.put("辅助驾驶硬件_环视摄像头像素[万]", "C_辅助驾驶硬件_环视摄像头像素万");
        columnMap.put("辅助驾驶硬件_激光雷达10%反射率探测距离[m]", "C_辅助驾驶硬件_激光雷达10反射率探测距离m");
        columnMap.put("辅助驾驶硬件_激光雷达点云数量[万/秒]", "C_辅助驾驶硬件_激光雷达点云数量万秒");
        columnMap.put("辅助驾驶硬件_激光雷达品牌", "C_辅助驾驶硬件_激光雷达品牌");
        columnMap.put("辅助驾驶硬件_激光雷达数量", "C_辅助驾驶硬件_激光雷达数量");
        columnMap.put("辅助驾驶硬件_激光雷达线数", "C_辅助驾驶硬件_激光雷达线数");
        columnMap.put("辅助驾驶硬件_激光雷达型号", "C_辅助驾驶硬件_激光雷达型号");
        columnMap.put("辅助驾驶硬件_激光雷达最远探测距离[m]", "C_辅助驾驶硬件_激光雷达最远探测距离m");
        columnMap.put("辅助驾驶硬件_驾驶辅助级别", "C_辅助驾驶硬件_驾驶辅助级别");
        columnMap.put("辅助驾驶硬件_前方感知摄像头类型", "C_辅助驾驶硬件_前方感知摄像头类型");
        columnMap.put("辅助驾驶硬件_前方感知摄像头像素[万]", "C_辅助驾驶硬件_前方感知摄像头像素万");
        columnMap.put("辅助驾驶硬件_芯片算力", "C_辅助驾驶硬件_芯片算力");
        columnMap.put("基本信息_CLTC纯电续航[km]", "C_基本信息_CLTC纯电续航km");
        columnMap.put("基本信息_CLTC综合油耗[L/100km]", "C_基本信息_CLTC综合油耗L100km");
        columnMap.put("基本信息_EPA纯电续航[km]", "C_基本信息_EPA纯电续航km");
        columnMap.put("基本信息_NEDC纯电续航[km]", "C_基本信息_NEDC纯电续航km");
        columnMap.put("基本信息_NEDC综合油耗[L/100km]", "C_基本信息_NEDC综合油耗L100km");
        columnMap.put("基本信息_WLTC纯电续航[km]", "C_基本信息_WLTC纯电续航km");
        columnMap.put("基本信息_WLTC综合油耗[L/100km]", "C_基本信息_WLTC综合油耗L100km");
        columnMap.put("基本信息_变速箱", "C_基本信息_变速箱");
        columnMap.put("基本信息_厂商", "C_基本信息_厂商");
        columnMap.put("基本信息_厂商指导价", "C_基本信息_厂商指导价");
        columnMap.put("基本信息_车款名称", "C_基本信息_车款名称");
        columnMap.put("基本信息_车身结构", "C_基本信息_车身结构");
        columnMap.put("基本信息_城市参考价", "C_基本信息_城市参考价");
        columnMap.put("基本信息_电动机[Ps]", "C_基本信息_电动机Ps");
        columnMap.put("基本信息_发动机", "C_基本信息_发动机");
        columnMap.put("基本信息_官方0-100km/h加速[s]", "C_基本信息_官方0100kmh加速s");
        columnMap.put("基本信息_环保标准", "C_基本信息_环保标准");
        columnMap.put("基本信息_级别", "C_基本信息_级别");
        columnMap.put("基本信息_快充电量[%]", "C_基本信息_快充电量");
        columnMap.put("基本信息_快充时间[h]", "C_基本信息_快充时间h");
        columnMap.put("基本信息_亏电状态油耗[L/100km]", "C_基本信息_亏电状态油耗L100km");
        columnMap.put("基本信息_慢充电量[%]", "C_基本信息_慢充电量");
        columnMap.put("基本信息_慢充时间[h]", "C_基本信息_慢充时间h");
        columnMap.put("基本信息_内饰颜色", "C_基本信息_内饰颜色");
        columnMap.put("基本信息_能源类型", "C_基本信息_能源类型");
        columnMap.put("基本信息_上市时间", "C_基本信息_上市时间");
        columnMap.put("基本信息_首任车主质保政策", "C_基本信息_首任车主质保政策");
        columnMap.put("基本信息_外观颜色", "C_基本信息_外观颜色");
        columnMap.put("基本信息_长*宽*高[mm]", "C_基本信息_长宽高mm");
        columnMap.put("基本信息_整车质保", "C_基本信息_整车质保");
        columnMap.put("基本信息_最大功率/最大扭矩", "C_基本信息_最大功率最大扭矩");
        columnMap.put("基本信息_最高车速[km/h]", "C_基本信息_最高车速kmh");
        columnMap.put("空调/制冷_车内PM2.5过滤装置", "C_空调制冷_车内PM25过滤装置");
        columnMap.put("空调/制冷_车载冰箱", "C_空调制冷_车载冰箱");
        columnMap.put("空调/制冷_车载空气净化器", "C_空调制冷_车载空气净化器");
        columnMap.put("空调/制冷_第二排空调", "C_空调制冷_第二排空调");
        columnMap.put("空调/制冷_第三排空调", "C_空调制冷_第三排空调");
        columnMap.put("空调/制冷_第一排空调", "C_空调制冷_第一排空调");
        columnMap.put("空调/制冷_负离子发生器", "C_空调制冷_负离子发生器");
        columnMap.put("空调/制冷_空气质量监测", "C_空调制冷_空气质量监测");
        columnMap.put("空调/制冷_热泵空调", "C_空调制冷_热泵空调");
        columnMap.put("空调/制冷_香氛系统", "C_空调制冷_香氛系统");
        columnMap.put("内部配置_ETC装置", "C_内部配置_ETC装置");
        columnMap.put("内部配置_HUD抬头显示", "C_内部配置_HUD抬头显示");
        columnMap.put("内部配置_单踏板模式", "C_内部配置_单踏板模式");
        columnMap.put("内部配置_电动可调踏板", "C_内部配置_电动可调踏板");
        columnMap.put("内部配置_多功能方向盘", "C_内部配置_多功能方向盘");
        columnMap.put("内部配置_方向盘材质", "C_内部配置_方向盘材质");
        columnMap.put("内部配置_方向盘调节", "C_内部配置_方向盘调节");
        columnMap.put("内部配置_方向盘换挡", "C_内部配置_方向盘换挡");
        columnMap.put("内部配置_方向盘加热", "C_内部配置_方向盘加热");
        columnMap.put("内部配置_换挡形式", "C_内部配置_换挡形式");
        columnMap.put("内部配置_内置行车记录仪", "C_内部配置_内置行车记录仪");
        columnMap.put("内部配置_全液晶仪表盘", "C_内部配置_全液晶仪表盘");
        columnMap.put("内部配置_手机无线充电", "C_内部配置_手机无线充电");
        columnMap.put("内部配置_手机无线最大充电功率[W]", "C_内部配置_手机无线最大充电功率W");
        columnMap.put("内部配置_行车电脑显示屏", "C_内部配置_行车电脑显示屏");
        columnMap.put("内部配置_仪表屏幕材质", "C_内部配置_仪表屏幕材质");
        columnMap.put("内部配置_仪表屏幕尺寸[英寸]", "C_内部配置_仪表屏幕尺寸英寸");
        columnMap.put("内部配置_仪表屏幕分辨率[px]", "C_内部配置_仪表屏幕分辨率px");
        columnMap.put("内部配置_仪表屏幕像素密度[PPI]", "C_内部配置_仪表屏幕像素密度PPI");
        columnMap.put("内部配置_主动降噪", "C_内部配置_主动降噪");
        columnMap.put("外部配置_侧滑门", "C_外部配置_侧滑门");
        columnMap.put("外部配置_车侧脚踏板", "C_外部配置_车侧脚踏板");
        columnMap.put("外部配置_车顶行李架", "C_外部配置_车顶行李架");
        columnMap.put("外部配置_低速行车警示音", "C_外部配置_低速行车警示音");
        columnMap.put("外部配置_电动扰流板", "C_外部配置_电动扰流板");
        columnMap.put("外部配置_电动尾门", "C_外部配置_电动尾门");
        columnMap.put("外部配置_电吸门", "C_外部配置_电吸门");
        columnMap.put("外部配置_感应尾门", "C_外部配置_感应尾门");
        columnMap.put("外部配置_光感天幕", "C_外部配置_光感天幕");
        columnMap.put("外部配置_货箱宝", "C_外部配置_货箱宝");
        columnMap.put("外部配置_轮圈材质", "C_外部配置_轮圈材质");
        columnMap.put("外部配置_哨兵(千里眼)模式", "C_外部配置_哨兵千里眼模式");
        columnMap.put("外部配置_天窗类型", "C_外部配置_天窗类型");
        columnMap.put("外部配置_拖车钩", "C_外部配置_拖车钩");
        columnMap.put("外部配置_拖车取电口", "C_外部配置_拖车取电口");
        columnMap.put("外部配置_尾门玻璃独立开启", "C_外部配置_尾门玻璃独立开启");
        columnMap.put("外部配置_尾门位置记忆", "C_外部配置_尾门位置记忆");
        columnMap.put("外部配置_无框车门", "C_外部配置_无框车门");
        columnMap.put("外部配置_无钥匙进入", "C_外部配置_无钥匙进入");
        columnMap.put("外部配置_无钥匙启动", "C_外部配置_无钥匙启动");
        columnMap.put("外部配置_钥匙类型", "C_外部配置_钥匙类型");
        columnMap.put("外部配置_隐藏电动门把手", "C_外部配置_隐藏电动门把手");
        columnMap.put("外部配置_运动外观套件", "C_外部配置_运动外观套件");
        columnMap.put("外部配置_整车无线充电", "C_外部配置_整车无线充电");
        columnMap.put("外部配置_主动闭合式进气格栅", "C_外部配置_主动闭合式进气格栅");
        columnMap.put("外部配置_自动开合车门", "C_外部配置_自动开合车门");
        columnMap.put("影音娱乐_USB/Type-C接口数量", "C_影音娱乐_USBTypeC接口数量");
        columnMap.put("影音娱乐_车内娱乐功能", "C_影音娱乐_车内娱乐功能");
        columnMap.put("影音娱乐_车载APP应用市场", "C_影音娱乐_车载APP应用市场");
        columnMap.put("影音娱乐_车载CD/DVD", "C_影音娱乐_车载CDDVD");
        columnMap.put("影音娱乐_多媒体/充电接口", "C_影音娱乐_多媒体充电接口");
        columnMap.put("影音娱乐_分屏功能", "C_影音娱乐_分屏功能");
        columnMap.put("影音娱乐_后排多媒体屏幕材质", "C_影音娱乐_后排多媒体屏幕材质");
        columnMap.put("影音娱乐_后排多媒体屏幕尺寸[英寸]", "C_影音娱乐_后排多媒体屏幕尺寸英寸");
        columnMap.put("影音娱乐_后排多媒体屏幕分辨率[px]", "C_影音娱乐_后排多媒体屏幕分辨率px");
        columnMap.put("影音娱乐_后排多媒体屏幕数量", "C_影音娱乐_后排多媒体屏幕数量");
        columnMap.put("影音娱乐_后排多媒体屏幕刷新率[Hz]", "C_影音娱乐_后排多媒体屏幕刷新率Hz");
        columnMap.put("影音娱乐_后排多媒体屏幕像素密度[PPI]", "C_影音娱乐_后排多媒体屏幕像素密度PPI");
        columnMap.put("影音娱乐_后排控制多媒体", "C_影音娱乐_后排控制多媒体");
        columnMap.put("影音娱乐_模拟声浪", "C_影音娱乐_模拟声浪");
        columnMap.put("影音娱乐_行李厢电源接口", "C_影音娱乐_行李厢电源接口");
        columnMap.put("影音娱乐_扬声器数量", "C_影音娱乐_扬声器数量");
        columnMap.put("影音娱乐_音响品牌", "C_影音娱乐_音响品牌");
        columnMap.put("影音娱乐_座舱220V/230V电源", "C_影音娱乐_座舱220V230V电源");
        columnMap.put("主动安全_ABS防抱死", "C_主动安全_ABS防抱死");
        columnMap.put("主动安全_安全带未系提醒", "C_主动安全_安全带未系提醒");
        columnMap.put("主动安全_车内生物监测", "C_主动安全_车内生物监测");
        columnMap.put("主动安全_车身稳定控制(ESP/DSC/VSC等)", "C_主动安全_车身稳定控制ESPDSCVSC等");
        columnMap.put("主动安全_牵引力控制(ASR/TCS/TRC等)", "C_主动安全_牵引力控制ASRTCSTRC等");
        columnMap.put("主动安全_胎压监测", "C_主动安全_胎压监测");
        columnMap.put("主动安全_制动辅助(BA/EBA/BAS等)", "C_主动安全_制动辅助BAEBABAS等");
        columnMap.put("主动安全_制动力分配(EBD/CBC等)", "C_主动安全_制动力分配EBDCBC等");
        columnMap.put("座椅配置_第二排座椅电动调节", "C_座椅配置_第二排座椅电动调节");
        columnMap.put("座椅配置_第二排座椅调节方式", "C_座椅配置_第二排座椅调节方式");
        columnMap.put("座椅配置_第二排座椅功能", "C_座椅配置_第二排座椅功能");
        columnMap.put("座椅配置_第三排座椅电动调节", "C_座椅配置_第三排座椅电动调节");
        columnMap.put("座椅配置_第三排座椅调节方式", "C_座椅配置_第三排座椅调节方式");
        columnMap.put("座椅配置_第三排座椅功能", "C_座椅配置_第三排座椅功能");
        columnMap.put("座椅配置_第一排座椅功能", "C_座椅配置_第一排座椅功能");
        columnMap.put("座椅配置_电动脚托", "C_座椅配置_电动脚托");
        columnMap.put("座椅配置_福祉座椅", "C_座椅配置_福祉座椅");
        columnMap.put("座椅配置_副驾座椅电动调节", "C_座椅配置_副驾座椅电动调节");
        columnMap.put("座椅配置_副驾座椅调节方式", "C_座椅配置_副驾座椅调节方式");
        columnMap.put("座椅配置_后排杯架", "C_座椅配置_后排杯架");
        columnMap.put("座椅配置_后排折叠桌板", "C_座椅配置_后排折叠桌板");
        columnMap.put("座椅配置_后排座椅电动放倒", "C_座椅配置_后排座椅电动放倒");
        columnMap.put("座椅配置_后排座椅放倒方式", "C_座椅配置_后排座椅放倒方式");
        columnMap.put("座椅配置_加热/制冷杯架", "C_座椅配置_加热制冷杯架");
        columnMap.put("座椅配置_老板键", "C_座椅配置_老板键");
        columnMap.put("座椅配置_零重力座椅功能", "C_座椅配置_零重力座椅功能");
        columnMap.put("座椅配置_运动风格座椅", "C_座椅配置_运动风格座椅");
        columnMap.put("座椅配置_中央扶手", "C_座椅配置_中央扶手");
        columnMap.put("座椅配置_主驾座椅电动调节", "C_座椅配置_主驾座椅电动调节");
        columnMap.put("座椅配置_主驾座椅调节方式", "C_座椅配置_主驾座椅调节方式");
        columnMap.put("座椅配置_座椅布局", "C_座椅配置_座椅布局");
        columnMap.put("座椅配置_座椅材质", "C_座椅配置_座椅材质");

        try {
            JSONObject Item_data = JSON.parseObject(contentJSON).getJSONObject("data");

            JSONArray Item_list = Item_data.getJSONArray("list");

            if (null != Item_list) {
                if (Item_list.size() > 0) {

//                1.构建空的BeanList
                    ArrayList<Bean_version_config> bean_version_configs_carList = new ArrayList<>();

                    JSONArray carList = Item_list.getJSONObject(0).getJSONArray("items").getJSONObject(0).getJSONArray("paramValues");

                    for (int i = 0; i < carList.size(); i++) {
                        Bean_version_config bean_version_config = new Bean_version_config();
                        String version_id = carList.getJSONObject(i).getString("id");
                        bean_version_config.setC_version_id(version_id);
                        bean_version_config.setC_source(filename);
                        bean_version_configs_carList.add(bean_version_config);
                    }


                    for (int i = 0; i < Item_list.size(); i++) {
                        JSONObject title_one_Object = Item_list.getJSONObject(i);
                        String title_one = title_one_Object.getString("name");
                        JSONArray title_one_Items = title_one_Object.getJSONArray("items");
                        if (title_one.equals("选配包") || title_one.equals("特色配置")) {
                            System.out.println("暂不需要选配 与 特色配置");
                        } else {
                            for (int j = 0; j < title_one_Items.size(); j++) {
                                JSONObject title_two_Object = title_one_Items.getJSONObject(j);
                                String title_two = title_two_Object.getString("name");
                                String columnName = title_one + "_" + title_two;
                                JSONArray paramValues = title_two_Object.getJSONArray("paramValues");
                                for (int k = 0; k < paramValues.size(); k++) {
                                    JSONObject one_car = paramValues.getJSONObject(k);
                                    String version_id = one_car.getString("id");
                                    String value = "";
                                    JSONArray subList = one_car.getJSONArray("subList");
                                    for (int l = 0; l < subList.size(); l++) {

                                        JSONObject one_date_value = subList.getJSONObject(l);

                                        String one_value = one_date_value.getString("value");

                                        String price = "";
                                        String desc = "";
                                        if (one_date_value.containsKey("price")) {
                                            price = (one_date_value.getString("price").equals("") ? "" : one_date_value.getString("price"));
                                        }
                                        if (one_date_value.containsKey("desc")) {
                                            desc = one_date_value.getString("desc");
                                        }
                                        value = value + one_value + desc + price;
                                    }

                                    for (int l = 0; l < bean_version_configs_carList.size(); l++) {
                                        String car_versionId = bean_version_configs_carList.get(l).getC_version_id();
                                        if (version_id.equals(car_versionId)) {
                                            Class c = bean_version_configs_carList.get(l).getClass();
                                            Field field = c.getDeclaredField(columnMap.get(columnName));
                                            field.setAccessible(true);
                                            field.set(bean_version_configs_carList.get(l), value);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    DaoFather dao_config = new DaoFather(0, 4);
                    for (int i = 0; i < bean_version_configs_carList.size(); i++) {
                        dao_config.MethodInsert(bean_version_configs_carList.get(i));
                    }
                    bean_version_configs_carList.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void Method_Analysis_choseConfig(String contentJSON, String filename) {
        DaoFather dao_choseConfig = new DaoFather(0, 5);
        try {
            JSONObject Item_data = JSON.parseObject(contentJSON).getJSONObject("data");

            JSONArray Item_list = Item_data.getJSONArray("list");

            if (null != Item_list) {
                if (Item_list.size() > 0) {

                    for (int i = 0; i < Item_list.size(); i++) {
                        JSONObject title_one_Object = Item_list.getJSONObject(i);
                        String title_one = title_one_Object.getString("name");
                        JSONArray title_one_Items = title_one_Object.getJSONArray("items");
                        if (title_one.equals("选配包") || title_one.equals("特色配置")) {
                            for (int j = 0; j < title_one_Items.size(); j++) {
                                JSONObject title_two_Object = title_one_Items.getJSONObject(j);
                                String title_two = title_two_Object.getString("name");
                                String columnName = title_one + "_" + title_two;
                                String desc_main = title_two_Object.getString("desc");
                                JSONArray paramValues = title_two_Object.getJSONArray("paramValues");
                                for (int k = 0; k < paramValues.size(); k++) {
                                    JSONObject one_car = paramValues.getJSONObject(k);
                                    String version_id = one_car.getString("id");
                                    JSONArray subList = one_car.getJSONArray("subList");
                                    for (int l = 0; l < subList.size(); l++) {
                                        JSONObject one_date_value = subList.getJSONObject(l);
                                        String one_value = one_date_value.getString("value");
                                        String price = "为空";
                                        String desc = "为空";
                                        if (one_date_value.containsKey("price")) {
                                            price = " price:" + (one_date_value.getString("price").equals("") ? "为空" : one_date_value.getString("price"));
                                        }
                                        if (one_date_value.containsKey("desc")) {
                                            desc = " desc:" + one_date_value.getString("desc");
                                        }
                                        Bean_config_chose bean_config_chose = new Bean_config_chose();
                                        bean_config_chose.setC_version_id(version_id);
                                        bean_config_chose.setC_congig_name(columnName);
                                        bean_config_chose.setC_config_value(one_value);
                                        bean_config_chose.setC_config_desc(desc);
                                        bean_config_chose.setC_config_price(price);
                                        bean_config_chose.setC_config_describe(desc_main);
                                        bean_config_chose.setC_source(filename);
                                        bean_config_chose.setC_congig_type(title_one);
                                        dao_choseConfig.MethodInsert(bean_config_chose);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
