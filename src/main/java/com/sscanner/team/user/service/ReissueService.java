package com.sscanner.team.user.service;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.jwt.JWTUtil;
import com.sscanner.team.user.entity.Refresh;
import com.sscanner.team.user.repository.RefreshRepository;
import com.sscanner.team.user.responseDto.RefreshResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class ReissueService {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ApiResponse<RefreshResponseDto> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        // 리프레시 토큰 존재 체크
        if (refresh == null) {
            throw new BadRequestException(ExceptionCode.NOT_EXIST_REFRESH_TOKEN);
        }

        // 리프레시토큰 만료 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ExceptionCode.EXPIRED_REFRESH_TOKEN);
        }

        // 리프레시 토큰이 맞는지 체크
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new BadRequestException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // DB에 해당 리프레시 토큰 존재 체크
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            throw new BadRequestException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtUtil.getEmail(refresh);
        String authority = jwtUtil.getAuthority(refresh);

        // 새 access 토큰 발급
        String newAccess = jwtUtil.createJwt("access", email, authority, 6000L);
        String newRefresh = jwtUtil.createJwt("refresh", email, authority, 86400000L);

        // 기존 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefresh(email, newRefresh, 86400000L);

        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        RefreshResponseDto responseDto = RefreshResponseDto.from(newAccess, newRefresh);
        return ApiResponse.ok(200, responseDto, "토큰 재발급 성공");
    }

    // 리프레시 토큰 추가 후 저장
    private void addRefresh(String email, String newRefresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refreshEntity = new Refresh(email, newRefresh, date.toString());
        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 1일 설정
        cookie.setHttpOnly(true); // js로 해당 쿠키 접근 불가하게 설정
        //cookie.setSecure(true);  // https 통신 진행 시 사용
        //cookie.setPath("/");  // 쿠키가 적용될 범위
        return cookie;
    }
}
