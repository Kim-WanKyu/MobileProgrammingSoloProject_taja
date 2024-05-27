package com.example.mobile_app_final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainShortActivity extends AppCompatActivity {

    TextView tvAccuracy, tvTypingKeystrokes;
    TextView tvPrevScreenText, tvPrevInputText, tvScreenText, tvInputText, tvNextScreenText;
    EditText etInput;
    Button btnSubmit, btnFinish;

    View resultDialog;
    TextView tvAccuracyDlg, tvTypingKeystrokesDlg;
    Button btnRestart, btnMain;

    long startTime; //문장 입력 시작 시간
    long endTime;   //문장 입력 종료 시간

    int charSequenceCount;  //현재 문장의 타수
    boolean isFirstInput;   //문장 첫 입력인지

    String[] proverbs;   //속담 100개 선정한 문자열 배열

    @Override
    public void onBackPressed() {  }    // 뒤로가기 버튼을 비활성화하도록 오버라이딩 (아무 작업도 수행하지 않음)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_short);
        setTitle("모바일 짧은 글(속담) 타자 연습");

        tvAccuracy = findViewById(R.id.tvAccuracy);
        tvTypingKeystrokes = findViewById(R.id.tvTypingKeystrokes);

        tvPrevScreenText = findViewById(R.id.tvPrevScreenText);
        tvPrevInputText = findViewById(R.id.tvPrevInputText);
        tvScreenText = findViewById(R.id.tvScreenText);
        tvInputText = findViewById(R.id.tvInputText);
        tvNextScreenText = findViewById(R.id.tvNextScreenText);
        etInput = findViewById(R.id.etInput);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnFinish = findViewById(R.id.btnFinish);

        resultDialog = View.inflate(MainShortActivity.this, R.layout.result_dialog, null);
        btnRestart = resultDialog.findViewById(R.id.btnRestart);
        btnMain = resultDialog.findViewById(R.id.btnMain);

        charSequenceCount = 0;  //문장 입력시 입력한 횟수
        isFirstInput = true;    //문장을 처음 입력하는지

        // proverbs.txt 파일 읽어오기
        if ( !(TxtFile.getStrings(R.raw.proverbs, this.getApplicationContext()))[0].equals("fail")) {
            proverbs = TxtFile.getStrings(R.raw.proverbs, this.getApplicationContext());
        }
        else { //파일 읽기 실패 시
            Toast.makeText(getApplicationContext(), "<짧은글 파일 읽기 실패>\n메인화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();

            finish();   //현재 액티비티 종료
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // 전환 애니메이션 제거
            overridePendingTransition(0,0); // 전환 애니메이션 제거

            startActivity(intent);  //StartActivity 시작 (시작화면으로 이동)
        }

        //프로그램 시작 시 타자관련 변수 초기화
        CalculateTaja.resetTaja();

        //프로그램 시작 시 현재 텍스트와 다음 텍스트의 문자열 가져오기
        tvScreenText.setText(proverbs[(int)(Math.random() * proverbs.length)]);
        tvNextScreenText.setText(proverbs[(int)(Math.random() * proverbs.length)]);

        //etInput 에디트텍스트 내용 변경 이벤트
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //에디트텍스트의 내용 변경 감지 시 작동

                charSequenceCount++;    //타수 증가;
                tvInputText.setText(charSequence.toString());

                if (isFirstInput) {     //처음 입력 시작이면
                    startTime = SystemClock.elapsedRealtime();  //입력 시작 시간 기록
                    isFirstInput = false;   //false 로 바꿔줌
                }

                if(charSequence.length() > 0 && charSequence.length() <= tvScreenText.getText().length()){  //현재 입력된 값이 있고, tvScreenText의 길이보다 짧아야 함.
                    //현재까지 입력한 길이까지 비교했을 때 같으면 검정 아니면 빨강 텍스트
                    if(charSequence.toString().equals(tvScreenText.getText().toString().substring(0,charSequence.length())))
                        tvInputText.setTextColor(Color.BLACK);
                    else
                        tvInputText.setTextColor(Color.RED);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        //확인 버튼 클릭 이벤트
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTime = SystemClock.elapsedRealtime();        //입력 종료 시간
                int intervalMS;
                if(startTime != 0)    //입력 시작을 하고 확인 버튼 누를 시
                    intervalMS = (int) ((endTime - startTime));  //입력 시간 차이 구하기 (millisecond)
                else    //입력 시작 없이 확인 버튼 누를 시
                    intervalMS = 0;

                //정확도, 평균타수 구해와서 문자열로 변환
                String strAccuracy = String.valueOf(CalculateTaja.calculateAccuracy(tvScreenText.getText().toString(), tvInputText.getText().toString()));
                String strStrokes = String.valueOf(CalculateTaja.calculateKeystrokes(charSequenceCount, intervalMS));
                tvAccuracy.setText(strAccuracy);
                tvTypingKeystrokes.setText(strStrokes);

                tvPrevScreenText.setText(tvScreenText.getText());
                if(tvInputText.getText().toString().equals(tvScreenText.getText().toString()))  //화면에 표시된 문자열과 입력한 문자열이 같으면 파랑
                    tvPrevScreenText.setTextColor(Color.BLUE);
                else
                    tvPrevScreenText.setTextColor(Color.RED);   //틀리면 빨강

                tvPrevInputText.setText(tvInputText.getText());
                tvScreenText.setText(tvNextScreenText.getText());
                tvInputText.setText("");
                etInput.setText("");
                tvNextScreenText.setText(proverbs[(int)(Math.random() * proverbs.length)]); // 다음 입력 문자열을 가져옴

                charSequenceCount = 0;  //입력개수를 0으로 초기화
                startTime = 0;          //입력 시작 시간을 0으로 초기화
                isFirstInput = true;    //다시 true 로 바꿔줌.
            }
        });

        //타자 종료 버튼 클릭 이벤트
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAccuracyDlg = resultDialog.findViewById(R.id.tvAccuracyDlg);
                tvTypingKeystrokesDlg = resultDialog.findViewById(R.id.tvTypingKeystrokesDlg);

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainShortActivity.this);
                dlg.setView(resultDialog);

                AlertDialog alertDialog = dlg.create();         //setCanceledOnTouchOutside() 쓰기 위해 AlertDialog 생성
                alertDialog.setCanceledOnTouchOutside(false);   //대화상자 바깥 클릭시 꺼지는 것 방지
                alertDialog.setCancelable(false);               //뒤로가기 눌러도 dialog 안 꺼지도록 처리

                try{    //만약 이미 다이얼로그 창이 떠 있으면, 다이얼로그 창을 끄고 다시 켜줌
                    if(alertDialog.isShowing())
                        alertDialog.dismiss();
                    alertDialog.show();
                }
                catch (Exception e) { e.printStackTrace(); }

                StringBuilder tmpStr1, tmpStr2;
                tmpStr1 = new StringBuilder().append("정확도: ").append(CalculateTaja.getAccuracy()).append('%');
                tmpStr2 = new StringBuilder().append("평균 타수: ").append(CalculateTaja.getKeystrokesPerSec()).append('타');
                tvAccuracyDlg.setText(tmpStr1.toString());
                tvTypingKeystrokesDlg.setText(tmpStr2.toString());

                //다시시작 버튼 클릭 이벤트
                btnRestart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();  //현재 대화상자 종료
                        finish();   //현재 액티비티 종료
                        startActivity(getIntent()); //현재 액티비티의 인텐트 가져와서 시작 -> 현재 액티비티(MainActivity) 재시작
                    }
                });

                //메인화면으로 버튼 클릭 이벤트
                btnMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();  //현재 대화상자 종료
                        finish();   //현재 액티비티 종료
                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(intent);  //StartActivity 시작 (시작화면으로 이동)
                    }
                });
            }
        });
    }
}