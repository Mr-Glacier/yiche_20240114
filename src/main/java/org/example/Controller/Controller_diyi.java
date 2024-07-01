package org.example.Controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.Dao.DaoFather;
import org.example.EntityErShouChe.Bean_car_ryk;
import org.example.EntityErShouChe.Bean_detail_diyi;
import org.example.EntityErShouChe.Bean_diyiche_county;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Controller_diyi {
    private SaveUntil saveUntil = new SaveUntil();
    private ReadUntil readUntil = new ReadUntil();


    public String Method_1_DownFirstHtml(String firstUrl) {
        try {
//            Document document = Jsoup.parse(new URL(firstUrl).openStream(), "UTF-8", firstUrl);
            Document document = Jsoup.connect(firstUrl).get();
//            System.out.println(document.html());
            System.out.println(document.select("title").text());
            Random random = new Random();
            int sleepTime = (random.nextInt(6) + 1) * 1000;
            Thread.sleep(sleepTime);
            return String.valueOf(document.html());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String Method_DownFirstHtmlByProxy(String firstUrl, String proxy_ip, String proxy_port) {
        try {
//            Jsoup.connect("https://www.sina.com").proxy("123.100.89.100", 8123).header("Accept", "*/*")
//                    .header("Accept-Encoding", "gzip, deflate")
//                    .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
//                    .header("Referer", "https://www.sina.com/")
//                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
//                    .timeout(5000)
//                    .get().text();
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_ip, Integer.parseInt(proxy_port)));
            Map<String, String> map = new HashMap<>();
            map.put("_df_id", "110.229.66.28_49803c99a72d417bbc383680b8b1dc63_1717660730");
            map.put("_df", "07ee3d738ac026343453f31da1255798");

            Document document = Jsoup.connect(firstUrl)
//                    .proxy(proxy)
                    .timeout(20000)
                    .header("Accept", "*/*")
                    .method(Connection.Method.GET)
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OPR/79.0.4143.72")
                    .cookies(map)
                    .get();
            // Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0
            // Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OPR/79.0.4143.72

            System.out.println(document.select("title").text());
            Random random = new Random();
            int sleepTime = (random.nextInt(3) + 1) * 1000;
            Thread.sleep(sleepTime);
            return String.valueOf(document.html());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void Method_2_AnalysisHtmlCountry(String Data) {
        DaoFather daoFather = new DaoFather(2, 0);
        Document document = Jsoup.parse(Data);
        Elements elements = document.select("div.city-content").select(".content-city").select(".city-li").select("dl").select("dd").select("a");
        for (Element element : elements) {
            String country = element.select("a").text();
            System.out.println(country);
            String url = element.select("a").attr("href");
            if (url != null) {
                //https://so.iautos.cn/anqing/
                System.out.println("https:" + url);
            }
            Bean_diyiche_county bean_diyiche_county = new Bean_diyiche_county();
            bean_diyiche_county.setC_county(country);
            bean_diyiche_county.setC_url("https:" + url);
            bean_diyiche_county.setC_downState("否");
            daoFather.MethodInsert(bean_diyiche_county);
        }
    }

    public void Method_3_DownCountyHtml(String savePath) {
        DaoFather daoFather = new DaoFather(2, 0);
        for (Object obj : daoFather.Method_Find()) {
            Bean_diyiche_county bean_diyiche_county = (Bean_diyiche_county) obj;
            System.out.println("开始下载-->" + bean_diyiche_county.getC_county());
            if (bean_diyiche_county.getC_downState().equals("否")) {
                int kk = 0;
                for (int i = 1; i < 51; i++) {
                    String url = bean_diyiche_county.getC_url() + "p" + i + "asdsvepcatcpbnscac/#buyCars";
                    if (i == 1) {
                        url = bean_diyiche_county.getC_url() + "#buyCars";
                    }
                    System.out.println(url);
                    String html = Method_1_DownFirstHtml(url);
//                    System.out.println(html);
                    Document document = Jsoup.parse(html);
                    Elements elements = document.select(".car-pic-form-box.car-box-list.clear");
                    System.out.println(elements.size());
                    if (elements.size() > 0) {
                        Elements elements1 = document.select("div.not-found").select("img");
                        if (elements1.size() > 0) {
                            break;
                        } else {
                            saveUntil.Method_SaveFile(savePath + bean_diyiche_county.getC_county() + "_" + i + ".txt", html);
                        }
                    } else {
                        kk++;
                    }
                    if (kk == 1) {
                        break;
                    }
                }
                daoFather.Method_OnlyChangeState(bean_diyiche_county.getC_ID());
                System.out.println("下载完成---> " + bean_diyiche_county.getC_county());
            }
        }
    }

    public void Method_4_AnalysisAllBrand(String savePath) {
        DaoFather daoFather = new DaoFather(2, 3);
        List<String> fileNames = readUntil.getFileNames(savePath);


        for (String fileName : fileNames) {
            System.out.println(fileName);
            String[] parts = fileName.split("_", 2);
            String county = parts[0];

            String html = readUntil.Method_ReadFile(savePath + fileName);
            Document document = Jsoup.parse(html);
            Elements elements = document.select(".car-pic-form-box.car-box-list.clear").select("li");

            for (Element element : elements) {

                String versionName = element.select("h6.name").text();
                String parameter = element.select("div.parameter").text();
                System.out.println(parameter);
                String[] parameterArr = parameter.split("/");
                String licenseDate = parameterArr[0];
                String mileage = parameterArr[1];
                String price = element.select(".price").select(".num").text();
                String source = "第一车网";

                Bean_car_ryk bean_car_ryk = new Bean_car_ryk();
                bean_car_ryk.setC_name(versionName);
                bean_car_ryk.setC_licenseDate(licenseDate);
                bean_car_ryk.setC_county(county);
                bean_car_ryk.setC_price(price);
                bean_car_ryk.setC_source(source);
                bean_car_ryk.setC_mileage(mileage);
                bean_car_ryk.setC_deatileUrl(element.select("a").attr("href"));
                bean_car_ryk.setC_fileName(fileName);
                daoFather.Method_Insert(bean_car_ryk);
            }
        }

    }

    public void Method_5_DownCountyHtmlByProxy(String savePath) {
        DaoFather daoFather = new DaoFather(2, 0);
        proxyController proxyController = new proxyController();
        for (Object obj : daoFather.Method_Find()) {


            Bean_diyiche_county bean_diyiche_county = (Bean_diyiche_county) obj;
            if (bean_diyiche_county.getC_downState().equals("否")) {

                System.out.println("开始下载-->" + bean_diyiche_county.getC_county());
                // 每次进行新的 数据项下载的时候,使用代理
//                Map<String,String> map = proxyController.Method_allControl();
//                String proxy_ip = map.get("ip");
//                String proxy_port = map.get("port");
//                String expire = map.get("expire");
//                System.out.println("----代理ip:"+proxy_ip+"---代理端口:"+proxy_port+"----代理过期时间:"+expire);

                int kk = 0;
                boolean flag = true;
                for (int i = 1; i < 51; i++) {
                    String url = bean_diyiche_county.getC_url() + "p" + i + "asdsvepcatcpbnscac/#buyCars";
                    if (i == 1) {
                        url = bean_diyiche_county.getC_url() + "#buyCars";
                    }
                    System.out.println(url);

                    String html = Method_DownFirstHtmlByProxy(url, "proxy_ip", "proxy_port");

                    if (null == html) {
                        flag = false;
                        break;
                    }

                    Document document = Jsoup.parse(html);
                    Elements elements = document.select(".car-pic-form-box.car-box-list.clear");
                    System.out.println(elements.size());
                    if (elements.size() > 0) {
                        Elements elements1 = document.select("div.not-found").select("img");
                        if (elements1.size() > 0) {
                            break;
                        } else {
                            saveUntil.Method_SaveFile(savePath + bean_diyiche_county.getC_county() + "_" + i + ".txt", html);
                        }
                    } else {
                        kk++;
                    }
                    if (kk == 1) {
                        break;
                    }
                }
                if (flag) {
                    daoFather.Method_OnlyChangeState(bean_diyiche_county.getC_ID());
                    System.out.println("下载完成---> " + bean_diyiche_county.getC_county());
                } else {
                    System.out.println("下载异常---> " + bean_diyiche_county.getC_county());
                }

            }
        }
    }


    public String httpProxy(String url, String proxy_ip, String proxy_port) {
        HttpGet request = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(new HttpHost(proxy_ip, Integer.parseInt(proxy_port)))
                .build();
        request.setConfig(requestConfig);
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .disableRedirectHandling()
                    .build();

            CloseableHttpResponse response = httpClient.execute(request);
            // Get HttpResponse Status
//            System.out.println(response.getVersion());
//            System.out.println(response.getCode());
//            System.out.println(response.getReasonPhrase());

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // return it as a String
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String Method_DownHtml(String url) {
        try {
            // 获取html页面
            Document document = Jsoup.connect(url).get();
            Random random = new Random();
            int i = random.nextInt(5000)+1000;
            Thread.sleep(i);
            return document.html();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void Method_6_DownDetailHtml(String savePath) {
        DaoFather daoFather = new DaoFather(2, 5);
        for (Object obj : daoFather.Method_Find()) {
            Bean_detail_diyi bean_detail_diyi = (Bean_detail_diyi) obj;
            if (bean_detail_diyi.getC_downState().equals("否")) {
                System.out.println("开始下载-->" + bean_detail_diyi.getC_name()+"------>"+ bean_detail_diyi.getC_ID());
                String html = Method_DownFirstHtmlByProxy(bean_detail_diyi.getC_deatileUrl(),"","");
                int C_ID = bean_detail_diyi.getC_ID();

                if (html != null) {
                    saveUntil.Method_SaveFile(savePath +  C_ID + ".txt", html);
                    daoFather.Method_OnlyChangeState(bean_detail_diyi.getC_ID());
                    System.out.println("下载完成---> " + C_ID );
                    daoFather.Method_OnlyChangeState(bean_detail_diyi.getC_ID());
                } else {
                    System.out.println("下载异常---> " + bean_detail_diyi.getC_name());
                }
            }
        }
    }


    public void Method_7_Analysis(){
        String content = readUntil.Method_ReadFile("E:\\ZKZD2024\\二手车ryk\\diyi\\diyi_detail\\63474.txt");
        Document document = Jsoup.parse(content);
        Elements elementsTitle = document.select("h1.title.de-drei-col");

        String carTitle = elementsTitle.text();


    }

    public static void main(String[] args) {
        Controller_diyi controller_diyi = new Controller_diyi();
        SaveUntil saveUntil = new SaveUntil();

//        for (int i = 1; i < 51; i++){
//            String firstUrl = "https://so.iautos.cn/quanguo/p"+i+"asdsvepcatcpbnscac/#buyCars";
//            if (i == 1){
//                 firstUrl = "https://so.iautos.cn/#buyCars";
//            }
//            String html = controller_diyi.Method_1_DownFirstHtml(firstUrl);
//            if (html != null){
//                saveUntil.Method_SaveFile("E:\\ZKZD2024\\二手车ryk\\diyi\\" + i + ".txt",html);
//            }
//        }
//        String firstUrl = "https://so.iautos.cn/";
//        String html = controller_diyi.Method_1_DownFirstHtml(firstUrl);
//        controller_diyi.Method_2_AnalysisHtmlCountry(html);

        String savePAth = "E:\\ZKZD2024\\二手车ryk\\diyi\\county\\";
//        controller_diyi.Method_4_AnalysisAllBrand(savePAth);
        String savePAth2 = "E:\\ZKZD2024\\二手车ryk\\diyi\\think_diyi\\";
//        controller_diyi.Method_4_AnalysisAllBrand(savePAth2);
//        controller_diyi.Method_4_AnalysisAllBrand(savePAth2);


        // 使用代理下载查看效果
        String proxySavePath = "E:\\ZKZD2024\\二手车ryk\\diyi\\proxyPath\\";
//        controller_diyi.Method_5_DownCountyHtmlByProxy(proxySavePath);
//        controller_diyi.Method_4_AnalysisAllBrand(proxySavePath);

        String detaileSavePath = "E:\\ZKZD2024\\二手车ryk\\diyi\\diyi_detail\\";
        controller_diyi.Method_6_DownDetailHtml(detaileSavePath);


    }
}
