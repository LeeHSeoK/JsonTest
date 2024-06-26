package org.zerock.jsontest.dto.board;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoginDTO {
    @NotEmpty
    @Size(min = 3, max = 12)
    private String id;
    @NotEmpty
    @Size(min = 6, max = 15)
    private String password;
}
