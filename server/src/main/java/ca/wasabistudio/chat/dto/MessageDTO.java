package ca.wasabistudio.chat.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import ca.wasabistudio.chat.entity.Message;

public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1502017135569482383L;

    private String body;

    private Date createTime;

    private String client;

    public MessageDTO() {
        body = "";
        createTime = new Date();
    }

    public MessageDTO(Message message) {
        this.body = message.getBody();
        this.createTime = message.getCreateTime();
        this.client = message.getClient().getUsername();
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

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
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
