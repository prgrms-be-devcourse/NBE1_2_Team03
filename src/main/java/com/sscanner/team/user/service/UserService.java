package com.sscanner.team.user.service;

import com.sscanner.team.User;
import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.DuplicateException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.user.repository.UserRepository;
import com.sscanner.team.user.requestDto.UserJoinRequestDto;
import com.sscanner.team.user.requestDto.UserUpdateRequestDto;
import com.sscanner.team.user.responseDto.UserMypageResponseDto;
import com.sscanner.team.user.responseDto.UserJoinResponseDto;
import com.sscanner.team.user.responseDto.UserUpdateResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.sscanner.team.global.utils.UserUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserUtils userUtils;

    // 이메일 중복 체크
    private void checkDuplicatedEmail(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException(ExceptionCode.DUPLICATED_EMAIL);
        }
    }

    //닉네임 중복 체크
    private void checkDuplicatedNickname(final String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicateException(ExceptionCode.DUPLICATED_NICKNAME);
        }
    }

    //핸드폰 중복 체크
    private void checkDuplicatedPhone(final String phone) {
        if (userRepository.existsByPhone(phone)) {
            throw new DuplicateException(ExceptionCode.DUPLICATED_PHONE);
        }
    }

    // 비밀번호 확인용 메서드
    public void confirmPassword(String password, String passwordCheck) {
        if(!password.equals(passwordCheck)) {
            throw new BadRequestException(ExceptionCode.PASSWORD_NOT_MATCH);
        }
    }

    // 회원가입
    public UserJoinResponseDto join(UserJoinRequestDto req){

        checkDuplicatedEmail(req.email());
        checkDuplicatedNickname(req.nickname());
        checkDuplicatedPhone(req.phone());

        confirmPassword(req.password(), req.passwordCheck());

        User userEntity = req.toEntity(passwordEncoder.encode(req.password()));
        userRepository.save(userEntity);

        return UserJoinResponseDto.from(userEntity);
    }

    // 마이페이지 조회
    public ApiResponse<UserMypageResponseDto> getMypage() {

        User user = userUtils.getUser();
        UserMypageResponseDto responseDto = UserMypageResponseDto.create(user);
        return ApiResponse.ok(responseDto, "마이페이지 조회");
    }

    // 회원정보 수정
    @Transactional
    public UserUpdateResponseDto updateUserInfo(UserUpdateRequestDto req){

        User user = userUtils.getUser();

        String nickname = req.nickname();
        String phone = req.phone();

        if (nickname!= null && !user.getNickname().equals(nickname)) {
            checkDuplicatedNickname(nickname);
            user.setNickname(nickname);
        }

        if (phone != null && !user.getPhone().equals(phone)) {
            checkDuplicatedPhone(phone);
            user.setPhone(phone);
        }

        userRepository.save(user);

        return UserUpdateResponseDto.from(user);
    }

    // 비밀번호 수정
    @Transactional
    public String changePassword(String currentPassword, String newPassword, String newPasswordConfirm) {

        User user = userUtils.getUser();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadRequestException(ExceptionCode.CURRENT_PASSWORD_NOT_MATCH);
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            throw new BadRequestException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "비밀번호가 성공적으로 변경되었습니다.";

    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(){
        User user = userUtils.getUser();
        userRepository.delete(user);
    }
}





