package com.example.thuc.restaurantsdeliveryfood.Activity;
import com.example.thuc.restaurantsdeliveryfood.Helper.DocumentHelper;
import com.example.thuc.restaurantsdeliveryfood.R;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.thuc.restaurantsdeliveryfood.model.Food;
import com.example.thuc.restaurantsdeliveryfood.model.ImageInfoRespone;
import com.example.thuc.restaurantsdeliveryfood.model.PostResponse;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitImgur;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditFoodActivity extends AppCompatActivity
{

    private int checkfind;
    private File chosenFile;
    private Uri returnUri;
    private ImageInfoRespone imageInfoRespone;
    private Food food ;
    private String nameFood;
    private String priceFood;
    private EditText nameFoodET;
    private EditText priceFoodET;
    private ImageView foodImage;

    private MaterialButton startEdit;
    private MaterialButton startDelete;
    private Button buttonFindNewImage;

    private final int PICK_IMAGE_TO_EDIT = 106;
    private final int READ_WRITE_EXTERNAL = 1003;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_food);
        checkfind=0;
        food = (Food) getIntent().getExtras().get("food");
        nameFoodET = (EditText) findViewById(R.id.ten_mon_edit);
        priceFoodET = (EditText)findViewById(R.id.gia_mon_edit);
        foodImage = (ImageView) findViewById(R.id.image_view_edit);
        buttonFindNewImage = (Button)findViewById(R.id.button_find_image_edit);
        startEdit = findViewById(R.id.edit_submit);
        startDelete =findViewById(R.id.delete_submit);

        loadData();
        buttonFindNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkfind=1;
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Picture"), PICK_IMAGE_TO_EDIT);
            }
        });
        startEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkfind==1)
                {
                    onUpload();
                    checkfind=0;
                }
                else
                {
                    StartEdit();
                }
            }
        });
        startDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDelete();
            }
        });
    }

    private void loadData() {
        nameFoodET.setText(food.getName());
        priceFoodET.setText(String.valueOf(food.getPrice()));
        new DownloadImageTask(foodImage).execute(food.getImg_path());
    }


    private void getInput() {
        nameFood = nameFoodET.getText().toString();
        priceFood = priceFoodET.getText().toString();

    }
    private boolean checkInput() {
        getInput();
        if (nameFood.isEmpty() || priceFood.isEmpty())
            return false;
        return true;
    }

    public void startDelete()
    {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).DeleteFood(food.getId()).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                Intent rslt_int = new Intent();
                if(response.body().getStatus() == 0)
                {
                    EditFoodActivity.this.setResult(MainActivity.DELETE_FOOD_SUCCES,rslt_int);
                }
                else
                {
                    EditFoodActivity.this.setResult(MainActivity.DELETE_FOOD_FAIL,rslt_int);
                }
                finish();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Intent rslt_int = new Intent();
                EditFoodActivity.this.setResult(MainActivity.DELETE_FOOD_FAIL,rslt_int);
                finish();
            }
        });

    }
    public void StartEdit() {
        if(!checkInput())
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Fail!").setMessage(R.string.str_missing_info).setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
        else {
            Retrofit retrofit = RetrofitObject.getInstance();
            food.setName(nameFood);
            food.setPrice(Integer.valueOf(priceFood));
            if(imageInfoRespone != null){
                food.setImg_path(imageInfoRespone.data.getLink());
            }
            retrofit.create(CvlApi.class).EditFood(food).enqueue(new Callback<Food>() {
                @Override
                public void onResponse(Call<Food> call, Response<Food> response) {
                    Intent rslt_int = new Intent();
                    if(response.body() != null )
                    {
                        EditFoodActivity.this.setResult(MainActivity.EDIT_FOOD_SUCCES, rslt_int);
                    }
                    else
                    {
                        EditFoodActivity.this.setResult(MainActivity.EDIT_FOOD_FAIL, rslt_int);
                    }
                    finish();
                }

                @Override
                public void onFailure(Call<Food> call, Throwable t) {

                }
            });

        }
    }
    private void onUpload() {
        if (chosenFile == null )
        {
            Toast.makeText(EditFoodActivity.this, "Choose a file before upload.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        CvlApi imgurService = RetrofitImgur.getInstance().create(CvlApi.class);
        //Resize image to (100,100)
        Bitmap bitmap = BitmapFactory.decodeFile(chosenFile.getAbsolutePath());
        Matrix matrix = new Matrix();
        matrix.setScale(100f/bitmap.getWidth(),100f/bitmap.getHeight());
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

        final Call<ImageInfoRespone> call =
                imgurService.upImage(
                        "", "", "", "", "image",
                        MultipartBody.Part.createFormData(
                                "image",
                                chosenFile.getName(),
                                RequestBody.create(MediaType.parse("image/*"), outputStream.toByteArray())
                        ));

        call.enqueue(new Callback<ImageInfoRespone>() {
            @Override
            public void onResponse(@NonNull Call<ImageInfoRespone> call, @NonNull Response<ImageInfoRespone> response) {
                if (response == null) {
                    return;
                }
                if (response.isSuccessful()) {
                    imageInfoRespone = response.body();
                        StartEdit();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ImageInfoRespone> call, @NonNull Throwable t) {
                t.printStackTrace();
                StartEdit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_TO_EDIT && resultCode == RESULT_OK && data != null && data.getData() != null) {

            returnUri = data.getData();

            super.onActivityResult(requestCode, resultCode, data);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                final List<String> permissionsList = new ArrayList<String>();
                addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
                addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (!permissionsList.isEmpty())
                    ActivityCompat.requestPermissions(EditFoodActivity.this,
                            permissionsList.toArray(new String[permissionsList.size()]),
                            READ_WRITE_EXTERNAL);
                else
                    getFilePath();
            } else {
                getFilePath();
            }
        }
    }

    private void getFilePath() {
        String filePath = DocumentHelper.getPath(this, this.returnUri);
        //Safety check to prevent null pointer exception
        Log.e("\n\n\nimage path:", filePath);
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);
        Bitmap bitmap = BitmapFactory.decodeFile(chosenFile.getAbsolutePath());
        if(bitmap!=null)
            foodImage.setImageBitmap(bitmap);
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            shouldShowRequestPermissionRationale(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE_EXTERNAL: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditFoodActivity.this, "All Permission are granted.", Toast.LENGTH_SHORT)
                            .show();
                    getFilePath();
                } else {
                    Toast.makeText(EditFoodActivity.this, "Some permissions are denied", Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
