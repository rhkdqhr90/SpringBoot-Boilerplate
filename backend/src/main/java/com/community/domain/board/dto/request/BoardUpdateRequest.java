package com.community.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateRequest {
    /**
     * 게시판 이름
     */
    @NotBlank(message = "게시판 이름을 입력해주세요")
    @Size(min = 2, max = 100, message = "게시판 이름은 2~100자여야 합니다")
    private String name;

    /**
     * 게시판 설명
     */
    @Size(max = 500, message = "설명은 500자 이하여야 합니다")
    private String description;

    /**
     * 게시판 설정 (JSONB)
     */
    private Map<String, Object> settings = new HashMap<>();

    /**
     * 표시 순서
     */
    private int displayOrder = 0;

    /**
     * 글쓰기 로그인 필요 여부
     */
    private boolean writeRequiresLogin = true;

    /**
     * 댓글 로그인 필요 여부
     */
    private boolean commentRequiresLogin = true;
}
