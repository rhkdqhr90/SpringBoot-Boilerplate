package com.community.domain.bookmark.service;

import com.community.core.exception.ErrorCode;
import com.community.core.exception.custom.BadRequestException;
import com.community.core.exception.custom.NotFoundException;
import com.community.domain.bookmark.dto.response.BookmarkResponse;
import com.community.domain.bookmark.dto.response.BookmarkToggleResponse;
import com.community.domain.bookmark.entity.Bookmark;
import com.community.domain.bookmark.repository.BookmarkRepository;
import com.community.domain.post.entity.Post;
import com.community.domain.post.repository.PostRepository;
import com.community.domain.post.service.PostService;
import com.community.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;

    /**
     *  북마크 토글
     * @param user 사용자
     * @param postId 게시글
     * @param folder 폴더
     * @return BookmarkToggleResponse
     */
    @Transactional
    public BookmarkToggleResponse toggleBookmark(User user, Long postId, String folder) {
        Post post = postRepository.findById(postId)
                .orElseThrow((() -> new NotFoundException(ErrorCode.POST_NOT_FOUND)));

        if (post.isDeleted()) {
            throw new BadRequestException(ErrorCode.POST_ALREADY_DELETED);
        }

        Optional<Bookmark> existingOpt = bookmarkRepository.findByUserIdAndPostId(user.getId(),postId);

        if (existingOpt.isPresent()) {
            bookmarkRepository.delete(existingOpt.get());

            log.info("[BOOKMARK_REMOVE] userId={}, postId={}", user.getId(), postId);
            return BookmarkToggleResponse.of(false);
        } else {
            Bookmark bookmark = Bookmark.create(user, post, folder);
            bookmarkRepository.save(bookmark);

            log.info("[BOOKMARK_ADD] userId={}, postId={}, folder={}", user.getId(), postId, folder);
            return BookmarkToggleResponse.of(true);
        }
    }

    /**
     *  내 북마크 조회
     * @param user 사용자
     * @param pageable 페이징
     * @return 북마크 목록
     */
    public Page<BookmarkResponse> getBookmarks(User user, Pageable pageable) {
        Page<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(user.getId(), pageable);
        return bookmarks.map(BookmarkResponse::from);
    }

    /**
     * 폴더별 북마크 목록 조회
     *
     * @param user     사용자
     * @param folder   폴더명
     * @param pageable 페이징 정보
     * @return 북마크 목록
     */
    public Page<BookmarkResponse> getBookmarksByFolder(User user, String folder, Pageable pageable) {
        Page<Bookmark> bookmarks = bookmarkRepository.findAllByUserIdAndFolder(
                user.getId(), folder, pageable
        );
        return bookmarks.map(BookmarkResponse::from);
    }

    /**
     *  내 폴더 목록
     * @param user 사용자
     * @return 폴더 목록
     */
    public List<String> getMyFolders(User user) {
        return bookmarkRepository.findDistinctFoldersByUserId(user.getId());
    }

    /**
     * 북마크 여부 확인
     * @param user 사용자
     * @param postId 게시글
     * @return 북마크 여부 불린
     */
    public boolean isBookmarked(User user, Long postId) {
        return bookmarkRepository.existsByUserIdAndPostId(user.getId(), postId);
    }
}
