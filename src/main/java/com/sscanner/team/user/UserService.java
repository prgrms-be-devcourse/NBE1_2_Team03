package com.sscanner.team.user;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    //이메일(아이디) 중복 체크 중복 시 true 리턴
    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //닉네임 중복 체크
    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //핸드폰 중복 체크
    public boolean checkPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    // 비밀번호 확인용 메서드
    public boolean confirmPassword(String password, String passwordCheck) {
        return password.equals(passwordCheck);
    }

    // 회원가입
    public UserJoinResponseDTO join(UserJoinRequestDTO req){
        var userEntity = req.toEntity(passwordEncoder.encode(req.password()));
        userRepository.save(userEntity);

        return UserJoinResponseDTO.fromEntity(userEntity);
    }


}


