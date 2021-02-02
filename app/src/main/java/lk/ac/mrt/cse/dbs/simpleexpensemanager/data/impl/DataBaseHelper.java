package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.io.Serializable;

public class DataBaseHelper extends SQLiteOpenHelper implements Serializable {
    public static final int version = 1;

    private static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    private static final String ID = "ID";
    private static final String CREATE_ACCOUNT="CREATE TABLE " +
            DB.Account.TABLE_NAME + " (" +
            DB.Account.ACCOUNT_NUMBER + " TEXT PRIMARY KEY," +
            DB.Account.BANK_NAME + " TEXT,"+
            DB.Account.HOLDER_NAME + " TEXT,"+
            DB.Account.BALANCE + " REAL)";
    private static final String CREATE_TRANSACTION= "CREATE TABLE " +
            DB.Transaction.TABLE_NAME + " (" +
            DB.Transaction.DATE + " TEXT PRIMARY KEY," +
            DB.Transaction.AMOUNT + " REAL,"+
            DB.Transaction.TYPE + " TEXT,"+
            DB.Transaction.ACCOUNT_NUMBER + " TEXT,"+
            "FOREIGN KEY (" + DB.Account.ACCOUNT_NUMBER + ") REFERENCES "+ DB.Account.TABLE_NAME + "(" + DB.Account.ACCOUNT_NUMBER +
            "))";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DB.dbname, null, version);
    }

    //This will only called the first time a database accessed.
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ACCOUNT);
        database.execSQL(CREATE_TRANSACTION);
    }

    //this called when version number get changed.
    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + DB.Transaction.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + DB.Account.TABLE_NAME);
        onCreate(database);
    }
}
