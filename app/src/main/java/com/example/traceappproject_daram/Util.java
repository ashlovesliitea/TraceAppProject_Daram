package com.example.traceappproject_daram;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.example.traceappproject_daram.data.LoginInfo;
import com.example.traceappproject_daram.data.Result;
import com.example.traceappproject_daram.reprot_page.heatmap.FeetMultiFrames;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.IconCompat;

import static com.example.traceappproject_daram.data.Cons.IMG_EXT;
import static com.example.traceappproject_daram.data.Cons.NAME_EXT;

public class Util {
    public static int idx = 0;

    public static String cvtBytesToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes,"UTF-8");
    }
    public static byte[] cvtStringToByte(String s){
        return s.getBytes();
    }

    public static final String makeFolderPath(Context context,Calendar calendar){
        String upperPath = context.getFilesDir().getPath().toString();//그냥 캐시 path
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");//날짜(시간까지)
        String strDate = sdf.format(calendar.getTime());
        return upperPath+"/"+strDate;
    }
    //context가 같으면 filedir도 같을 것이다.
    public static final String make_path(Context context, Calendar calendar, int idx){//full path
        String upperPath = context.getFilesDir().getPath().toString();//그냥 캐시 path
        String pureName = Integer.toString(idx);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");//날짜(시간까지)
        String strDate = sdf.format(calendar.getTime());
        return upperPath+"/"+strDate+"/"+pureName+IMG_EXT;
    }

    public static void showWarning(AppCompatActivity cmp,String msg){
        AlertDialog.Builder a=new AlertDialog.Builder(cmp);
        a.setTitle("Warning-Message");
        a.setMessage(msg);
        a.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        a.show();
    }
    //overload
    public static void clickUpload(Context context,String id, Calendar calendar, int idx) throws FileNotFoundException {
        //firebase storage에 업로드하기

        //1. FirebaseStorage을 관리하는 객체 얻어오기
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();

        //2. 업로드할 파일의 node를 참조하는 객체(파이어베이스쪽)
        //파일 명이 중복되지 않도록 날짜를 이용

        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
        String filename= id+"/"+sdf.format(calendar)+idx+ IMG_EXT/*".png"*/;//현재 시간으로 파일명 지정 20191023142634


        //원래 확장자는 파일의 실제 확장자를 얻어와서 사용해야함. 그러려면 이미지의 절대 주소를 구해야함.

        StorageReference imgRef= firebaseStorage.getReference("uploads/"+filename);
        //uploads라는 폴더가 없으면 자동 생성

        //참조 객체를 통해 이미지 파일 업로드
        // imgRef.putFile(imgUri);
        //업로드 결과를 받고 싶다면..
        String imgUri = Util.make_path(context,calendar,idx);
        InputStream stream = new FileInputStream(new File(imgUri));

        UploadTask uploadTask = imgRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("Util","firebase file upload fail : ");
                exception.getStackTrace();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.i("Util","firebase file upload success :");
            }
        });

        //업로드한 파일의 경로를 firebaseDB에 저장하면 게시판 같은 앱도 구현할 수 있음.
    }

    public static void storeObject(Serializable o,String path,int type) {
        //path argument는 make_folder함수 사용해 만들기
        //result.bin을 여기서 붙여주자
        File directory = new File(path);
        if (!directory.exists()) {       // 원하는 경로에 폴더가 있는지 확인

            directory.mkdirs();    // 하위폴더를 포함한 폴더를 전부 생성

        }

        path +="/"+NAME_EXT[type];
        FileOutputStream fout = null;

        try {
            fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(o);
            fout.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //나중에 형변환 필요
    public static Result reviveResult(String path){
        path +="/"+NAME_EXT[0];
        FileInputStream fin = null;
        Result obj = null;
        try{
            fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            obj = (Result) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
    public static FeetMultiFrames reviveFrames(String path){
        path +="/"+NAME_EXT[1];
        FileInputStream fin = null;
        FeetMultiFrames obj = null;
        try{
            fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            obj = (FeetMultiFrames) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
    //fullpath말고
    public static void uploadObjToFire(Calendar calendar, Context context,int type,String id){

        //1. FirebaseStorage을 관리하는 객체 얻어오기
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();

        //2. 업로드할 파일의 node를 참조하는 객체(파이어베이스쪽)
        //파일 명이 중복되지 않도록 날짜를 이용
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");//날짜(시간까지)
        String strDate = sdf.format(calendar.getTime());
        String filename= strDate+"/"+NAME_EXT[type];

        //원래 확장자는 파일의 실제 확장자를 얻어와서 사용해야함. 그러려면 이미지의 절대 주소를 구해야함.

        StorageReference imgRef= firebaseStorage.getReference("uploads/"+id+"/"+filename);
        //uploads라는 폴더가 없으면 자동 생성

        //참조 객체를 통해 이미지 파일 업로드
        // imgRef.putFile(imgUri);
        //업로드 결과를 받고 싶다면..
        String path = makeFolderPath(context,calendar);
        String filePhone = path +"/"+NAME_EXT[type];

        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(filePhone));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = imgRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("Util","firebase file upload fail : ");
                exception.getStackTrace();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.i("Util","firebase file upload success :");
            }
        });


    }
}