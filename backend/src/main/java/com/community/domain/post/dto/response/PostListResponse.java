package com.community.domain.post.dto.response;

import com.community.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 목록 응답 DTO
 *
 * <p>게시글 목록에 표시될 요약 정보만 포함합니다.</p>
 */
@Getter
@Builder
public class PostListResponse {

    /**
     * 게시글 ID
     */
    private Long id;

    /**
     * 제목
     */
    private String title;

    /**
     * 내용 미리보기 (100자)
     */
    private String preview;

    /**
     * 작성자 정보
     */
    private AuthorInfo author;

    /**
     * 익명 여부
     */
    private boolean isAnonymous;

    /**
     * 조회수
     */
    private int viewCount;

    /**
     * 댓글 수
     */
    private int commentCount;

    /**
     * 좋아요 수
     */
    private int likeCount;

    /**
     * 이미지 포함 여부
     */
    private boolean hasImage;

    /**
     * 썸네일 URL (GALLERY 타입)
     */
    private String thumbnailUrl;

    /**
     * 태그 목록
     */
    private List<String> tags;

    /**
     * 공지글 여부
     */
    private boolean isNotice;

    /**
     * 작성일시
     */
    private LocalDateTime createdAt;

    /**
     * Entity → DTO 변환
     */
    public static PostListResponse from(Post post, List<String> tags) {
        return PostListResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .preview(extractPreview(post.getContent()))
                .author(AuthorInfo.from(post.getUser(), post.isAnonymous()))
                .isAnonymous(post.isAnonymous())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .hasImage(!post.getImages().isEmpty())
                .thumbnailUrl(extractThumbnailUrl(post))
                .tags(tags)
                .isNotice(post.isNotice())
                .createdAt(post.getCreatedAt())
                .build();
    }

    /**
     * 내용에서 미리보기 추출 (HTML 태그 제거 후 100자)
     */
    private static String extractPreview(String content) {
        if (content == null) {
            return "";
        }

        // HTML 태그 제거
        String plainText = content.replaceAll("<[^>]*>", "");

        // 100자로 자르기
        if (plainText.length() > 100) {
            return plainText.substring(0, 100) + "...";
        }

        return plainText;
    }

    /**
     * 썸네일 URL 추출 (GALLERY 타입의 경우)
     */
    private static String extractThumbnailUrl(Post post) {
        Object thumbnailUrl = post.getExtraFields().get("thumbnailUrl");
        return thumbnailUrl != null ? thumbnailUrl.toString() : null;
    }

    /**
     * 작성자 정보 (내부 클래스)
     */
    @Getter
    @Builder
    public static class AuthorInfo {
        private Long id;
        private String nickname;
        private String profileImage;

        public static AuthorInfo from(com.community.domain.user.entity.User user, boolean isAnonymous) {
            if (isAnonymous) {
                return AuthorInfo.builder()
                        .id(null)
                        .nickname("익명")
                        .profileImage(null)
                        .build();
            }

            return AuthorInfo.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .build();
        }
    }
}