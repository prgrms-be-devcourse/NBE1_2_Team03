package com.sscanner.team.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sscanner.team.user.entity.Refresh;
import com.sscanner.team.user.repository.RefreshRepository;
import com.sscanner.team.user.requestdto.UserLoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;


@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {

        UserLoginRequestDto loginDto;

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDto = objectMapper.readValue(messageBody, UserLoginRequestDto.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = loginDto.email();
        String password = loginDto.password();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //유저 정보 가져옴
        String email = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authority = authorities.iterator().next().getAuthority();

        // 토큰 생성 및 저장
        String access = jwtUtil.createJwt("access", email, authority, 2000000L);
        String refresh = jwtUtil.createJwt("refresh", email, authority, 86400000L);

        addRefreshEntity(email, refresh, 86400000L);

        // SecurityContextHolder에 인증 정보 설정
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    // 리프레시 토큰 저장소에 저장
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refreshEntity = new Refresh(username, refresh, date.toString());
        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);  // https통신 진행시
        cookie.setPath("/");  // 쿠키가 적용될 범위
        cookie.setHttpOnly(true);

        return cookie;
    }

}





