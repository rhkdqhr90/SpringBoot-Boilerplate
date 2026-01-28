package com.community.domain.board.controller;

import com.community.core.common.dto.ApiResponse;
import com.community.core.security.annotation.CurrentUser;
import com.community.domain.board.dto.request.BoardCreateRequest;
import com.community.domain.board.dto.request.BoardUpdateRequest;
import com.community.domain.board.dto.response.BoardListResponse;
import com.community.domain.board.dto.response.BoardResponse;
import com.community.domain.board.service.BoardService;
import com.community.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 활성화된 게시판 목록 조회
     */
    @GetMapping("/boards")
    public ResponseEntity<ApiResponse<List<BoardListResponse>>> getActiveBoards() {
        List<BoardListResponse> boards = boardService.getActiveBoards();
        return ResponseEntity.ok(ApiResponse.success(boards));
    }

    /**
     * 게시판 상세 조회 (slug)
     */
    @GetMapping("/boards/{slug}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoardBySlug(
            @PathVariable String slug
    ) {
        BoardResponse board = boardService.getBoardBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(board));
    }

    // ========== 관리자 전용 ==========

    /**
     * 전체 게시판 목록 조회 (관리자)
     */
    @GetMapping("/admin/boards")
    public ResponseEntity<ApiResponse<List<BoardListResponse>>> getAllBoards(
            @CurrentUser User user
    ) {
        List<BoardListResponse> boards = boardService.getAllBoards(user);
        return ResponseEntity.ok(ApiResponse.success(boards));
    }

    /**
     * 게시판 상세 조회 by ID (관리자)
     */
    @GetMapping("/admin/boards/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoardById(
            @PathVariable Long boardId,
            @CurrentUser User user
    ) {
        BoardResponse board = boardService.getBoardById(boardId, user);
        return ResponseEntity.ok(ApiResponse.success(board));
    }

    /**
     * 게시판 생성 (관리자)
     */
    @PostMapping("/admin/boards")
    public ResponseEntity<ApiResponse<Map<String, Long>>> createBoard(
            @Valid @RequestBody BoardCreateRequest request,
            @CurrentUser User user
    ) {
        Long boardId = boardService.createBoard(request, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(Map.of("id", boardId)));
    }

    /**
     * 게시판 수정 (관리자)
     */
    @PutMapping("/admin/boards/{boardId}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardUpdateRequest request,
            @CurrentUser User user
    ) {
        boardService.updateBoard(boardId, request, user);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 게시판 활성화/비활성화 토글 (관리자)
     */
    @PatchMapping("/admin/boards/{boardId}/toggle")
    public ResponseEntity<ApiResponse<Void>> toggleBoardActive(
            @PathVariable Long boardId,
            @CurrentUser User user
    ) {
        boardService.toggleBoardActive(boardId, user);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 게시판 삭제 (관리자)
     */
    @DeleteMapping("/admin/boards/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable Long boardId,
            @CurrentUser User user
    ) {
        boardService.deleteBoard(boardId, user);
        return ResponseEntity.ok(ApiResponse.success("게시판이 삭제되었습니다."));
    }
}
