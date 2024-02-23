package com.code.paridhan.RetrofitAPI;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("VersionList")
    Call<String> VersionList();

    @Headers("Content-Type: application/json")
    @POST("LoginService")
    Call<String> LoginService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetBanner")
    Call<String> GetBanner(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getrootcategorymaster")
    Call<String> getrootcategorymaster(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("DashboardList")
    Call<String> DashboardList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getCategorymaster")
    Call<String> getCategorymaster(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("getSubCategorymaster")
    Call<String> getSubCategorymasterAPI(
            @Body String body);




    @Headers("Content-Type: application/json")
    @POST("CustomerRegistration")
    Call<String> CustomerRegistration(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("LoginCustomer")
    Call<String> LoginCustomer(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProductListbycategoryid")
    Call<String> ProductListbycategoryid(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProductDetails")
    Call<String> ProductDetails(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Addtocart")
    Call<String> Addtocart(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("CustomerProfile")
    Call<String> CustomerProfile(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("ViewCartList")
    Call<String> ViewCartList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("OrderList")
    Call<String> OrderList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("UpdateCartdetail")
    Call<String> UpdateCartdetail(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("DeleteCartdetail")
    Call<String> DeleteCartdetail(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("State_CityList")
    Call<String> State_CityList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("InsertUpdateCustomerAddress")
    Call<String> CustomerAddress(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("OrderPlaced")
    Call<String> OrderPlaced(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("OrderItemDetails")
    Call<String> OrderItemDetails(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GenerateOrderToken")
    Call<String> GenerateOrderToken(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("NotificationList")
    Call<String> NotificationList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProductSearch")
    Call<String> ProductSearch(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("UpdateCustomerProfile")
    Call<String> UpdateCustomerProfile(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("OrderSuccess")
    Call<String> OrderSuccess(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("AddtowishList")
    Call<String> AddtowishList(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("GetWishListByCustomerID")
    Call<String> GetWishListByCustomerID(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("OfferDetails")
    Call<String> OfferDetails(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("OrderCancel")
    Call<String> OrderCancelAPI(
            @Body String body);


}


