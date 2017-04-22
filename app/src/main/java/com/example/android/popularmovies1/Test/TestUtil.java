package com.example.android.popularmovies1.Test;

/**
 * Created by Maryline on 3/30/2017.
 */

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.popularmovies1.data.MoviesContract;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.MovieslistEntry.COLUMN_TITLE, "John");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_RELEASE_DATE,"jwnfwo");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_USER_RATING,"jwefwfwefwo");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_SYNOPSIS,"jwwfwfwfw");
        list.add(cv);

        cv = new ContentValues();
        cv.put(MoviesContract.MovieslistEntry.COLUMN_TITLE, "Marta");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_RELEASE_DATE,"11111");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_USER_RATING,"2222");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_SYNOPSIS,"33333");
        list.add(cv);

        cv = new ContentValues();
        cv.put(MoviesContract.MovieslistEntry.COLUMN_TITLE, "Olivier");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_RELEASE_DATE,"44444");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_USER_RATING,"5555");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_SYNOPSIS,"6666");
        list.add(cv);

        cv = new ContentValues();
        cv.put(MoviesContract.MovieslistEntry.COLUMN_TITLE, "Chloe");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_RELEASE_DATE,"77777");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_USER_RATING,"888888888");
        cv.put(MoviesContract.MovieslistEntry.COLUMN_SYNOPSIS,"999999999");
        list.add(cv);


        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            //db.delete (MoviesContract.MovieslistEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(MoviesContract.MovieslistEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }
}
