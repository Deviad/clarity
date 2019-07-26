package com.clarity.transactiondispatcher.web.model;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AccountBalanceRequestDTO {
    @Getter
    @Setter
    @NotBlank
    String wallet;
}
