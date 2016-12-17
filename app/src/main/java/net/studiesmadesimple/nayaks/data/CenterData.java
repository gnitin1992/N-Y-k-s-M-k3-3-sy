package net.studiesmadesimple.nayaks.data;

/**
 * Created by studiesmadesimple on 10/17/2016.
 */

public class CenterData {

    private String centerId,centerName,centerAddress,centerPhone,centerEmail;

    public CenterData(String centerId, String centerName, String centerAddress, String centerPhone, String centerEmail) {
        this.centerId = centerId;
        this.centerName = centerName;
        this.centerAddress = centerAddress;
        this.centerPhone = centerPhone;
        this.centerEmail = centerEmail;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getCenterAddress() {
        return centerAddress;
    }

    public void setCenterAddress(String centerAddress) {
        this.centerAddress = centerAddress;
    }

    public String getCenterPhone() {
        return centerPhone;
    }

    public void setCenterPhone(String centerPhone) {
        this.centerPhone = centerPhone;
    }

    public String getCenterEmail() {
        return centerEmail;
    }

    public void setCenterEmail(String centerEmail) {
        this.centerEmail = centerEmail;
    }
}
