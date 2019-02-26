package com.sangtaohay.memestudio.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sangtaohay.memestudio.contract.MemeContract;
import com.sangtaohay.memestudio.db.MemeDbHelper;
import com.sangtaohay.memestudio.dto.MemeDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class MemeDAO {
    public static ArrayList<MemeDTO> getListMemeDTO(MemeDbHelper mHelper, Boolean isAnimated) {
        ArrayList<MemeDTO> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String selection = MemeContract.MemeEntry.COL_STATUS + " = ?";
        List<String> selectionArgsList = new ArrayList<>();
        selectionArgsList.add("1");

        selection += " AND "+ MemeContract.MemeEntry.COL_TYPE + " = ?";
        selectionArgsList.add(isAnimated?"2":"1");

        String[] selectionArgs = new String[selectionArgsList.size()];
        selectionArgs = selectionArgsList.toArray(selectionArgs);

        Cursor cursor = db.query(MemeContract.MemeEntry.TABLE,
                new String[]{MemeContract.MemeEntry._ID,
                        MemeContract.MemeEntry.COL_TITLE,
                        MemeContract.MemeEntry.COL_TEXT_TOP,
                        MemeContract.MemeEntry.COL_TEXT_BOTTOM,
                        MemeContract.MemeEntry.COL_TEXT_BOTTOM_LEFT,
                        MemeContract.MemeEntry.COL_TEXT_BOTTOM_RIGHT,
                        MemeContract.MemeEntry.COL_IMAGE,
                        MemeContract.MemeEntry.COL_THUMB,
                        MemeContract.MemeEntry.COL_BACKGROUNDCOLOR,
                        MemeContract.MemeEntry.COL_CREATEDAT,
                        MemeContract.MemeEntry.COL_POSITION,
                        MemeContract.MemeEntry.COL_WIDTH,
                        MemeContract.MemeEntry.COL_HEIGHT},
                selection, selectionArgs, null, null, MemeContract.MemeEntry.COL_POSITION + " desc");
        while (cursor.moveToNext()) {
            int idx0 = cursor.getColumnIndex(MemeContract.MemeEntry._ID);
            int idx = cursor.getColumnIndex(MemeContract.MemeEntry.COL_TITLE);
            int idx2 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_CREATEDAT);
            int idx3 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_TEXT_TOP);
            int idx4 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_TEXT_BOTTOM);
            int idx10 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_TEXT_BOTTOM_LEFT);
            int idx11 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_TEXT_BOTTOM_RIGHT);
            int idx5 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_IMAGE);
            int idx12 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_THUMB);
            int idx6 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_BACKGROUNDCOLOR);
            int idx7 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_POSITION);
            int idx8 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_WIDTH);
            int idx9 = cursor.getColumnIndex(MemeContract.MemeEntry.COL_HEIGHT);
            MemeDTO MemeDTO = new MemeDTO();
            MemeDTO.id = cursor.getInt(idx0);
            MemeDTO.name = cursor.getString(idx);
            MemeDTO.createdAt = cursor.getInt(idx2);
            MemeDTO.textTop = cursor.getString(idx3);
            MemeDTO.textBottom = cursor.getString(idx4);
            MemeDTO.imageName = cursor.getString(idx5);
            MemeDTO.backgroundColor = cursor.getInt(idx6);
            MemeDTO.position = cursor.getInt(idx7);
            MemeDTO.imageWidth = cursor.getInt(idx8);
            MemeDTO.imageHeight = cursor.getInt(idx9);
            MemeDTO.textBottomLeft = cursor.getString(idx10);
            MemeDTO.textBottomRight = cursor.getString(idx11);
            MemeDTO.thumb = cursor.getString(idx12);
            taskList.add(MemeDTO);
        }
        return taskList;
    }

    public static MemeDTO getItem(MemeDbHelper mHelper, String _id) {
        for (MemeDTO place : getListMemeDTO(mHelper,null)) {
            if (place.id.equals(_id)) {
                return place;
            }
        }
        return null;
    }

    public static void deleteItemForever(MemeDbHelper mHelper, int position) {
        ArrayList<MemeDTO> listMemeDTO = getListMemeDTO(mHelper,null);
        MemeDTO MemeDTO = listMemeDTO.get(position);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(MemeContract.MemeEntry.TABLE,
                MemeContract.MemeEntry._ID + " = ?",
                new String[]{String.valueOf(MemeDTO.id)});
        db.close();
    }

    public static void removeItem(MemeDbHelper mHelper, int position) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Integer dbId = MemeDAO.getListMemeDTO(mHelper,null).get(position).id;
        String strFilter = MemeContract.MemeEntry._ID + "=" + dbId;
        ContentValues args = new ContentValues();
        args.put(MemeContract.MemeEntry.COL_STATUS, 0);
        db.update(MemeContract.MemeEntry.TABLE, args, strFilter, null);
        db.close();
    }
    public static void updatePosition(MemeDbHelper mHelper, long dbId) {
        setPositionById(mHelper,dbId,dbId);
    }
    public static void swapPosition(MemeDbHelper mHelper, long sourceDbId, long targetDbId) {
        long sourcePosition = getItem(mHelper,String.valueOf(sourceDbId)).position;
        long destPosition= getItem(mHelper,String.valueOf(targetDbId)).position;
        setPositionById(mHelper,sourcePosition,targetDbId);
        setPositionById(mHelper,destPosition,sourceDbId);
    }
    public static void setPositionById(MemeDbHelper mHelper, long position, long id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String strFilter = MemeContract.MemeEntry._ID + "=" + id;
        ContentValues args = new ContentValues();
        args.put(MemeContract.MemeEntry.COL_POSITION, position);
        db.update(MemeContract.MemeEntry.TABLE, args, strFilter, null);
        db.close();
    }

    public static void restoreItem(MemeDbHelper mHelper, String dbId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String strFilter = MemeContract.MemeEntry._ID + "=" + dbId;
        ContentValues args = new ContentValues();
        args.put(MemeContract.MemeEntry.COL_STATUS, 1);
        db.update(MemeContract.MemeEntry.TABLE, args, strFilter, null);
        db.close();
    }
    public static long addItem(MemeDbHelper mHelper, String top, String bottom, int backgroundColor) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MemeContract.MemeEntry.COL_TITLE, top);
        values.put(MemeContract.MemeEntry.COL_TEXT_TOP, top);
        values.put(MemeContract.MemeEntry.COL_TEXT_BOTTOM, bottom);
        values.put(MemeContract.MemeEntry.COL_BACKGROUNDCOLOR, backgroundColor);
        values.put(MemeContract.MemeEntry.COL_STATUS, 1);
        values.put(MemeContract.MemeEntry.COL_CREATEDAT, (int) System.currentTimeMillis() / 1000L);
        long id = db.insertWithOnConflict(MemeContract.MemeEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }
    public static long updateItem(MemeDbHelper mHelper, Integer memeId, String top, String bottom, String bottomLeft, String bottomRight, int backgroundColor) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MemeContract.MemeEntry.COL_TITLE, top);
        values.put(MemeContract.MemeEntry.COL_TEXT_TOP, top);
        values.put(MemeContract.MemeEntry.COL_TEXT_BOTTOM, bottom);
        values.put(MemeContract.MemeEntry.COL_TEXT_BOTTOM_LEFT, bottomLeft);
        values.put(MemeContract.MemeEntry.COL_TEXT_BOTTOM_RIGHT, bottomRight);
        values.put(MemeContract.MemeEntry.COL_STATUS, 1);
        values.put(MemeContract.MemeEntry.COL_CREATEDAT, (int) System.currentTimeMillis() / 1000L);
        String strFilter = MemeContract.MemeEntry._ID + "= ?";

        long id = db.update(MemeContract.MemeEntry.TABLE, values, strFilter, new String[]{String.valueOf(memeId)});
        db.close();
        return id;
    }
}
