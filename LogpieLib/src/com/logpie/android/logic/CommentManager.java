package com.logpie.android.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.logpie.android.connection.GenericConnection;
import com.logpie.android.logic.AuthManager.AuthType;
import com.logpie.android.util.JSONHelper;
import com.logpie.android.util.LogpieDateTime;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.EndPoint.ServiceURL;
import com.logpie.commonlib.RequestKeys;
import com.logpie.commonlib.ResponseKeys;

public class CommentManager
{
    private static String TAG = CommentManager.class.getName();

    private static CommentManager sCommentManager;
    private final Context mContext;

    private CommentManager(Context context)
    {
        mContext = context;
    }

    public synchronized static CommentManager getInstance(Context context)
    {
        if (sCommentManager == null)
        {
            sCommentManager = new CommentManager(context);
        }
        return sCommentManager;
    }

    public List<String> getComments()
    {
        return null;
    }

    public ArrayList<Comment> loadCommentsForActivity(final String activity_id)
    {
        JSONObject postData = new JSONObject();
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.CommentService, AuthType.NormalAuth, mContext);
        try
        {
            postData.put(RequestKeys.KEY_REQUEST_SERVICE, RequestKeys.SERVICE_SHOW_COMMENTS);
            postData.put(RequestKeys.KEY_REQUEST_TYPE, RequestKeys.REQUEST_TYPE_QUERY);

            ArrayList<String> keyList = new ArrayList<String>();
            keyList.add(RequestKeys.KEY_NICKNAME);
            keyList.add(RequestKeys.KEY_COMMENT_CONTENT);
            JSONArray queryKey = JSONHelper.buildQueryKey(keyList);
            postData.put(RequestKeys.KEY_QUERY_KEY, queryKey);

            Map<String, Map<String, String>> constraints = new HashMap<String, Map<String, String>>();
            Map<String, String> map_activity = new HashMap<String, String>();
            map_activity.put(RequestKeys.KEY_EQUAL, activity_id);
            constraints.put(RequestKeys.KEY_SEND_TO_ACTIVITYID, map_activity);

            // TODO: this is a short-term hack for table-link query
            Map<String, String> tableLinkMap = new HashMap<String, String>();
            tableLinkMap.put("comment.user_id", "\"user\".uid");

            JSONArray constraintKeyValue = JSONHelper.buildConstraintKeyValue(constraints,
                    tableLinkMap);
            postData.put(RequestKeys.KEY_CONSTRAINT_KEYVALUE_PAIR, constraintKeyValue);
            connection.setRequestData(postData);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when get nearby activity list", e);
            return null;
        }
        try
        {
            Bundle resultBundle = connection.send(null).get();
            if (resultBundle == null)
            {
                return null;
            }

            String responseData = resultBundle.getString(GenericConnection.KEY_RESPONSE_DATA);
            if (responseData == null)
            {
                return null;
            }

            JSONObject resultJSON = new JSONObject(responseData);
            String result = resultJSON.getString(ResponseKeys.KEY_COMMENT_RESULT);

            if (TextUtils.equals(result, ResponseKeys.RESULT_SUCCESS))
            {
                JSONArray commentArray = resultJSON.getJSONArray(ResponseKeys.KEY_METADATA);
                if (commentArray != null && commentArray.length() != 0)
                {
                    ArrayList<Comment> commentArrayList = new ArrayList<Comment>();
                    int length = commentArray.length();
                    for (int i = 0; i < length; i++)
                    {
                        JSONObject singleComment = commentArray.getJSONObject(i);
                        String userName = singleComment.getString(ResponseKeys.KEY_NICKNAME);
                        String content = singleComment.getString(ResponseKeys.KEY_COMMENT_CONTENT);
                        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(content))
                        {
                            LogpieLog.e(TAG, "userName or content is null from the server");
                            continue;
                        }
                        commentArrayList.add(new Comment(userName, content));
                    }
                    return commentArrayList;
                }

                return null;
            }
            else
            {
                LogpieLog.d(TAG, "Add comment fail! fail reason:" + result);
                return null;
            }

        } catch (InterruptedException e)
        {
            LogpieLog.e(TAG, "InterruptedException happened when try to get add comment result", e);
        } catch (ExecutionException e)
        {
            LogpieLog.e(TAG, "ExecutionException happened when try to get add comment result", e);
        } catch (JSONException e)
        {
            LogpieLog
                    .e(TAG,
                            "JSONException happened when try to get add comment result. Mainly because the response from server is malformated",
                            e);
        } catch (Exception e)
        {
            LogpieLog.e(TAG, "Unknown Exception", e);
        }
        return null;
    }

    public boolean writeComment(final String uid, final String activity_id, final String content)
    {
        JSONObject postData = new JSONObject();
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.CommentService, AuthType.NormalAuth, mContext);

        // Get current Time
        LogpieDateTime currentTime = new LogpieDateTime();
        Map<String, String> insertMap = new HashMap<String, String>();
        insertMap.put(RequestKeys.KEY_SENDER_USER_ID, uid);
        insertMap.put(RequestKeys.KEY_SEND_TO_ACTIVITYID, activity_id);
        insertMap.put(RequestKeys.KEY_COMMENT_CONTENT, content);
        insertMap.put(RequestKeys.KEY_COMMENT_TIME, currentTime.getDateTimeString());

        try
        {
            postData.put(RequestKeys.KEY_REQUEST_SERVICE,
                    RequestKeys.SERVICE_INSERT_COMMENT_TO_ACTIVITY);
            postData.put(RequestKeys.KEY_REQUEST_TYPE, RequestKeys.REQUEST_TYPE_INSERT);
            JSONArray insertKeyValuePair = JSONHelper.buildInsertKeyValue(insertMap);
            postData.put(RequestKeys.KEY_INSERT_KEYVALUE_PAIR, insertKeyValuePair);

        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when create activity!");
            return false;
        }
        connection.setRequestData(postData);
        try
        {
            Bundle resultBundle = connection.send(null).get();
            if (resultBundle == null)
            {
                return false;
            }

            String responseData = resultBundle.getString(GenericConnection.KEY_RESPONSE_DATA);
            if (responseData == null)
            {
                return false;
            }

            JSONObject resultJSON = new JSONObject(responseData);
            String result = resultJSON.getString(ResponseKeys.KEY_COMMENT_RESULT);
            if (TextUtils.equals(result, ResponseKeys.RESULT_SUCCESS))
            {
                LogpieLog.d(TAG, "Add comment success");
                return true;
            }
            else
            {
                LogpieLog.d(TAG, "Add comment fail! fail reason:" + result);
                return false;
            }

        } catch (InterruptedException e)
        {
            LogpieLog.e(TAG, "InterruptedException happened when try to get add comment result", e);
        } catch (ExecutionException e)
        {
            LogpieLog.e(TAG, "ExecutionException happened when try to get add comment result", e);
        } catch (JSONException e)
        {
            LogpieLog
                    .e(TAG,
                            "JSONException happened when try to get add comment result. Mainly because the response from server is malformated",
                            e);
        } catch (Exception e)
        {
            LogpieLog.e(TAG, "Unknown Exception", e);
        }
        return false;
    }
}
