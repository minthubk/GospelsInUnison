package net.terang.dunia.gospels.in.unison.view;

import java.io.*;
import java.sql.*;

import net.terang.dunia.gospels.in.unison.*;
import net.terang.dunia.gospels.in.unison.activity.*;
import net.terang.dunia.gospels.in.unison.context.*;
import net.terang.dunia.gospels.in.unison.db.*;
import net.terang.dunia.gospels.in.unison.model.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class TocAdapter
    extends BaseAdapter
{
    private static final String TAG_NAME = TocAdapter.class.getSimpleName();
    private final LayoutInflater mInflater;
    private final TocDbContext tocDbContext;
    private final Context context;

    /**
     * Constructor
     * 
     * @param context
     * @throws SQLException
     * @throws IOException
     */
    public TocAdapter(Context context) throws SQLException, IOException
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        tocDbContext = new TocDbContext(context);
        Log.d(TAG_NAME, "TocAdapter constructed successfully!");
    }

    @Override
    public int getCount()
    {
        try {
            return tocDbContext.toc.length();
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
            return tocDbContext.toc.getById(position + 1);

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
        TocItem thisItem = getItem(position);
        Log.d(TAG_NAME, "thisItem: " + thisItem);

        View newView = convertView;
        if (newView == null) {
            newView = mInflater.inflate(R.layout.list_item, parent, false);
        }

        TextView txtTocItem = (TextView) newView.findViewById(R.id.listItem);
        Log.d(TAG_NAME, Integer.toString(position + 1));

        // populate textview with db data
        try {
            // convert from 0-based (here) to 1-based (SQL) indexing
            TocItem item = tocDbContext.toc.getById(position + 1);
            Log.d(TAG_NAME, item.getId() + " / " + item.getTitle());
            txtTocItem.setText(item.getId() + ": " + item.getTitle());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // attach event handler
        final int pos = position;
        txtTocItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG_NAME, "TOC Item pos@" + (pos + 1) + " was clicked!");
                Intent myIntent = new Intent(context, BookListActivity.class);

                // pass parameters onto next Activity //
                // 1. first create the bundle and initialize it
                Bundle bundle = new Bundle();

                // 2. then add the parameters to bundle as required
                bundle.putString("TITLE", getItem(pos).getTitle());
                bundle.putInt("CHAPTER", pos + 1);

                // 3. add this bundle to the intent
                myIntent.putExtras(bundle);

                // now finally start next Activity
                v.getContext().startActivity(myIntent);
            }
        });

        return newView;
    }
}
