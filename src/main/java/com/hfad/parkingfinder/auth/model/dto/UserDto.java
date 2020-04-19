package com.hfad.parkingfinder.auth.model.dto;

import com.hfad.parkingfinder.auth.model.UserEntity;
import com.hfad.parkingfinder.auth.model.UserType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer userId;
    private boolean enabled;
    private UserType userType;

    public UserDto(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.enabled = userEntity.isEnabled();
        this.userType = userEntity.getFbToken() != null ? UserType.FB_USER : UserType.EMAIL_USER;
    }
}
