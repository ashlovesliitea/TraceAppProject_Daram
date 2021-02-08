package com.example.traceappproject_daram.data;

public class LoginInfo {
    private String id;
    private String pw;

    public LoginInfo(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }
    public LoginInfo(){
        //dummy
        this.id = "daram01";
        this.pw = "dldmsdk";
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
