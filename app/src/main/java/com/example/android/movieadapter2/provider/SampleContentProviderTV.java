/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.movieadapter2.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.movieadapter2.db.AppDatabaseTV;
import com.example.android.movieadapter2.db.TaskDaoTV;
import com.example.android.movieadapter2.db.TaskEntryTV;

import java.util.ArrayList;


/**
 * A {@link ContentProvider} based on a Room database.
 *
 * <p>Note that you don't need to implement a ContentProvider unless you want to expose the data
 * outside your process or your application already uses a ContentProvider.</p>
 */
public class SampleContentProviderTV extends ContentProvider {

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.example.android.movieadapter2.providertv";

    /** The URI for the Menu table. */
    public static final Uri URI_MENUTV = Uri.parse(
            "content://" + AUTHORITY + "/" + TaskEntryTV.TABLE_NAME);

    /** The match code for some items in the Menu table. */
    private static final int CODE_MENU_DIR = 1;

    /** The match code for an item in the Menu table. */
    private static final int CODE_MENU_ITEM = 2;

    /** The URI matcher. */
    private static final UriMatcher MATCHERTV = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHERTV.addURI(AUTHORITY, TaskEntryTV.TABLE_NAME, CODE_MENU_DIR);
        MATCHERTV.addURI(AUTHORITY, TaskEntryTV.TABLE_NAME + "/*", CODE_MENU_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHERTV.match(uri);
        if (code == CODE_MENU_DIR || code == CODE_MENU_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            TaskDaoTV menu = AppDatabaseTV.getInstance(context).taskDao();
            final Cursor cursor;
            if (code == CODE_MENU_DIR) {
                cursor = menu.selectAll();
            } else {
                cursor = menu.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHERTV.match(uri)) {
            case CODE_MENU_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TaskEntryTV.TABLE_NAME;
            case CODE_MENU_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TaskEntryTV.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHERTV.match(uri)) {
            case CODE_MENU_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = AppDatabaseTV.getInstance(context).taskDao()
                        .insert(TaskEntryTV.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_MENU_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (MATCHERTV.match(uri)) {
            case CODE_MENU_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MENU_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = AppDatabaseTV.getInstance(context).taskDao()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (MATCHERTV.match(uri)) {
            case CODE_MENU_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MENU_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final TaskEntryTV menu = TaskEntryTV.fromContentValues(values);
                menu.id = ContentUris.parseId(uri);
                final int count = AppDatabaseTV.getInstance(context).taskDao()
                        .update(menu);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final AppDatabaseTV database = AppDatabaseTV.getInstance(context);
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (MATCHERTV.match(uri)) {
            case CODE_MENU_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final AppDatabaseTV database = AppDatabaseTV.getInstance(context);
                final TaskEntryTV[] menus = new TaskEntryTV[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    menus[i] = TaskEntryTV.fromContentValues(valuesArray[i]);
                }
                return database.taskDao().insertAll(menus).length;
            case CODE_MENU_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
