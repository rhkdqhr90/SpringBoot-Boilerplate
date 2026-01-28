package com.community.domain.board.entity;

import com.community.core.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "boards")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    /**
     * 게시판 이름
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * URL 슬러그 (고유값)
     *
     * <p>예: "free", "notice", "market"</p>
     */
    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    /**
     * 게시판 설명
     */
    @Column(length = 500)
    private String description;

    /**
     * 게시판 타입
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false, length = 20)
    private BoardType boardType;

    /**
     * 게시판 타입별 설정 (JSONB)
     *
     * <p>예: {"allowAnonymous": true, "maxImages": 10}</p>
     */
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> settings = new HashMap<>();

    /**
     * 표시 순서
     */
    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    /**
     * 활성화 여부
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    /**
     * 글쓰기 로그인 필요 여부
     */
    @Column(name = "write_requires_login", nullable = false)
    private boolean writeRequiresLogin = true;

    /**
     * 댓글 로그인 필요 여부
     */
    @Column(name = "comment_requires_login", nullable = false)
    private boolean commentRequiresLogin = true;

    // ========== 생성자 ==========

    /**
     * 게시판 생성 (관리자)
     */
    public static Board create(
            String name,
            String slug,
            String description,
            BoardType boardType,
            Map<String, Object> settings
    ) {
        Board board = new Board();
        board.name = name;
        board.slug = slug;
        board.description = description;
        board.boardType = boardType;
        board.settings = settings != null ? settings : new HashMap<>();
        return board;
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 게시판 정보 수정
     */
    public void update(
            String name,
            String description,
            Map<String, Object> settings,
            int displayOrder,
            boolean writeRequiresLogin,
            boolean commentRequiresLogin
    ) {
        this.name = name;
        this.description = description;
        this.settings = settings;
        this.displayOrder = displayOrder;
        this.writeRequiresLogin = writeRequiresLogin;
        this.commentRequiresLogin = commentRequiresLogin;
    }

    /**
     * 게시판 활성화
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * 게시판 비활성화
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * 표시 순서 변경
     */
    public void changeDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
