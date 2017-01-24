package com.mservice.totp.api.model;

/**
 * Created by anton on 15/12/16.
 */
public class Secret {
    private String secret;

    public Secret() {
    }

    public Secret(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Secret{");
        sb.append("secret='").append(secret).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
