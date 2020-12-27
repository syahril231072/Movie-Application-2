/*
 * Created by Jihad044.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 25/10/18 9:45 AM.
 */

package com.example.android.movieadapter2.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class
StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}