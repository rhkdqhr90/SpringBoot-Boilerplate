package com.community.domain.board.repository;


import com.community.domain.board.entity.Board;
import com.community.domain.board.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    /**
     * slug로 게시판 조회
     */
    Optional<Board> findBySlug(String slug);

    /**
     * slug 존재 여부 확인
     */
    boolean existsBySlug(String slug);

    /**
     * 활성화된 게시판 목록 조회 (표시 순서)
     */
    @Query("SELECT b FROM Board b WHERE b.isActive = true ORDER BY b.displayOrder ASC, b.id ASC")
    List<Board> findAllActiveBoards();

    /**
     * 게시판 타입으로 조회
     */
    List<Board> findByBoardType(BoardType boardType);

    /**
     * 전체 게시판 목록 조회 (관리자용, 표시 순서)
     */
    @Query("SELECT b FROM Board b ORDER BY b.displayOrder ASC, b.id ASC")
    List<Board> findAllOrderByDisplayOrder();
}
