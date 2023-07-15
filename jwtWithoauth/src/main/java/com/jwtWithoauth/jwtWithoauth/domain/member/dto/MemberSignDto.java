package com.jwtWithoauth.jwtWithoauth.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.jwtWithoauth.jwtWithoauth.domain.member.constant.SocialType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
// 자체 로그인 회원가입에 RequestBody로 사용할 Dto
public class MemberSignDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private SocialType socialType;


}
