package com.sangtaohay.frame;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sangtaohay.memestudio.dao.MemeDAO;
import com.sangtaohay.memestudio.db.MemeDbHelper;
import com.sangtaohay.memestudio.dto.MemeDTO;

import java.util.ArrayList;
import java.util.List;

public class GridViewActivity extends AppCompatActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";

    private MemeDbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_gridview);

        mGridView = findViewById(R.id.gridView);
        mProgressBar = findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
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
                        putExtra("textBottomRight", item.getTextBottomRight());

                //Start details activity
                startActivity(intent);
            }
        });

        //Start download
//        new AsyncHttpTask().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);

        mHelper = new MemeDbHelper(getApplicationContext());

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
                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

            //Hide progressbar
            mProgressBar.setVisibility(View.GONE);
        }
    }


    String streamToString(/*InputStream stream*/) {
     /*   BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;*/
     return "{\"posts\":[{\"title\":\"This is my face\",\"attachments\":[{\"url\":\"http://i.imgur.com/UkwirVu.jpg\"}]},{\"title\":\"Shutup and take my money\",\"attachments\":[{\"url\":\"https://i.pinimg.com/originals/a9/ff/17/a9ff17db85f8136619feb0d5a200c0e4.png\"}]},{\"title\":\"Thanh nho la day\",\"attachments\":[{\"url\":\"https://i.imgflip.com/ttrhl.jpg\"}]},{\"title\":\"F**k up!\",\"attachments\":[{\"url\":\"https://i.gifer.com/Z4AL.gif\"}]},{\"title\":\"Just do it\",\"attachments\":[{\"url\":\"https://media.giphy.com/media/IJJXidQr4jvIk/giphy.gif\"}]},{\"title\":\"Walk carefully\",\"attachments\":[{\"url\":\"https://img.memecdn.com/FFFUUU-Gif_o_120812.gif\"}]},{\"title\":\"Day la thang ban toi\",\"attachments\":[{\"url\":\"https://media.giphy.com/media/ZfPQAbzRm0dZ6/giphy.gif\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]},{\"image\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\",\"title\":\"haha\",\"attachments\":[{\"url\":\"http://www.oponayfarmsllc.com/images/gallery/hay.jpg\"}]}]}";
    }

    /**
     * Parsing the feed results and get the list
     *
     */
   /* private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("posts");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                item = new GridItem();
                item.setTitle(title);
                JSONArray attachments = post.getJSONArray("attachments");
                if (null != attachments && attachments.length() > 0) {
                    JSONObject attachment = attachments.getJSONObject(0);
                    if (attachment != null)
                        item.setImage(attachment.getString("url"));
                }
                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    private void parseResult() {
        List<MemeDTO> listMeme = new MemeDAO().getListMemeDTO(mHelper, null);
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
    protected void onResume() {
        super.onResume();
        new AsyncHttpTask().execute(FEED_URL);
        mGridAdapter.notifyDataSetChanged();
    }
}