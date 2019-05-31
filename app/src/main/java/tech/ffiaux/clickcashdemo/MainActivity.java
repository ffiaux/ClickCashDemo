package tech.ffiaux.clickcashdemo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import tech.ffiaux.clickcashdemo.adapters.AdsListAdapter;
import tech.ffiaux.clickcashdemo.model.Advertisement;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private List<Advertisement> getAds()
    {
        List<Advertisement> ads = new ArrayList<Advertisement>();

        Advertisement ad = new Advertisement();
        ad.setId(1L);
        ad.setTitle("Anuncio1");
        ad.setDescription("Descricao1");
        ad.setThumbUrl("https://img.olx.com.br/thumbs256x256/53/539818109387479.jpg");
        ads.add(ad);

        ad = new Advertisement();
        ad.setId(2L);
        ad.setTitle("Anuncio2");
        ad.setDescription("Descricao2");
        ad.setThumbUrl("https://img.olx.com.br/thumbs256x256/53/537818106659323.jpg");
        ads.add(ad);

        ad = new Advertisement();
        ad.setId(3L);
        ad.setTitle("Anuncio3");
        ad.setDescription("Descricao3");
        ad.setThumbUrl("https://img.olx.com.br/thumbs256x256/53/531818107539490.jpg");
        ads.add(ad);

        ad = new Advertisement();
        ad.setId(4L);
        ad.setTitle("Anuncio4");
        ad.setDescription("Descricao4");
        ad.setThumbUrl("https://img.olx.com.br/thumbs256x256/53/537818103731951.jpg");
        ads.add(ad);

        ad = new Advertisement();
        ad.setId(5L);
        ad.setTitle("Anuncio5");
        ad.setDescription("Descricao5");
        ad.setThumbUrl("https://img.olx.com.br/thumbs256x256/63/636715023779786.jpg");
        ads.add(ad);

        ad = new Advertisement();
        ad.setId(6L);
        ad.setTitle("Anuncio6");
        ad.setDescription("Descricao6");
        ad.setThumbUrl("https://img.olx.com.br/thumbs256x256/53/539818101402411.jpg");
        ads.add(ad);

        return ads;
    }

    private class DownloadAds extends AsyncTask<Void, Void, List<Advertisement>>
    {
        private String url;
        private List<Advertisement> ads;
        private Activity context;

        public DownloadAds(Activity context)
        {
            this.context = context;
        }

        protected void onPreExecute()
        {
            this.ads = new ArrayList<Advertisement>();
        }

        @Override
        protected List<Advertisement> doInBackground(Void... voids)
        {
            setViewData();

            return this.ads;
        }

        protected void onPostExecute(List<Advertisement> ads)
        {
            ListView adsList = (ListView) findViewById(R.id.adsList);
            AdsListAdapter adapter = new AdsListAdapter(this.context, ads);
            adsList.setAdapter(adapter);
        }

        private void setViewData()
        {
            String jsonStr = "";

            try
            {
                URL url = new URL("http://ffiaux.tech/cc_all.php");
                URLConnection conn = url.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                InputStream is = httpConn.getInputStream();
                jsonStr = convertinputStreamToString(is);

                JSONArray jsonArr = new JSONArray(jsonStr);
                for(int i=0; i<jsonArr.length(); i++)
                {
                    JSONObject obj = (JSONObject) jsonArr.get(i);
                    System.out.println(obj);
                    Advertisement ad = new Advertisement();
                    ad.setId(new Long(obj.getString("id")));
                    ad.setTitle(obj.getString("title"));
                    ad.setDescription(obj.getString("description"));
                    ad.setThumbUrl(obj.getString("thumbUrl"));
                    ads.add(ad);
                }

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DownloadAds downloadAds = new DownloadAds(this);
        downloadAds.execute();

        //ListView adsList = (ListView) findViewById(R.id.adsList);
        //AdsListAdapter adapter = new AdsListAdapter(this, getAds());
        //adsList.setAdapter(adapter);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery)
        {

        } else if (id == R.id.nav_slideshow)
        {

        } else if (id == R.id.nav_manage)
        {

        } else if (id == R.id.nav_share)
        {

        } else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}