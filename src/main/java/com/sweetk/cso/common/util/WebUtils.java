package com.sweetk.cso.common.util;

import com.sweetk.cso.common.security.AdmUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class WebUtils {

    /**
     * Authentication 객체를 가져오는 메소드
     * @return
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * 로그인 인증 객체를 return해주는 메소드
     * @return
     */
    public static AdmUserDetails getLogin() {
        return (AdmUserDetails) getAuthentication().getDetails();
    }

    /**
     * 로그인 되어 있는지를 체크하는 변수
     * @return
     */
    public static boolean isLogin() {
        return getLogin().getAdm() != null;
    }

}
