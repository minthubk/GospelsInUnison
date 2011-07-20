package net.terang.dunia.gospels.in.unison.db;

import java.sql.*;

import android.content.Context;

public class DatabaseContext
{
    private final DatabaseHelper db;
    public DatabaseDaoImpl toc;

    public DatabaseContext(Context context) throws SQLException
    {
        DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
        db = manager.getHelper(context);
        toc = new DatabaseDaoImpl(db);
    }
}
