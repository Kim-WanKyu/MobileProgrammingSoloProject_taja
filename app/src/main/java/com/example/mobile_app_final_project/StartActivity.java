package com.example.mobile_app_final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class StartActivity extends Activity {

    Button btnStart;

    View startDialog;
    Button btnStartWord, btnStartShort, btnReturn;

    @Override
    public void onBackPressed() {  }    // 뒤로가기 버튼을 비활성화하도록 오버라이딩 (아무 작업도 수행하지 않음)

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnStart = (Button) findViewById(R.id.btnStart);

        startDialog = View.inflate(StartActivity.this, R.layout.start_dialog, null);    //시작 다이얼로그
        btnStartWord = startDialog.findViewById(R.id.btnStartWord);
        btnStartShort = startDialog.findViewById(R.id.btnStartShort);
        btnReturn = startDialog.findViewById(R.id.btnReturn);

        //시작하기 버튼 클릭 이벤트
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(StartActivity.this);
                dlg.setView(startDialog);

                AlertDialog alertDialog = dlg.create();         //setCanceledOnTouchOutside() 쓰기 위해 AlertDialog 생성
                alertDialog.setCanceledOnTouchOutside(false);   //대화상자 바깥 클릭시 꺼지는 것 방지
                alertDialog.setCancelable(false);               //뒤로가기 눌러도 dialog 안 꺼지도록 처리

                alertDialog.show();

                //단어 연습 버튼 클릭 이벤트
                btnStartWord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();  //현재 대화상자 종료
                        finish();   //현재 액티비티 종료
                        Intent intent = new Intent(getApplicationContext(), MainWordActivity.class);
                        startActivity(intent);  //MainWordActivity 시작 (단어 연습 으로 이동)
                    }
                });

                //짧은 글 연습(속담) 버튼 클릭 이벤트
                btnStartShort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainShortActivity.class);
                        startActivity(intent);  //MainShortActivity 시작 (짧은 글 연습 으로 이동)
                    }
                });

                //뒤로가기 버튼 클릭 이벤트
                btnReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();  //현재 대화상자 종료
                        finish();
                        Intent intent = getIntent();  //현재 StartActivity
                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);    // 변환 효과 애니메이션 제거
                        startActivity(intent);  //현재 StartActivity 다시 시작
                        overridePendingTransition(0, 0);    // 변환 효과 애니메이션 제거
                    }
                });

            }
        });

    }
}