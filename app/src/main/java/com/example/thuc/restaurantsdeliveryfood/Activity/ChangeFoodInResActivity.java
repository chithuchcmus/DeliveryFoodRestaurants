package com.example.thuc.restaurantsdeliveryfood.Activity;
import com.example.thuc.restaurantsdeliveryfood.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.thuc.restaurantsdeliveryfood.model.Food;

import java.io.InputStream;
import java.net.URL;

public class ChangeFoodInResActivity extends AppCompatActivity
{
    private Food food ;
    private TextView nameFood;
    private TextView priceFood;
    private TextView descriptionFood;
    private ImageView foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_food_in_res);

        food = (Food) getIntent().getExtras().get("food");
        nameFood = (TextView)findViewById(R.id.name_food_in_change);
        priceFood = (TextView)findViewById(R.id.price_food_in_change);
        descriptionFood = (TextView)findViewById(R.id.description_food_in_change);
        foodImage = (ImageView) findViewById(R.id.image_in_food_change);
        loadData();
        new DownloadImageTask(foodImage).execute(food.getImg_path());
    }

    private void loadData() {
        nameFood.setText(food.getName());
        priceFood.setText(String.valueOf(food.getPrice()));
        descriptionFood.setText(food.getDescription());
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
