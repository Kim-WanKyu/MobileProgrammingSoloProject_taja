package com.example.mobile_app_final_project;

//타자 계산하는 클래스
public class CalculateTaja{

    private static int ACCURACY;            //정확도
    private static int KEYSTROKES_PER_SEC;  //평균 타수
    public static int getAccuracy() { return ACCURACY; }  //정확도 리턴 메소드
    public static int getKeystrokesPerSec() { return KEYSTROKES_PER_SEC; }    //평균 타수 리턴 메소드

    //정확도 계산 변수
    private static long TOTAL_LENGTH = 0;           //총 입력 문자열
    private static long TOTAL_CORRECT_COUNT = 0;    //총 맞은 개수

    //평균 타수 계산 변수
    private static long TOTAL_INTERVAL_SEC = 1;      //총 입력 시간(초)
    private static long TOTAL_CHAR_SEQUENCE_COUNT = 0; //총 타수

    //정확도 계산 메소드
    public static int calculateAccuracy(String screenStr, String inputStr){
        int correctCount = 0;

        if (screenStr.length() >= inputStr.length()) {
            for(int i = 0; i < screenStr.length(); i++){    //맞은 문자 개수 체크
                if (inputStr.length() > i){
                    if (screenStr.charAt(i) == inputStr.charAt(i))
                        correctCount++;
                }
                else {
                    break;
                }
            }
            TOTAL_LENGTH += screenStr.length();
        }
        else {
            for(int i=0; i< inputStr.length(); i++){
                if (screenStr.length() > i){
                    if (screenStr.charAt(i) == inputStr.charAt(i))
                        correctCount++;
                }
                else {
                    break;
                }

            }
            TOTAL_LENGTH += inputStr.length();
        }

        TOTAL_CORRECT_COUNT += correctCount;
        System.out.println("TOTAL_CORRECT_COUNT: " + TOTAL_CORRECT_COUNT + " TOTAL_LENGTH: " + TOTAL_LENGTH);

        ACCURACY = (int) ( (double) TOTAL_CORRECT_COUNT / TOTAL_LENGTH * 100);
        return ACCURACY;
    }

    //평균 타수 계산 메소드
    public static int calculateKeystrokes(int charSequenceCount, int milliseconds){
        if(charSequenceCount > 0){
            TOTAL_CHAR_SEQUENCE_COUNT += charSequenceCount;
        }
        if(milliseconds > 0){
            TOTAL_INTERVAL_SEC += (int)(milliseconds/1000.0);
        }
        System.out.println("TOTAL_CHAR_SEQUENCE_COUNT: " + TOTAL_CHAR_SEQUENCE_COUNT + " TOTAL_INTERVAL_SEC: " + TOTAL_INTERVAL_SEC);

        KEYSTROKES_PER_SEC = (int) ( (double) TOTAL_CHAR_SEQUENCE_COUNT / TOTAL_INTERVAL_SEC * 60);
        return KEYSTROKES_PER_SEC;
    }

    //초기화 메소드
    public static void resetTaja(){
        ACCURACY = 0;
        KEYSTROKES_PER_SEC = 0;
        TOTAL_LENGTH = 0;
        TOTAL_CORRECT_COUNT = 0;
        TOTAL_INTERVAL_SEC = 1; //시간이 0초가 되면 안되므로 1로 초기화
        TOTAL_CHAR_SEQUENCE_COUNT = 0;
    }
}