package com.roboadvice.dto;

import lombok.*;
import org.hibernate.validator.constraints.Email;


import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String name;
    private String surname;

    @NotNull @Email
    private String email;

    @NotNull
    private String password;

}
