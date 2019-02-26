package com.sangtaohay.frame.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sangtaohay.frame.DetailsActivity;
import com.sangtaohay.frame.GridItem;
import com.sangtaohay.frame.GridViewActivity;
import com.sangtaohay.frame.GridViewAdapter;
import com.sangtaohay.frame.R;
import com.sangtaohay.memestudio.dao.MemeDAO;
import com.sangtaohay.memestudio.db.MemeDbHelper;
import com.sangtaohay.memestudio.dto.MemeDTO;

import java.util.ArrayList;
import java.util.List;


public class OneFragment extends Fragment{
    private static final String TAG = GridViewActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";

    private MemeDbHelper mHelper;
    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_one, container, false);
        RelativeLayout linearLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_one, container, false);

        mGridView = linearLayout.findViewById(R.id.gridView);
        mProgressBar = linearLayout.findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                ImageView imageView = v.findViewById(R.id.grid_item_image);

                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView.getWidth()).
                        putExtra("height", imageView.getHeight()).
                        putExtra("_id", item.getMemeId()).
                        putExtra("title", item.getTitle()).
                        putExtra("textTop", item.getTextTop()).
                        putExtra("textBottom", item.getTextBottom()).
                        putExtra("image", item.getImage()).
                        putExtra("imageWidth", item.getImageWidth()).
                        putExtra("imageHeight", item.getImageHeight()).
                        putExtra("textBottomLeft", item.getTextBottomLeft()).
                        putExtra("textBottomRight", item.getTextBottomRight()).
                        putExtra("enableAnimate", true);

                //Start details activity
                startActivity(intent);
            }
        });

        //Start download
//        new AsyncHttpTask().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);

        mHelper = new MemeDbHelper(getActivity().getApplicationContext());
        return linearLayout;
    }
    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = 200;//httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    // String response = streamToString();//streamToString(httpResponse.getEntity().getContent());
                    // parseResult(response);
                    parseResult();
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Lets update UI

            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

            //Hide progressbar
            mProgressBar.setVisibility(View.GONE);
        }
    }
    private void parseResult() {
        List<MemeDTO> listMeme = new MemeDAO().getListMemeDTO(mHelper, false);
        mGridData.clear();
        for (MemeDTO meme: listMeme) {
            GridItem item = new GridItem();
            item.setTitle(meme.textTop);
            item.setTitle(meme.textTop);
            item.setImage(meme.imageName);
            item.setThumb(meme.thumb);
            item.setTextTop(meme.textTop);
            item.setTextBottom(meme.textBottom);
            item.setImageWidth(meme.imageWidth);
            item.setImageHeight(meme.imageHeight);
            item.setTextBottomLeft(meme.textBottomLeft);
            item.setTextBottomRight(meme.textBottomRight);
            item.setMemeId(meme.id);
            mGridData.add(item);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        new AsyncHttpTask().execute(FEED_URL);
        mGridAdapter.notifyDataSetChanged();
    }
}
