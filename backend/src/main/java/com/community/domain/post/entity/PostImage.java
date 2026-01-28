package com.community.domain.post.entity;

import com.community.core.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseEntity {
    /**
     * 소속 게시글
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /**
     * 이미지 URL
     */
    @Column(nullable = false, length = 500)
    private String url;

    /**
     * 원본 파일명
     */
    @Column(name = "original_name", length = 255)
    private String originalName;

    /**
     * 파일 크기 (bytes)
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * 표시 순서
     */
    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    // ========== 생성자 ==========

    @Builder
    public PostImage(String url, String originalName, Long fileSize, int displayOrder) {
        this.url = url;
        this.originalName = originalName;
        this.fileSize = fileSize;
        this.displayOrder = displayOrder;
    }

    // ========== 연관관계 메서드 ==========

    /**
     * 게시글 설정 (양방향 관계)
     */
    public void setPost(Post post) {
        this.post = post;
    }
}
