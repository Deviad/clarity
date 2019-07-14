package com.clarity.transactiondispatcher.web.model;

import lombok.*;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountRequestDTO {
    @Pattern(regexp = "/(?=.*[a-z]+)(?=.*[0-9]+)(?=.*[A-Z]+)(?=.*[!@#$%^&*()_+\\[\\]{}:\\\\\";,.<>?|=-_]+).{8,20}/")
    @Getter
    @Setter
    String password;
}
