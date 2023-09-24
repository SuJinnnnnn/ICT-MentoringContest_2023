package com.example.sample;


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class martActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mart);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        // TextToSpeech 초기화
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.KOREAN); // TTS 언어 설정

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(martActivity.this, "TTS 언어 설정 오류", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(martActivity.this, "TTS 초기화 오류", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnPage2 = (Button) findViewById(R.id.btnPage2);
        btnPage2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        // TTS 이벤트 리스너 추가
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // TTS 시작 시 실행되는 메서드
            }

            @Override
            public void onDone(String utteranceId) {
                // TTS 완료 시 실행되는 메서드
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("장애물이 있습니다");
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {
                // TTS 오류 시 실행되는 메서드
            }
        });

        // 버튼 클릭 이벤트 처리
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText("장애물이 있습니다");
            }
        });
    }

    private void speakText(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId");
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


}
