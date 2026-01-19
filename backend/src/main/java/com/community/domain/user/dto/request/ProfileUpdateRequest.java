package com.community.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequest {
    @Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,20}$",
            message = "닉네임은 한글, 영문, 숫자만 사용 가능합니다"
    )
    private String nickname;

    @Size(max = 500, message = "프로필 이미지 URL은 최대 500자까지 가능합니다")
    private String profileImage;

    /**
     * 닉네임 변경 요청 여부
     */
    public boolean hasNickname() {
        return nickname != null && !nickname.isBlank();
    }

    /**
     * 프로필 이미지 변경 요청 여부
     */
    public boolean hasProfileImage() {
        return profileImage != null && !profileImage.isBlank();
    }
}
