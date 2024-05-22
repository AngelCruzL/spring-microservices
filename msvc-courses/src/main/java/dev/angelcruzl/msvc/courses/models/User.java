package dev.angelcruzl.msvc.courses.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    private String name;

    private String email;

    private String password;

}
