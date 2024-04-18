package com.sweetk.cso.common.config;

import com.sweetk.cso.common.security.AdmAuthenticationProvider;
import com.sweetk.cso.common.security.LoginAuthenticationEntryPoint;
import com.sweetk.cso.common.security.handler.LoginFailureHandler;
import com.sweetk.cso.common.security.handler.LoginSuccessHandler;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final AdmAuthenticationProvider admAuthenticationProvider;
    private final LoginAuthenticationEntryPoint loginAuthenticationEntryPoint;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(admAuthenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests -> requests
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()   // Security 5.8.X only perform authorization once per request / Security 6.X.X secure all dispatch types
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()  // ViewResolver 가 Forward 하는건 필터링 제외 (WEB-INF 아래 경로)
                .requestMatchers(
                        new MvcRequestMatcher(introspector, "/login"),
                        new MvcRequestMatcher(introspector, "/resources/**")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(config -> config
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("admId")
                .passwordParameter("admPw")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
            )
            .logout(config -> config
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")    //@@@ 쿠키이름
                .logoutSuccessUrl("/login")
            )
            .sessionManagement(config -> config.maximumSessions(1))
            .exceptionHandling(config -> config.authenticationEntryPoint(loginAuthenticationEntryPoint));
        return http.build();
    }

}
