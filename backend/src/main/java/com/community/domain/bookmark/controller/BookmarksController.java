package com.community.domain.bookmark.controller;

import com.community.core.common.dto.ApiResponse;
import com.community.core.common.dto.PageResponse;
import com.community.core.security.annotation.CurrentUser;
import com.community.domain.bookmark.dto.request.BookmarkRequest;
import com.community.domain.bookmark.dto.response.BookmarkResponse;
import com.community.domain.bookmark.dto.response.BookmarkToggleResponse;
import com.community.domain.bookmark.service.BookmarkService;
import com.community.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarksController {
    private final BookmarkService bookmarkService;

    /**
     *  북마크 토글 추가
     * @param postId 게시글
     * @param request 요청 DTO
     * @param user 사용자
     * @return BookmarkToggleResponse
     */
    @PostMapping("/posts/{postId}/bookmarks")
    public ResponseEntity<ApiResponse<BookmarkToggleResponse>> bookmarkToggle(
            @PathVariable Long postId,
            @RequestBody(required = false) BookmarkRequest request,
            @CurrentUser User user
    ){
        String folder = (request != null) ? request.getFolder() : null;
        BookmarkToggleResponse response = bookmarkService.toggleBookmark(user,postId,folder);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     *  내 북마크 목록 조회
     * @param folder 폴더명
     * @param pageable 페이지 번호 (0부터 시작)
     * @param user 사용자
     * @return 북마크 목록
     */
    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse<PageResponse<BookmarkResponse>>> getBookmarks(
            @RequestParam(required = false) String folder,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @CurrentUser User user
            ){
        Page<BookmarkResponse> bookmarks;
        if (folder != null && !folder.isBlank()) {
            bookmarks = bookmarkService.getBookmarksByFolder(user, folder, pageable);
        } else {
            bookmarks = bookmarkService.getBookmarks(user, pageable);
        }
        PageResponse<BookmarkResponse> response = PageResponse.of(bookmarks);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     *  내 폴더 목록 조회
     * @param user 사용자
     * @return 폴더명 목록
     */
    @GetMapping("/bookmarks/folders")
    public ResponseEntity<ApiResponse<List<String>>> getFolders(@CurrentUser User user) {

        List<String> folders = bookmarkService.getMyFolders(user);

        return ResponseEntity.ok(ApiResponse.success(folders));
    }
}
