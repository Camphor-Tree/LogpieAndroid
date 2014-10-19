package com.logpie.android.requests;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

import com.logpie.android.logic.AuthManager.AuthType;
import com.logpie.android.util.LogpieCallback;

public class UploadPhotoRequest
{
    private File file;
    private Bitmap bitMap;
    private String fileName;
    private String userId;
    private AuthType authType;
    private Context context;
    private boolean isSyncCall;
    private LogpieCallback callback;

    private UploadPhotoRequest(Builder builder)
    {
        this.bitMap = builder.bitMap;
        this.fileName = builder.fileName;
        this.userId = builder.userId;
        this.authType = builder.authType;
        this.context = builder.context;
        this.isSyncCall = builder.isSyncCall;
        this.callback = builder.callback;
        this.file = builder.file;
    }

    public static final class Builder
    {
        private File file;
        private Bitmap bitMap;
        private String fileName;
        private String userId;
        private AuthType authType;
        private Context context;
        private boolean isSyncCall;
        private LogpieCallback callback;

        public Builder()
        {
        }

        public Builder withFile(File file)
        {
            this.file = file;
            return this;
        }

        public Builder withBitmap(Bitmap bitMap)
        {
            this.bitMap = bitMap;
            return this;
        }

        public Builder withFileName(String fileName)
        {
            this.fileName = fileName;
            return this;
        }

        public Builder withUserId(String userId)
        {
            this.userId = userId;
            return this;
        }

        public Builder withAuthType(AuthType authType)
        {
            this.authType = authType;
            return this;
        }

        public Builder withContext(Context context)
        {
            this.context = context;
            return this;
        }

        public Builder isSyncCall(boolean isSyncCall)
        {
            this.isSyncCall = isSyncCall;
            return this;
        }

        public Builder getLogpieCallback(LogpieCallback callback)
        {
            this.callback = callback;
            return this;
        }

        public UploadPhotoRequest build()
        {
            return new UploadPhotoRequest(this);
        }
    }

    public File getFile()
    {
        return file;
    }

    public Bitmap getBitmap()
    {
        return bitMap;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getUserId()
    {
        return userId;
    }

    public AuthType getAuthType()
    {
        return authType;
    }

    public Context getContext()
    {
        return context;
    }

    public boolean isSyncCall()
    {
        return isSyncCall;
    }

    public LogpieCallback getLogpieCallback()
    {
        return callback;
    }
}
