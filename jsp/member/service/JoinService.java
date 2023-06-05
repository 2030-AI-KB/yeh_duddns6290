package com.member.service;

import com.common.*;
import com.dto.MemberDTO;
import com.member.ds.MemberDs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.regex.Pattern;

import static com.common.Constants.BASIC_VIEW_PATH;
import static com.common.Validator.isValidated;

public class JoinService implements AppService {
    @Override
    public ServiceForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 폼 데이터 로드
        // 아이디, 비밀번호, 비밀번호확인, 이름, 휴대전화번호, 생년월일, 이메일, 주소3
        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");
        String passwordCheck = request.getParameter("passwordCheck");
        String name = request.getParameter("name");
        String mobileNo = request.getParameter("mobileNo");
        String birthday = request.getParameter("birthday");
        String email = request.getParameter("email");
        String zipcode = request.getParameter("zipcode");
        String address = request.getParameter("address");
        String addressDetail = request.getParameter("addressDetail");

        // 데이터 확인
        if(!isValidated(loginId, "^[a-z0-9]{4,15}$", true)
                ||!isValidated(password, "^[a-zA-Z0-9!@#$]{4,15}$", true)
                ||!isValidated(name, "^[가-힣]{2,10}$", true)
                ||!isValidated(mobileNo, "^[0-9]{10,12}$", true)
                ||!isValidated(birthday, "^[0-9]{8,8}$", false)
                ||!isValidated(email, "^.{1,50}$", true)
                ||!isValidated(zipcode, "^[0-9]{5,5}$", false)
                ||!isValidated(address, "^.{1,100}$", false)
                ||!isValidated(addressDetail, "^.{1,100}$", false)
        ){
            return null;
        }

        if(!password.equals(passwordCheck)){
            return null;
        }
        // 비밀번호 암호화(복호화가 가능)
//        AES256 aes256 = new AES256();
//        password = aes256.encrypt(password);

        // 비밀번호 암호화(복호화가 불가능)
        password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        // BCrypt.gensalt() :: 암호화 복잡도. 숫자가 낮을수록 암호화 복잡도가 낮기때문에 빠르다. 하지만 보통 복잡하게 한다.

        // DTO 데이터 세팅
        MemberDTO memberDTO = MemberDTO.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .mobileNo(mobileNo)
                .email(email)
                .zipcode(zipcode)
                .address(address)
                .addressDetail(addressDetail)
                .build();

        // 생일은 선태값이므로 데이터 존재시만 날짜로 변환
        if(birthday != null && !birthday.equals("")){
            birthday = birthday.substring(0, 4)
                    + "-" + birthday.substring(4, 6)
                    + "-" + birthday.substring(6);
            memberDTO.setBirthday(LocalDate.parse(birthday));
        }

        // 데이터베이스에 저장
        MemberDs ds = new MemberDs();
        boolean isSuccess = ds.insertMemberInfo(memberDTO);
        if (!isSuccess){
            return null;
        }
        ds.insertMemberInfo(memberDTO);

        // 뷰 이동 데이터 세팅 및 리턴
        request.setAttribute("name", memberDTO.getName());
        return ServiceForward.builder()
                .path(BASIC_VIEW_PATH + "member/joinresult.jsp")
                .build();

    }
}
