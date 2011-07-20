package net.terang.dunia.gospels.in.unison.db;

import java.sql.*;
import java.util.*;

import net.terang.dunia.gospels.in.unison.model.*;

import com.j256.ormlite.dao.*;

/**
 * implements CRUD, among other utility functions
 */
public class DatabaseDaoImpl
{
    Dao<TocItem, String> tocDao;

    public DatabaseDaoImpl(DatabaseHelper db) throws SQLException
    {
        tocDao = db.getTocDao();
    }

    public int create(TocItem article)
        throws SQLException
    {
        return tocDao.create(article);
    }

    public int update(TocItem article)
        throws SQLException
    {
        return tocDao.update(article);
    }

    public int delete(TocItem article)
        throws SQLException
    {
        return tocDao.delete(article);
    }

    public TocItem getById(int id)
        throws SQLException
    {
        return tocDao.queryForId(Integer.toString(id));
    }

    public int length()
        throws SQLException
    {
        return tocDao.queryForAll().size();
    }

    public List<TocItem> getAll()
        throws SQLException
    {
        return tocDao.queryForAll();
    }
}
