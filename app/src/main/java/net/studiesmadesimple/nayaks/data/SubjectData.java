package net.studiesmadesimple.nayaks.data;

/**
 * Created by studiesmadesimple on 10/31/2016.
 */

public class SubjectData {

    String  subjectId,subjectName,streamId,segmentId;

    public SubjectData(String subjectId, String subjectName, String streamId, String segmentId) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.streamId = streamId;
        this.segmentId = segmentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }
}
