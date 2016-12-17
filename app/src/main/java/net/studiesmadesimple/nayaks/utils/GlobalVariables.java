package net.studiesmadesimple.nayaks.utils;

import android.app.Application;

/**
 * Created by studiesmadesimple on 11/15/2016.
 */

public class GlobalVariables extends Application {

    private boolean isStreamsLoaded,isSegmentsLoaded,isDemoContentLoaded,
    isSubjectsLoaded,isChaptersLoaded,isAssessmentsLoaded,isPDFsLoaded;

    public static GlobalVariables globalState = new GlobalVariables();

    private GlobalVariables() {

    }

    public static synchronized GlobalVariables getInstance(){

        return globalState;
    }

    public boolean isStreamsLoaded() {
        return isStreamsLoaded;
    }

    public void setStreamsLoaded(boolean streamLoaded) {
        isStreamsLoaded = streamLoaded;
    }

    public boolean isSegmentsLoaded() {
        return isSegmentsLoaded;
    }

    public void setSegmentsLoaded(boolean segmentsLoaded) {
        isSegmentsLoaded = segmentsLoaded;
    }

    public boolean isDemoContentLoaded() {
        return isDemoContentLoaded;
    }

    public void setDemoContentLoaded(boolean demoContentLoaded) {
        isDemoContentLoaded = demoContentLoaded;
    }

    public boolean isSubjectsLoaded() {
        return isSubjectsLoaded;
    }

    public void setSubjectsLoaded(boolean subjectsLoaded) {
        isSubjectsLoaded = subjectsLoaded;
    }

    public boolean isChaptersLoaded() {
        return isChaptersLoaded;
    }

    public void setChaptersLoaded(boolean chaptersLoaded) {
        isChaptersLoaded = chaptersLoaded;
    }

    public boolean isAssessmentsLoaded() {
        return isAssessmentsLoaded;
    }

    public void setAssessmentsLoaded(boolean assessmentsLoaded) {
        isAssessmentsLoaded = assessmentsLoaded;
    }

    public boolean isPDFsLoaded() {
        return isPDFsLoaded;
    }

    public void setPDFsLoaded(boolean PDFsLoaded) {
        isPDFsLoaded = PDFsLoaded;
    }

}
