package com.logpie.android.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.logpie.android.connection.GenericConnection;
import com.logpie.android.exception.InvalidParametersException;
import com.logpie.android.requests.UploadPhotoRequest;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.EndPoint.ServiceURL;

public class LogpiePhotoHelper
{
    private static final String TAG = LogpiePhotoHelper.class.getName();
    private static final String SERVICE_NAME = "PhotoService";
    private static final String SERVICE_NAME_EC2 = "PhotoServiceEC2";
    private static final int COMPRESS_RATE = 70;

    public void uploadPhoto(UploadPhotoRequest request) throws InvalidParametersException
    {
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.ActivityService, request.getAuthType(),
                request.getContext());
        connection.setRetriable(true);

        Bitmap bitmapImage = null;
        if (request.getBitmap() == null)
        {
            if (request.getFile() != null)
            {
                bitmapImage = BitmapFactory.decodeFile(request.getFile().getAbsolutePath());
            }
            else
            {
                String errorReason = "File and Bitmap cannot be both null in the requet for user "
                        + request.getUserId();
                LogpieLog.e(TAG, errorReason);
                throw new InvalidParametersException(errorReason);
            }
        }
        else
        {
            bitmapImage = request.getBitmap();
        }

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, COMPRESS_RATE, bao);
        byte[] ba = bao.toByteArray();

        String encodedImage = Base64.encodeToString(ba, Base64.DEFAULT);
        JSONObject connectionData = new JSONObject();

        try
        {
            connectionData.put("UserId", request.getUserId());
            connectionData.put("FileName", request.getFileName());
            connectionData.put("File", encodedImage);
        } catch (JSONException e)
        {
            String errorReason = "Failed to create json request on path: for User ID : "
                    + request.getUserId() + " Error message: " + e.getMessage();
            LogpieLog.e(TAG, errorReason);
        }

        connection.setRequestData(connectionData);

        if (request.isSyncCall())
        {
            connection.send(null);
        }
        else
        {
            connection.send(request.getLogpieCallback());
        }
    }

    // TODO move this to use Android API to save file
    public void saveImage(Bitmap bmp, String pictureName, String savePath)
    {
        File dirFile = new File(savePath);
        if (!dirFile.exists())
        {
            dirFile.mkdirs();
        }

        File file = new File(savePath + pictureName + ".jpg");

        if (file.exists())
        {
            file.delete();
        }

        FileOutputStream fOut = null;
        try
        {
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e)
        {
            String errorReason = "Failed to get file when saving fileon path: " + savePath
                    + " Error message: " + e.getMessage();
            LogpieLog.e(TAG, errorReason);
        } catch (IOException e)
        {
            String errorReason = "Failed to save file on path: " + savePath + " Error message: "
                    + e.getMessage();
            LogpieLog.e(TAG, errorReason);
        }
    }

    private static int safeLongToInt(long l)
    {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE)
        {
            throw new IllegalArgumentException(l
                    + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}
