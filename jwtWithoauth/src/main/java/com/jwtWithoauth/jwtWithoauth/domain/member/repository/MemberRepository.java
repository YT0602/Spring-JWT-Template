package com.jwtWithoauth.jwtWithoauth.domain.member.repository;

import com.jwtWithoauth.jwtWithoauth.domain.member.constant.SocialType;
import com.jwtWithoauth.jwtWithoauth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByRefreshToken(String refreshToken);

    Optional<Member> findBySocialId(String socialId);
}
