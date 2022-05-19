package Model;

import java.time.LocalDateTime;


/**
 * Appointment class
 */
public class Appointment {
    private int apptId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDT;
    private LocalDateTime endDT;
    private int contactId;
    private int custId;
    private int userId;
    private LocalDateTime createDate;

    /**
     * Constructor for Appointment objects
     * @param apptId
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startDT
     * @param endDT
     * @param contactId
     * @param custId
     * @param userId
     * @param createDate
     */
    public Appointment(int apptId, String title, String description, String location, String type, LocalDateTime startDT, LocalDateTime endDT, int contactId, int custId, int userId, LocalDateTime createDate) {
        this.apptId = apptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDT = startDT;
        this.endDT = endDT;
        this.contactId = contactId;
        this.custId = custId;
        this.userId = userId;
        this.createDate = createDate;
    }

    public int getApptId() {
        return apptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStartDT() {
        return startDT;
    }

    public void setStartDT(LocalDateTime startDT) {
        this.startDT = startDT;
    }

    public LocalDateTime getEndDT() {
        return endDT;
    }

    public void setEndDT(LocalDateTime endDT) {
        this.endDT = endDT;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

}
