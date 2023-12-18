package com.sweetk.cso.common.security;

import com.sweetk.cso.entity.Adm;
import com.sweetk.cso.repository.AdmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdmUserDetailsService implements UserDetailsService {

    private final AdmRepository admRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Adm adm = admRepository.findByAdmId(username);
        if(adm == null){
            throw new UsernameNotFoundException("존재하지 않는 ID");
        }
        return new AdmUserDetails(adm, getAuthorities());
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO 플랫폼 웹에서 역할 분리가 필요한지 확인 (메뉴별 권한 포함)
        // 필요하다면 해당 메소드에서 권한 세팅
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        return roles;
    }

}
