package net.studiesmadesimple.nayaks.data;

/**
 * Created by studiesmadesimple on 10/31/2016.
 */

public class ChapterData {

    String chapterId,chapterName,subjectId;

    public ChapterData(String chapterId, String chapterName, String subjectId) {
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.subjectId = subjectId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
