package net.terang.dunia.gospels.in.unison.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.*;

public class DatabaseInitializer
    extends SQLiteOpenHelper
{
    private SQLiteDatabase database;
    private final Context context;
    private final String DB_PATH, DB_NAME;

    public DatabaseInitializer(Context context)
    {
        super(context, context.getResources().getString(
            context.getResources().getIdentifier("database_name", "string",
                context.getPackageName())), null, 1/* database version */);
        this.context = context;

        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        DB_NAME = context.getResources().getString(
            context.getResources().getIdentifier("database_name", "string",
                context.getPackageName()));
    }

    public void createDatabase()
        throws IOException
    {
        boolean dbExist = checkDatabase();

        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    private boolean checkDatabase()
    {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
                SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    private void copyDatabase()
        throws IOException
    {
        InputStream myInput = context.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public synchronized void close()
    {
        if (database != null) database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(DatabaseInitializer.class.getName(), "onCreate(" + db.toString()
                        + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i(DatabaseInitializer.class.getName(), "onUpgrade(" + db.toString()
                        + "," + oldVersion + "=>" + newVersion + ")");
    }
}
