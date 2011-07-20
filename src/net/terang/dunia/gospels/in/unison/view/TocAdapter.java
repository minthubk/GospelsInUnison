package net.terang.dunia.gospels.in.unison.view;

import java.sql.*;

import net.terang.dunia.gospels.in.unison.*;
import net.terang.dunia.gospels.in.unison.db.*;
import net.terang.dunia.gospels.in.unison.model.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class TocAdapter
    extends BaseAdapter
{
    private static final String TAG_NAME = TocAdapter.class.getSimpleName();
    private final LayoutInflater mInflater;
    private final DatabaseContext dbContext;

    /**
     * Constructor
     * 
     * @param context
     * @throws SQLException
     */
    public TocAdapter(Context context) throws SQLException
    {
        mInflater = LayoutInflater.from(context);
        dbContext = new DatabaseContext(context);
        Log.d(TAG_NAME, "TocAdapter constructed successfully!");
    }

    @Override
    public int getCount()
    {
        try {
            return dbContext.toc.length();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public TocItem getItem(int position)
    {
        try {
            Log.d(TAG_NAME, "title " + position + " retrieved successfully");

            // convert from 0-based (here) to 1-based (SQL) indexing
            return dbContext.toc.getById(position + 1);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View newView = convertView;
        if (newView == null) {
            newView = mInflater.inflate(R.layout.list_item, parent, false);
        }

        TextView txtTocItem = (TextView) newView.findViewById(R.id.tocItem);
        Log.d(TAG_NAME, Integer.toString(position + 1));
        try {
            // convert from 0-based (here) to 1-based (SQL) indexing
            TocItem item = dbContext.toc.getById(position + 1);
            Log.d(TAG_NAME, item.getId() + " / " + item.getTitle());
            txtTocItem.setText(item.getId() + ": " + item.getTitle());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newView;
    }

}
