package com.example.service;

import com.common.AppService;
import com.common.ServiceForward;
import com.example.ds.ExampleDs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.common.Constants.BASIC_VIEW_PATH;

public class ResultService implements AppService {
    @Override
    public ServiceForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // String형 name을 선언하고 myName 키 값으로 입력한 데이터를 저장한다.
        String name = request.getParameter("myName");
        // String형 age를 선언하고 myage 키 값으로 입력한 데이터를 저장한다.
        String age = request.getParameter("myAge");

        ExampleDs ds = new ExampleDs();
        ds.insertName(name);

        // request 객체에 뷰로 전송할 name 데이터를 저장한다.
        request.setAttribute("name", name);
        // request 객체에 뷰로 전송할 age 데이터를 저장한다.
        request.setAttribute("age", age);
        // serviceForward 객체 forward를 선언하고 빌더를 통해 경로 데이터를 저장하여 리턴하다.
        ServiceForward forward = ServiceForward.builder()
                .path(BASIC_VIEW_PATH + "result.jsp")
                .build();
        // forward 객체를 리턴한다.
        return forward;
    }
}
