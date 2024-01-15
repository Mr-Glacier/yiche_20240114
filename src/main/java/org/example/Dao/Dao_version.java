package org.example.Dao;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Dao_version extends DaoFather {
    public Dao_version(int choseDB, int choseTable) {
        super(choseDB, choseTable);
    }


    public ArrayList<Object> Method_Find_ByGroup(int Group) {
        ArrayList<Object> BeanLsit = new ArrayList<>();
        try {
            String sql = "Select * from " + this.TableName +" where GroupNumber = "+Group;
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
                    String columnName = resultSetMetaData.getColumnName(i + 1);
//                    获取值
                    Object columnValue = resultSet.getObject(i + 1);
                    Field field = C.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(o, columnValue);
                }
                BeanLsit.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BeanLsit;
    }

    public void Method_分组(){
        String sql = "WITH GroupedRows AS ( SELECT C_ID,(C_ID - 1)/10+1 AS GroupNumber FROM " + this.TableName + " )  UPDATE " + this.TableName + "  SET GroupNumber = GroupedRows.GroupNumber FROM GroupedRows  WHERE " + this.TableName + ".C_ID = GroupedRows.C_ID;";
        Method_IUD(sql);
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void Method_ChangeState(int Group) {
        String sql = "update " + this.TableName + " set C_DownState = '是'  where GroupNumber = " + Group;
        Method_IUD(sql);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());
        String sql1 = "update " + this.TableName + " set C_DownTime = '" + time + "'  where GroupNumber = " + Group;
        Method_IUD(sql1);
    }


}
