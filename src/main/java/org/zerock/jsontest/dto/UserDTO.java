package org.zerock.jsontest.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    //로그인용
    @NotEmpty
    private String id;
    private String nickname;
    private String password;
    private String address;
}
