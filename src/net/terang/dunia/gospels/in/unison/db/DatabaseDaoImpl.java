package net.terang.dunia.gospels.in.unison.db;

import java.sql.*;
import java.util.*;

import net.terang.dunia.gospels.in.unison.model.*;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.stmt.*;

/**
 * implements CRUD, among other utility functions
 * 
 * @author lwy08
 */
public class DatabaseDaoImpl
{
    Dao<Article, String> articleDao;

    public DatabaseDaoImpl(DatabaseHelper db) throws SQLException
    {
        articleDao = db.getArticleDao();
    }

    public int create(Article animal)
        throws SQLException
    {
        return articleDao.create(animal);
    }

    public int update(Article animal)
        throws SQLException
    {
        return articleDao.update(animal);
    }

    public int delete(Article animal)
        throws SQLException
    {
        return articleDao.delete(animal);
    }

    public Article getById(int id)
        throws SQLException
    {
        QueryBuilder<Article, String> queryBuilder = articleDao.queryBuilder();
        queryBuilder.where().eq("id", id);
        PreparedQuery<Article> pq = queryBuilder.prepare();
        return articleDao.queryForFirst(pq);
    }

    public List<Article> getAll()
        throws SQLException
    {
        return articleDao.queryForAll();
    }
}
