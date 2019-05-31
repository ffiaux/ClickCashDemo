package tech.ffiaux.clickcashdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import tech.ffiaux.clickcashdemo.model.Advertisement;
import tech.ffiaux.clickcashdemo.tasks.DownloadImageFromInternet;

public class AdsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        String id = getIntent().getExtras().getString("adId");
        //setViewData(id);

        ImageView thumb = findViewById(R.id.adThumb);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        DownloadAd downloadImage = new DownloadAd(thumb, progressBar);
        downloadImage.execute(id);
    }

    private class DownloadAd extends AsyncTask<String, Void, Advertisement>
    {
        private ImageView thumb;
        private ProgressBar progressBar;
        private String url;

        public DownloadAd(ImageView thumb, ProgressBar progressBar)
        {
            this.thumb = thumb;
            this.progressBar = progressBar;
        }

        protected void onPreExecute()
        {
            //display progress dialog.
        }

        protected Advertisement doInBackground(String... params)
        {
            String adId = params[0];
            setViewData(new Long(adId));

            return null;
        }

        protected void onPostExecute(Advertisement result)
        {
            // dismiss progress dialog and update ui
            this.progressBar.setVisibility(View.INVISIBLE);

            DownloadImageFromInternet downloadImage = new DownloadImageFromInternet(this.thumb);
            downloadImage.execute(this.url);
        }

        private void setViewData(Long id)
        {
            String jsonStr = "";

            try
            {
                URL url = new URL("http://ffiaux.tech/cc_ad.php?id=" + id.toString());
                URLConnection conn = url.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                InputStream is = httpConn.getInputStream();
                jsonStr = convertinputStreamToString(is);

                JSONObject jsonObj = new JSONObject(jsonStr);
                this.url = jsonObj.getString("thumbUrl");
                System.out.println(this.url);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        private String convertinputStreamToString(InputStream ists) throws IOException
        {
            if (ists != null)
            {
                StringBuilder sb = new StringBuilder();
                String line;

                try
                {
                    BufferedReader r1 = new BufferedReader(new InputStreamReader(ists, "UTF-8"));
                    while ((line = r1.readLine()) != null)
                    {
                        sb.append(line).append("\n");
                    }
                }
                finally
                {
                    ists.close();
                }
                return sb.toString();
            }
            else
            {
                return "";
            }
        }
    }
}
