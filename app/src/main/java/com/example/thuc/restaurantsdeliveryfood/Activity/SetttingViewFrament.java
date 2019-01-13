package com.example.thuc.restaurantsdeliveryfood.Activity;

import android.Manifest;

import android.content.pm.PackageManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.thuc.restaurantsdeliveryfood.R;
import java.io.InputStream;
import java.net.URL;

public class SetttingViewFrament extends Fragment {
    private TextView nameRes;
    private TextView addRes;
    private MaterialCardView changePass;
    private MaterialCardView logout;
    private MaterialCardView infoApp;
    private ImageView avarata;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.setting_view_frament, container, false);
        nameRes = (TextView)view.findViewById(R.id.name_res_setting);
        addRes = view.findViewById(R.id.address_res_setting);
        avarata = (ImageView)view.findViewById(R.id.avarta_setting);

        changePass = view.findViewById(R.id.thay_doi_mat_khau_setting);
        infoApp = view.findViewById(R.id.thong_tin_app);
        logout = view.findViewById(R.id.logut_setting);
        setData();
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SetttingViewFrament.this.getActivity(), ChangePasswordActivity.class);
                SetttingViewFrament.this.getActivity().startActivityForResult(i, MainActivity.CHANGE_PASS_STATUS);
            }
        });

        infoApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetttingViewFrament.this.getActivity(),InfoAppActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetttingViewFrament.this.getActivity(),LoginActivity.class);
                MainActivity.restaurant=null;
                startActivity(intent);
                SetttingViewFrament.this.getActivity().finish();
            }
        });

        return view;
    }

    private void setData()
    {
        nameRes.setText(MainActivity.restaurant.getName());
        addRes.setText(MainActivity.restaurant.getAddress());
        new DownloadImageTask(avarata).execute(MainActivity.restaurant.getImage_path());
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
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
