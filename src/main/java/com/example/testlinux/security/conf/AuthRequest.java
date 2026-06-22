package com.example.testlinux.security.conf;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class AuthRequest {

    String login;

    String password;

    public boolean isValid() {
        return StringUtils.isNotBlank(login) && StringUtils.isNotBlank(password);
    }
}
