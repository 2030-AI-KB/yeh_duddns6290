package com.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MemberDTO {
    private int id;
    private String loginId;
    private String password;
    private LocalDate joinDate;
    private String name;
    private String mobileNo ;
    private LocalDate birthday ;
    private String email;
    private String zipcode;
    private String address;
    private String addressDetail;
    private int memberDetailId;
}

// 자바빈의 조건
// 멤버 변수는 다 private
// getter, setter만 들고 있다