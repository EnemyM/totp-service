package com.mservice.totp.api.model;

/**
 * Created by anton
 */
public class ResponseContainer {

    private Integer code;
    private String content;

    public ResponseContainer() {
    }

    public ResponseContainer(Integer code, String content) {
        this.code = code;
        this.content = content;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResponseContainer{");
        sb.append("code=").append(code);
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
