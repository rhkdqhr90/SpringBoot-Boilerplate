package com.community.domain.post.dto.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 검색 조건 DTO
 */
@Getter
@Setter
@Builder
public class PostSearchCondition {

    /**
     * 게시판 ID
     */
    private Long boardId;

    /**
     * 검색 키워드 (제목 + 내용)
     */
    private String keyword;

    /**
     * 태그
     */
    private String tag;

    /**
     * 작성자 ID
     */
    private Long userId;
}