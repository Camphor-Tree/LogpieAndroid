package com.logpie.android.util;

import android.os.Bundle;

public interface LogpieCallback
{
    public void onSuccess(Bundle result);

    public void onError(Bundle errorMessage);
}
