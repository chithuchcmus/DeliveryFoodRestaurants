package com.example.thuc.restaurantsdeliveryfood.Activity;
import com.example.thuc.restaurantsdeliveryfood.Helper.DocumentHelper;
import com.example.thuc.restaurantsdeliveryfood.R;
import com.example.thuc.restaurantsdeliveryfood.model.Food;
import com.example.thuc.restaurantsdeliveryfood.model.ImageInfoRespone;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitImgur;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class AddFoodActivity extends AppCompatActivity {

    private String nameFood;
    private String price;
    private Button addImage;
    private MaterialButton addFood;
    private ImageView imageViewFood;
    private final int RESULT_LOAD_IMAGE = 5;
    private TextInputEditText nameFoodET;
    private TextInputEditText priceET;
    private Food food;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int READ_WRITE_EXTERNAL = 1003;
    private ImageInfoRespone imageInfoRespone;

    private void getInput() {
        nameFood = nameFoodET.getText().toString();
        price = priceET.getText().toString();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        imageViewFood = findViewById(R.id.image_view_add);
        nameFoodET = findViewById(R.id.ten_mon_add);
        priceET = findViewById(R.id.gia_mon_add);

        addFood = findViewById(R.id.add_submit);
        addImage = findViewById(R.id.button_find_image_add);

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrlFromImage();
            }
        });
       addImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent gallery = new Intent();
               gallery.setType("image/*");
               gallery.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE_REQUEST);
           }
       });
    }

    private boolean checkInput() {
        getInput();
        if (nameFood.isEmpty() || price.isEmpty())
            return false;
        return true;
    }

    public void addFoodInRes() {
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
            food = new Food();
            food.setName(nameFood);
            food.setPrice(Integer.valueOf(price));
            food.setId_quan(MainActivity.restaurant.getId());
            if(imageInfoRespone != null){
                food.setImg_path(imageInfoRespone.data.getLink());
            }

            retrofit.create(CvlApi.class).newFoodInRes(food).enqueue(new Callback<Food>() {
                @Override
                public void onResponse(@NonNull Call<Food> call, Response<Food> response) {
                    Intent rslt_int = new Intent();
                    if(response.body() != null )
                    {
                        AddFoodActivity.this.setResult(MainActivity.ADD_FOOD_SUCCES, rslt_int);
                    }
                    else
                    {
                        AddFoodActivity.this.setResult(MainActivity.ADD_FOOD_FAIL, rslt_int);
                    }
                    finish();
                }
                @Override
                public void onFailure(@NonNull Call<Food> call, @NonNull Throwable t) {
                    Intent rslt_int = new Intent();
                    AddFoodActivity.this.setResult(MainActivity.ADD_FOOD_FAIL, rslt_int);
                    finish();
                }
            });

        }
    }

    private void getUrlFromImage() {
        if (chosenFile == null)
        {
            Toast.makeText(AddFoodActivity.this, "Choose a file before upload.", Toast.LENGTH_SHORT)
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
                    Toast.makeText(AddFoodActivity.this, "Upload successful !", Toast.LENGTH_SHORT)
                            .show();
                    imageInfoRespone = response.body();
                    addFoodInRes();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageInfoRespone> call, @NonNull Throwable t) {
                Toast.makeText(AddFoodActivity.this, "An unknown error has occured.", Toast.LENGTH_SHORT)
                        .show();
                t.printStackTrace();
                addFoodInRes();
            }
        });
    }

    private File chosenFile;
    private Uri returnUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            returnUri = data.getData();

            super.onActivityResult(requestCode, resultCode, data);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                final List<String> permissionsList = new ArrayList<String>();
                addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
                addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (!permissionsList.isEmpty())
                    ActivityCompat.requestPermissions(AddFoodActivity.this,
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
         imageViewFood.setImageBitmap(bitmap);
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
                    Toast.makeText(AddFoodActivity.this, "All Permission are granted.", Toast.LENGTH_SHORT)
                            .show();
                    getFilePath();
                } else {
                    Toast.makeText(AddFoodActivity.this, "Some permissions are denied", Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
