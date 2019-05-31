package tech.ffiaux.clickcashdemo.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import tech.ffiaux.clickcashdemo.AdsActivity;
import tech.ffiaux.clickcashdemo.MainActivity;
import tech.ffiaux.clickcashdemo.R;
import tech.ffiaux.clickcashdemo.model.Advertisement;

public class AdsListAdapter extends ArrayAdapter<Advertisement> implements View.OnClickListener
{
    private List<Advertisement> adsList;

    public AdsListAdapter(Activity context, List<Advertisement> objects)
    {
        super(context, R.layout.ads_list, objects);
        this.adsList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Activity context = (Activity) super.getContext();
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.ads_list, null, true);

        rowView.setOnClickListener(this);

        TextView adId = (TextView) rowView.findViewById(R.id.adId);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.adThumb);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.adTitle);
        TextView description = (TextView) rowView.findViewById(R.id.adDescription);

        Advertisement rowAd = adsList.get(position);
        adId.setText(rowAd.getId().toString());
        txtTitle.setText(rowAd.getTitle());
        description.setText(rowAd.getDescription());

        DownloadImageFromInternet downloadImage = new DownloadImageFromInternet(imageView);
        downloadImage.execute(rowAd.getThumbUrl());

        return rowView;
    }

    @Override
    public void onClick(View v)
    {
        TextView adId = (TextView) v.findViewById(R.id.adId);
        MainActivity main = (MainActivity) super.getContext();
        Intent intent = new Intent(main, AdsActivity.class);
        intent.putExtra("adId", adId.getText());
        main.startActivity(intent);
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap>
    {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView)
        {
            this.imageView = imageView;
            //Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls)
        {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try
            {
                //InputStream in = new java.net.URL(imageURL).openStream();
                URL url = new java.net.URL(imageURL);
                //URLConnection urlConnection = url.openConnection();
                //InputStream in = urlConnection.getInputStream();

                //bimage = BitmapFactory.decodeStream(in);
                bimage = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            }
            catch (Exception e)
            {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }

            return bimage;
        }

        protected void onPostExecute(Bitmap result)
        {
            imageView.setImageBitmap(result);
        }
    }
}
