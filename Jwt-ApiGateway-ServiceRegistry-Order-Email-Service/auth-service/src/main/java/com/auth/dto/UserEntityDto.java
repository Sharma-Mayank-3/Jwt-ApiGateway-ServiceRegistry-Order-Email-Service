package com.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntityDto {
    private int userId;
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;

    private int userAge;
}
