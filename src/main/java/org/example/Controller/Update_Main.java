package org.example.Controller;

import org.example.Until.HelpCreateFile;

public class Update_Main {
    public static void main(String[] args) {

        Controller_Update CU = new Controller_Update();

//        主要存放路径
        String main_Path = "E:\\ZKZD2024\\易车网\\";
//        日期路径
        String date = "20240115";//此处需要手动填写

        String date_path = main_Path + date + "\\";
        HelpCreateFile.Method_Creat_folder(date_path);

        String brand_savePath = date_path + "brand\\";
        String model_savePath = date_path + "model\\";
        String version_savePath = date_path + "version\\";
        String Config_savePath = date_path + "Config\\";
        HelpCreateFile.Method_Creat_folder(brand_savePath);
        HelpCreateFile.Method_Creat_folder(model_savePath);
        HelpCreateFile.Method_Creat_folder(version_savePath);
        HelpCreateFile.Method_Creat_folder(Config_savePath);

        String first_url = "https://car.yiche.com/";
        String first_fileName = "brand_html.txt";

//        1.下载品牌所在文件
//        CU.Method_1_Down_brand(brand_savePath, first_url, first_fileName);

//        2.更新品牌
//        CU.Method_2_Analysis_brand(brand_savePath,first_fileName);

        String modelAPI = "https://mapi.yiche.com/web_api/car_model_api/api/v1/brand/get_brand_list?";
        String model_fileName = "_models.txt";

//        3.下载车型页面
//        CU.Method_3_Down_model(model_savePath,modelAPI,model_fileName);

//        4.更新车型
//        CU.Method_4_Analysis_model(model_savePath,model_fileName);


        String verionAPI = "https://mapi.yiche.com/web_api/car_model_api/api/v1/car/car_list_condition?";
        String version_fileName = "_version.txt";
//        5.下载版本JSON
//        CU.Method_5_Down_version(version_savePath,verionAPI,version_fileName);

//        6.更新版本
//        更新版本信息数据需要 2H
//        CU.Method_6_Analysis_version(version_savePath, version_fileName);

        String ConfigAPI = "https://mhapi.yiche.com/hcar/h_car/api/v1/param/get_param_details?";
        String Config_fileName = "_Config.txt";

//        7.下载版本配置信息  5:50
//        CU.Method_7_Down_Config(Config_savePath, ConfigAPI, Config_fileName);

//        8.更新版本配置信息
//        更新版本配置数据需要 2H
        CU.Method_8_Analysis_Config(Config_savePath,Config_fileName);
    }
}
