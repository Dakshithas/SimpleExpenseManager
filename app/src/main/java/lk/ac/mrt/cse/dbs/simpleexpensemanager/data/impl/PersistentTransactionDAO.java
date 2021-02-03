package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO{
    private DataBaseHelper db_helper;
    public PersistentTransactionDAO(Context context){
        db_helper=new DataBaseHelper(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        String sql_transaction = String.format("INSERT OR IGNORE INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                DB.Transaction.TABLE_NAME,
                DB.Transaction.DATE,
                DB.Transaction.ACCOUNT_NUMBER,
                DB.Transaction.TYPE,
                DB.Transaction.AMOUNT);

        db.execSQL(sql_transaction, new Object[]{
                date.getTime(),
                accountNo,
                expenseType,
                amount});
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = db_helper.getWritableDatabase();
        String sql_transaction = String.format("SELECT %s, %s, %s, %s FROM %s)",
                DB.Transaction.DATE,
                DB.Transaction.ACCOUNT_NUMBER,
                DB.Transaction.TYPE,
                DB.Transaction.AMOUNT,
                DB.Transaction.TABLE_NAME);
        final Cursor cursor = db.rawQuery(sql_transaction, null);
        if (cursor.moveToFirst()) {
            do {
                transactions.add(new Transaction(
                        new Date(cursor.getLong(0)),
                        cursor.getString(1),
                        Enum.valueOf(ExpenseType.class, cursor.getString(2)),
                        cursor.getDouble(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> results = new ArrayList<>();
        SQLiteDatabase db = db_helper.getWritableDatabase();
        String sql = String.format("SELECT %s, %s, %s, %s FROM %s LIMIT %s",
                DB.Transaction.DATE,
                DB.Transaction.ACCOUNT_NUMBER,
                DB.Transaction.TYPE,
                DB.Transaction.AMOUNT,
                DB.Transaction.TABLE_NAME,
                limit);
        final Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                results.add(new Transaction(
                        new Date(cursor.getLong(0)),
                        cursor.getString(1),
                        Enum.valueOf(ExpenseType.class, cursor.getString(2)),
                        cursor.getDouble(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return results;
    }
}
