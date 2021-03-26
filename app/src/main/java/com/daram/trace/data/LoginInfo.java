package com.daram.trace.data;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private static String id = "not assigned id";
    private static String pw = "not assigned pw";

    public LoginInfo(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }
    public LoginInfo(){
        //dummy
        this.id = "daram01";
        this.pw = "dldmsdk";
    }
    public static String getId() {
        return id;
    }

    public static void setId(String i) {
        id = i;
    }

    public static String getPw() {
        return pw;
    }

    public static void setPw(String p) {
        pw = p;
    }
}
