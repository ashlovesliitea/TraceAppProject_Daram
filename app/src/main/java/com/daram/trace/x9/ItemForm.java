package com.daram.trace.x9;
import com.daram.trace.data.Result;
public class ItemForm {
    private String Date;
    private String Measure;
    private Result result;
    public ItemForm(String Date, String Measure,Result result){
        this.Date=Date;
        this.Measure=Measure;
        this.result = result;
    }
    public void setResult(Result result){
        this.result=result;
    }
    public Result getResult(){
        return result;
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
