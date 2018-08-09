package com.assignment.animation.animationassignment.details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.transition.Transition;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.animation.animationassignment.R;
import com.assignment.animation.animationassignment.utility.Util;

import java.io.InputStream;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "header_url";
    public static final String EXTRA_PARAM_TITLE = "header_title";
    public static final String VIEW_NAME_HEADER_IMAGE = "selected_view_id";
    public static final String VIEW_NAME_HEADER_TITLE = "selected_view_title";
    private String selectedImageUrl;
    private ImageView mHeaderImage;
    private TextView mHeaderTitle;
    private String headerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        selectedImageUrl = getIntent().getStringExtra(EXTRA_PARAM_ID);
        headerTitle = getIntent().getStringExtra(EXTRA_PARAM_TITLE);

        initView();

        ViewCompat.setTransitionName(mHeaderImage, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(mHeaderTitle, VIEW_NAME_HEADER_TITLE);

        loadImage();
        mHeaderTitle.setText(headerTitle);
    }

    private void loadImage() {
        if(Util.isOnline(DetailsActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
                new DownloadImageTask(mHeaderImage).execute(selectedImageUrl);
            } else {
                new DownloadImageTask(mHeaderImage).execute(selectedImageUrl);
            }
        }
        else{
            Util.showMessage(DetailsActivity.this,getString(R.string.connect_to_internet));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        final android.transition.Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            transition.addListener(new android.transition.Transition.TransitionListener() {
                @Override
                public void onTransitionStart(android.transition.Transition transition) {

                }

                @Override
                public void onTransitionEnd(android.transition.Transition transition) {
                    transition.removeListener(this);
                    new DownloadImageTask(mHeaderImage).execute(selectedImageUrl);
                }

                @Override
                public void onTransitionCancel(android.transition.Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(android.transition.Transition transition) {

                }

                @Override
                public void onTransitionResume(android.transition.Transition transition) {

                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }

    private void initView() {
        mHeaderImage = findViewById(R.id.bannerImage);
        mHeaderTitle = findViewById(R.id.bannerTitle);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... objs) {

            String urlDisplay = objs[0];
            Bitmap mIcon11 = null;
            mIcon11 = Util.getBitmapFromUrl(urlDisplay);
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
