package com.example.ssak3.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User 에러
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "가입 이력이 있는 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 전화번호입니다."),
    PASSWORD_SAME_AS_OLD(HttpStatus.CONFLICT, "현재 비밀번호와 일치해서 변경할 수 없습니다."),
    UNREGISTERED_USER(HttpStatus.NOT_FOUND, "가입되지 않은 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    WITHDRAWN_USER(HttpStatus.NOT_FOUND, "탈퇴한 회원입니다."),
    NOT_ALLOWED_CHANGE_SUPER_ADMIN(HttpStatus.BAD_REQUEST, "최고 관리자로는 변경할 수 없습니다."),
    ALREADY_REVIEWED(HttpStatus.BAD_REQUEST, "이미 후기를 작성한 유저입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_BIRTH(HttpStatus.BAD_REQUEST, "생일은 현재 시점보다 과거여야 합니다."),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    OAUTH_USER_CANNOT_BE_ADMIN(HttpStatus.FORBIDDEN, "소셜 로그인 유저는 관리자가 될 수 없습니다."),

    // Product 에러
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    PRODUCT_NOT_FOR_SALE(HttpStatus.BAD_REQUEST, "판매 중인 상품이 아닙니다."),
    PRODUCT_INSUFFICIENT(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "유효한 재고 값이 아닙니다."),
    PRODUCT_NOT_VIEWABLE(HttpStatus.BAD_REQUEST, "현재 상품 상태에서는 조회할 수 없습니다."),
    INVALID_ROLLBACK_QUANTITY(HttpStatus.BAD_REQUEST, "복구할 수량은 1 이상이어야 합니다."),
    INVALID_PRODUCT_STATUS(HttpStatus.BAD_REQUEST, "올바른 상품 상태 값이 아닙니다."),
    PRODUCT_HAS_TIME_DEAL(HttpStatus.BAD_REQUEST, "타임딜이 진행중이거나 준비중인 상태의 상품은 삭제할 수 없습니다."),

    // Review 에러
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "후기를 찾을 수 없습니다."),
    REVIEW_AUTHOR_MISMATCH(HttpStatus.FORBIDDEN, "사용자와 후기 작성자가 일치하지 않습니다."),

    // Category 에러
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    CATEGORY_HAS_PRODUCTS(HttpStatus.BAD_REQUEST, "카테고리에 포함된 상품이 있습니다."),
    CATEGORY_DUPLICATED(HttpStatus.CONFLICT, "중복된 카테고리 이름입니다."),

    // Cart 에러
    CART_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에서 상품을 찾을 수 없습니다."),
    CART_ACCESS_DENIED(HttpStatus.FORBIDDEN, "내 장바구니가 아닙니다."),
    CART_PRODUCT_LIMIT(HttpStatus.BAD_REQUEST, "장바구니에 담을 수 있는 수량을 초과했습니다."),

    // Order 에러
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    ORDER_PRODUCT_IS_NULL(HttpStatus.BAD_REQUEST, "주문할 상품이 선택되지 않았습니다."),
    ORDER_CAN_NOT_BE_CANCELED(HttpStatus.BAD_REQUEST, "주문을 취소할 수 없습니다."),
    ORDER_PAYMENT_PENDING_EXISTS(HttpStatus.BAD_REQUEST, "결제 대기 중인 주문이 있습니다."),
    ORDER_PRICE_CHANGED(HttpStatus.BAD_REQUEST, "가격이 변경된 상품이 있습니다."),
    USER_NOT_PURCHASED_PRODUCT(HttpStatus.FORBIDDEN, "해당 상품을 구매한 사용자만 이용할 수 있습니다."),

    // TimeDeal 에러
    TIME_DEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 세일 상품을 찾을 수 없습니다."),
    SALE_PRICE_MUST_BE_LOWER_THAN_ORIGINAL_PRICE(HttpStatus.BAD_REQUEST, "할인가는 정가보다 낮아야 합니다."),
    INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "시작 시간은 종료 시간보다 늦을 수 없습니다."),
    ACTIVE_TIME_DEAL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "현재 진행중인 타임딜 상품입니다."),
    TIME_DEAL_CANNOT_DELETE(HttpStatus.BAD_REQUEST, "현재 진행중이거나 삭제할 수 없는 상태의 타임딜 상품입니다."),
    TIME_DEAL_CANNOT_CREATE(HttpStatus.BAD_REQUEST, "타임딜을 생성할 수 없습니다. 상품 상태를 확인해 주세요"),
    TIME_DEAL_CANNOT_UPDATE(HttpStatus.BAD_REQUEST, "상품 재고가 없어 타임딜을 수정할 수 없습니다."),
    TIME_DEAL_INVALID_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 타임딜 상태입니다."),
    TIME_DEAL_DELETED_STATUS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "삭제된 타임딜 상태로는 조회할 수 없습니다."),
    TIME_DEAL_START_TIME_MUST_BE_IN_FUTURE(HttpStatus.BAD_REQUEST, "타임딜 시작 시간은 현재 시간 이후여야 합니다."),
    TIME_DEAL_PRODUCT_MISMATCH(HttpStatus.BAD_REQUEST, "해당 상품의 타임딜이 아닙니다."),

    // Image 에러
    IMAGE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장에 실패했습니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "유효하지 않은 파일입니다."),
    INVALID_URL(HttpStatus.BAD_REQUEST, "유효하지 않은 URL입니다."),

    // Token 에러
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // Coupon 에러
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    COUPON_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 지급된 쿠폰입니다."),
    COUPON_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "중복된 쿠폰 이름입니다."),
    COUPON_OUT_OF_STOCK(HttpStatus.CONFLICT, "쿠폰 수량이 모두 소진되었습니다."),
    COUPON_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 쿠폰입니다."),
    COUPON_INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "종료일은 시작일 이전일 수 없습니다."),
    COUPON_INVALID_START_TIME(HttpStatus.BAD_REQUEST, "시작일은 현재 시간 이후여야 합니다."),
    COUPON_INVALID_END_TIME(HttpStatus.BAD_REQUEST, "종료일은 현재 시간 이전일 수 없습니다."),
    COUPON_MIN_ORDER_PRICE_NOT_MET(HttpStatus.BAD_REQUEST, "최소 주문 금액을 만족하지 못해 쿠폰을 사용할 수 없습니다."),
    COUPON_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "사용 가능한 상태의 쿠폰이 아닙니다."),
    DISCOUNT_EXCEEDS_MIN_ORDER_PRICE(HttpStatus.BAD_REQUEST, "할인 금액은 최소 주문 금액보다 클 수 없습니다."),
    ADMIN_CANNOT_ISSUE_COUPON(HttpStatus.FORBIDDEN, "관리자는 쿠폰을 발급받을 수 없습니다."),
    FORBIDDEN_COUPON_ACCESS(HttpStatus.FORBIDDEN, "본인의 쿠폰만 사용할 수 있습니다."),

    // Inquiry 에러
    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "문의 내역을 찾을 수 없습니다."),
    NOT_INQUIRY_WRITER(HttpStatus.FORBIDDEN, "내 문의 내역이 아닙니다."),
    INQUIRY_ALREADY_ANSWERED(HttpStatus.BAD_REQUEST, "답변 완료된 문의입니다."),
    INQUIRY_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "삭제된 문의입니다."),

    // InquiryReply 에러
    INQUIRY_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "문의 답변 내역을 찾을 수 없습니다."),
    INQUIRY_REPLY_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "삭제된 문의 답변입니다."),

    // InquiryChat 에러
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "문의 채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_ALREADY_ASSIGNED(HttpStatus.CONFLICT, "이미 배정 완료된 문의입니다."),
    CHAT_ROOM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 문의 채팅방 접근 권한이 없습니다."),
    CHAT_ROOM_COMPLETE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "문의 채팅을 종료할 권한이 없습니다."),
    INQUIRY_CHAT_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 종료된 문의 채팅입니다."),
    STOMP_MESSAGE_ACCESS_FAILED(HttpStatus.BAD_REQUEST, "유효하지 않은 권한입니다."),

    // Payment 에러
    PAYMENT_NOT_CANCELABLE(HttpStatus.BAD_REQUEST, "결제 취소 가능한 상태가 아닙니다."),
    ORDER_NOT_IN_PAYMENT_PENDING(HttpStatus.BAD_REQUEST, "결제 진행 중인 주문이 아닙니다."),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "결제 금액이 주문 금액과 일치하지 않습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 내역을 찾을 수 없습니다."),
    PAYMENT_CANCEL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 취소에 실패했습니다."),

    // Redis 에러,
    REDIS_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 연결에 실패했습니다."),

    // Lock 에러
    LOCK_ACQUISITION_FAILED(HttpStatus.CONFLICT, "Lock 획득에 실패했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
