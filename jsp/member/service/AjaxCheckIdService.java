package com.member.service;

import com.common.AppService;
import com.common.ServiceForward;
import com.member.ds.MemberDs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.regex.Pattern;

import static com.common.Constants.BASIC_VIEW_PATH;

public class AjaxCheckIdService implements AppService {
    @Override
    public ServiceForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // String 변수 loginId를 선언하고 loginId를 키 값으로 데이터를 로드하여 저장한다.
        String loginId = request.getParameter("loginId");

        // loginId가 null, 빈 값, 정규표현식 통과 여부를 확인한다.
        if (loginId == null || loginId.equals("") || !Pattern.matches("^[a-z0-9]{4,15}$", loginId)) {
            return null;
        }

        // 데이터베이스에서 loginId의 데이터와 같은 login_id가 몇개 인지 확인한다.
        MemberDs ds = new MemberDs();
        int count = ds.selectAccountCountByLoginId(loginId);

        // 0이라면
            // 중복된 아이디 없음
        // 아니라면
            // 중복된 아이디 존재
        request.setAttribute("result", count == 0 ? true : false);
        return ServiceForward.builder()
                .path(BASIC_VIEW_PATH + "ajax/result.jsp")
                .build();
    }
}
