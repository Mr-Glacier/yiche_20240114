package org.example.Entity;

public class Bean_brand {
    private int C_ID;

    private String C_备注;
    private String C_备注时间;

    public String getC_备注() {
        return C_备注;
    }

    public void setC_备注(String c_备注) {
        C_备注 = c_备注;
    }

    public String getC_备注时间() {
        return C_备注时间;
    }

    public void setC_备注时间(String c_备注时间) {
        C_备注时间 = c_备注时间;
    }

    private String C_letter;
    private String C_brand_id;
    private String C_brand_name;
    private String C_brand_url;
    private String C_brand_picurl;
    private String C_DownState;
    private String C_DownTime;

    public int getC_ID() {
        return C_ID;
    }

    public void setC_ID(int c_ID) {
        C_ID = c_ID;
    }

    public String getC_letter() {
        return C_letter;
    }

    public void setC_letter(String c_letter) {
        C_letter = c_letter;
    }

    public String getC_brand_id() {
        return C_brand_id;
    }

    public void setC_brand_id(String c_brand_id) {
        C_brand_id = c_brand_id;
    }

    public String getC_brand_name() {
        return C_brand_name;
    }

    public void setC_brand_name(String c_brand_name) {
        C_brand_name = c_brand_name;
    }

    public String getC_brand_url() {
        return C_brand_url;
    }

    public void setC_brand_url(String c_brand_url) {
        C_brand_url = c_brand_url;
    }

    public String getC_brand_picurl() {
        return C_brand_picurl;
    }

    public void setC_brand_picurl(String c_brand_picurl) {
        C_brand_picurl = c_brand_picurl;
    }

    public String getC_DownState() {
        return C_DownState;
    }

    public void setC_DownState(String c_DownState) {
        C_DownState = c_DownState;
    }

    public String getC_DownTime() {
        return C_DownTime;
    }

    public void setC_DownTime(String c_DownTime) {
        C_DownTime = c_DownTime;
    }


    @Override
    public String toString() {
        return "Bean_brand{" +
                "C_letter='" + C_letter + '\'' +
                ", C_brand_id='" + C_brand_id + '\'' +
                ", C_brand_name='" + C_brand_name + '\'' +
                ", C_brand_url='" + C_brand_url + '\'' +
                ", C_brand_picurl='" + C_brand_picurl + '\'' +
                '}';
    }
}
