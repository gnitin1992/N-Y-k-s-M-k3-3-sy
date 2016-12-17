package net.studiesmadesimple.nayaks.data;

/**
 * Created by sagar on 12/4/2016.
 */

public class NotificationData {

    String notificationId,heading,message,deliveredDate;

    public NotificationData(String notificationId, String heading, String message, String deliveredDate) {
        this.notificationId = notificationId;
        this.heading = heading;
        this.message = message;
        this.deliveredDate = deliveredDate;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }
}
