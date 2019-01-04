package com.example.thuc.restaurantsdeliveryfood.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thuc.restaurantsdeliveryfood.Adapter.CommentViewAdapter;
import com.example.thuc.restaurantsdeliveryfood.R;

import java.io.InputStream;
import java.net.URL;

public class SetttingViewFrament extends Fragment {
    private TextView nameRes;
    private ImageView avarata;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.setting_frament, container, false);
        nameRes = (TextView)view.findViewById(R.id.setting_res_name);
        avarata = (ImageView)view.findViewById(R.id.setting_avarta);
        setData();
        return view;
    }
    private void setData()
    {
        nameRes.setText(MainActivity.restaurant.getName());
        new DownloadImageTask(avarata).execute(MainActivity.restaurant.getImage_path());
    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        DownloadImageTask(ImageView bmImage) {
            if(bmImage != null)
                this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
