package org.example;

import org.example.Controller.Controller_yiche;
import org.example.Until.HelpCreateFile;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Controller_yiche CY = new Controller_yiche();

        String mainPath = "E:\\ZKZD2024\\易车网\\";
        String datePath = mainPath + "20240311\\";
        HelpCreateFile.Method_Creat_folder(datePath);

        
        String brand_savePath = datePath+"brand\\";
        HelpCreateFile.Method_Creat_folder(brand_savePath);
//        String brand_url= "https://car.yiche.com/";
        String brand_url= "https://mapi.yiche.com/web_api/car_model_api/api/v1/master/get_master_list?";

        String brand_fileName = "brand_Json.txt";
//        下载品牌页面
//        CY.Method_1_RequestYiche_Brand(brand_savePath,brand_url,brand_fileName);

//        解析品牌页面
//        CY.Method_2_Analysis_Yiche_Brand(brand_savePath, brand_fileName);


        String model_savePath = datePath+"model\\";
        HelpCreateFile.Method_Creat_folder(model_savePath);
        String model_API = "https://mapi.yiche.com/web_api/car_model_api/api/v1/brand/get_brand_list?";
        String modelfileName = "_models.txt";

//        3.下载厂商,车型页面
//        CY.Method_3_Down_FactoryANDModel(model_savePath,model_API,modelfileName);

//        4.解析厂商页面,车型页面
//        CY.Method_4_Analysis_models(model_savePath, modelfileName);

        String versionListAPI = "https://mapi.yiche.com/web_api/car_model_api/api/v1/car/car_list_condition?";
        String version_savePath =datePath+"\\version\\";
        HelpCreateFile.Method_Creat_folder(version_savePath);
        String version_fileName = "_version.txt";

//        5.下载版本数据  1.5 H
//        for (int i = 0; i < 5; i++) {
//            CY.Method_5_Down_version(version_savePath, versionListAPI, version_fileName);
//        }

//        6.解析版本数据
        CY.Method_6_Analysis_version(version_savePath, version_fileName);


        String Config_savePath =datePath+"Config\\";
        HelpCreateFile.Method_Creat_folder(Config_savePath);
        String Config_fileName = "_Config.txt";
        String ConfigAPI= "https://mhapi.yiche.com/hcar/h_car/api/v1/param/get_param_details?";

//        7.下载版本配置数据
//        CY.Method_7_Down_versionConfig(Config_savePath,ConfigAPI,Config_fileName);

//        8.解析版本配置数据
//        CY.Method_8_Analysis_Config(Config_savePath, Config_fileName);



    }
}