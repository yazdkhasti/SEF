package edu.rmit.sef.core.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.rmit.command.core.CommandUtil;

import java.util.UUID;

public class SocketMessage {

    private UUID messageId;
    private String name;
    private Class<?> type;
    private String serialisedObject;

    public SocketMessage() {

    }

    public Class<?> getType() {
        return type;
    }

    public String getSerialisedObject() {
        return serialisedObject;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public void setSerialisedObject(String serialisedObject) {
        this.serialisedObject = serialisedObject;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static SocketMessage newMessage(Object o) {
        SocketMessage msg = new SocketMessage();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String serialisedObject = mapper.writeValueAsString(o);
            msg.setSerialisedObject(serialisedObject);
            msg.setType(o.getClass());
            msg.setMessageId(UUID.randomUUID());
        } catch (Exception ex) {
            CommandUtil.throwAppExecutionException(ex);
        }

        return msg;
    }

    public SocketMessage getResponse(Object o) {
        SocketMessage msg = new SocketMessage();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String serialisedObject = mapper.writeValueAsString(o);
            msg.setSerialisedObject(serialisedObject);
            msg.setType(o.getClass());
            msg.setMessageId(this.messageId);
        } catch (Exception ex) {
            CommandUtil.throwAppExecutionException(ex);
        }
        return msg;
    }

    public static <T> T toObject(SocketMessage msg) {
        T object = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            object = (T) mapper.readValue(msg.getSerialisedObject(), msg.getType());

        } catch (Exception ex) {
            CommandUtil.throwAppExecutionException(ex);
        }
        return object;
    }


}
