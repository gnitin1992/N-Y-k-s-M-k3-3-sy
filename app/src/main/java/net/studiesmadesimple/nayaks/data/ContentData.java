package net.studiesmadesimple.nayaks.data;

/**
 * Created by studiesmadesimple on 10/31/2016.
 */

public class ContentData {

    String contentName,courseId;

    public ContentData(String contentName, String courseId) {
        this.contentName = contentName;
        this.courseId = courseId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
