import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uiproject.R;

public class SignActivity extends Activity implements View.OnClickListener{
    Button sign_up;//팝업버튼선언
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_1);

    // 팝업버튼 설정
    sign_up=(Button)findViewById(R.id.sign_up);//R.id.alert는 팝업버튼 아이디
    sign_up.setOnClickListener(this);
    }

    public void onClick(View view){ if(view==sign_up){ //view가 alert 이면 팝업실행 즉 버튼을 누르면 팝업창이 뜨는 조건 //
        new AlertDialog.Builder(this)
                .setTitle("회원가입")
                .setMessage("회원가입")
                .setNeutralButton("닫기",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
                        //닫기 버튼을 누르면 아무것도 안하고 닫기 때문에 그냥 비움
                        }
                }) .show(); // 팝업창 보여줌
         }
    }
}


