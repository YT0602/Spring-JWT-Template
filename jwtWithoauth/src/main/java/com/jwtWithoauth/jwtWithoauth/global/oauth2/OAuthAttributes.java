package com.jwtWithoauth.jwtWithoauth.global.oauth2;

import com.jwtWithoauth.jwtWithoauth.domain.member.constant.Role;
import com.jwtWithoauth.jwtWithoauth.domain.member.constant.SocialType;
import com.jwtWithoauth.jwtWithoauth.domain.member.entity.Member;
import com.jwtWithoauth.jwtWithoauth.global.oauth2.memberinfo.GoogleOAuth2MemberInfo;
import com.jwtWithoauth.jwtWithoauth.global.oauth2.memberinfo.KakaoOAuth2MemberInfo;
import com.jwtWithoauth.jwtWithoauth.global.oauth2.memberinfo.NaverOAuth2MemberInfo;
import com.jwtWithoauth.jwtWithoauth.global.oauth2.memberinfo.OAuth2MemberInfo;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스
 */
@Data
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2MemberInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2MemberInfo oAuth2MemberInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oAuth2MemberInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : memberNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(SocialType socialType,
                                     String memberAttributeName, Map<String, Object> attributes) {
        if (socialType == SocialType.GOOGLE) {
            return ofGoogle(memberAttributeName, attributes);
        } else if (socialType == SocialType.NAVER) {
            return ofNaver(memberAttributeName, attributes);
        } else {
            return ofKakao(memberAttributeName, attributes);
        }
    }

    public static OAuthAttributes ofGoogle(String memberAttributesName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(memberAttributesName)
                .oAuth2MemberInfo(new GoogleOAuth2MemberInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String memberAttributesName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(memberAttributesName)
                .oAuth2MemberInfo(new NaverOAuth2MemberInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofKakao(String memberAttributesName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(memberAttributesName)
                .oAuth2MemberInfo(new KakaoOAuth2MemberInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정, 추가정보 입력시 USER로 변경
     */
    public Member toEntity(SocialType socialType, OAuth2MemberInfo oAuth2MemberInfo) {
        return Member.builder()
                .socialType(socialType)
                .socialId(socialType + "-" + oAuth2MemberInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .nickname(oAuth2MemberInfo.getNickname())
                .imageUrl(oAuth2MemberInfo.getImageUrl())
                .role(Role.GUEST)
                .build();
    }
}
