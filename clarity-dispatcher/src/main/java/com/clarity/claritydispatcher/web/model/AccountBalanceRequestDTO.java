package com.clarity.claritydispatcher.web.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AccountBalanceRequestDTO {
  @Getter @Setter @NotBlank String wallet;
}
