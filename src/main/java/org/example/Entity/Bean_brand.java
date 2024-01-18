package org.example.Entity;

public class Bean_brand {
    private int C_ID;

    private String C_备注="空";
    private String C_备注时间="空";
    private String C_letter="空";
    private String C_brand_id="空";
    private String C_brand_name="空";
    private String C_brand_url="空";
    private String C_brand_picurl="空";
    private String C_DownState="空";
    private String C_DownTime="空";

    public int getC_ID() {
        return C_ID;
    }

    public void setC_ID(int c_ID) {
        C_ID = c_ID;
    }

    public String getC_letter() {
        return C_letter.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public void setC_letter(String c_letter) {
        C_letter = c_letter.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public String getC_brand_id() {
        return C_brand_id.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public void setC_brand_id(String c_brand_id) {
        C_brand_id = c_brand_id.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public String getC_brand_name() {
        return C_brand_name.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public void setC_brand_name(String c_brand_name) {
        C_brand_name = c_brand_name.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public String getC_brand_url() {
        return C_brand_url.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public void setC_brand_url(String c_brand_url) {
        C_brand_url = c_brand_url.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public String getC_brand_picurl() {
        return C_brand_picurl.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public void setC_brand_picurl(String c_brand_picurl) {
        C_brand_picurl = c_brand_picurl.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public String getC_DownState() {
        return C_DownState.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public void setC_DownState(String c_DownState) {
        C_DownState = c_DownState.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public String getC_DownTime() {
        return C_DownTime.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public void setC_DownTime(String c_DownTime) {
        C_DownTime = c_DownTime.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }


    public String getC_备注() {
        return C_备注.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public void setC_备注(String c_备注) {
        C_备注 = c_备注.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public String getC_备注时间() {
        return C_备注时间.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
    }

    public void setC_备注时间(String c_备注时间) {
        C_备注时间 = c_备注时间.replace(" ","").replace("\t","").replace("\n","").replace("\r","");
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
