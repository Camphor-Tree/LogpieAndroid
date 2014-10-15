package com.logpie.android.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.logpie.android.util.LogpieDateTime;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.RequestKeys;
import com.logpie.commonlib.ResponseKeys;

// TODO: remove all the set method. All the member variable should be final.
/**
 * Make LogpieActivity parcelable, so that it can be pass in the intent.
 * 
 * @author yilei
 * 
 */
public class LogpieActivity implements Parcelable
{
    private static final String TAG = LogpieActivity.class.getName();
    public static final String DEFAULT_AVATAR = "";

    private String mActivityID;
    private String mUserAvatar;
    private String mUserID;
    private String mUserName;
    private String mDescription;
    private String mCategoryId;
    private String mCategoryString;
    private String mSubCategoryId;
    private String mSubCategoryString;
    private LogpieLocation mLocation;
    private LogpieDateTime mCreateTime;
    private LogpieDateTime mStartTime;
    private LogpieDateTime mEndTime;
    private int mCountLike;
    private int mCountDislike;
    private List<Comment> mComments;

    /**
     * This is used to create a new activity
     */
    public LogpieActivity()
    {
        mStartTime = new LogpieDateTime();
        mEndTime = new LogpieDateTime();
    }

    /**
     * This is used when create an activity
     * 
     * @param id
     * @param userName
     * @param description
     * @param location
     * @param startTime
     * @param endTime
     */
    public LogpieActivity(String uid, String userName, String description, LogpieLocation location,
            LogpieDateTime startTime, LogpieDateTime endTime, LogpieDateTime createTime)
    {
        mUserID = uid;
        mUserName = userName;
        mDescription = description;
        mLocation = location;
        mStartTime = startTime;
        mEndTime = endTime;
        mCreateTime = createTime;

        // set default value
        mUserAvatar = DEFAULT_AVATAR;
        mCreateTime = createTime;
        mCountLike = 0;
        mCountDislike = 0;
        mComments = new ArrayList<Comment>();
    }

    public LogpieActivity(String aid, String uid, String userName, String userAvatar,
            String description, LogpieLocation location, LogpieDateTime startTime,
            LogpieDateTime endTime, LogpieDateTime createTime, int countLike, int countDislike,
            List<Comment> comments)
    {
        mActivityID = aid;
        mUserID = uid;
        mUserName = userName;
        mUserAvatar = userAvatar;
        mDescription = description;
        mLocation = location;
        mStartTime = startTime;
        mEndTime = endTime;
        mCreateTime = createTime;
        mCountLike = countLike;
        mCountDislike = countDislike;
        mComments = new ArrayList<Comment>(comments);
    }

