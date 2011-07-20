package net.terang.dunia.gospels.in.unison.model;

import java.sql.*;

import javax.persistence.*;

import net.terang.dunia.gospels.in.unison.db.*;

@Entity(name = "toc")
public class TocItem
{
    @Id
    private int id;

    @Column
    private String title;

    public TocItem()
    {
        // required by ORMLite
    }

    public int save(DatabaseContext context)
        throws SQLException
    {
        if (context.toc.getById(id) == null) {
            return context.toc.create(this);
        } else {
            return context.toc.update(this);
        }
    }

    public int delete(DatabaseContext context)
        throws SQLException
    {
        return context.toc.delete(this);
    }

    public TocItem(int id, String title)
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