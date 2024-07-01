package org.example.Controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TestMain {

    public static void main(String[] args) {
        String url = "https://shenyu.cheyipai.com/auction/biz/auctionListController/getAuctionCarList.json";
        String jsonData = "{\"lastAuctionId\":\"1470725242541113345\",\"pageIndex\":2,\"pageSize\":10,\"activityCode\":\"\",\"pageType\":3,\"auctionScreenListQuery\":{\"quickConditionInfoVOS\":[]}}";
        String cookie = "JSESSIONID=0CC3C6DEDCB0D99875975A6377BF47D8; acw_tc=b65cfd2c17177396994197960e6dd3f4a85b41edde571f6b13e60b55b2e3f0";

        try {
            Document document = Jsoup.connect(url)
                    .header("Content-Type", "application/json")
                    .header("Cookie", cookie)
                    .requestBody(jsonData)
                    .ignoreContentType(true)
                    .post();

            // 打印响应
            System.out.println(document.body().text());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
