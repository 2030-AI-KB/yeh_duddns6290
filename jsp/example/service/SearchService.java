package com.example.service;

import com.common.AppService;
import com.common.ServiceForward;
import com.example.ds.ExampleDs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.regex.Pattern;

import static com.common.Constants.BASIC_VIEW_PATH;

public class SearchService implements AppService {
    @Override
    public ServiceForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // String형 input을 선언하고 입력한 id를 로드한다.
        String input = request.getParameter("myNameId");

        // input 변수의 데이터가 숫자인 확인한다.
//          if (input == null) {
//            // 오류 메시지
//        } else {
//            if (input.equals("")) {
//                // 오류 메시지
//            } else {
//                // 정규 표현식 사용하여 숫자인지 확인
//                if (/*정규표현식 맞냐*/) {
//
//                } else {
//
//                }
//            }
//        }

        /*
         * 단락 평가(SCE: Short Circuit Evaluation) :: 논리 연산자 && 혹은 ||를 포함한 연산 결과 True/False에서 앞의 연산을 통해서 뒤의 연산 결과 전에 판단을 완료하는 것
         * && 조건(false를 만났을 시 뒤의 연산 X)
         * || 조건(true를 만났을 시 뒤의 연산 X)
         *
         */
        if (input == null || input.equals("") || !Pattern.matches("^[0-9]{1,10}$", input)/*정규표현식 검사 -> 틀리다면*/) {
            // 오류 메시지
            return null;
        }

        /*
         * 정규표현식 검사
         * ^ :: 시작을 알리는 기호
         * [0-9] :: 0~ 9까지만 인정하겠다
         * [0-9a-zA-Z가-힣!@#$%^&*] :: 0~9, 소문자 a~z, 대문자 A~Z, 한글, 특수기호(!@#$%^&*)을 허용(false를 리턴)한다.
         * {1, 10} :: 글자길이 설정. 10자리까지 허용한다.
         * $ :: 끝을 알리는 기호
         */

        // 정수형 id를 선언하고 input 변수를 숫자로 변환한다.
        int id = Integer.parseInt(input);

        // String형 name을 선언하고 데이터베이스에서 id로 이름을 검색하여 저장한다.
        ExampleDs ds = new ExampleDs();
        String name = ds.selectNameById(id);
        if (name == null) {
            name = "검색 결과가 존재하지 않습니다.";
        }

        // request 객체의 attribute에 검색한 이름을 저장한다.
        request.setAttribute("result", name);

        // view를 위한 forward 객체 리턴
        return ServiceForward.builder()
                .path(BASIC_VIEW_PATH + "search.jsp")
                .build();
    }
}
