package net.terang.dunia.gospels.in.unison.activity;

import java.sql.*;

import net.terang.dunia.gospels.in.unison.*;
import net.terang.dunia.gospels.in.unison.view.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class BookListActivity
    extends BaseListActivity
{
    private static final String TAG_NAME = BookListActivity.class
                    .getSimpleName();
    private TextView txtTitle;
    private Button btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        Log.d(TAG_NAME, "Book ListView activated successfully!");

        // 1. first extract the bundle from intent
        Bundle bundle = getIntent().getExtras();

        // Set the test adapter
        try {
            setListAdapter(new BookAdapter(this, bundle));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // TextViews
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnBack = (Button) findViewById(R.id.btnExit);
        if (txtTitle == null || btnBack == null) {
            Log.d(TAG_NAME, "txtTitle / btnBack in main View is null");
            return;
        }

        // set values
        txtTitle.setText(bundle.getString("TITLE"));
        btnBack.setText("Back");
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG_NAME, "Back button was clicked!");
                finish();
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        System.gc();
        finish();
    }

    /**
     * Set the current orientation to landscape. This will prevent the OS from
     * changing
     * the app's orientation.
     */
    public void lockOrientationLandscape()
    {
        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Set the current orientation to portrait. This will prevent the OS from
     * changing
     * the app's orientation.
     */
    public void lockOrientationPortrait()
    {
        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Locks the orientation to a specific type. Possible values are:
     * <ul>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_BEHIND}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_NOSENSOR}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_SENSOR}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_UNSPECIFIED}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_USER}</li>
     * </ul>
     * 
     * @param orientation
     */
    public void lockOrientation(int orientation)
    {
        setRequestedOrientation(orientation);
    }
}