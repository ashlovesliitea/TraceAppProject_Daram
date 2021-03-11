package com.example.traceappproject_daram.x9;

public class ItemForm {
    private String Date;
    private String Measure;

    public ItemForm(String Date, String Measure){
        this.Date=Date;
        this.Measure=Measure;

    }

    public String getDate() {
        return Date;
    }

    public String getMeasure() {
        return Measure;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setMeasure(String measure) {
        Measure = measure;
    }
}
