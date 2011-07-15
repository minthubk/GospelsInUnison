package net.terang.dunia.gospels.in.unison;

import java.sql.*;

import net.terang.dunia.gospels.in.unison.db.*;
import net.terang.dunia.gospels.in.unison.model.*;
import net.terang.dunia.gospels.in.unison.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.text.method.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class MainActivity
    extends Activity
{
    private TextView txtTitle, txtContent;
    private DatabaseContext context;
    @SuppressWarnings("unused")
    private boolean isCustomTitleSupported = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // textviews
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtContent = (TextView) findViewById(R.id.txtContent);
        if (txtTitle == null || txtContent == null) {
            return;
        }

        // set values
        txtTitle.setText("Table of Contents");
        try {
            context = new DatabaseContext(this);
            Article article = context.articles.getById(1);

            txtContent.setMovementMethod(new ScrollingMovementMethod());
            txtContent.setText(article.getTitle());

            Log.d(MainActivity.class.getName(),
                "article title retrieved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        isCustomTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        // setContentView(new Banner(this));
    }

    @SuppressWarnings("unused")
    private class myView
        extends View
    {

        public myView(Context context)
        {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo);
            canvas.drawBitmap(myBitmap, 0, 0, null);
            int pic_width = myBitmap.getWidth(), pic_height = myBitmap
                            .getHeight();
        }
    }
}