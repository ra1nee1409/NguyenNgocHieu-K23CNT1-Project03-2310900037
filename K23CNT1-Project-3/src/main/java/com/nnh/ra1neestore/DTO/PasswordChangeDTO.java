package com.nnh.ra1neestore.DTO;

import lombok.Data;

@Data
public class PasswordChangeDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
