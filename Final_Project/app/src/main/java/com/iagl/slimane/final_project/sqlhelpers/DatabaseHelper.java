package com.iagl.slimane.final_project.sqlhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iagl.slimane.final_project.R;
import com.iagl.slimane.final_project.datasources.Person;
import com.iagl.slimane.final_project.datasources.Profile;
import com.iagl.slimane.final_project.datasources.Transaction;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by slimane on 16/10/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "projet.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Person,Integer> personDao;
    private Dao<Transaction,Integer> transactionDao;
    private Dao<Profile,Integer> profileDao;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            //Log.d("Tables created in %s", DATABASE_NAME);
            TableUtils.createTable(connectionSource, Person.class);
            TableUtils.createTable(connectionSource, Profile.class);
            TableUtils.createTable(connectionSource, Transaction.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Person.class,true);
            TableUtils.dropTable(connectionSource, Profile.class,true);
            TableUtils.dropTable(connectionSource, Transaction.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Person, Integer> getPersonDao() throws SQLException {
        if (personDao == null) {
            personDao = getDao(Person.class);
        }
        return personDao;
    }

    public Dao<Profile, Integer> getProfileDao() throws SQLException {
        if (profileDao == null) {
            profileDao = getDao(Profile.class);
        }
        return profileDao;
    }

    public Dao<Transaction, Integer> getTransactionDao() throws SQLException {
        if (transactionDao == null) {
            transactionDao = getDao(Transaction.class);
        }
        return transactionDao;
    }
}
