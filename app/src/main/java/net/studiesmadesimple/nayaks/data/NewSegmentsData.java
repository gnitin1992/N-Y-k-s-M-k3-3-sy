package net.studiesmadesimple.nayaks.data;

/**
 * Created by sagar on 12/4/2016.
 */

public class NewSegmentsData {

    String segmentId,segmentName,contentType;

    public NewSegmentsData(String segmentId, String segmentName, String contentType) {
        this.segmentId = segmentId;
        this.segmentName = segmentName;
        this.contentType = contentType;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
