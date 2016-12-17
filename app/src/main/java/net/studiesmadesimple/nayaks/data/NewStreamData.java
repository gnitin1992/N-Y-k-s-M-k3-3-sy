package net.studiesmadesimple.nayaks.data;

/**
 * Created by sagar on 12/4/2016.
 */

public class NewStreamData {

    String streamId,streamName;

    public NewStreamData(String streamId, String streamName) {
        this.streamId = streamId;
        this.streamName = streamName;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

}
