package com.community.domain.board.service;

import com.community.core.exception.ErrorCode;
import com.community.core.exception.custom.BadRequestException;
import com.community.core.exception.custom.ForbiddenException;
import com.community.core.exception.custom.NotFoundException;
import com.community.domain.board.dto.request.BoardCreateRequest;
import com.community.domain.board.dto.request.BoardUpdateRequest;
import com.community.domain.board.dto.response.BoardListResponse;
import com.community.domain.board.dto.response.BoardResponse;
import com.community.domain.board.entity.Board;
import com.community.domain.board.repository.BoardRepository;
import com.community.domain.user.entity.Role;
import com.community.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    // ========== 조회 ==========

    /**
     * 활성화된 게시판 목록 조회 (일반 사용자)
     */
    public List<BoardListResponse> getActiveBoards() {
        return boardRepository.findAllActiveBoards().stream()
                .map(BoardListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 전체 게시판 목록 조회 (관리자)
     */
    public List<BoardListResponse> getAllBoards(User user) {
        validateAdmin(user);

        return boardRepository.findAllOrderByDisplayOrder().stream()
                .map(BoardListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 게시판 상세 조회 (slug)
     */
    public BoardResponse getBoardBySlug(String slug) {
        Board board = findBoardBySlug(slug);
        return BoardResponse.from(board);
    }

    /**
     * 게시판 상세 조회 (id, 관리자용)
     */
    public BoardResponse getBoardById(Long boardId, User user) {
        validateAdmin(user);

        Board board = findBoardById(boardId);
        return BoardResponse.from(board);
    }

    // ========== 생성 ==========

    /**
     * 게시판 생성 (관리자)
     */
    @Transactional
    public Long createBoard(BoardCreateRequest request, User user) {
        validateAdmin(user);

        // slug 중복 체크
        if (boardRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException(ErrorCode.DUPLICATE_BOARD_SLUG);
        }

        // 게시판 생성
        Board board = Board.create(
                request.getName(),
                request.getSlug(),
                request.getDescription(),
                request.getBoardType(),
                request.getSettings()
        );

        // 추가 설정 반영
        board.changeDisplayOrder(request.getDisplayOrder());

        boardRepository.save(board);

        log.info("[BOARD_CREATE] boardId={}, slug={}, type={}, admin={}",
                board.getId(), board.getSlug(), board.getBoardType(), user.getId());

        return board.getId();
    }

    // ========== 수정 ==========

    /**
     * 게시판 수정 (관리자)
     */
    @Transactional
    public void updateBoard(Long boardId, BoardUpdateRequest request, User user) {
        validateAdmin(user);

        Board board = findBoardById(boardId);

        board.update(
                request.getName(),
                request.getDescription(),
                request.getSettings(),
                request.getDisplayOrder(),
                request.isWriteRequiresLogin(),
                request.isCommentRequiresLogin()
        );

        log.info("[BOARD_UPDATE] boardId={}, admin={}", boardId, user.getId());
    }

    /**
     * 게시판 활성화/비활성화 (관리자)
     */
    @Transactional
    public void toggleBoardActive(Long boardId, User user) {
        validateAdmin(user);

        Board board = findBoardById(boardId);

        if (board.isActive()) {
            board.deactivate();
        } else {
            board.activate();
        }

        log.info("[BOARD_TOGGLE] boardId={}, isActive={}, admin={}",
                boardId, board.isActive(), user.getId());
    }

    // ========== 삭제 ==========

    /**
     * 게시판 삭제 (관리자)
     *
     * <p>물리 삭제 (게시글이 있으면 삭제 불가)</p>
     */
    @Transactional
    public void deleteBoard(Long boardId, User user) {
        validateAdmin(user);

        Board board = findBoardById(boardId);

        // TODO: 게시글 존재 여부 체크 (Post 도메인 구현 후)
        // if (postRepository.existsByBoardId(boardId)) {
        //     throw new BadRequestException(ErrorCode.BOARD_HAS_POSTS);
        // }

        boardRepository.delete(board);

        log.info("[BOARD_DELETE] boardId={}, slug={}, admin={}",
                boardId, board.getSlug(), user.getId());
    }

    // ========== 헬퍼 메서드 ==========

    /**
     * 관리자 권한 검증
     */
    private void validateAdmin(User user) {
        if (user.getRole() != Role.ADMIN) {
            throw new ForbiddenException(ErrorCode.ADMIN_ONLY);
        }
    }

    /**
     * 게시판 조회 (ID)
     */
    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    /**
     * 게시판 조회 (Slug)
     */
    private Board findBoardBySlug(String slug) {
        return boardRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

}
