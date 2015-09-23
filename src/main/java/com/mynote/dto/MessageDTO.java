package com.mynote.dto;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
public class MessageDTO {

    private String messageCode;
    private String message;

    public MessageDTO() {
    }

    public MessageDTO(String messageCode, String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
