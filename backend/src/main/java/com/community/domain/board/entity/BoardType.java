package com.community.domain.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {
    /**
     * 일반 게시판
     *
     * <p>기본 CRUD 기능만 제공</p>
     */
    GENERAL("일반 게시판"),

    /**
     * 갤러리 게시판
     *
     * <p>이미지 필수, 썸네일 자동 생성, 그리드 뷰</p>
     */
    GALLERY("갤러리"),
    /**
     * 장터 게시판
     *
     * <p>가격, 거래상태, 위치 필드 추가</p>
     */
    MARKET("중고장터"),

    /**
     * 질문답변 게시판
     *
     * <p>채택 기능, 채택 시 포인트 지급</p>
     */
    QNA("질문답변");
    private final String description;
}
