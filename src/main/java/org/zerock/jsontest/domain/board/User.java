package org.zerock.jsontest.domain.board;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    private String id;
    private String name;
    private String password;

    public void changeUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
