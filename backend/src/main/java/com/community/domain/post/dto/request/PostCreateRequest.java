package com.community.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 게시글 생성 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequest {

    /**
     * 게시글 제목
     */
    @NotBlank(message = "제목을 입력해주세요")
    @Size(min = 2, max = 200, message = "제목은 2~200자여야 합니다")
    private String title;

    /**
     * 게시글 내용 (HTML)
     */
    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 50000, message = "내용은 50000자 이하여야 합니다")
    private String content;

    /**
     * 익명 여부
     */
    private boolean isAnonymous = false;

    /**
     * 태그 목록 (최대 5개)
     */
    @Size(max = 5, message = "태그는 최대 5개까지 가능합니다")
    private List<String> tags;

    /**
     * 이미지 ID 목록
     *
     * <p>이미지 업로드 API를 통해 받은 ID들을 전달합니다.</p>
     */
    private List<Long> imageIds;

    /**
     * 장터 게시판용 추가 필드
     *
     * <p>예: {"price": 50000, "tradeStatus": "SELLING", "location": "서울"}</p>
     */
    private Map<String, Object> marketFields;
}