package com.jwtWithoauth.jwtWithoauth.domain.member.service;

import com.jwtWithoauth.jwtWithoauth.domain.member.constant.Role;
import com.jwtWithoauth.jwtWithoauth.domain.member.constant.SocialType;
import com.jwtWithoauth.jwtWithoauth.domain.member.dto.MemberSignDto;
import com.jwtWithoauth.jwtWithoauth.domain.member.entity.Member;
import com.jwtWithoauth.jwtWithoauth.domain.member.repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 자체 회원가입
    public void signUp(MemberSignDto memberSignDto) throws Exception{
        validateDuplicateMember(memberSignDto);

        Member member = Member.builder()
                .email(memberSignDto.getEmail())
                .password(memberSignDto.getPassword())
                .nickname(memberSignDto.getNickname())
                .role(Role.USER)
                .build();

        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }

    // 중복 검증
    private void validateDuplicateMember(MemberSignDto memberSignDto) throws Exception {
        if(memberRepository.findByEmail(memberSignDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        if(memberRepository.findByNickname(memberSignDto.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }
    }
}
