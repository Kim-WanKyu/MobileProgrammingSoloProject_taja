package com.example.mobile_app_final_project;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

// 텍스트파일 관련 클래스
public class TxtFile {

    //fileId 의 파일 읽어와서 문자열 배열 리턴하는 메소드 / 실패 시 new String[] {"fail"} 리턴
    static String[] getStrings(int fileId, Context context){ //  Activity 가 아닌 클래스에서 getResources()를 사용하기 위해 Activity 를 갖는 호출 클래스의 Context 를 받아옴.
        String[] strings;   //리턴할 문자열 배열
        try {
            InputStream inputStream = context.getResources().openRawResource(fileId);
            byte[] txt = new byte[inputStream.available()]; //읽을 수 있는 만큼 txt로 읽을 수 있게하고
            inputStream.read(txt);  //txt 만큼 읽기

            String tmpAllStrings = new String(txt);   //일단 전체 내용을 받아온 것을 문자열로 바꾸어 tmpAllWords 에 저장.
            inputStream.close();    //inputStream 닫기

            strings = tmpAllStrings.split("\n");    //개행으로 나누어진 각 문자열을 "\n"로 분리해 줌.

            //개행하면서 남은 문자열 양 옆의 공백 제거 "\r" 등
            for (int i=0; i< strings.length; i++) {
                strings[i] = strings[i].trim();
            }
            return strings;
        }
        catch (FileNotFoundException e) {
            System.out.println("words.txt 파일 없음");
            e.printStackTrace();
            return new String[] {"fail"};
        }
        catch (IOException e) {
            System.out.println("words.txt 파일 읽기 실패");
            e.printStackTrace();
            return new String[] {"fail"};
        }
    }
}