    public static LogpieActivity ActivityJSONHelper(JSONObject data)
    {
        if (data.isNull(ResponseKeys.KEY_AID) || data.isNull(ResponseKeys.KEY_UID)
                || data.isNull(ResponseKeys.KEY_NICKNAME)
                || data.isNull(ResponseKeys.KEY_CREATE_TIME)
                || data.isNull(ResponseKeys.KEY_DESCRIPTION)
                || data.isNull(ResponseKeys.KEY_LOCATION)
                || data.isNull(ResponseKeys.KEY_START_TIME)
                || data.isNull(ResponseKeys.KEY_END_TIME) || data.isNull(ResponseKeys.KEY_CITY))
        {
            LogpieLog.e(TAG, "Missing the required key for create an activity.");
            return null;
        }

        try
        {
            // Required parameters
            String aid = data.getString(ResponseKeys.KEY_AID);
            String uid = data.getString(ResponseKeys.KEY_UID);
            String userName = data.getString(ResponseKeys.KEY_NICKNAME);
            String description = data.getString(ResponseKeys.KEY_DESCRIPTION);
            LogpieLocation location = new LogpieLocation(null, null,
                    data.getString(ResponseKeys.KEY_LOCATION),
                    data.getString(ResponseKeys.KEY_CITY));
            LogpieDateTime startTime = getFormatDate(data.getString(ResponseKeys.KEY_START_TIME));
            LogpieDateTime endTime = getFormatDate(data.getString(ResponseKeys.KEY_END_TIME));
            LogpieDateTime createTime = getFormatDate(data.getString(ResponseKeys.KEY_CREATE_TIME));

            // Optional parameters
            String userAvatar = data.has(ResponseKeys.KEY_AID) ? data
                    .getString(ResponseKeys.KEY_AID) : DEFAULT_AVATAR;
            int countLike = data.has(ResponseKeys.KEY_COUNT_LIKE) ? Integer.valueOf(data
                    .getString(ResponseKeys.KEY_COUNT_LIKE)) : 0;
            int countDislike = data.has(ResponseKeys.KEY_COUNT_DISLIKE) ? Integer.valueOf(data
                    .getString(ResponseKeys.KEY_COUNT_DISLIKE)) : 0;
            List<Comment> comments = new ArrayList<Comment>();
            if (data.has(ResponseKeys.KEY_COMMENT))
            {
                // TODO: build comments
            }

            LogpieActivity activity = new LogpieActivity(aid, uid, userName, userAvatar,
                    description, location, startTime, endTime, createTime, countLike, countDislike,
                    comments);

            return activity;
        } catch (ParseException e)
        {
            LogpieLog.e(TAG, "ParseException happened when parse JSON data to an activity", e);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when parse JSON data to an activity", e);
        } catch (NumberFormatException e)
        {
            LogpieLog.e(TAG, "NumberFormatException happened when parse JSON data to an activity",
                    e);
        }
        return null;
    }

    /**
     * set the current time to the create time. This should be called when the
     * user click create activity.
     */
    public void setCreateTime()
    {
        mCreateTime = new LogpieDateTime();
    }

    public String getActivityID()
    {
        return mActivityID;
    }

    public void setActivityID(String mActivityID)
    {
        this.mActivityID = mActivityID;
    }

    public String getUserAvatar()
    {
        return mUserAvatar;
    }

    public void setUserAvatar(String mUserAvatar)
    {
        this.mUserAvatar = mUserAvatar;
    }

    public String getUserID()
    {
        return mUserID;
    }

    public void setUserID(String mUserID)
    {
        this.mUserID = mUserID;
    }

    public String getUserName()
    {
        return mUserName;
    }

    public void setUserName(String mUserName)
    {
        this.mUserName = mUserName;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String mDescription)
    {
        this.mDescription = mDescription;
    }

    public LogpieLocation getLocation()
    {
        return mLocation;
    }

    public void setLocation(LogpieLocation mLocation)
    {
        this.mLocation = mLocation;
    }

    public void setAddress(final String address)
    {
        if (mLocation == null)
        {
            mLocation = new LogpieLocation();
        }
        this.mLocation.setAddress(address);

    }

    public void setCity(final String city)
    {
        if (mLocation == null)
        {
            mLocation = new LogpieLocation(city);
            return;
        }
        this.mLocation.setCity(city);
    }

    public LogpieDateTime getCreateTime()
    {
        return mCreateTime;
    }

    public void setCreateTime(LogpieDateTime createTime)
    {
        this.mCreateTime = createTime;
    }

    public LogpieDateTime getStartTime()
    {
        return mStartTime;
    }

    public void setStartTime(LogpieDateTime mStartTime)
    {
        this.mStartTime = mStartTime;
    }

    public LogpieDateTime getEndTime()
    {
        return mEndTime;
    }

    public String getStartDateFormatString()
    {
        return mStartTime.getDateString();
    }

    public void setEndTime(LogpieDateTime mEndTime)
    {
        this.mEndTime = mEndTime;
    }

