package net.studiesmadesimple.nayaks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.studiesmadesimple.nayaks.data.ActionData;
import net.studiesmadesimple.nayaks.data.AssessmentData;
import net.studiesmadesimple.nayaks.data.CenterData;
import net.studiesmadesimple.nayaks.data.ChapterData;
import net.studiesmadesimple.nayaks.data.NewSegmentsData;
import net.studiesmadesimple.nayaks.data.NewStreamData;
import net.studiesmadesimple.nayaks.data.NotificationData;
import net.studiesmadesimple.nayaks.data.PDFData;
import net.studiesmadesimple.nayaks.data.SubjectData;
import net.studiesmadesimple.nayaks.data.TopicData;
import net.studiesmadesimple.nayaks.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by studiesmadesimple on 10/26/2016.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "appManager";

    // Table Names
    private static final String TABLE_STREAMS_LIST = "tableStreamsList";
    private static final String TABLE_SEGMENTS_LIST = "tableSegmentsList";
    private static final String TABLE_SUBJECTS_LIST = "tableSubjectsList";
    private static final String TABLE_CHAPTERS_LIST = "tableChaptersList";
    private static final String TABLE_TOPICS_LIST = "tableTopicsList";
    private static final String TABLE_ACTIONS_LIST = "tableActionsList";
    private static final String TABLE_CENTERS_LIST = "tableCentersList";
    private static final String TABLE_NOTIFICATIONS_LIST = "tableNotificaionsList";
    private static final String TABLE_ASSESSMENT_LIST = "tableAssessmentList";
    private static final String TABLE_PDFS_LIST = "tablePdfList";
    private static final String TABLE_OFFLINE_VIDEOS_LIST = "tableOfflineVideosList";


    // common Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_STREAM_ID = "courseId";
    private static final String KEY_SUBJECT_ID = "subjectId";


    // columns of TABLE Segment LIST
    private static final String KEY_SEGMENT_NAME = "segmentName";

    // columns of TABLE SUBJECT LIST
    // keyStreamId as subject will also be mapped to course
    // segmentId as subject will also be mapped to segment
    private static final String KEY_SUBJECT_NAME = "subjectName";

    // columns of TABLE  MAIN CHAPTER LIST
    // chapters will be mapped to subjects so will refer to subject
    private static final String KEY_CHAPTER_NAME = "chapterName";

    // columns of TABLE ACTION
    private static final String KEY_ACTION_NAME = "actionName";

    // columns of TABLE TOPICS
    //references KeyId of the chapter
    // will also be maped to SUBJECT ID incase 1 chapters belong to 2 subjects
    private static final String KEY_ACTION_ID = "actionId";
    private static final String KEY_TOPIC_NAME = "topicName";
    private static final String KEY_CHAPTER_ID = "chapterId";
    private static final String KEY_TOPIC_URL = "topicUrl";
    private static final String KEY_OFFLINE_PATH = "offlinePath";
    private static final String KEY_IS_DEMO_CONTENT = "demoContent";

    // columns of TABLE CENTERS
    private static final String KEY_CENTER_ADDRESS = "address";
    private static final String KEY_CENTER_CONTACT_NUMBER = "contactNumber";
    private static final String KEY_CENTER_NAME = "centerName";

    // columns of  TABLE_STREAM_LIST
    private static final String KEY_STREAM_NAME = "streamName";

    // columns of  TABLE_NOTIFICATIONS_LIST
    private static final String KEY_HEADING = "heading";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DELIVERED_DATE = "deliveredDate";

    // columns of  TABLE_ASSESSMENT_LIST and TABLE_PDFS_LIST
    private static final String KEY_SEGMENT_ID = "segmentId";
    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";
    private static final String KEY_CONTENT_TYPE = "contentType";


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_STREAMS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_STREAMS_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_STREAM_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_STREAMS_LIST);

        String CREATE_TABLE_SEGMENTS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_SEGMENTS_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_SEGMENT_NAME + " TEXT,"
                + KEY_CONTENT_TYPE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_SEGMENTS_LIST);

        String CREATE_TABLE_SUBJECTS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_SUBJECTS_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_SUBJECT_NAME + " TEXT,"
                + KEY_STREAM_ID + " INT,"
                + KEY_SEGMENT_ID + " INT"
                + ")";
        db.execSQL(CREATE_TABLE_SUBJECTS_LIST);

        String CREATE_TABLE_CHAPTERS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_CHAPTERS_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_CHAPTER_NAME + " TEXT,"
                + KEY_SUBJECT_ID + " INT"
                + ")";
        db.execSQL(CREATE_TABLE_CHAPTERS_LIST);

        String CREATE_TABLE_TOPICS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_TOPICS_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_TOPIC_NAME + " TEXT,"
                + KEY_ACTION_ID + " INT,"
                + KEY_CHAPTER_ID + " INT,"
                + KEY_TOPIC_URL + " TEXT,"
                + KEY_OFFLINE_PATH + " TEXT,"
                + KEY_IS_DEMO_CONTENT + " INT"
                + ")";
        db.execSQL(CREATE_TABLE_TOPICS_LIST);

        String CREATE_TABLE_OFFLINE_VIDEOS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_OFFLINE_VIDEOS_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_OFFLINE_PATH + " TEXT)";
        db.execSQL(CREATE_TABLE_OFFLINE_VIDEOS_LIST);

        String CREATE_TABLE_ACTIONS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_ACTIONS_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_ACTION_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_ACTIONS_LIST);

        String CREATE_TABLE_CENTERS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_CENTERS_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_CENTER_ADDRESS + " TEXT,"
                + KEY_CENTER_CONTACT_NUMBER + " TEXT,"
                + KEY_CENTER_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CENTERS_LIST);

        String CREATE_TABLE_NOTIFICATIONS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATIONS_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_HEADING + " TEXT,"
                + KEY_MESSAGE + " TEXT,"
                + KEY_DELIVERED_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_NOTIFICATIONS_LIST);

        String CREATE_TABLE_ASSESSMENT_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_ASSESSMENT_LIST + "("
                + KEY_ID + " INT PRIMARY KEY, "
                + KEY_SEGMENT_ID + " INT,"
                + KEY_TITLE + " TEXT,"
                + KEY_URL + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_ASSESSMENT_LIST);

        String CREATE_TABLE_PDFS_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_PDFS_LIST + "("
                + KEY_ID + " INT PRIMARY KEY, "
                + KEY_SEGMENT_ID + " INT,"
                + KEY_TITLE + " TEXT,"
                + KEY_URL + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_PDFS_LIST);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STREAMS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEGMENTS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAPTERS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIONS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CENTERS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDFS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE_VIDEOS_LIST);

        onCreate(db);
    }


    public void addSegment(NewSegmentsData segmentsData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, segmentsData.getSegmentId());
        contentValues.put(KEY_SEGMENT_NAME, segmentsData.getSegmentName());
        contentValues.put(KEY_CONTENT_TYPE, segmentsData.getContentType());

        // Inserting Row
        db.replace(TABLE_SEGMENTS_LIST, null, contentValues);
        db.close(); // Closing database connection


    }

    public void addSubject(SubjectData subjectData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, subjectData.getStreamId());
        contentValues.put(KEY_SUBJECT_NAME, subjectData.getSubjectName());

        // Inserting Row
        db.replace(TABLE_SUBJECTS_LIST, null, contentValues);
        db.close(); // Closing database connection

    }

    public void addChapter(ChapterData chapterData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, chapterData.getChapterId());
        contentValues.put(KEY_CHAPTER_NAME, chapterData.getChapterName());
        contentValues.put(KEY_SUBJECT_ID, chapterData.getSubjectId());

        // Inserting Row
        db.replace(TABLE_CHAPTERS_LIST, null, contentValues);
        db.close(); // Closing database connection


    }

    public void addTopic(TopicData topicData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, topicData.getTopicId());
        contentValues.put(KEY_TOPIC_NAME, topicData.getTopicName());
        contentValues.put(KEY_ACTION_ID, topicData.getActionId());
        contentValues.put(KEY_CHAPTER_ID, topicData.getChapterId());
        contentValues.put(KEY_TOPIC_URL, topicData.getTopicUrl());
        contentValues.put(KEY_OFFLINE_PATH, topicData.getOfflinePath());
        contentValues.put(KEY_IS_DEMO_CONTENT, topicData.isDemo());

        // Inserting Row
        db.replace(TABLE_TOPICS_LIST, null, contentValues);
        db.close(); // Closing database connection

    }

    public void addAction(ActionData actionData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ACTION_NAME, actionData.getActionName());

        // Inserting Row
        db.replace(TABLE_ACTIONS_LIST, null, contentValues);
        db.close(); // Closing database connection

    }

    public void addCenter(CenterData centerData) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, centerData.getCenterId());
        contentValues.put(KEY_CENTER_NAME, centerData.getCenterName());
        contentValues.put(KEY_CENTER_ADDRESS, centerData.getCenterAddress());
        contentValues.put(KEY_CENTER_CONTACT_NUMBER, centerData.getCenterPhone());
