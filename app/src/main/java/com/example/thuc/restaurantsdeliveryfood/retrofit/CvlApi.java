package com.example.thuc.restaurantsdeliveryfood.retrofit;


import com.example.thuc.restaurantsdeliveryfood.model.Bill;
import com.example.thuc.restaurantsdeliveryfood.model.BillDetail;
import com.example.thuc.restaurantsdeliveryfood.model.Comment;
import com.example.thuc.restaurantsdeliveryfood.model.Food;
import com.example.thuc.restaurantsdeliveryfood.model.ImageInfoRespone;
import com.example.thuc.restaurantsdeliveryfood.model.PostResponse;
import com.example.thuc.restaurantsdeliveryfood.model.Restaurant;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface CvlApi {

    @GET("/login/quan_an")
    Call<List<Restaurant>> loginRes(@Query("sdt") String phone, @Query("pass") String password);

    @GET("/signup/quan_an")
    Call<List<Restaurant>> newRes(@QueryMap Map<String, String> info);


    @GET("/db/quan_an")
    Call<List<Restaurant>> getRestaurant();
    @GET("/mon_an")
    Call<List<Food>> getFoodsList(@Query("id_quan_an") int res_id);

    @GET("/hoa_don")
    Call<List<Bill>> getBillsListNeedToAccept(@Query("id_quan_an")  int res_id, @Query("trangthai") int trangthai);

    @GET("/chi_tiet_hoa_don")
    Call<List<BillDetail>> getDetailBillList(@Query("id_hoa_don") int hoa_don_id);

    @GET("/quan_an/xac_nhan")
    Call<PostResponse> ResAcceptBill(@Query("id_hoa_don") int hoa_don_id);

    @GET("/quan_an/xac_nhan")
    Call<PostResponse> DenyBill(@Query("id_hoa_don") int hoa_don_id, @Query("huy") int huy);

    @POST("/quan_an/them_mon_an")
    Call<Food> newFoodInRes(@Body Food food);

    @GET("/quan_an/xoa_mon_an")
    Call<PostResponse> DeleteFood(@Query("id_mon_an") int id_mon);

    @POST("/quan_an/sua_mon_an")
    Call<Food>EditFood(@Body Food food);

    @GET("/xem_danh_gia")
    Call<List<Comment>>getComments(@Query("id_quan_an") int quanan);

    @Multipart
    @Headers({
            "Authorization: Client-ID 1a89bd8577b6371"
    })
    @POST("image")
    Call<ImageInfoRespone> upImage(
            @Query("album") String albumId,
            @Query("title") String title,
            @Query("description") String description,
            @Query("name") String name,
            @Query("type") String type,
            @Part MultipartBody.Part file);
}
