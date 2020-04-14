package com.example.gonggamgak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLIENT_ID = ""; // "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    private RecognitionHandler  handler;
    private NaverRecognizer naverRecognizer;
    private String mResult;
    private AudioWriterPCM writer;


    private Button btn_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();
    }
    private void bindUI(){
        btn_call = findViewById(R.id.btn_call);
        btn_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_call:
                Toast.makeText(getApplicationContext(),"호출 버튼 클릭",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
