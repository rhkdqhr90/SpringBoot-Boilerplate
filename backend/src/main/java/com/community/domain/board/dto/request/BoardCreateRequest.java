package com.community.domain.board.dto.request;

import com.community.domain.board.entity.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class BoardCreateRequest {

    /**
     * 게시판 이름
     */
    @NotBlank(message = "게시판 이름을 입력해주세요")
    @Size(min = 2, max = 100, message = "게시판 이름은 2~100자여야 합니다")
    private String name;

    /**
     * URL 슬러그
     *
     * <p>영문 소문자, 숫자, 하이픈만 허용</p>
     */
    @NotBlank(message = "슬러그를 입력해주세요")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "슬러그는 영문 소문자, 숫자, 하이픈만 사용 가능합니다")
    @Size(min = 2, max = 100, message = "슬러그는 2~100자여야 합니다")
    private String slug;

    /**
     * 게시판 설명
     */
    @Size(max = 500, message = "설명은 500자 이하여야 합니다")
    private String description;

    /**
     * 게시판 타입
     */
    @NotNull(message = "게시판 타입을 선택해주세요")
    private BoardType boardType;

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
