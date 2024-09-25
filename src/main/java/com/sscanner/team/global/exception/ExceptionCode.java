package com.sscanner.team.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    //Bad Request
    NOT_FOUND_PRODUCT_ID(400, "해당하는 상품이 존재하지 않습니다."),
    NOT_FOUND_ORDER_ID(400, "취소되었거나 존재하지 않는 주문입니다."),
    NOT_FOUND_ITEM_ID(400, "해당 주문에 상품이 존재하지 않습니다."),

    //Unauthorized
    INVALID_PASSWORD(401, "이메일이나 비밀번호가 일치하지 않습니다."),
    INVALID_EMAIL(401, "이메일이나 비밀번호가 일치하지 않습니다."),

    //conflict
    DUPLICATED_PRODUCT_NAME(409, "이미 존재하는 상품명입니다.");


    private final int code;
    private final String message;
}
