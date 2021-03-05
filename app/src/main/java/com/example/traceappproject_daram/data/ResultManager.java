package com.example.traceappproject_daram.data;
import android.content.Context;

import com.example.traceappproject_daram.data.Result;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import androidx.annotation.Nullable;

public class ResultManager{

    //Result Object 그대로 받아서 저장
    //Application Context 전달 필수
    public void saveResult(Result result,Context context){
        File file = new File(context.getFilesDir().getAbsolutePath()+result.calendar.toString());
        //mkdir:파일 생성
        //mkdirs:폴더 생성
        if(!file.exists()){
            file.mkdirs();
        }
        try{
            ObjectOutputStream oos =
                new ObjectOutputStream
                     (new FileOutputStream(file + "result.bin"));
            oos.writeObject(result);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //파일 경로 찾기는 추후 업데이트 예정
    @Nullable
    public Result readResult(String filePath,Context context){
        File file = new File(context.getFilesDir().getAbsolutePath()+filePath);

        Object object;

        try{
            ObjectInputStream ois =
                    new ObjectInputStream
                                    (new FileInputStream(file+"result.bin"));
            //bin 안의 객체가 단일 객체일시 if 문 사용
            //bin 안의 객체가 여러개 일때는 while 문 사용 -> 마지막에 EOF exception 발생
            //catch문을 이용해 잡아주기

            if((object = ois.readObject())!=null){
                Result result = (Result) object;
                ois.close();
                return result;
            }
            else {
                ois.close();
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
