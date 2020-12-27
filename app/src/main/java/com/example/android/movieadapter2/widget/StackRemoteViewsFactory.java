/*
 * Created by Jihad044.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 25/10/18 9:55 AM.
 */

package com.example.android.movieadapter2.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.android.movieadapter2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.android.movieadapter2.db.TaskEntry.COLUMN_MOVIE;
import static com.example.android.movieadapter2.provider.SampleContentProvider.URI_MENU;
import static com.example.android.movieadapter2.provider.SampleContentProviderTV.URI_MENUTV;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    private Cursor list;

    public void setData(String data) {
        this.data = data;
    }

    private String data;

    private final List<Bitmap> mWidgetItems = new ArrayList<>();

    StackRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }
    @Override
    public void onDataSetChanged() {
        if (list != null){
            list.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        // querying ke database
        list = mContext.getContentResolver().query(URI_MENU, null, null, null, null);

        for(list.moveToFirst(); !list.isAfterLast(); list.moveToNext()) {
            setData(list.getString(
                    list.getColumnIndexOrThrow(COLUMN_MOVIE)));


            Bitmap bitmap = null;

            try {
                bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load(data)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            mWidgetItems.add(bitmap);
        }

        list = mContext.getContentResolver().query(URI_MENUTV, null, null, null, null);

        for(list.moveToFirst(); !list.isAfterLast(); list.moveToNext()) {
            setData(list.getString(
                    list.getColumnIndexOrThrow(COLUMN_MOVIE)));


            Bitmap bitmap = null;

            try {
                bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load(data)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            mWidgetItems.add(bitmap);
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }
    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.favourite_widget_item);



        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(i));
        Bundle extras = new Bundle();
        extras.putInt(FavouriteWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    @Override
    public void onDestroy() {

    }


}
