package com.example.gonggamgak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonggamgak.DetectorActivity;
import com.example.gonggamgak.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Intent intentListen;
    SpeechRecognizer mRecognizer;
    private TextView tv_result;


    private Button btn_start, btn_objectDetection, btn_ocr, btn_tts, btn_test;

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private TextToSpeech tts;
    //wkrjdoiwefa
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bindUI();
        setTTS();


    }

    private void bindUI() {
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_objectDetection = findViewById(R.id.btn_objectDetection);
        btn_objectDetection.setOnClickListener(this);

        btn_ocr = findViewById(R.id.btn_ocr);
        btn_ocr.setOnClickListener(this);


        btn_tts = findViewById(R.id.btn_tts);
        btn_tts.setOnClickListener(this);
        btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);

        setVoiceRecognizer();
    }

    private void setVoiceRecognizer() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
            }
        }

        intentListen = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentListen.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intentListen.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

    }

    private void setTTS() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    private void handleVoiceOrder(String order) {
//        들어줘 : 전체 기능을 음성으로 사용하는 기능
//        읽어줘 : 텍스트 인식 후 사용자에게 들려주는 기능
//        보여줘 : 앞에 어떤 물체가 있는지 알려주는 기능
        switch (order) {
            case "읽어줘":
                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                startActivity(intent);
                break;
            case "보여줘":
                //객체인식에서 감지되는 모든 사물을 말해주는 기능


                break;
            case "알려줘":
                //객체인식에서 감지되는 모든 사물을 말해주는 기능

                break;
            case "찾아줘":
                tts.speak("무엇을 찾아드릴까요?", TextToSpeech.QUEUE_FLUSH, null);


                break;
            case "도와줘":
                //저장된 보호자에게 문자로 보내는 기능
                sendMessage("01055770860", "도와주세요");
                break;
        }
    }

    private void sendMessage(String phoneNumber, String content) {
        String phoneNo = phoneNumber;
        String sms = content;
        try {
            //전송
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            tts.speak("문자 보내기 성공", TextToSpeech.QUEUE_FLUSH, null);
        } catch (Exception e) {
            tts.speak("문자 보내기 실패", TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            tts.speak("원하시는 명령을 해주세요.", TextToSpeech.QUEUE_FLUSH, null);
        }

        @Override
        public void onBeginningOfSpeech() {
            tv_result.setText("듣는중입니다.");
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int i) {
            tv_result.setText("너무 늦게 말하면 오류뜹니다");
        }

        @Override
        public void onResults(Bundle bundle) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            tv_result.setText(rs[0]);
            handleVoiceOrder(rs[0]);
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                mRecognizer.startListening(intentListen);
                break;
            case R.id.btn_objectDetection:
                Intent intent = new Intent(MainActivity.this, DetectorActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_ocr:
                Intent intent2 = new Intent(MainActivity.this, ReadActivity.class);
                startActivity(intent2);
                break;

            case R.id.btn_test:
                Intent intent3 = new Intent(MainActivity.this, FirebaseActivity.class);
                startActivity(intent3);
                break;

            case R.id.btn_tts:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21("문자 보내기 성공");
                } else {
                    ttsUnder20("문자 보내기 성공");
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts !=null){
            tts.stop();
            tts.shutdown();

        }
    }
}