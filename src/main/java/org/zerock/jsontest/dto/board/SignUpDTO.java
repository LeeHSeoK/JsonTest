package org.zerock.jsontest.dto.board;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    @NotEmpty
    @Size(min = 3, max = 12)
    private String name;

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 12)
    private String id;

    @NotEmpty
    @Size(min = 6, max = 15)
    private String password;
}
