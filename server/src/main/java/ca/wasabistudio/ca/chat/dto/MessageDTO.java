package ca.wasabistudio.ca.chat.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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

    public static Collection<MessageDTO> toDTOs(Collection<Message> messages) {
        Collection<MessageDTO> result = new HashSet<MessageDTO>();
        for (Message message : messages) {
            result.add(new MessageDTO(message));
        }
        return result;
    }

    public static List<MessageDTO> toDTOs(List<Message> messages) {
        List<MessageDTO> result = new ArrayList<MessageDTO>(messages.size());
        for (Message message : messages) {
            result.add(new MessageDTO(message));
        }
        return result;
    }

}
