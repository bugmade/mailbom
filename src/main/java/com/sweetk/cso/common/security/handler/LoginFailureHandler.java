package com.sweetk.cso.common.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private static final String NOT_VALID = "아이디 또는 비밀번호가 유효하지 않습니다.";
    private static final String BAD_CREDENTIALS = "아이디 또는 비밀번호가 일치하지 않습니다.";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMsg = null;

        if (exception instanceof AuthenticationCredentialsNotFoundException) { // 아이디 또는 비밀번호 유효하지 않음
            errorMsg = NOT_VALID;
        } else {
            errorMsg = BAD_CREDENTIALS;
        }
        log.error("##### Log-In Error : [{}]", errorMsg);
        response.sendRedirect("/login?error=true");
    }

}
