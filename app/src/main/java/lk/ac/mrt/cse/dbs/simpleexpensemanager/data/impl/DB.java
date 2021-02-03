package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.provider.BaseColumns;

import java.io.Serializable;

final class DB{
    public static final String dbname = "180626A";
    static class Account implements BaseColumns{
        static final String TABLE_NAME="account";
        static final String BANK_NAME="bank_name";
        static final String HOLDER_NAME="holder_name";
        static final String ACCOUNT_NUMBER="account_number";
        static final String BALANCE="balance";
    }
    static class Transaction implements BaseColumns{
        static final String TABLE_NAME="transactions";
        static final String ACCOUNT_NUMBER="account_number";
        static final String DATE="date";
        static final String TYPE="type";
        static final String AMOUNT="amount";
    }
}
