package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.example.Dao.DaoFather;
import org.example.Entity.Bean_DianPingContent;
import org.example.Entity.Bean_Dianping_Pages;
import org.example.Entity.Bean_Dianping_detail_page;
import org.example.Entity.Bean_model;
import org.example.Until.HttpUntil;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

public class Controller_DianPing {

    private SaveUntil saveUntil = new SaveUntil();
    private ReadUntil readUntil = new ReadUntil();
    private HttpUntil httpUntil = new HttpUntil();

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


    public void Method_1_DownFirstHtml(String savePath, String saveName) {
        DaoFather dao_model = new DaoFather(1, 0);
        ArrayList<Object> list = dao_model.Method_Find();

        for (Object bean : list) {
            Bean_model bean_model = (Bean_model) bean;

            String downState = bean_model.getC_DownState();
            String modelId = bean_model.getC_model_id();
            String EnglishName = bean_model.getC_allSpell();
            if (downState.equals("否")) {
                String mainUrl = "https://dianping.yiche.com/" + EnglishName + "/koubei/";
                String html = Method_DownHTML(mainUrl);
                if (!html.equals("Error")) {
                    saveUntil.Method_SaveFile(savePath + modelId + saveName, html);
                    dao_model.Method_ChangeState(bean_model.getC_ID());
                    System.out.println("成功下载：" + bean_model.getC_ID());
                }
            }
        }
    }

    public void Method_2_AnalysisHtml1(String savePath, String saveName) {
        DaoFather dao_model = new DaoFather(1, 0);
        DaoFather dao_pages = new DaoFather(1, 1);
        ArrayList<Object> list = dao_model.Method_Find();
        for (Object bean : list) {
            Bean_model bean_model = (Bean_model) bean;

            String downState = bean_model.getC_DownState();
            String modelId = bean_model.getC_model_id();
            String EnglishName = bean_model.getC_allSpell();
            String content = readUntil.Method_ReadFile(savePath + modelId + saveName);

            Document document = Jsoup.parse(content);
            System.out.println(modelId);
            String number = document.select(".sizes.pg-item").select("span").text();
            if (number.equals("")) {
                System.out.println("没有页数");
            } else {
                int pageNumber = Integer.parseInt(number);
                System.out.println(pageNumber);

//                https://dianping.yiche.com/benchiglaji/koubei-2.html
                String url = "";
                for (int i = 1; i <= pageNumber; i++) {
                    if (i == 1) {
                        url = "https://dianping.yiche.com/" + EnglishName + "/koubei/";
                    } else {
                        url = "https://dianping.yiche.com/" + EnglishName + "/koubei-" + i + ".html";
                    }
                    Bean_Dianping_Pages bean_dianping_pages = new Bean_Dianping_Pages();
                    bean_dianping_pages.setC_pageNumber(i);
                    bean_dianping_pages.setC_PageUrl(url);
                    bean_dianping_pages.setC_DownState("否");
                    bean_dianping_pages.setC_modelId(modelId);
                    bean_dianping_pages.setC_modelName(bean_model.getC_name());
                    dao_pages.Method_Insert(bean_dianping_pages);
                }
            }
        }
    }

    public void Method_3_DownPagesHtml(String savePath, String saveName) {
        DaoFather dao_pages = new DaoFather(1, 1);
        ArrayList<Object> list = dao_pages.Method_Find();
        for (Object bean : list) {
            Bean_Dianping_Pages bean_dianping_pages = (Bean_Dianping_Pages) bean;
            String downState = bean_dianping_pages.getC_DownState();
            if (downState.equals("否")) {
                String url = bean_dianping_pages.getC_PageUrl();
                String html = Method_DownHTML(url);
                if (!html.equals("Error")) {
                    saveUntil.Method_SaveFile(savePath + bean_dianping_pages.getC_modelId() + "_" + bean_dianping_pages.getC_pageNumber() + saveName, html);
                    dao_pages.Method_ChangeState(bean_dianping_pages.getC_ID());
                    System.out.println("成功下载：" + bean_dianping_pages.getC_ID());
                }
            }
        }
    }

    public void Method_4_AnalysisPagesHtml(String savePath, String saveName) {
        DaoFather dao_pages = new DaoFather(1, 1);
        DaoFather dao_detail = new DaoFather(1, 2);
        ArrayList<Object> list = dao_pages.Method_Find();
        for (Object bean : list) {
            Bean_Dianping_Pages bean_dianping_pages = (Bean_Dianping_Pages) bean;
            String modelID = bean_dianping_pages.getC_modelId();
            int pageNumber = bean_dianping_pages.getC_pageNumber();
            String content = readUntil.Method_ReadFile(savePath + modelID + "_" + pageNumber + saveName);
            Document document = Jsoup.parse(content);
            Elements Items = document.select(".cm-content-moudle");
            for (Element item : Items) {
                String onePage = "https://dianping.yiche.com" + item.select("a").get(1).attr("href");
//                https://dianping.yiche.com/baomam2/koubei/785188/
                System.out.println(onePage);
                Bean_Dianping_detail_page bean_dianping_detail_page = new Bean_Dianping_detail_page();
                bean_dianping_detail_page.setC_page_url(onePage);
                bean_dianping_detail_page.setC_model_id(modelID);
                bean_dianping_detail_page.setC_model_name(bean_dianping_pages.getC_modelName());
                bean_dianping_detail_page.setC_DownState("否");
                dao_detail.Method_Insert(bean_dianping_detail_page);

            }
        }
    }

