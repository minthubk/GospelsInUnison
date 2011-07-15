package net.terang.dunia.gospels.in.unison.db;

import java.io.*;
import java.sql.*;

import net.terang.dunia.gospels.in.unison.model.*;

import android.content.*;
import android.database.sqlite.*;
import android.util.*;

import com.j256.ormlite.android.apptools.*;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.support.*;
import com.j256.ormlite.table.*;

public class DatabaseHelper
    extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "injil-tuhan-yesus.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Article, String> articleDao = null;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DatabaseInitializer initializer = new DatabaseInitializer(context);
        try {
            initializer.createDatabase();
            initializer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        Log.i(DatabaseHelper.class.getName(), "onCreate");
        // TableUtils.createTable(connectionSource, Article.class);
    }

    @Override
    public void onUpgrade(
        SQLiteDatabase db,
        ConnectionSource connectionSource,
        int oldVersion,
        int newVersion)
    {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Article.class, true);
            onCreate(db);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Article, String> getArticleDao()
        throws SQLException
    {
        if (articleDao == null) {
            articleDao = DaoManager.createDao(getConnectionSource(),
                Article.class);
        }
        return articleDao;
    }

    @Override
    public void close()
    {
        super.close();
        articleDao = null;
    }
}
