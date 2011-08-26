package net.terang.dunia.gospels.in.unison.db;

import java.io.*;

import android.content.*;
import android.database.sqlite.*;
import android.util.*;

public class DatabaseInitializer
{
    private static final String TAG_NAME = DatabaseInitializer.class
                    .getSimpleName();
    private SQLiteDatabase database;
    private final Context context;
    private final String DB_PATH, DB_NAME;

    public DatabaseInitializer(Context context)
    {
        Log.d(TAG_NAME, "::init()");
        this.context = context;

        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        DB_NAME = context.getResources().getString(
            context.getResources().getIdentifier("database_name", "string",
                context.getPackageName()));
    }

    public void createDatabase()
        throws IOException
    {
        Log.d(TAG_NAME, "createDatabase()");
        // check if db exists - isDatabaseExists()
        try {
            Log.d(TAG_NAME, "Updating database '" + DB_NAME + "'...");
            copyDatabase();
        } catch (IOException e) {
            throw new IOException("Error updating database '" + DB_NAME + "'");
        }
    }

    @SuppressWarnings("unused")
    private boolean isDatabaseExists()
    {
        try {
            String path = new File(DB_PATH, DB_NAME).getAbsolutePath();
            database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database doesn't exist yet
            e.printStackTrace();
        }

        if (database != null) {
            database.close();
        }

        return database != null;
    }

    private void copyDatabase()
        throws IOException
    {
        String path = new File(DB_PATH, DB_NAME).getAbsolutePath();
        Log.d(TAG_NAME, String.format("copyDatabase(dstPath=%s)", path));

        InputStream myInput = context.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(path, false);

        // buffered IO write operation
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

        myOutput = null;
        myInput = null;
    }

    public synchronized void close()
    {
        if (database != null) {
            database.close();
            database = null;
        }
    }
}
