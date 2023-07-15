package com.jwtWithoauth.jwtWithoauth.global.oauth2;

import com.jwtWithoauth.jwtWithoauth.domain.member.constant.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2Member extends DefaultOAuth2User {

    private String email;
    private Role role;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2Member(Collection<? extends GrantedAuthority> authorities,
                              Map<String, Object> attributes, String nameAttributeKey,
                              String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }

}
