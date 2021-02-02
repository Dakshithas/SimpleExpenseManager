package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO, Serializable {
    private DataBaseHelper db_helper;
    public PersistentAccountDAO(Context context){
        db_helper = new DataBaseHelper(context);
    }
    @Override
    public List<String> getAccountNumbersList() {
        List<String> account_numbers = new ArrayList<>();
        SQLiteDatabase db = db_helper.getReadableDatabase();
        String sql = String.format("SELECT %s FROM %s",DB.Account.ACCOUNT_NUMBER,DB.Account.TABLE_NAME);
        final Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                account_numbers.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return account_numbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> account_list = new ArrayList<>();
        SQLiteDatabase db = db_helper.getReadableDatabase();
        //get all data specifying each column to get column in order instead of using *.
        String sql = String.format("SELECT %s, %s, %s, %s FROM %s",DB.Account.ACCOUNT_NUMBER,DB.Account.BANK_NAME,DB.Account.HOLDER_NAME,DB.Account.BALANCE,DB.Account.TABLE_NAME);
        final Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                account_list.add(new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return account_list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = db_helper.getReadableDatabase();
        //get all data specifying each column to get column in order instead of using *.
        String sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s = ?",DB.Account.ACCOUNT_NUMBER,DB.Account.BANK_NAME,DB.Account.HOLDER_NAME,DB.Account.BALANCE,DB.Account.TABLE_NAME,DB.Account.ACCOUNT_NUMBER);
        final Cursor cursor = db.rawQuery(sql, new String[]{accountNo});
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            cursor.close();
            return account;
        }
        cursor.close();
        throw new InvalidAccountException(String.format("Account number %s is not valid",accountNo));
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = db_helper.getWritableDatabase();
        String sql = String.format("INSERT OR IGNORE INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                DB.Account.TABLE_NAME,
                DB.Account.ACCOUNT_NUMBER,
                DB.Account.BANK_NAME,
                DB.Account.HOLDER_NAME,
                DB.Account.BALANCE);
        db.execSQL(sql, new Object[]{
                account.getAccountNo(),
                account.getBankName(),
                account.getAccountHolderName(),
                account.getBalance()});
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        getAccount(accountNo);
        SQLiteDatabase db = db_helper.getWritableDatabase();
        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                DB.Account.TABLE_NAME,
                DB.Account.ACCOUNT_NUMBER);

        db.execSQL(sql, new Object[]{accountNo});

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        getAccount(accountNo);
        SQLiteDatabase db = db_helper.getWritableDatabase();
        String operation = null;
        switch (expenseType) {
            case EXPENSE:
                operation = "-";
                break;
            case INCOME:
                operation = "+";
                break;
        }
        String sql = String.format("UPDATE %s SET %s = %s %s ? WHERE %s = ?",
                DB.Account.TABLE_NAME,
                DB.Account.BALANCE,
                DB.Account.BALANCE,
                operation,
                DB.Account.ACCOUNT_NUMBER);

        db.execSQL(sql, new Object[]{amount, accountNo});
    }
}
