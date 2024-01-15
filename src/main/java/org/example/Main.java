package org.example;

import org.example.Controller.Controller_yiche;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        Controller_yiche CY = new Controller_yiche();

        String brand_savePath = "E:\\ZKZD2024\\易车网\\20240114\\brand\\";
        String brand_url= "https://car.yiche.com/";
        String brand_fileName = "brand_html.txt";
//        下载品牌页面
//        CY.Method_1_RequestYiche_Brand(brand_savePath,brand_url,brand_fileName);

//        解析品牌页面
//        CY.Method_2_Analysis_Yiche_Brand(brand_savePath, brand_fileName);


        String model_savePath = "E:\\ZKZD2024\\易车网\\20240114\\model\\";
        String model_API = "https://mapi.yiche.com/web_api/car_model_api/api/v1/brand/get_brand_list?";
        String modelfileName = "_models.txt";

//        3.下载厂商,车型页面
//        CY.Method_3_Down_FactoryANDModel(model_savePath,model_API,modelfileName);

//        4.解析厂商页面,车型页面
//        CY.Method_4_Analysis_models(model_savePath, modelfileName);



        String versionListAPI = "https://mapi.yiche.com/web_api/car_model_api/api/v1/car/car_list_condition?";
        String version_savePath ="E:\\ZKZD2024\\易车网\\20240114\\version\\";
        String version_fileName = "_version.txt";

//        5.下载版本数据
//        for (int i = 0; i < 5; i++) {
//            CY.Method_5_Down_version(version_savePath, versionListAPI, version_fileName);
//        }

//        6.解析版本数据
//        CY.Method_6_Analysis_version(version_savePath, version_fileName);


        String Config_savePath ="E:\\ZKZD2024\\易车网\\20240114\\Config\\";
        String Config_fileName = "_Config.txt";
        String ConfigAPI= "https://mhapi.yiche.com/hcar/h_car/api/v1/param/get_param_details?";

//        7.下载版本配置数据
//        CY.Method_7_Down_versionConfig(Config_savePath,ConfigAPI,Config_fileName);

//        8.解析版本配置数据
        CY.Method_8_Analysis_Config(Config_savePath, Config_fileName);


    }
}