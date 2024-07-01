package org.example;

import org.example.Dao.DaoFather;
import org.example.Entity.Bean_DianPingContent;
import org.example.Until.ReadUntil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestWorldsMain {
    public static void main(String[] args) {


        DaoFather dao_content = new DaoFather(1, 3);

        ArrayList<Object> beanList = dao_content.Method_Find();
        for (Object bean : beanList) {
            Bean_DianPingContent bean_content = (Bean_DianPingContent) bean;
            String Content = bean_content.getC_content();


            Pattern pattern = Pattern.compile("【");
            Matcher matcher = pattern.matcher(Content);

            String modelName = bean_content.getC_modelname();
            String verisonName = bean_content.getC_versionName();



            int count = 0;
            while (matcher.find()) {
                count++;
            }
            if (count>2){
                String[] paragraphs = Content.split("【[^【】]*】"); // 使用正则表达式匹配【】之间的内容来划分段落
                for (String paragraph : paragraphs) {
                    paragraph = paragraph.trim(); // 去除段落前后的空格
                    if (!paragraph.isEmpty()) {
//                        System.out.println(paragraph); // 输出每个段落的内容
//                        System.out.println("==============================");
                    }
                }
            }else {
                if (Content.charAt(0) == '#') {
                    String[] segments = Content.substring(1).split("#");
                    for (String segment : segments) {
                        System.out.println(segment);
                        System.out.println(segment.length());


                        System.out.println("================================");
//            System.out.println("\n");
                    }
                }
            }
        }

//        ReadUntil readUntil = new ReadUntil();
//        String Content = readUntil.Method_ReadFile("F:\\ZKZD\\Java项目\\yiche_20240114\\words.txt").replace("\n", "").replace(" ","");





    }
}
