package org.example;

import org.example.Controller.Controller_DianPing;
import org.example.Until.HelpCreateFile;

public class KouBeiMain {
    public static void main(String[] args) {
        Controller_DianPing controller = new Controller_DianPing();
        String mainPath = "F:\\A_ZKZD_2024\\yiche_dianping\\";
        String firstPath = mainPath+"firstHtml\\";
        HelpCreateFile.Method_Creat_folder(firstPath);
        String fistName = ".txt";

//        controller.Method_1_DownFirstHtml(firstPath,fistName);
//        controller.Method_2_AnalysisHtml1(firstPath,fistName);

//        String secondPath = mainPath+"secondHtml\\";
//        HelpCreateFile.Method_Creat_folder(secondPath);
////        controller.Method_3_DownPagesHtml(secondPath,fistName);
////        controller.Method_4_AnalysisPagesHtml(secondPath,fistName);
//
//        String thirdPath = mainPath+"thirdHtml\\";
//        HelpCreateFile.Method_Creat_folder(thirdPath);
//        controller.Method_5_DownDetailHtml(thirdPath,fistName);


        String JSONSavePath = mainPath+"DiapingJSON\\";


//        controller.Method_6_DownDianpingJSON(JSONSavePath,fistName);

        controller.Method_7_AnalysisDianpingJSON(JSONSavePath,fistName);



    }
}
