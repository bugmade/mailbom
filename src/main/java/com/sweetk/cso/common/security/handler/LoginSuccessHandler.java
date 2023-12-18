package com.sweetk.cso.common.security.handler;

import com.sweetk.cso.common.security.AdmUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            AdmUserDetails details = (AdmUserDetails) authentication.getDetails();
            HttpSession httpSession = request.getSession();
            httpSession.setMaxInactiveInterval(3600);

            // 사용자 정보 저장
            httpSession.setAttribute("user", details);
            log.info("#### Log-In Success!! ID : [{}]", details.getUsername());
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServletException("onAuthenticationSuccess error");
        }
        // 세션 만료되어 재로그인 했을 경우 이전 URL 로 돌아갈 수 있도록 SavedRequestAwareAuthenticationSuccessHandler 사용
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
