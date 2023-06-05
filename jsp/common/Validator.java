package com.common;

import java.util.regex.Pattern;

public class Validator {
    // 선택값일 때 데이터 검사
    // 필수값일 때 데이터 검사
    public static boolean isValidated(String data, String regexp, boolean isEssential) {
        boolean isValidated = true;

        if (isEssential) { // 필수값일 경우
            if (data == null || data.equals("") || !Pattern.matches(regexp, data)) { // 자바스크립트에서 정규표현식을 진행했어도 한번더 해야된다.
                isValidated = false;
            }
        } else { // 선택값일 경우
            if (data != null && !data.equals("") && !Pattern.matches(regexp, data)) { // 데이터 입력 시
                isValidated = false;
            }
        }

        return isValidated;
    }
}
