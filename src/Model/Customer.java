package Model;

import java.time.LocalDateTime;

/**
 * Customer class
 */
public class Customer {
    private int custId;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private int divId;
    private LocalDateTime createDate;

    /**
     * Constructor for Customer objects
     * @param custId
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param divId
     * @param createDate
     */

    public Customer(int custId, String name, String address, String postalCode, String phone, int divId, LocalDateTime createDate) {
        this.custId = custId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divId = divId;
        this.createDate = createDate;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDivId() {
        return divId;
    }

    public void setDivId(int divId) {
        this.divId = divId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return Integer.toString(getCustId());
    }
}
