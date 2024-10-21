package com.sscanner.team.user.service;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.DuplicateException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.user.repository.UserRepository;
import com.sscanner.team.user.requestdto.UserJoinRequestDto;
import com.sscanner.team.user.requestdto.UserPasswordChangeRequestDto;
import com.sscanner.team.user.requestdto.UserPhoneUpdateRequestDto;
import com.sscanner.team.user.responsedto.*;
import jakarta.transaction.Transactional;
import com.sscanner.team.user.requestdto.SmsVerifyRequestDto;
import com.sscanner.team.user.responsedto.UserJoinResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.sscanner.team.global.utils.UserUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SmsService smsService;
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

    // 비밀번호 확인용 메서드 (비번 , 비번확인 같은지)
    public void confirmPassword(String password, String passwordCheck) {
        if(!password.equals(passwordCheck)) {
            throw new BadRequestException(ExceptionCode.PASSWORD_NOT_MATCH);
        }
    }

    // 비밀번호 수정 시 검증
    private void validatePasswordChange(UserPasswordChangeRequestDto requestDto, User user) {
        if (!passwordEncoder.matches(requestDto.currentPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionCode.CURRENT_PASSWORD_NOT_MATCH);
        }

        confirmPassword(requestDto.newPassword(), requestDto.confirmNewPassword());
    }

    // 회원가입
    public UserJoinResponseDto join(UserJoinRequestDto req){

        checkDuplicatedEmail(req.email());
        checkDuplicatedNickname(req.nickname());
        checkDuplicatedPhone(req.phone());

        if (!smsService.verifyCode(new SmsVerifyRequestDto(req.phone(), req.smsCode()))) {
            throw new IllegalArgumentException("핸드폰 인증에 실패하였습니다."); // 인증 실패 시 예외 던짐
        }


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


    // 비밀번호 확인 (현재 비밀번호 확인)
    public boolean confirmPassword(String password){
        User user = userUtils.getUser();

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(ExceptionCode.CURRENT_PASSWORD_NOT_MATCH);
        }
        return true;
    }

    // 핸드폰 번호 수정
    @Transactional
    public UserPhoneUpdateResponseDto updatePhoneNumber(UserPhoneUpdateRequestDto req) {
        User user = userUtils.getUser();

        if (user.isPhoneEqual(req.newPhone())) {
            throw new BadRequestException(ExceptionCode.SAME_PHONE_NUMBER);
        }

        checkDuplicatedPhone(req.newPhone());

        if (!smsService.verifyCode(new SmsVerifyRequestDto(req.newPhone(), req.smsCode()))) {
            throw new IllegalArgumentException("핸드폰 인증에 실패하였습니다.");
        }

        user.changePhone(req.newPhone());
        return UserPhoneUpdateResponseDto.from(user);
    }

    // 닉네임 수정
    @Transactional
    public UserNicknameUpdateResponseDto updateNickname(String  newNickname) {
        User user = userUtils.getUser();

        if (!user.isNicknameEqual(newNickname)) {
            checkDuplicatedNickname(newNickname);
            user.changeNickname(newNickname);
        }
        return UserNicknameUpdateResponseDto.from(user);
    }

    // 비밀번호 수정
    @Transactional
    public String changePassword(UserPasswordChangeRequestDto requestDto) {

        User user = userUtils.getUser();

        validatePasswordChange(requestDto, user);

        user.changePassword(passwordEncoder.encode(requestDto.newPassword()));
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






