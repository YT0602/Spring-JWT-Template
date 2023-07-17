package com.jwtWithoauth.jwtWithoauth.api.member.controller;

import com.jwtWithoauth.jwtWithoauth.domain.member.dto.MemberSignDto;
import com.jwtWithoauth.jwtWithoauth.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/test/jwt")
    public String test(){
        return "jwt 요청 성공";
    }
    // 회원가입 api
    @PostMapping("/signup")
    public String signUp(@RequestBody MemberSignDto memberSignDto) throws Exception{
        memberService.signUp(memberSignDto);
        return "회원가입 완료";
    }

    @GetMapping("/oauth2/signup")
    public String oauth2SignUp(){
        return "oauth2";
    }

}
