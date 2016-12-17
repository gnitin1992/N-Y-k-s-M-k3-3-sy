package net.studiesmadesimple.nayaks.data;

import java.io.Serializable;

/**
 * Created by studiesmadesimple on 10/31/2016.
 */

public class TopicData implements Serializable {

    String topicId,topicName,actionId,chapterId,topicUrl,offlinePath;
    int isDemo;

    public TopicData(String topicId, String topicName, String actionId, String chapterId, String topicUrl, String offlinePath, int isDemo) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.actionId = actionId;
        this.chapterId = chapterId;
        this.topicUrl = topicUrl;
        this.offlinePath = offlinePath;
        this.isDemo = isDemo;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getTopicUrl() {
        return topicUrl;
    }

    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }

    public String getOfflinePath() {
        return offlinePath;
    }

    public void setOfflinePath(String offlinePath) {
        this.offlinePath = offlinePath;
    }

    public int isDemo() {
        return isDemo;
    }

    public void setDemo(int demo) {
        isDemo = demo;
    }
}