    public static LogpieDateTime getFormatDate(String s) throws ParseException
    {
        return new LogpieDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(s));
    }

    public int getCountLike()
    {
        return mCountLike;
    }

    public void setCountLike(int mCountLike)
    {
        this.mCountLike = mCountLike;
    }

    public int getCountDislike()
    {
        return mCountDislike;
    }

    public void setCountDislike(int mCountDislike)
    {
        this.mCountDislike = mCountDislike;
    }

    public List<Comment> getComments()
    {
        return mComments;
    }

    public void setComments(List<Comment> mComments)
    {
        this.mComments = mComments;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Parcelable.Creator<LogpieActivity> CREATOR = new Parcelable.Creator<LogpieActivity>()
    {
        public LogpieActivity createFromParcel(Parcel in)
        {
            return new LogpieActivity(in);
        }

        public LogpieActivity[] newArray(int size)
        {
            return new LogpieActivity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mActivityID);
        dest.writeString(mUserID);
        dest.writeString(mUserName);
        dest.writeString(mDescription);
        dest.writeParcelable(mStartTime, flags);
        dest.writeParcelable(mEndTime, flags);
        dest.writeParcelable(mCreateTime, flags);
        dest.writeInt(mCountLike);
        dest.writeInt(mCountDislike);
        dest.writeParcelable(mLocation, flags);

    }

    private LogpieActivity(Parcel in)
    {
        mActivityID = in.readString();
        mUserID = in.readString();
        mUserName = in.readString();
        mDescription = in.readString();
        mStartTime = in.readParcelable(getClass().getClassLoader());
        mEndTime = in.readParcelable(getClass().getClassLoader());
        mCreateTime = in.readParcelable(getClass().getClassLoader());
        mCountLike = in.readInt();
        mCountDislike = in.readInt();
        mLocation = in.readParcelable(getClass().getClassLoader());
    }

    public ArrayList<String> getCreateAcitivtyValues()
    {
        ArrayList<String> createActivityValues = new ArrayList<String>();
        createActivityValues.add(mUserID);
        createActivityValues.add(mUserName);
        createActivityValues.add(mDescription);
        createActivityValues.add(mLocation.getAddress());
        if (mLocation.getLatitude() != null && mLocation.getLongitude() != null)
        {
            createActivityValues.add(mLocation.getLatitude().toString());
            createActivityValues.add(mLocation.getLongitude().toString());
        }
        createActivityValues.add(mStartTime.getDateTimeString());
        createActivityValues.add(mEndTime.getDateTimeString());
        createActivityValues.add(mLocation.getCity());
        createActivityValues.add(mCategoryId);
        createActivityValues.add(mSubCategoryId);
        return createActivityValues;
    }

    public ArrayList<String> getCreateAcitivtyKeys()
    {
        ArrayList<String> createActivityKeys = new ArrayList<String>();
        createActivityKeys.add(RequestKeys.KEY_UID);
        createActivityKeys.add(RequestKeys.KEY_NICKNAME);
        createActivityKeys.add(RequestKeys.KEY_DESCRIPTION);
        createActivityKeys.add(RequestKeys.KEY_LOCATION);
        if (mLocation.getLatitude() != null && mLocation.getLongitude() != null)
        {
            createActivityKeys.add(RequestKeys.KEY_LATITUDE);
            createActivityKeys.add(RequestKeys.KEY_LONGITUDE);
        }
        createActivityKeys.add(RequestKeys.KEY_START_TIME);
        createActivityKeys.add(RequestKeys.KEY_END_TIME);
        // we don't pass the create time to server, server will just use the
        // current time.
        createActivityKeys.add(RequestKeys.KEY_CITY);
        createActivityKeys.add(RequestKeys.KEY_CATEGORY);
        createActivityKeys.add(RequestKeys.KEY_SUBCATEGORY);
        return createActivityKeys;
    }

    public String getCategoryId()
    {
        return mCategoryId;
    }

    public void setCategoryId(String categoryId)
    {
        mCategoryId = categoryId;
    }

    public String getCategoryString()
    {
        return mCategoryString;
    }

    public void setCategoryString(String categoryString)
    {
        mCategoryString = categoryString;
    }

    public String getSubCategoryId()
    {
        return mSubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId)
    {
        mSubCategoryId = subCategoryId;
    }

    public String getSubCategoryString()
    {
        return mSubCategoryString;
    }

    public void setSubCategoryString(String subCategoryString)
    {
        mSubCategoryString = subCategoryString;
    }

}
