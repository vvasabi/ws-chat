package ca.wasabistudio.ca.chat.dto;

import java.util.Date;

import ca.wasabistudio.chat.entity.Message;

public class MessageDTO {

    private String body;

    private Date createTime;

    public MessageDTO() {
        body = "";
        createTime = new Date();
    }

    public MessageDTO(Message message) {
        this.body = message.getBody();
        this.createTime = message.getCreateTime();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
