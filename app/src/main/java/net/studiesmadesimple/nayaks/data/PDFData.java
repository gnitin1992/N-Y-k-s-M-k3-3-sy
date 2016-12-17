package net.studiesmadesimple.nayaks.data;

/**
 * Created by sagar on 12/4/2016.
 */

public class PDFData {


    String pdfId,segmentId,title,url;

    public PDFData(String pdfId, String segmentId, String title, String url) {
        this.pdfId = pdfId;
        this.segmentId = segmentId;
        this.title = title;
        this.url = url;
    }

    public String getPdfId() {
        return pdfId;
    }

    public void setPdfId(String pdfId) {
        this.pdfId = pdfId;
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
