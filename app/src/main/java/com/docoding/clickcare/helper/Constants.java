package com.docoding.clickcare.helper;

import com.docoding.clickcare.state.GlobalUserState;

import java.util.HashMap;

public class Constants {
    private static final String PACKAGE_NAME = "com.docoding.clickcare";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final int SUCCESS_RESULT = 1;
    public static final int FAILURE_RESULT = 0;

    // firebase cloud messaging
    public static final String KEY_LOGIN_INFO = "pasien";
    public static final String KEY_NO_ANTRIAN = "pasien";
    public static final String KEY_NAMA_PASIEN = "name_pasien";
    public static final String KEY_ANTRIAN_PASIEN = "no_antrian";
    public static final String KEY_KELUHAN_PASIEN = "keluhan_pasien";
    public static final String KEY_WAKTU_PASIEN = "waktu_pasien";
    public static final String KEY_DOKTER_PASIEN = "dokter_pasien";
    public static final String KEY_COLLECTION_USERS = "Users";
    public static final String KEY_COLLECTION_DOCTORS = "doctor-register";
    public static final String KEY_NAME = "name";
    public static final String KEY_NAME_DOCTOR = "nama";
    public static final String KEY_DOCTOR_SPESIALIST = "spesialis";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String, String> remoteMsgHeaders = null;
    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAscuYF1g:APA91bGMaeGbHoeMkZp5rywvW6IsoNd1iXMGA7EvElqX-oYqGLtBQputY3UWLVTNJv-GAHKI3UM7ncCe6vxtV7jI7TKQGBUvM3s_5_Wl4X1SVow7jrQuAQGR4D9My74NHFnvEAMoF0hq"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }
}
