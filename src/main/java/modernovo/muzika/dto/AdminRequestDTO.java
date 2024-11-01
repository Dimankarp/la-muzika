package modernovo.muzika.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import modernovo.muzika.model.AdminRequestStatus;

import java.time.ZonedDateTime;

public class AdminRequestDTO {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private ZonedDateTime creationDate;
    private AdminRequestStatus status;
    private String senderName;
    private Long senderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public AdminRequestStatus getStatus() {
        return status;
    }

    public void setStatus(AdminRequestStatus status) {
        this.status = status;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}
