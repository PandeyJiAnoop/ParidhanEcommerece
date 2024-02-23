package com.code.paridhan.RetrofitAPI;
/**
 * Created by akp-639454240.
 */


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class GlobalAppApis {
    public String Register(String userid, String name, String email) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("UserId", userid);
            jsonObject1.put("Name", name);
            jsonObject1.put("Email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String Login(String mobile, String deviceid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("id", "");
            jsonObject1.put("Password", deviceid);
            jsonObject1.put("Userid", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String getrootcategorymaster() {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String getCategorymaster(String rootcategoryid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", rootcategoryid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String getSubCategorymasteraa(String rootcategoryid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CategoryId", rootcategoryid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String CustomerRegistration(String name, String mobile, String email, String password) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CustomerName", name);
            jsonObject1.put("EmailAddress", email);
            jsonObject1.put("MobileNo", mobile);
            jsonObject1.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String LoginCustomer(String email, String password) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Password", password);
            jsonObject1.put("UserName", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String ProductListbycategoryid(String CategoryId, String Userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CategoryId", CategoryId);
            jsonObject1.put("CustomerId", Userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetWishListByCustomerID(String Userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CustomerId", Userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String AddFav(String PropertyId, String CustomerId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CustomerId", CustomerId);
            jsonObject1.put("ProductId", PropertyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String OfferDetails(String CustomerId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", "1");
            jsonObject1.put("CustomerId", CustomerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String ProductDetails(String CategoryId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("ProductId", CategoryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String Addtocart(String username, String productcode, String Quantity, String color, String Size) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("ColorId", color);
            jsonObject1.put("CustomerId", username);
            jsonObject1.put("ProductId", productcode);
            jsonObject1.put("Quantity", Quantity);
            jsonObject1.put("SizeId", Size);
            Log.v("jsonObject1jsonObject1", String.valueOf(jsonObject1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String CustomerProfile(String CustomerId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CustomerId", CustomerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String ViewCartList(String CustomerId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CustomerId", CustomerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String UpdateCartdetail(String CustomerId, String quantity, String productId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CartId", productId);
            jsonObject1.put("CustomerId", CustomerId);
            jsonObject1.put("Quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String DeleteCartdetail(String CustomerId, String quantity, String productId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CartId", productId);
            jsonObject1.put("CustomerId", CustomerId);
            jsonObject1.put("Quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String OrderList(String CustomerId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CustomerId", CustomerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String State_CityList(String Action, String State) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", Action);
            jsonObject1.put("StateId", State);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String CustomerAddress(String username, String customerName, String phone, String flat, String street, String landmark, String pincode, String state, String city) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", "1");
            jsonObject1.put("CityId", city);
            jsonObject1.put("CustomerId", username);
            jsonObject1.put("CustomerName", customerName);
            jsonObject1.put("House", flat);
            jsonObject1.put("IsDefault", "1");
            jsonObject1.put("Landmark", landmark);
            jsonObject1.put("MobileNo", phone);
            jsonObject1.put("Pincode", pincode);
            jsonObject1.put("StateId", state);
            jsonObject1.put("Street", street);
            jsonObject1.put("addresstype", "Home");
            jsonObject1.put("srno", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetCustomerAddress(String username) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", "2");
            jsonObject1.put("CityId", "");
            jsonObject1.put("CustomerId", username);
            jsonObject1.put("CustomerName", "");
            jsonObject1.put("House", "");
            jsonObject1.put("IsDefault", "2");
            jsonObject1.put("Landmark", "");
            jsonObject1.put("MobileNo", "");
            jsonObject1.put("Pincode", "");
            jsonObject1.put("StateId", "");
            jsonObject1.put("Street", "");
            jsonObject1.put("addresstype", "");
            jsonObject1.put("srno", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String DeletCustomerAddress(String username, String srno) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", "4");
            jsonObject1.put("CityId", "");
            jsonObject1.put("CustomerId", username);
            jsonObject1.put("CustomerName", "");
            jsonObject1.put("House", "");
            jsonObject1.put("IsDefault", "2");
            jsonObject1.put("Landmark", "");
            jsonObject1.put("MobileNo", "");
            jsonObject1.put("Pincode", "");
            jsonObject1.put("StateId", "");
            jsonObject1.put("Street", "");
            jsonObject1.put("addresstype", "");
            jsonObject1.put("srno", srno);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String UpdateCustomerAddress(String username, String customerName, String phone, String flat, String street, String landmark, String pincode, String state, String city, String srno) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", "3");
            jsonObject1.put("CityId", city);
            jsonObject1.put("CustomerId", username);
            jsonObject1.put("CustomerName", customerName);
            jsonObject1.put("House", flat);
            jsonObject1.put("IsDefault", "1");
            jsonObject1.put("Landmark", landmark);
            jsonObject1.put("MobileNo", phone);
            jsonObject1.put("Pincode", pincode);
            jsonObject1.put("StateId", state);
            jsonObject1.put("Street", street);
            jsonObject1.put("addresstype", "Home");
            jsonObject1.put("srno", srno);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String OrderPlaced(String paymentMethod, String paymentmode, String username, String srno, String PayableAmt) {
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("Action", paymentMethod);
            jsonObject1.put("CustomerId", username);
            jsonObject1.put("ReferenceId", "");
            jsonObject1.put("addressid", srno);
            jsonObject1.put("amount", PayableAmt);
            jsonObject1.put("paymentStatus", paymentmode);
            jsonObject1.put("paymentmode", "Cash");
            jsonObject1.put("tempOrderId", "");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String OrderItemDetails(String username, String orderId) {
        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("CustomerId", username);
            jsonObject1.put("OrderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GenerateOrderToken(String username, String orderId, String productId) {
        JSONObject jsonObject1 = new JSONObject();

        try {
            /*jsonObject1.put("CustomerId", username);*/
            jsonObject1.put("orderAmount", orderId);
            jsonObject1.put("orderCurrency", "INR");
            jsonObject1.put("orderId", productId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String NotificationList(String userId) {
        JSONObject jsonObject1 = new JSONObject();

        try {
            /*jsonObject1.put("CustomerId", username);*/
            jsonObject1.put("CustomerId", userId);
            jsonObject1.put("notId", "");
            jsonObject1.put("nuId", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String ChangePassword(String userId, String toString, String toString1) {
        JSONObject jsonObject1 = new JSONObject();
        try {

            /*jsonObject1.put("CustomerId", username);*/
            jsonObject1.put("CustomerId", userId);
            jsonObject1.put("NewPassword", toString);
            jsonObject1.put("OldPassword", toString1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String ProductSearch(String ProductName, String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {

            jsonObject1.put("ProductName", ProductName);
            jsonObject1.put("CustomerID", userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String UpdateCustomerProfile(String username, String name, String contact, String email, String imgString) {
        JSONObject jsonObject1 = new JSONObject();
        try {


            jsonObject1.put("CustomerId", username);
            jsonObject1.put("CustomerName", name);
            jsonObject1.put("DOB", "");
            jsonObject1.put("EmailAddress", email);
            jsonObject1.put("MobileNo", contact);
            jsonObject1.put("Name", name);
            jsonObject1.put("Profile", imgString);
            jsonObject1.put("gender", "");
            jsonObject1.put("title", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String OrderSuccess(String userName, String status, String token, String orderid) {
        JSONObject jsonObject1 = new JSONObject();
        try {


            jsonObject1.put("CustomerId", userName);
            jsonObject1.put("Status", status);
            jsonObject1.put("TokenNo", token);
            jsonObject1.put("orderId", orderid);
            jsonObject1.put("referenceId", "");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String OrderCancel(String CustomerId,String OrderId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CustomerId", CustomerId);
            jsonObject1.put("Narration", "Narration");
            jsonObject1.put("OrderId", OrderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

}