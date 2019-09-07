package com.clarity.claritydispatcher.web.model;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TransactionRequestDTO {
    @Pattern(regexp = "(?=.*[a-z]+)(?=.*[0-9]+)(?=.*[A-Z]+)(?=.*[!@#$%^&*()_+\\[\\]{}:\\\\\";,.<>?|=-_]+).{8,20}")
    @Getter
    @Setter
    @NotBlank
    String password;

    @Getter
    @Setter
    @NotBlank
    String wallet; //base64

    @Getter
    @Setter
    @NotBlank
    String toAddress;

    @Getter
    @Setter
    @NotNull
    BigDecimal amount;
}
