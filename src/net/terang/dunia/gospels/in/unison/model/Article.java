package net.terang.dunia.gospels.in.unison.model;

import java.sql.*;

import javax.persistence.*;

import net.terang.dunia.gospels.in.unison.db.*;

@Entity(name = "toc")
public class Article
{
    @Id
    private int id;

    @Column
    private String title;

    public Article()
    {
        // required by ORMLite
    }

    public int save(DatabaseContext context)
        throws SQLException
    {
        if (context.articles.getById(id) == null) {
            return context.articles.create(this);
        } else {
            return context.articles.update(this);
        }
    }

    public int delete(DatabaseContext context)
        throws SQLException
    {
        return context.articles.delete(this);
    }

    public Article(int id, String title)
    {
        super();
        this.id = id;
        this.title = title;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}