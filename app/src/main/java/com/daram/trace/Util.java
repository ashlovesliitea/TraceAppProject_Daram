package com.daram.trace;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daram.trace.data.LoginInfo;
import com.daram.trace.data.Result;
import com.daram.trace.reprot_page.heatmap.FeetMultiFrames;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.daram.trace.data.Cons.IMG_EXT;
import static com.daram.trace.data.Cons.NAME_EXT;

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
        String filename= id+"/"+sdf.format(calendar.getTime())+"/"+idx+ IMG_EXT/*".png"*/;//현재 시간으로 파일명 지정 20191023142634


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
    public static void deleteObject(Serializable o,String path,int type) {
        //path argument는 make_folder함수 사용해 만들기
        //result.bin을 여기서 붙여주자
        Log.i(TAG,"attempt deleting result : "+path);
        path +="/"+NAME_EXT[type];
        File file = new File(path);
        if(file.exists()){
            file.delete();
            Log.i(TAG,"really deleting result : "+path);
        }
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

    public static void savePicker(Context context,Picker p){
        String upperPath = context.getFilesDir().getPath().toString();//그냥 캐시 path

        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(upperPath+"/picker.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(p);
            fout.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Picker retrievePicker(Context context){
        String upperPath = context.getFilesDir().getPath().toString();//그냥 캐시 path
        FileInputStream fin = null;
        File file = new File("picker.bin");
        Picker picker =null;
        try {
            fin = new FileInputStream(upperPath+"/picker.bin");
            ObjectInputStream ois = new ObjectInputStream(fin);
            picker=(Picker) ois.readObject();
            fin.close();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return picker;
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
    private static String TAG = "Util";
    public static void sendResultRecord(Context context,Result result) {
        String id=LoginInfo.getId();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
        String measure_date=sdf.format(result.getCalendar().getTime());
        String folderpath= id+"/"+measure_date;
        String arch=Integer.toString(result.getLeftArchLevel());//아치 단계
        String heel=Integer.toString(result.getLeftBackLevel());//꿈치 단계
        String admin_send="true";


        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://clean-circuit-303203.appspot.com/communicatewAndroid/sendMeasure.jsp";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"send record to server resp : "+response);
                Toast myToast = Toast.makeText(context,"서버 전달 완료!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast myToast = Toast.makeText(context,"서버 전달 실패!", Toast.LENGTH_SHORT);
                Log.i(TAG,"send record fail : "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id",id);
                params.put("measure_date",measure_date);
                params.put("folderpath",folderpath);
                params.put("arch",arch);
                params.put("heel",heel);
                params.put("admin_send",admin_send);
                return params;
            }
        };

        stringRequest.setTag("main");
        queue.add(stringRequest);
    }

}