//        contentValues.put(KEY_CENTER_EMAIL_ADDRE, centerData.getCenterEmail());

        // Inserting Row
        db.replace(TABLE_CENTERS_LIST, null, contentValues);
        db.close(); // Closing database connection


    }


    public void addStream(NewStreamData streamsData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, streamsData.getStreamId());
        contentValues.put(KEY_STREAM_NAME, streamsData.getStreamName());
        // Inserting Row
        db.replace(TABLE_STREAMS_LIST, null, contentValues);
        db.close(); // Closing database connection

    }

    public void addNotification(NotificationData notificationData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, notificationData.getNotificationId());
        contentValues.put(KEY_HEADING, notificationData.getHeading());
        contentValues.put(KEY_MESSAGE, notificationData.getMessage());
        contentValues.put(KEY_DELIVERED_DATE, notificationData.getDeliveredDate());

        // Inserting Row
        db.replace(TABLE_NOTIFICATIONS_LIST, null, contentValues);
        db.close(); // Closing database connection

    }

    public void addAssessment(AssessmentData assessmentData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, assessmentData.getAssessmentId());
        contentValues.put(KEY_SEGMENT_ID, assessmentData.getSegmentId());
        contentValues.put(KEY_TITLE, assessmentData.getTitle());
        contentValues.put(KEY_URL, assessmentData.getUrl());

        // Inserting Row
        db.replace(TABLE_ASSESSMENT_LIST, null, contentValues);
        db.close(); // Closing database connection

    }

    public void addPDF(PDFData pdfData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, pdfData.getPdfId());
        contentValues.put(KEY_SEGMENT_ID, pdfData.getSegmentId());
        contentValues.put(KEY_TITLE, pdfData.getTitle());
        contentValues.put(KEY_URL, pdfData.getUrl());

        // Inserting Row
        db.insert(TABLE_PDFS_LIST, null, contentValues);
        db.close(); // Closing database connection

    }

    public void addOfflineVideo(String id,String path) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, id);
        contentValues.put(KEY_OFFLINE_PATH,path);

        // Inserting Row
        db.insert(TABLE_OFFLINE_VIDEOS_LIST, null, contentValues);
        db.close(); // Closing database connection

    }

    // all the delete functionalities

    public void updateSegment(NewSegmentsData segmentsData) {

    }

    public void updateSubject(SubjectData subjectData) {

    }

    public void updateChapter(ChapterData chapterData) {

    }


    public String[] fetchOfflinePath(String topicId) {


        String selectQuery = "SELECT * FROM " + TABLE_OFFLINE_VIDEOS_LIST + " WHERE " + KEY_ID + " = " + topicId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                String id = cursor.getString(cursor.getColumnIndex(KEY_ID));
                String path = cursor.getString(cursor.getColumnIndex(KEY_OFFLINE_PATH));
                return new String[]{id,path};

            } while (cursor.moveToNext());
        }

        return null;

    }

    public int updateTopic(TopicData topicData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTION_ID, topicData.getActionId());
        values.put(KEY_OFFLINE_PATH, topicData.getOfflinePath());

        // updating row
        return db.update(TABLE_TOPICS_LIST, values, KEY_ID + " = ?",
                new String[]{String.valueOf(topicData.getTopicId())});

    }

    public void updateAction(ActionData actionData) {

    }

    public void updateCenter(CenterData centerData) {

    }


    public void updateStream(NewStreamData streamData) {

    }

    public void updateNotification(NotificationData notificationData) {


    }

    public void updateAssessment(AssessmentData assessmentData) {


    }

    public void updatePDF(PDFData pdfData) {

    }


    // all the get functionalities

    public List<NewSegmentsData> getAllSegments() {


        List<NewSegmentsData> segmentsDatas = new ArrayList<NewSegmentsData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SEGMENTS_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                NewSegmentsData segmentData = new NewSegmentsData(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));

                // Adding contact to list
                segmentsDatas.add(segmentData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return segmentsDatas;

    }

    public List<SubjectData> getAllSubjects() {

        List<SubjectData> subjectDatas = new ArrayList<SubjectData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUBJECTS_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                SubjectData subjectData = new SubjectData(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));

                subjectDatas.add(subjectData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return subjectDatas;

    }

    public List<ChapterData> getAllChapters() {


        List<ChapterData> chapterDatas = new ArrayList<ChapterData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHAPTERS_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ChapterData chapterData = new ChapterData(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));

                // Adding contact to list
                chapterDatas.add(chapterData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return chapterDatas;


    }

    public List<TopicData> getAllTopics() {


        List<TopicData> topicDatas = new ArrayList<TopicData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TOPICS_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                TopicData topicData = new TopicData(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5),
                        cursor.getInt(6));

                // Adding contact to list
                topicDatas.add(topicData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return topicDatas;

    }

    public List<ActionData> getAllActions() {

        return null;
    }

    public List<CenterData> getAllCenters() {

        List<CenterData> centerDatas = new ArrayList<CenterData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CENTERS_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                CenterData centerData = new CenterData(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        "");

                // Adding contact to list
                centerDatas.add(centerData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return centerDatas;

    }


    public List<NewStreamData> getAllStreams() {

        List<NewStreamData> streamDatas = new ArrayList<NewStreamData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STREAMS_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d(Constants.LOG_TAG, " the cursor count is " + cursor.getCount());

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                NewStreamData streamData = new NewStreamData(
                        cursor.getString(0), cursor.getString(1));

                // Adding contact to list
                streamDatas.add(streamData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return streamDatas;

    }

    public List<NotificationData> getAllNotifications() {

        List<NotificationData> notificationDatas = new ArrayList<NotificationData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                NotificationData notificationData = new NotificationData(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));

                notificationDatas.add(notificationData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return notificationDatas;


    }

    public List<AssessmentData> getAllAssessments() {

        List<AssessmentData> assessmentDatas = new ArrayList<AssessmentData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ASSESSMENT_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                AssessmentData assessmentData = new AssessmentData(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));

                assessmentDatas.add(assessmentData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return assessmentDatas;
    }

    public List<PDFData> getAllPDFs() {

        List<PDFData> pdfDatas = new ArrayList<PDFData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PDFS_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                PDFData pdfData = new PDFData(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));

                // Adding contact to list
                pdfDatas.add(pdfData);
            } while (cursor.moveToNext());
        }

        return pdfDatas;
    }

    // all the count facilities

    public int getAllSegmentsCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SEGMENTS_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }

    public int getAllSubjectsCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SUBJECTS_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }

    public int getAllChaptersCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CHAPTERS_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }

    public int getAllTopicsCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TOPICS_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }

    public int getAllActionsCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_ACTIONS_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }

    public int getAllCentersCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CENTERS_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }


    public int getAllStreamsCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_STREAMS_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }

    public int getAllNotificationsCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NOTIFICATIONS_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }

    public int getAllAssessmentsCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_ASSESSMENT_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();

    }

    public int getAllPDFsCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PDFS_LIST;

        Cursor c = db.rawQuery(selectQuery, null);
        return c.getCount();
    }


    public HashMap<String, List<String>> getChapters(String whichStream, int position) {

        SQLiteDatabase db = this.getReadableDatabase();

//        String selectQuery = "SELECT "+KEY_ACTION_ID+" FROM "+TABLE_ACTIONS_LIST+" WHERE "+KEY_ACTION_NAME+" = "+whichStream;
//        String selectQuery = "SELECT * FROM "+TABLE_TOPICS_LIST+" WHERE "+KEY_ACTION_ID +" slecte query 1 sort ascending now" ;
//        String selectQuery = "SELECT * FROM "+TABLE_CHAPTERS_LIST;
//        db.execSQL(selectQuery);


        return null;

    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }


    }


}
