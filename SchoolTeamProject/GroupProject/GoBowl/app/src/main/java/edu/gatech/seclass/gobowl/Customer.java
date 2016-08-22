package edu.gatech.seclass.gobowl;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Customer POJO
 */
public class Customer {

    // -
    private String firstName;
    private String lastName;
    private String email;
    private int dbID;
    private String customerID;
    private int total;
    private int vipStatus;

    public Customer(int dbID, String firstName, String lastName, String email, int total, int vipStatus) {
        this.dbID = dbID;
        this.customerID = Utils.intToHex(dbID);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.total = total;
        this.vipStatus = vipStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get the 4 digit customer hex ID
     *
     * @return
     */
    public String getCustomerID() {
        return customerID;
    }

    public String getCustomerIdInt() {
        return Integer.decode(this.customerID).toString();
    }

    public int getDbID() {
        return dbID;
    }

    public String getDbIDString() {
        return Integer.toString(this.dbID);
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public int getVipStatus() {
        return vipStatus;
    }

    public String vipStatusToString() {
        return (this.vipStatus == 1) ? "Yes" : "No";
    }

    public void setVipStatus(int vipStatus) {
        this.vipStatus = vipStatus;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public String uiString() {
        return getCustomerID() + " - " + getFirstName() + " " + getLastName();
    }
}
