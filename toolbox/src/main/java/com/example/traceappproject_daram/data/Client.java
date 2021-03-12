package com.example.traceappproject_daram.data;

public class Client {
    private LoginInfo loginInfo;
    private String name;
    private String phone;
    //private String Shipping;//근데 이건 주문내역이 없어지니까 없어질 것
    private int feetSize;
    private String gender;
    private int weight;
    private int height;

    public Client(LoginInfo loginInfo, String name, String phone, int feetSize, String gender, int weight, int height) {
        this.loginInfo = loginInfo;
        this.name = name;
        this.phone = phone;
        this.feetSize = feetSize;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
    }
    //회원가입 창에서 바로 쓸 때
    public Client(String id, String pw,String name, String phone, int feetSize, String gender, int weight, int height) {
        this.loginInfo.setId(id);
        this.loginInfo.setPw(pw);
        this.name = name;
        this.phone = phone;
        this.feetSize = feetSize;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
    }
    public String getID(){
        return loginInfo.getId();
    }
    public String getPw(){
        return loginInfo.getPw();
    }
    //아래는 걍 위에 있는 변수 getter setter
    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getFeetSize() {
        return feetSize;
    }

    public void setFeetSize(int feetSize) {
        this.feetSize = feetSize;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
