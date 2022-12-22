package com.agilesekeri.asugar_api.model.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
