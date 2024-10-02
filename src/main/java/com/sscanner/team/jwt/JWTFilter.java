package com.sscanner.team.jwt;

import com.sscanner.team.User;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.user.responseDto.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 토큰 꺼냄
        String accessToken = request.getHeader("access");

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ExceptionCode.EXPIRED_ACCESS_TOKEN);
        }

        // access 토큰인지 확인
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            throw new BadRequestException(ExceptionCode.INVALID_ACCESS_TOKEN);
        }

        // access 토큰이면 email, authority 가져옴
        String email = jwtUtil.getEmail(accessToken);
        String authority = jwtUtil.getAuthority(accessToken);

        User user = User.builder()
                .email(email)
                .authority(authority)
                .build();

        UserDetailsImpl customUserDetails = new UserDetailsImpl(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
