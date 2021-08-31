package com.example.gochat.utilities;

import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Constants {
    public static final String KEY_PREFERENCE_NAME = "chapAppPreference";
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_NAME = "name";
    public static final String KEY_ABOUT = "about";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_IS_USER_AVAILABLE = "isUserAvailable";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timeStamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization"; // These 4 keys are should be same
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String,String> remoteMsgHeaders = null;

    public static HashMap<String,String> getRemoteMsgHeaders(){
        if(remoteMsgHeaders == null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAe6DFQgs:APA91bEHRBA8hE5O2nxCCYnIU0OeCtneALIrmrJf0GFlaTWCgI48pmtqKGU1RbmgdcehO5LTc2fdtANFp7CHxcrziMvlaBXqASphiQk3YTwBUnNB10cc3rFu1CNDETssKKpLv0vLbk2Y"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }

    public static String encodeString(String s) {
        try {
            byte[] bytes = s.getBytes("UTF-8");
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decodeString(String s){
        try {
            byte[] bytes = Base64.decode(s,Base64.DEFAULT);
            String text = new String(bytes,"UTF-8");
            return text;
        }
        catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }
}

