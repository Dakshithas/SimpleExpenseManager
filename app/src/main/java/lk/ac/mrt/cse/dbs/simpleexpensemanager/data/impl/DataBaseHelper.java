package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int version = 1;
    public static final String dbname = "180626A";

    public DataBaseHelper(@Nullable Context context) {
        super(context, dbname, null, version);
    }
    //This will only called the first time a database accessed.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement="";
        db.execSQL(createTableStatement);
    }
    //this called when version number get changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
