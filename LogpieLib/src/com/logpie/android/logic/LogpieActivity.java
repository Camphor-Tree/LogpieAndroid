package com.logpie.android.logic;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

// TODO: remove all the set method. All the member variable should be final.
/**
 * Make LogpieActivity parcelable, so that it can be pass in the intent.
 * 
 * @author yilei
 * 
 */
public class LogpieActivity implements Parcelable
{
    private String mActivityID;
    private String mUserID;
    private String mUserName;
    private String mDescription;
    private LogpieLocation mLocation;
    // TODO combine the time into one Period class
    private String mStartTime;
    private String mEndTime;
    private int mCountLike;
    private int mCountDislike;
    private List<Comment> mComments;

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
    public LogpieActivity(String userName, String description, LogpieLocation location,
            String startTime, String endTime)
    {
        mUserName = userName;
        mDescription = description;
        mLocation = location;
        // TODO combine the time into one Period clss
        this.mStartTime = startTime;
        this.mEndTime = endTime;
    }

    /**
     * This is used when get an activity from Server
     * 
     * @param id
     * @param userName
     * @param description
     * @param location
     * @param startTime
     * @param endTime
     */
    public LogpieActivity(String id, String userName, String description,
            LogpieLocation location, String startTime, String endTime, int countLike,
            int countDislike)
    {
        mActivityID = id;
        mUserName = userName;
        mDescription = description;
        mLocation = location;
        // TODO combine the time into one Period clss
        mStartTime = startTime;
        mEndTime = endTime;
        mCountLike = countLike;
        mCountDislike = countDislike;
    }

    public void setUserId(String userId)
    {
        mUserID = userId;
    }

    public String getUserID()
    {
        return mUserID;
    }

    public void setUserName(String userName)
    {
        mUserName = userName;
    }

    public String getUserName()
    {
        return mUserName;
    }

    public int getCountLike()
    {
        return mCountLike;
    }

    public void setCountLike(int countLike)
    {
        this.mCountLike = countLike;
    }

    public int getCountDislike()
    {
        return mCountDislike;
    }

    public void setCountDislike(int countDislike)
    {
        this.mCountDislike = countDislike;
    }

    public String getID()
    {
        return mActivityID;
    }

    public void setID(String iD)
    {
        mActivityID = iD;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }

    public LogpieLocation getLocation()
    {
        return mLocation;
    }

    public void setLocation(LogpieLocation location)
    {
        mLocation = location;
    }

    public String getStartTime()
    {
        return mStartTime;
    }

    public void setStartTime(String startTime)
    {
        this.mStartTime = startTime;
    }

    public String getEndTime()
    {
        return mEndTime;
    }

    public void setEndTime(String endTime)
    {
        this.mEndTime = endTime;
    }

    public List<Comment> getComments()
    {
        return mComments;
    }

    public void setComments(List<Comment> comments)
    {
        mComments = comments;
    }

    public LogpieActivity(String id, String description, LogpieLocation location,
            String startTime, String endTime, List<Comment> comments)
    {
        mUserID = id;
        mDescription = description;
        mLocation = location;
        // TODO combine the time into one Period clss
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        mComments = comments;
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
        dest.writeString(mStartTime);
        dest.writeString(mEndTime);
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
        mStartTime = in.readString();
        mEndTime = in.readString();
        mCountLike = in.readInt();
        mCountDislike = in.readInt();
        mLocation = in.readParcelable(getClass().getClassLoader());

    }

}
