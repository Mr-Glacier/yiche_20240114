package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于调用 品易 代理
 *
 */
public class proxyController {


    /**
     * 获取代理地址方法
     * 发送get请求
     * 得到响应数据
     * count : 每次提取的数量
     * type : 代理类型 数据格式（1：txt，2：json，3：html）
     * port : 代理协议 代理协议（1：http，2：https，11：socks5）
     * yys : 运营商
     * https://http.py.cn/api/#api_syff 获取代理配置详情
     *
     * @return
     */
    public String Method_getProxy(){
        String result = null;
        try{
            String url = "http://zltiqu.pyhttp.taolop.com/getip?count=40&neek=90886&type=2&yys=0&port=1&sb=&mr=2&sep=0&ts=1";
            Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .timeout(10000)
                    .execute();
            if (response.statusCode() == 200){
//                System.out.println(response.body());
                result = response.body();
            }
        }catch (Exception e){
            throw new RuntimeException(e+"提取代理IP->发送错误");
        }
        return result;
    }


    /**
     * 返回一个 Map
     *
     */
    public Map<String,String> Method_allControl(){

        // 获取代理
        String content= Method_getProxy();

        // 处理代理,找出过期时间较长的一个
        if (content != null){
            JSONObject jsonObject = JSONObject.parseObject(content);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            List<Map<String,String>> mapList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String ip = jsonObject1.getString("ip");
                String port = jsonObject1.getString("port");
                String expire = jsonObject1.getString("expire_time");
                Map<String,String> map = new HashMap<>();
                map.put("ip",ip);
                map.put("port",port);
                map.put("expire",expire);
                mapList.add(map);
            }

            //找出最大的那一个
            mapList.sort((o1, o2) -> {
                String expire1 = o1.get("expire");
                String expire2 = o2.get("expire");
                return expire1.compareTo(expire2);
            });
            // 找出expire最大的Map
            Map<String, String> maxExpireTimeProxy = mapList.get(mapList.size() - 1);

            if (maxExpireTimeProxy != null){
                System.out.println(maxExpireTimeProxy);
            }
            return maxExpireTimeProxy;
        }
        return null;
    }



}
