package com.example.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

public class scActivity extends AppCompatActivity {


    private TextView resultTextView;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        resultTextView = findViewById(R.id.resultTextView);

        // Firebase Realtime Database의 "data" 노드에 접근
        databaseReference = FirebaseDatabase.getInstance().getReference("product");

        // ValueEventListener를 사용하여 데이터 검색
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 데이터가 변경될 때 호출되는 메서드
                if (dataSnapshot.exists()) {
                    // 데이터가 있는 경우
                    String data = dataSnapshot.getValue(String.class);
                    resultTextView.setText(data);
                } else {
                    // 데이터가 없는 경우
                    resultTextView.setText("데이터가 없습니다.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultTextView.setText("데이터베이스 오류: " + databaseError.getMessage());
            }
        });


        Button btnPage3 = (Button) findViewById(R.id.btnPage3);
        btnPage3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }
}
