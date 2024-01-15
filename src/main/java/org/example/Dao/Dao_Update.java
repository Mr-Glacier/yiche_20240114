package org.example.Dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class Dao_Update extends DaoFather {
    public Dao_Update(int choseDB, int choseTable) {
        super(choseDB, choseTable);
    }


    public boolean Method_FindExist(String columnName, String columnValue) {
        try {
            String sql = "Select * from " + this.TableName + " where " + columnName + " = " + columnValue;
            MethodCreateSomeObject();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //    根据某项ID查询对应实体
    public Object Method_Find_oneBean(String columnName, String columnValue) {
        Object result = new Object();
        try {
            String sql = "Select * from " + this.TableName + " where " + columnName + " = " + columnValue;
            MethodCreateSomeObject();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Class C = Class.forName(this.EntityName);
                Object o = C.newInstance();
//                获取列名
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
//                获取列数
                int lins = resultSetMetaData.getColumnCount();

                for (int i = 0; i < lins; i++) {
                    String columnName1 = resultSetMetaData.getColumnName(i + 1);
//                    获取值
                    Object columnValue1 = resultSet.getObject(i + 1);
                    Field field = C.getDeclaredField(columnName1);
                    field.setAccessible(true);
                    field.set(o, columnValue1);
                }
                result = o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //    如果不同进行更新update
    public void Method_Update(Object obj, String flag_columnName, String flag_columnValue) {
        try {
            Class c = obj.getClass();
            Method[] methods = c.getDeclaredMethods();
            String final_Str = "";
            for (Method method : methods) {
                if (method.getName().equals("get" + this.PrimaryKey) || method.getName().equals("getC_DownState") || method.getName().equals("getC_DownTime")) {
                    continue;
                }
                if (method.getName().startsWith("get")) {
                    String columnName = method.getName().replace("get", "");

                    String value = method.invoke(obj) == null ? "-" : method.invoke(obj).toString();
                    //如果为空则替换为-
                    if (method.getReturnType() == String.class) {
                        final_Str += columnName + "=" + "'" + value + "',";
                    } else {
                        final_Str += columnName + "=" + value + ",";
                    }
                }
            }
            final_Str = final_Str.substring(0, final_Str.length() - 1);
            String sql = "Update " + this.TableName + " set " + final_Str + " where " + flag_columnName + " = '" + flag_columnValue + "';";
//            System.out.println(sql);
            Method_IUD(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


//    对版本数据下载进行分组,组号从1开始
    public void Method_分组(){
        String sql = "WITH GroupedRows AS ( SELECT C_ID,(C_ID - 1)/10+1 AS GroupNumber FROM " + this.TableName + " )  UPDATE " + this.TableName + "  SET GroupNumber = GroupedRows.GroupNumber FROM GroupedRows  WHERE " + this.TableName + ".C_ID = GroupedRows.C_ID;";
        Method_IUD(sql);
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
