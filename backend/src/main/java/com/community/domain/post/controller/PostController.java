package com.community.domain.post.controller;

import com.community.core.common.dto.ApiResponse;
import com.community.core.common.dto.PageResponse;
import com.community.core.security.annotation.CurrentUser;
import com.community.domain.post.dto.condition.PostSearchCondition;
import com.community.domain.post.dto.request.PostCreateRequest;
import com.community.domain.post.dto.request.PostUpdateRequest;
import com.community.domain.post.dto.response.PostDetailResponse;
import com.community.domain.post.dto.response.PostListResponse;
import com.community.domain.post.service.PostQueryService;
import com.community.domain.post.service.PostService;
import com.community.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostQueryService postQueryService;

    /**
     * 게시글 작성
     * @param slug 게시판 슬러그
     * @param request 생성 요청DTO
     * @param user 작성자
     * @return 생성된 게시글 ID
     */
    @PostMapping("/boards/{slug}/posts")
    public ResponseEntity<ApiResponse<Long>> createPost(@PathVariable String slug,@Valid @RequestBody PostCreateRequest request, @CurrentUser User user) {
        Long postId = postService.createPost(slug, request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(postId));
    }

    /**
     *  게시글 상세 조회
     * @param postId 게시글 ID
     * @param user 사용자( 비로그인 가능)
     * @return 게시글 상세 정보
     */
    @GetMapping("posts/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(@PathVariable Long postId,@CurrentUser(required = false) User user){
        PostDetailResponse response = postQueryService.getPostDetail(postId, user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 게시글 수정
     * @param postId 게시글 ID
     * @param request 게시글 수정 요청
     * @param user 사용자 ID(필수)
     * @return 성공메세지
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(@PathVariable Long postId, @Valid @RequestBody PostUpdateRequest request,@CurrentUser User user){
        postService.updatePost(postId, request, user);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 게시글 삭제
     * @param postId 게시글 ID
     * @param user 사용자 ID
     * @return 성공메세지
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId,@CurrentUser User user){
        postService.deletePost(postId, user);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 게시판별 게시글 목록 조회
     * @param slug 게시판 슬러그
     * @param pageable 페이지 정보(page, size, sort)
     * @return 게시글 목록(페이징)
     */
    @GetMapping("/boards/{slug}/posts")
    public ResponseEntity<ApiResponse<PageResponse<PostListResponse>>> getPosts(@PathVariable String slug, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse<PostListResponse> response = postQueryService.getPostList(slug, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 게시글 검색
     * @param keyword 검색키워드 (선택)
     * @param boardId 게시판 ID (선택)
     * @param tag 태그 (선택)
     * @param pageable 페이징
     * @return 검색결과
     */
    @GetMapping("/posts/search")
    public ResponseEntity<ApiResponse<PageResponse<PostListResponse>>> searchPost(
          @RequestParam(required = false) String keyword,
          @RequestParam(required = false) Long boardId,
          @RequestParam(required = false) String tag,
          @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        PostSearchCondition condition = PostSearchCondition.builder()
                .keyword(keyword)
                .boardId(boardId)
                .tag(tag)
                .build();
        PageResponse<PostListResponse> response = postQueryService.searchPost(condition, pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 사용자 작설 게시글 목록 조회
     * @param userId 사용자 ID
     * @param pageable 페이징
     * @return 게시글 목록
     */
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<ApiResponse<PageResponse<PostListResponse>>> getPostsByUser(@PathVariable Long userId, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse<PostListResponse> response = postQueryService.getPostsByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     *  공지글 토글
     * @param postId 게시글 ID
     * @param user 관리자
     * @return 성공 메세지
     */
    @PatchMapping("/posts/{postId}/notice")
    public ResponseEntity<ApiResponse<Void>> noticePost(@PathVariable Long postId,@CurrentUser User user){
        postService.toggleNotice(postId, user);
        return ResponseEntity.ok(ApiResponse.success("공지 상태가 변경되었습니다."));
    }

    /**
     *  내 작성글 목록 조회
      * @param user  사용자
     * @param pageable  페이징 정보
     * @return 게시판 목록
     */
    @GetMapping("/posts/me")
    public ResponseEntity<ApiResponse<PageResponse<PostListResponse>>> getMyPosts(@CurrentUser User user,@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse<PostListResponse> response = postQueryService.getPostsByUser(user.getId(), Pageable.unpaged());
        return ResponseEntity.ok(ApiResponse.success(response));
    }



}
