package com.sweetk.cso.common.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 로그인이 필수인 Ajax 통신 URL 에 로그인 하지 않았을 경우,
 * 명시적으로 401 에러를 주고 프론트에서 로그인 요청 팝업을 띄우기 위한 Class
 *
 */
@Component
public class LoginAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public LoginAuthenticationEntryPoint() {
        super("/login");
    }

    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        // AJAX 통신일 경우, 해당 헤더에 'XMLHttpRequest' 값이 들어감
        String ajaxHeader = request.getHeader("X-Requested-With");
        if(ajaxHeader != null && "XMLHttpRequest".equals(ajaxHeader)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            super.commence(request, response, authException);
        }
    }

}

