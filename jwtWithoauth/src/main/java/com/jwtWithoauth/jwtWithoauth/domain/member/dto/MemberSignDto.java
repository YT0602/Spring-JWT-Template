package com.jwtWithoauth.jwtWithoauth.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
// 자체 로그인 회원가입에 RequestBody로 사용할 Dto
public class MemberSignDto {

    private String email;
    private String password;
    private String nickname;
}
