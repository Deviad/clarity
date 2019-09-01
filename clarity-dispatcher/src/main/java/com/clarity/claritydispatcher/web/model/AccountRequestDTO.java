package com.clarity.claritydispatcher.web.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Introspected
public class AccountRequestDTO {
    @Pattern(
            regexp =
                    "(?=.*[a-z]+)(?=.*[0-9]+)(?=.*[A-Z]+)(?=.*[!@#$%^&*()_+\\[\\]{}:\\\\\";,.<>?|=-_]+).{8,20}")
    @Getter
    @NotBlank
    String password;
    @Getter
    @NotBlank
    String username;
    @Getter
    @NotBlank
    String firstName;
    @Getter
    @NotBlank
    String LastName;
}
