package com.example.traceappproject_daram.comm_opensource;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.traceappproject_daram.Util;
import com.example.traceappproject_daram.data.Cons;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SendImgs {
    //send multiple images

    private static final String ROOT_URL = "http://seoforworld.com/api/v1/file-upload.php";//예제 코드에서 열어둔 서버 java 에서 동작하는지 확인할 필요 있음
    private static final int REQUEST_PERMISSIONS = 100;//안씀
    private static final int PICK_IMAGE_REQUEST =1 ;//안씀
    private String TAG ="OPEN_SENDIMGS";
    private Calendar resultCalendar;
    private int sz;

    private Activity parent;
    public SendImgs(Calendar resultCalendar, int sz, Activity parent){//계열 이름만 받기
        this.parent = parent;
        this.resultCalendar = resultCalendar;
        this.sz = sz;
        //check permission and upload if everything is fine
        if ((ContextCompat.checkSelfPermission(parent,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(parent,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(parent,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(parent,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(parent,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            Log.e("Else", "Else");
            //showFileChooser();
            //bitmap 바꿔가면서 upload
            for(int i = 0;i<sz;i++){
                String path = Util.make_path(parent, resultCalendar, i);
                Bitmap bitmap = readBitmap(resultCalendar, i);
                uploadBitmap(bitmap);
            }
        }
    }
    public Bitmap readBitmap(/*String strFilePath,String filename*/Calendar resultTime, int idx){
        String fullpath = Util.make_path(parent, resultTime,idx);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(fullpath, options);
        return bitmap;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.i(TAG,"onresponse : "+obj.getString("message"));
                        } catch (JSONException e) {
                            Log.i(TAG,"ONRESPONSE JSONEXCEPTION");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(parent, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG,""+error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        //adding the request to volley
        Volley.newRequestQueue(parent).add(volleyMultipartRequest);
    }
}