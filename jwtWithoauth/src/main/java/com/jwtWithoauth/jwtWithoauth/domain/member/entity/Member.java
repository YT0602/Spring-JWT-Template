package com.jwtWithoauth.jwtWithoauth.domain.member.entity;

import com.jwtWithoauth.jwtWithoauth.domain.member.constant.Role;
import com.jwtWithoauth.jwtWithoauth.domain.member.constant.SocialType;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberid;

    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @Column(length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(length = 200)
    private String profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SocialType socialType;

    @Column(length = 250)
    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.USER;
    }

    //== 유저 필드 업데이트 ==//
    public void updateNickname(String updateNickname) {
        this.nickname = updateNickname;
    }
    public void updatePassword(String updatePassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(updatePassword);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;

    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }



}
