package com.logpie.android.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.logpie.android.util.LogpieLog;

public class Comment
{
    public static String KEY_SENDER_USER_ID = "com.logpie.android.comment.sender_userid";
    public static String KEY_SEND_TO_USER_ID = "com.logpie.android.comment.sendto_userid";
    public static String KEY_SENDER_USER_NAME = "com.logpie.android.comment.sender_username";
    public static String KEY_SEND_TO_USER_NAME = "com.logpie.android.comment.sendto_username";
    public static String KEY_CONTENT = "com.logpie.android.comment.content";
    public static String KEY_TIME = "com.logpie.android.comment.time";

    private static String TAG = Comment.class.getName();

    private String mSenderUserId;
    private String mSendToUserId;
    private String mSenderUserName;
    private String mSendToUserName;
    private String mContent;
    private String mTime;

    private Comment(String senderId, String sendToUserId, String senderUserName,
            String sendToUserName, String content, String time)
    {
        mSenderUserId = senderId;
        mSendToUserId = sendToUserId;
        mSenderUserName = senderUserName;
        mSendToUserName = sendToUserName;
        mContent = content;
        mTime = time;
    }

    /**
     * Build the comment from bundle.
     * 
     * @param commentBundle
     *            bundle must contain information about senderUserId,
     *            senderUserName, content, time;
     * @return
     */
    public static Comment initializeComment(Bundle commentBundle)
    {
        // check whether contains senderUserId, senderUserName, content, time.
        // These are mandatory.
        if (!commentBundle.containsKey(KEY_SENDER_USER_ID)
                || !commentBundle.containsKey(KEY_SENDER_USER_NAME)
                || !commentBundle.containsKey(KEY_CONTENT) || !commentBundle.containsKey(KEY_TIME))
        {
            LogpieLog.e(TAG,
                    "Building comment must contain senderUserId,senderUserName, content, time");
            return null;
        }

        String sender_user_id = commentBundle.getString(KEY_SENDER_USER_ID);
        String sendto_user_id = commentBundle.getString(KEY_SEND_TO_USER_ID);
        String sender_user_name = commentBundle.getString(KEY_SENDER_USER_NAME);
        String sendto_user_name = commentBundle.getString(KEY_SEND_TO_USER_ID);
        String content = commentBundle.getString(KEY_CONTENT);
        String time = commentBundle.getString(KEY_TIME);

        return new Comment(sender_user_id, sendto_user_id, sender_user_name, sendto_user_name,
                content, time);
    }

    /**
     * Build the comment from json.
     * 
     * @param commentjson
     *            json must contain information about senderUserId,
     *            senderUserName, content, time;
     * @return
     */
    public static Comment initializeComment(JSONObject commentJSON)
    {
        // check whether contains senderUserId, senderUserName, content, time.
        // These are mandatory.
        if (!commentJSON.has(KEY_SENDER_USER_ID) || !commentJSON.has(KEY_SENDER_USER_NAME)
                || !commentJSON.has(KEY_CONTENT) || !commentJSON.has(KEY_TIME))
        {
            LogpieLog.e(TAG,
                    "Building comment must contain senderUserId,senderUserName, content, time");
            return null;
        }

        String sender_user_id;
        try
        {
            sender_user_id = commentJSON.getString(KEY_SENDER_USER_ID);
            String sendto_user_id = commentJSON.getString(KEY_SEND_TO_USER_ID);
            String sender_user_name = commentJSON.getString(KEY_SENDER_USER_NAME);
            String sendto_user_name = commentJSON.getString(KEY_SEND_TO_USER_ID);
            String content = commentJSON.getString(KEY_CONTENT);
            String time = commentJSON.getString(KEY_TIME);
            return new Comment(sender_user_id, sendto_user_id, sender_user_name, sendto_user_name,
                    content, time);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException when try to retrieve the commentJSON", e);
            return null;
        }
    }

    /**
     * @return the senderUserId
     */
    public String getSenderUserId()
    {
        return mSenderUserId;
    }

    /**
     * @param senderUserId
     *            the senderUserId to set
     */
    public void setSenderUserId(final String senderUserId)
    {
        mSenderUserId = senderUserId;
    }

    /**
     * @return the sendToUserId
     */
    public String getSendToUserId()
    {
        return mSendToUserId;
    }

    /**
     * @param sendToUserId
     *            the sendToUserId to set
     */
    public void setSendToUserId(final String sendToUserId)
    {
        mSendToUserId = sendToUserId;
    }

    /**
     * @return the senderUserName
     */
    public String getSenderUserName()
    {
        return mSenderUserName;
    }

    /**
     * @param senderUserName
     *            the senderUserName to set
     */
    public void setSenderUserName(final String senderUserName)
    {
        mSenderUserName = senderUserName;
    }

    /**
     * @return the sendToUserName
     */
    public String getSendToUserName()
    {
        return mSendToUserName;
    }

    /**
     * @param sendToUserName
     *            the sendToUserName to set
     */
    public void setSendToUserName(final String sendToUserName)
    {
        mSendToUserName = sendToUserName;
    }

    /**
     * @return the content
     */
    public String getContent()
    {
        return mContent;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(final String content)
    {
        mContent = content;
    }

    /**
     * @return the time
     */
    public String getTime()
    {
        return mTime;
    }

    /**
     * @param time
     *            the time to set
     */
    public void setTime(final String time)
    {
        mTime = time;
    }

}
