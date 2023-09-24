package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class pageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page);

        // 버튼들을 참조합니다.
        Button btnFirst = findViewById(R.id.btnfirst);
        Button btnSearch = findViewById(R.id.btnsearch);
        Button btnSecond = findViewById(R.id.btnsecond);
        Button btnExit = findViewById(R.id.btnexit);


        // 각 버튼에 클릭 이벤트 리스너를 설정합니다.
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
                // '장바구니' 버튼을 클릭했을 때 처리할 코드를 여기에 추가합니다.
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        scActivity.class);
                startActivity(intent);
            }
        });

        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        martActivity.class);
                startActivity(intent);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}