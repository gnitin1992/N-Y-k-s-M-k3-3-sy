package net.studiesmadesimple.nayaks.utils;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public interface Constants {

    String APP_NAME = "Nayaks";
    String LOG_TAG = "Nayaks";

    String CONTENT_TYPE = "contentType";
    String COUPON_VERIFIED = "couponVerified";
    String DEVICE_NAME = "deviceName";
    String DEVICE_ID = "deviceId";
    String IS_FIRST_TIME_USER = "isFirstTimeUser";
    String MOBILE_NUMBER = "mobileNumber";
    String MOBILE_NUMBER_VERIFIED = "mobileNumberVerified";
    String STREAM_ID = "streamId";
    String SEGMENT_ID = "segmentId";
    String UNIQUE_ID = "uniqueId";
    String UNIQUE_ID_ENTERED = "uniqueIdEntered";
    String URL = "url";
    String USER_REGISTERED = "userRegistered";

    // all the activities
    String MAIN_ACTIVITY = " MAIN ACTIVITY ";
    String MY_YOUTUBE_ACTIVITY = " MY YOUTUBE ACTIVITY";

    // all the fragment
    //a

    //b

    //c
    String CENTER_LIST_FRAGMENT = " CENTER LIST FRAGMENT ";
    String CHAPTERS_FRAGMENT = " CHAPTERS FRAGMENT ";

    //d
    String DEMO_CONTENT_FRAGMENT = " DEMO_CONTENT_FRAGMENT ";

    //e
    String ENTER_COUPON_CODE_FRAGMENT = " ENTER COUPON CODE FRAGMENT ";
    String ENTER_OTP_FRAGMENT = " ENTER OTP FRAGMENT ";
    String ENTER_UNIQUE_ID_FRAGMENT = " ENTER_UNIQUE_ID_FRAGMENT ";

    //f
    String FULL_SCREEN_VIDEO_FRAGMENT = " FULL SCREEN VIDEO FRAGMENT ";
    //g
    //h
    //i
    //j
    //k
    //l
    //m
    //n
    String NOTIFICATIONS_FRAGMENT = " NOTIFICATIONS FRAGMENT ";
    //o
    String OFFLINE_CONTENT_FRAGMENT = " ONLINE CONTENT FRAGMENT ";

    //p
    //q
    //r
    String REGISTRATION_FRAGMENT = " REGISTRATION FRAGMENT ";

    //s
    String SEGMENTS_FRAGMENT = " SEGMENTS FRAGMENT ";
    String SPLASH_SCREEN_FRAGMENT = " SPLASH SCREEN FRAGMENT ";
    String SUBJECTS_FRAGMENT = " SPLASH SCREEN FRAGMENT ";
    String STREAMS_FRAGMENT = " STREAMS FRAGMENT ";

    //t
    //u
    //v
    //w
    //x
    //y
    //z


    // all the adapters
    //a
    String ASSESSMENT_RECYCLER_ADAPTER = " ASSESSMENT RECYCLER ADAPTER ";
    //b
    //c
    String CENTER_LIST_RECYCLER_ADAPTER = " CENTER LIST RECYCLER ADAPTER ";
    String CHAPTERS_EXPANDABLE_LIST_ADAPTER = " CHAPTERS EXPANDABLE LIST ADAPTER ";

    //d
    String DEMO_CONTENT_RECYCLER_ADAPTER = " DEMO CONTENT RECYCLER ADAPTER ";

    //e
    //f
    //g
    //h
    //i
    //j
    //k
    //l
    //m
    //n
    String NOTIFICATIONS_RECYCLER_ADAPTER = " NOTIFICATIONS RECYCLER ADAPTER ";
    //o
    //p
    String PDF_RECYCLER_ADAPTER = " PDF RECYCLER ADAPTER ";
    //q
    //r
    //s
    String SEGMENTS_RECYCLER_ADAPTER = " SEGMENTS RECYCLER ADAPTER ";
    String SEGMENTS_GRID_ADAPTER = " SEGMENTS GRID ADAPTER ";
    String SUBJECTS_RECYCLER_ADAPTER = " SUBJECTS RECYCLER ADAPTER ";
    String STREAMS_GRID_ADAPTER = " STREAMS GRID ADAPTER ";
    //t
    //u
    //v
    //w
    //x
    //y
    //z


    // all links
//    String BASE_URL = "http://ec2-35-154-36-20.ap-south-1.compute.amazonaws.com/mobile-api/";
    String BASE_URL = "http://studiesmadesimple.com/mobile-api/";

    String streamUrl = BASE_URL + "get-all-stream";
    String segmentUrl = BASE_URL + "get-all-segment";

    String checkUniqueCodeUrl = BASE_URL + "check-unique-code";
    String registerUrl = BASE_URL + "registration";

    String demoContentUrl = BASE_URL + "demo-content";
    String verifyCouponCodeUrl = BASE_URL + "get-unique-code";

    String fetchCentersUrl = BASE_URL + "get-all-center";
    String segmentSelectionUrl = BASE_URL + "get-segment-selection";

    String subjectUrl = BASE_URL + "get-subject";

    String chaptersUrl = BASE_URL + "get-chapter";
    String notificationsUrl = BASE_URL + "set-token";

}
