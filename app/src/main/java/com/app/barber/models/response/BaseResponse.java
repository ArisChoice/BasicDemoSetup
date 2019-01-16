package com.app.barber.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 30/10/18.
 */

public class BaseResponse {

    /**
     * Message : Message
     * Status : 401
     */

    @SerializedName("Message")
    private String Message;
    @SerializedName("Status")
    private int Status;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }
}
