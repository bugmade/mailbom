package com.sweetk.cso.common.security;

import com.sweetk.cso.entity.Adm;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class AdmUserDetails implements UserDetails {

    private final Adm adm;
    private final Collection<? extends GrantedAuthority> authorities;

    public AdmUserDetails(Adm adm, Collection<? extends GrantedAuthority> authorities){
        this.adm = adm;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return adm.getAdmPw();
    }

    @Override
    public String getUsername() {
        return adm.getAdmId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;    // 만료일자 사용할 경우 로직 생성
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;    // 계정 Lock 기능 사용할 경우 로직 생성
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
