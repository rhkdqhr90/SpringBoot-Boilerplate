package com.community.domain.user.dto.response;

import com.community.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProfileResponse {
    private Long id;
    private String nickname;
    private String profileImage;
    private Integer postCount;
    private Integer commentCount;
    private LocalDateTime createdAt;

    /**
     * User 엔티티와 통계 정보로부터 ProfileResponse 생성
     *
     * @param user User 엔티티
     * @param postCount 작성한 게시글 수 (집계 쿼리)
     * @param commentCount 작성한 댓글 수 (집계 쿼리)
     * @return ProfileResponse
     */
    public static ProfileResponse from(User user, Integer postCount, Integer commentCount) {
        return ProfileResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .postCount(postCount)
                .commentCount(commentCount)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
