package com.sweetk.cso.common.security;

import com.sweetk.cso.entity.Adm;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log4j2
@Component
public class AdmAuthenticationProvider implements AuthenticationProvider {

    private final AdmUserDetailsService admUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdmAuthenticationProvider(@Lazy AdmUserDetailsService admUserDetailsService, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder){
        this.admUserDetailsService = admUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private static final String NOT_VALID = "아이디 또는 비밀번호가 유효하지 않습니다.";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String admId = (String) authentication.getPrincipal();
        String admPw = (String) authentication.getCredentials();

        if (!StringUtils.hasText(admId)) {
            log.error("ID 미입력");
            throw new AuthenticationCredentialsNotFoundException(NOT_VALID);
        }

        if (!StringUtils.hasText(admPw)) {
            log.error("PW 미입력");
            throw new AuthenticationCredentialsNotFoundException(NOT_VALID);
        }

        // 0. DB 에서 userID 검색(ID에 따른 권한 부여)
        AdmUserDetails admUserDetails = (AdmUserDetails) admUserDetailsService.loadUserByUsername(admId);
        Adm adm = admUserDetails.getAdm();

        // 1. 사용자 유효성 검증
        if(!bCryptPasswordEncoder.matches(admPw, adm.getAdmPw())) {
            log.error("PW 불일치");
            throw new BadCredentialsException(NOT_VALID);
        }

        // 2. 인증 토큰 생성
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(admId, admPw, admUserDetails.getAuthorities());
        result.setDetails(admUserDetails);
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // AuthenticationProvider 는 오직 UsernamePasswordAuthenticationToken 클래스를 지원하며, 다른 유형의 인증 토큰에 대해서는 처리하지 않음을 의미
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