    public void Method_5_DownDetailHtml(String savePath, String saveName) {
        DaoFather dao_detail = new DaoFather(1, 2);
        ArrayList<Object> list = dao_detail.Method_Find();
        for (Object bean : list) {
            Bean_Dianping_detail_page bean_dianping_detail_page = (Bean_Dianping_detail_page) bean;
            String downState = bean_dianping_detail_page.getC_DownState();
            if (downState.equals("否")) {

                String url = bean_dianping_detail_page.getC_page_url();
                String html = Method_DownHTML(url);
                String id = url.substring(url.indexOf("koubei/")).replace("koubei/", "").replace("/", "");
                System.out.println(id);
                if (!html.equals("Error")) {
                    saveUntil.Method_SaveFile(savePath + bean_dianping_detail_page.getC_model_id() + "_" + id + saveName, html);
                    dao_detail.Method_ChangeState(bean_dianping_detail_page.getC_ID());
                    System.out.println("成功下载：" + bean_dianping_detail_page.getC_ID());
                }
            }
        }
    }


    public void Method_6_DownDianpingJSON(String savePath, String saveName) {
        DaoFather dao_pages = new DaoFather(1, 1);
        ArrayList<Object> list = dao_pages.Method_Find();
        for (Object bean : list) {
            Bean_Dianping_Pages bean_dianping_pages = (Bean_Dianping_Pages) bean;
            String downState = bean_dianping_pages.getC_DownState();
            int pageNumber = bean_dianping_pages.getC_pageNumber();
            String modelId = bean_dianping_pages.getC_modelId();
            if (downState.equals("否")) {
                String param = "{\"tagId\":\"-10\",\"currentPage\":\"" + pageNumber + "\",\"serialId\":\"" + modelId + "\",\"pageSize\":20}";
                String html = httpUntil.Method_RequestAPI("https://mapi.yiche.com/information_api/api/v1/point_comment/query_comment_page_list?", param);
                if (!html.equals("Error")) {
                    String state = JSONObject.parseObject(html).getString("status");
                    if (state.equals("1")) {
                        saveUntil.Method_SaveFile(savePath + modelId + "_" + pageNumber + saveName, html);
                        dao_pages.Method_ChangeState(bean_dianping_pages.getC_ID());
                        System.out.println("成功下载：" + bean_dianping_pages.getC_ID());
                    }
                }
            }
        }
    }

    public void Method_7_AnalysisDianpingJSON(String savePath, String saveName) {
        DaoFather dao_pages = new DaoFather(1, 1);
        DaoFather dao_content = new DaoFather(1, 3);
        ArrayList<Object> list = dao_pages.Method_Find();
        for (Object bean : list) {
            Bean_Dianping_Pages bean_dianping_pages = (Bean_Dianping_Pages) bean;
            String modelId = bean_dianping_pages.getC_modelId();
            String modelName = bean_dianping_pages.getC_modelName();
            int pageNumber = bean_dianping_pages.getC_pageNumber();
            String content = readUntil.Method_ReadFile(savePath + modelId + "_" + pageNumber + saveName);
            JSONObject jsonObject = JSONObject.parseObject(content).getJSONObject("data");

            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.size(); i++) {
                Bean_DianPingContent bean_dianping_content = new Bean_DianPingContent();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("tagInfoList");
                if (jsonArray1.size() > 0) {
                    System.out.println(modelId);
                    for (int j = 0; j < jsonArray1.size(); j++) {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
//                        String tagId = jsonObject2.getString("tagId");
                        String tagIdName = jsonObject2.getString("name").replace("#", "").replace(" ", "");
                        String number = jsonObject2.getString("rating");
                        System.out.println(number);
                        if (!StringUtils.isEmpty(number)){
                            switch (tagIdName) {
                                case "外观":
                                    bean_dianping_content.setC_外观(Double.parseDouble(number));
                                    break;
                                case "内饰":
                                    bean_dianping_content.setC_内饰(Double.parseDouble(number));
                                    break;
                                case "舒适性":
                                    bean_dianping_content.setC_舒适性(Double.parseDouble(number));
                                    break;
                                case "空间":
                                    bean_dianping_content.setC_空间(Double.parseDouble(number));
                                    break;
                                case "动力":
                                    bean_dianping_content.setC_动力(Double.parseDouble(number));
                                    break;
                                case "操控":
                                    bean_dianping_content.setC_操控(Double.parseDouble(number));
                                    break;
                                case "油耗":
                                    bean_dianping_content.setC_油耗(Double.parseDouble(number));
                                    break;
                                case "续航":
                                    bean_dianping_content.setC_续航(Double.parseDouble(number));
                                    break;
                            }
                        }
                    }
                } else {
                    System.out.println("没有" + modelId);
                }

                bean_dianping_content.setC_content(jsonObject1.getString("content"));
                String score = jsonObject1.getString("score");
                if (!StringUtils.isEmpty(score)){
                    bean_dianping_content.setC_score(Double.parseDouble(jsonObject1.getString("score")));
                }
                bean_dianping_content.setC_title(jsonObject1.getString("title"));
                bean_dianping_content.setC_createtime(jsonObject1.getString("createTime"));
                bean_dianping_content.setC_purchaseDate(jsonObject1.getString("purchaseDate"));
                bean_dianping_content.setC_purchaseCityName(jsonObject1.getString("purchaseCityName"));
                bean_dianping_content.setC_purchaseCityId(jsonObject1.getString("purchaseCityId"));
                bean_dianping_content.setC_purchasePrice(jsonObject1.getString("purchasePrice"));
                bean_dianping_content.setC_dianpingID(jsonObject1.getString("id"));
                bean_dianping_content.setC_modelname(modelName);
                bean_dianping_content.setC_modelId(modelId);
                bean_dianping_content.setC_versionId(jsonObject1.getString("carId"));
                bean_dianping_content.setC_versionName(jsonObject1.getString("carName"));
                dao_content.Method_Insert(bean_dianping_content);
            }
        }
    }

}
