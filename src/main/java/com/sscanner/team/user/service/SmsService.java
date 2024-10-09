package com.sscanner.team.user.service;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.user.SmsCertificationUtil;
import com.sscanner.team.user.repository.SmsRepository;
import com.sscanner.team.user.repository.UserRepository;
import com.sscanner.team.user.requestdto.SmsRequestDto;
import com.sscanner.team.user.requestdto.SmsVerifyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SmsService {

    private final SmsCertificationUtil smsCertificationUtil;
    private final SmsRepository smsRepository;
    private final UserRepository userRepository;

    public void SendSms(SmsRequestDto smsRequestDto) {
        String phoneNum = smsRequestDto.phoneNum();

        if (userRepository.findByPhone(phoneNum).isPresent()) {
            throw new BadRequestException(ExceptionCode.DUPLICATED_PHONE);
        }

        String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000); // 인증 코드(6자리랜덤)
        smsCertificationUtil.sendSMS(phoneNum, certificationCode);
        smsRepository.createSmsCertification(phoneNum, certificationCode);
    }

    public boolean verifyCode(SmsVerifyRequestDto smsVerifyDto) {
        if (isVerify(smsVerifyDto.phoneNum(), smsVerifyDto.code())) {
            smsRepository.deleteSmsCertification(smsVerifyDto.phoneNum());
            return true;
        } else {
            return false;
        }
    }

    public boolean isVerify(String phoneNum, String code) {
        return smsRepository.hasKey(phoneNum) && // 전화번호에 대한 키가 존재하고
                smsRepository.getSmsCertification(phoneNum).equals(code); // 저장된 인증 코드와 입력된 인증 코드가 일치하는지 확인
    }

}
