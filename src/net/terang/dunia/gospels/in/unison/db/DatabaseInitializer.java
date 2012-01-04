package net.terang.dunia.gospels.in.unison.db;

import java.io.*;

import android.content.*;
import android.content.res.*;
import android.content.res.AssetManager.*;
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

        DB_PATH = String.format("/data/data/%s/databases/",
            context.getPackageName());
        // TODO: optimise!
        int resourceId = context.getResources().getIdentifier("database_name",
            "string", context.getPackageName());

        DB_NAME = context.getResources().getString(resourceId);
    }

    public void createDatabase()
    {
        Log.d(TAG_NAME, "createDatabase()");

        try {
            Log.d(TAG_NAME, String.format("Updating database '%s'...", DB_NAME));
            copyDatabase();
        } catch (IOException e) {
            Log.e(TAG_NAME,
                String.format("Error: cannot update database '%s'", DB_NAME), e);
        }
    }

    @SuppressWarnings("unused")
    /**
     * queries existence of database by attempting to open it
     */
    private boolean isDatabasePresent()
    {
        try {
            String path = new File(DB_PATH, DB_NAME).getAbsolutePath();
            database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e(TAG_NAME, "Error: database not found", e);
            return false;
        }

        if (database != null) {
            database.close();
            database = null;
        }

        return true;
    }

    private void copyDatabase()
        throws IOException
    {
        File outputFile = new File(DB_PATH, DB_NAME);
        String outputPath = outputFile.getAbsolutePath();
        Log.d(TAG_NAME,
            String.format("copyDatabase(): %s -> %s", DB_NAME, outputPath));

        AssetManager assets = context.getAssets();
        InputStream input = assets.open(DB_NAME, Context.MODE_WORLD_READABLE);
        DataInputStream dataIO = new DataInputStream(input);
        FileOutputStream output = new FileOutputStream(outputPath);
        byte[] buffer = new byte[1024];
        while (dataIO.read(buffer) != -1) {
            output.write(buffer);
        }
        output.flush();

        // close inputs
        if (dataIO != null) {
            dataIO.close();
            dataIO = null;
        }
        if (input != null) {
            input.close();
            input = null;
        }

        // close outputs
        if (output != null) {
            output.close();
            output = null;
        }
    }

    public synchronized void closeDatabase()
    {
        if (database != null) {
            database.close();
            database = null;
        }
    }
}
