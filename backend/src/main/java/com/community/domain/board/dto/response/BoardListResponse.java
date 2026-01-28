package com.community.domain.board.dto.response;

import com.community.domain.board.entity.Board;
import com.community.domain.board.entity.BoardType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardListResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BoardType boardType;
    private String boardTypeDescription;
    private int displayOrder;
    private boolean isActive;

    // TODO: 게시글 개수 추가 (Post 도메인 구현 후)
    // private long postCount;

    /**
     * Entity → DTO 변환
     */
    public static BoardListResponse from(Board board) {
        return BoardListResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .slug(board.getSlug())
                .description(board.getDescription())
                .boardType(board.getBoardType())
                .boardTypeDescription(board.getBoardType().getDescription())
                .displayOrder(board.getDisplayOrder())
                .isActive(board.isActive())
                .build();
    }
}
