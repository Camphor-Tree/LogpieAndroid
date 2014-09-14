package com.logpie.android.logic;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

//TODO: remove all the set method. All the member variable should be final.
/**
 * Make LogpieActivity parcelable, so that it can be pass in the intent.
 * 
 * @author yilei
 * 
 */
public class LogpieActivity implements Parcelable
{
    private String mID;
    private String mDescription;
    private LogpieLocation mLocation;
    // TODO combine the time into one Period class
    private String mStartTime;
    private String mEndTime;
    private int mCountLike;
    private int mCountDislike;
    private List<Comment> mComments;

    public int getmCountLike()
    {
        return mCountLike;
    }

    public void setmCountLike(int countLike)
    {
        this.mCountLike = countLike;
    }

    public int getmCountDislike()
    {
        return mCountDislike;
    }

    public void setmCountDislike(int countDislike)
    {
        this.mCountDislike = countDislike;
    }

    public String getID()
    {
        return mID;
    }

    public void setID(String iD)
    {
        mID = iD;
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

    public LogpieActivity(String id, String description, LogpieLocation location, String startTime,
            String endTime, List<Comment> comments)
    {
        mID = id;
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
        dest.writeString(mID);
        dest.writeString(mDescription);
        dest.writeString(mStartTime);
        dest.writeString(mEndTime);
        dest.writeInt(mCountLike);
        dest.writeParcelable(mLocation, flags);

    }

    private LogpieActivity(Parcel in)
    {
        mID = in.readString();
        mDescription = in.readString();
        mStartTime = in.readString();
        mEndTime = in.readString();
        mCountLike = in.readInt();
        mLocation = in.readParcelable(getClass().getClassLoader());

    }

}
