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

import com.example.android.movieadapter2.db.AppDatabase;
import com.example.android.movieadapter2.db.TaskDao;
import com.example.android.movieadapter2.db.TaskEntry;

import java.util.ArrayList;


/**
 * A {@link ContentProvider} based on a Room database.
 *
 * <p>Note that you don't need to implement a ContentProvider unless you want to expose the data
 * outside your process or your application already uses a ContentProvider.</p>
 */
public class SampleContentProvider extends ContentProvider {

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.example.android.movieadapter2.provider";

    /** The URI for the Menu table. */
    public static final Uri URI_MENU = Uri.parse(
            "content://" + AUTHORITY + "/" + TaskEntry.TABLE_NAME);

    /** The match code for some items in the Menu table. */
    private static final int CODE_MENU_DIR = 1;

    /** The match code for an item in the Menu table. */
    private static final int CODE_MENU_ITEM = 2;

    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TaskEntry.TABLE_NAME, CODE_MENU_DIR);
        MATCHER.addURI(AUTHORITY, TaskEntry.TABLE_NAME + "/*", CODE_MENU_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_MENU_DIR || code == CODE_MENU_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            TaskDao menu = AppDatabase.getInstance(context).taskDao();
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
        switch (MATCHER.match(uri)) {
            case CODE_MENU_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TaskEntry.TABLE_NAME;
            case CODE_MENU_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TaskEntry.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_MENU_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = AppDatabase.getInstance(context).taskDao()
                        .insert(TaskEntry.fromContentValues(values));
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
        switch (MATCHER.match(uri)) {
            case CODE_MENU_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MENU_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = AppDatabase.getInstance(context).taskDao()
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
        switch (MATCHER.match(uri)) {
            case CODE_MENU_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MENU_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final TaskEntry menu = TaskEntry.fromContentValues(values);
                menu.id = ContentUris.parseId(uri);
                final int count = AppDatabase.getInstance(context).taskDao()
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
        final AppDatabase database = AppDatabase.getInstance(context);
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
        switch (MATCHER.match(uri)) {
            case CODE_MENU_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final AppDatabase database = AppDatabase.getInstance(context);
                final TaskEntry[] menus = new TaskEntry[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    menus[i] = TaskEntry.fromContentValues(valuesArray[i]);
                }
                return database.taskDao().insertAll(menus).length;
            case CODE_MENU_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
