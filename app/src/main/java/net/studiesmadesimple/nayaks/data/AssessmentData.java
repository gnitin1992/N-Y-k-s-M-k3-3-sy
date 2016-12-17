package net.studiesmadesimple.nayaks.data;

/**
 * Created by sagar on 12/4/2016.
 */

public class AssessmentData {

    String assessmentId,segmentId,title,url;

    public AssessmentData(String assessmentId, String segmentId, String title, String url) {
        this.assessmentId = assessmentId;
        this.segmentId = segmentId;
        this.title = title;
        this.url = url;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
