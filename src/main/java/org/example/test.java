package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.HttpClient;
import org.example.Until.ReadUntil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class test {

    public static String HttpPost(String mainurl, String data){
        try{
            String result = "";
            URL url = new URL(mainurl);
            // 创建 HttpURLConnection 对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 发送数据
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = data.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 获取响应
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                result = response.toString();
            }
            // 关闭连接
            connection.disconnect();
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }


    public static void main(String[] args) {

        String data_creat  = " {\"user_name\": \"ryk\",\"user_id\": \"1234567890\"}";
        String data_question = "{\"conv_uid\":\"ef02dffc-e051-11ee-8adc-0242ac110003\",\"chat_model\":\"chat_with_db_execute\",\"db_name\":\"testgas\",\"model_name\":\"proxyllm\",\"user_input\":\"内蒙兴圣2023年天然气价格\"}";

        String result = HttpPost("https://chatgas.llm.tudb.work/chat",data_question);
        System.out.println(result);


//        try {
//            // 构建数据
//            String jsonInputString = "{\"user_name\": \"ryk\", \"user_id\": \"2\"}";
//
//            // 创建 URL 对象
//            URL url = new URL("https://chatgas.llm.tudb.work/create_user_conversation_id");
//
//            // 创建 HttpURLConnection 对象
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            // 发送数据
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = jsonInputString.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            // 获取响应
//            try (BufferedReader br = new BufferedReader(
//                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
//                StringBuilder response = new StringBuilder();
//                String responseLine = null;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//                System.out.println(response.toString());
//            }
//            // 关闭连接
//            connection.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        try {
//            // 定义请求数据
//            String jsonData = "{\"conv_uid\":\"365eabee-e050-11ee-8adc-0242ac110003\"," +
//                    "\"chat_model\":\"chat_with_db_execute\"," +
//                    "\"db_name\":\"testgas\"," +
//                    "\"model_name\":\"proxyllm\"," +
//                    "\"user_input\":\"内蒙兴圣2023年价格\"}";
//
//            // 定义请求 URL
//            URL url = new URL("https://chatgas.llm.tudb.work/chat");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setDoOutput(true);
//
//            // 发送请求数据
//            OutputStream os = conn.getOutputStream();
//            os.write(jsonData.getBytes());
//            os.flush();
//
//            // 检查响应状态码
//            int responseCode = conn.getResponseCode();
////            if (responseCode == HttpURLConnection.HTTP_OK) { // 如果请求成功
//                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String inputLine;
//                StringBuffer response = new StringBuffer();
//
//                // 读取响应内容
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                // 打印响应内容
//                System.out.println(response.toString());
////            } else {
////                System.out.println("POST request failed with response code: " + responseCode);
////            }
//            conn.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }
}
