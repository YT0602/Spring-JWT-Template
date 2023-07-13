package com.jwtWithoauth.jwtWithoauth.domain.member.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SocialType {

    KAKAO, NAVER, GOOGLE;

    public static boolean isMemberType(String type) {
        List<SocialType> socialTypes = Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.name().equals(type))
                .collect(Collectors.toList());
        return socialTypes.size() != 0;
    }

}
