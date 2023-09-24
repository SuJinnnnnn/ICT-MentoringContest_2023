package com.example.sample;
//
import androidx.appcompat.app.AppCompatActivity;
//
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
//
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ValueEventListener;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity<textToSpeech> extends AppCompatActivity {
    private static final int RECORD_AUDIO_PERMISSION = 1;
    private EditText editText;
    private SpeechRecognizer speechRecognizer;
    private DatabaseReference databaseReference;
    private ListView dataListView;
    private ArrayAdapter<String> dataAdapter;
    private ArrayList<String> dataList;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        Button startButton = findViewById(R.id.startButton);

        // Firebase Realtime Database의 레퍼런스 가져오기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("list");

        // 권한 요청
        requestAudioPermission();

        // 음성 인식기 설정
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String speechText = matches.get(0);
                    editText.setText(speechText);

                    // Firebase Realtime Database에 음성 텍스트 저장
                    databaseReference.push().child("name").setValue(speechText);
                    Toast.makeText(MainActivity.this, "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }

            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

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
            public void onError(int error) {
                // 오류 처리
            }
        });

        Button btnPage = (Button) findViewById(R.id.btnPage);
        btnPage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
        // 음성 인식 시작 버튼 클릭 이벤트 처리
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechRecognition();
            }
        });

        // ListView 초기화
        dataListView = findViewById(R.id.dataListView);
        dataList = new ArrayList<>();
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        dataListView.setAdapter(dataAdapter);

        // Firebase에서 데이터 가져오기
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String data = dataSnapshot.child("name").getValue(String.class);
                    dataList.add(data);
                }
                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
            }
        });


    textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.KOREAN); // TTS 언어 설정 (예: 영어)
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(MainActivity.this, "TTS 언어 지원되지 않음", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "TTS 초기화 실패", Toast.LENGTH_SHORT).show();
            }
        }
    });

    // TTS 버튼 클릭 이벤트 처리
    Button ttsButton = findViewById(R.id.ttsButton);
        ttsButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            speakListContents();
        }
    });
}

    // 리스트 내용을 TTS로 읽어주는 메서드
    private void speakListContents() {
        StringBuilder textToSpeak = new StringBuilder();
        for (String item : dataList) {
            textToSpeak.append(item).append(". "); // 각 항목을 읽을 때마다 마침표를 추가하여 음성 간 구분
        }

        String text = textToSpeak.toString();
        if (text.length() > 0) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ttsID");
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

    private void requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용되면 작업 수행
            } else {
                Toast.makeText(this, "마이크 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startSpeechRecognition() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); // 원하는 언어 설정
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            speechRecognizer.startListening(intent);
        } else {
            requestAudioPermission();
        }
    }

}
