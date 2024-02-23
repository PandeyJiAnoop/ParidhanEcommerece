package com.code.paridhan.basic;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.code.paridhan.BuildConfig;
import com.code.paridhan.R;
import com.code.paridhan.order.OfferActivity;
import com.code.paridhan.order.OrderListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.code.paridhan.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    LinearLayout llProfile;
    ImageView cart,ivSearch,notifications,menu;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        llProfile = findViewById(R.id.llProfile);
        cart =findViewById(R.id.cart);
        ivSearch = findViewById(R.id.ivSearch);
        notifications = findViewById(R.id.notifications);
        menu = findViewById(R.id.menu);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }});

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SerachActivity.class));
            }});

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificationList.class));
            }});

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

/*
        findViewById(R.id.hamburger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout .isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }}});
*/
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);


        Drawable background = getResources().getDrawable(R.drawable.gradient_home, getTheme());
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent, getTheme()));
        }
        getWindow().setBackgroundDrawable(background);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        switchfragment(fragment);
                        break;
                    case R.id.cart:
                        cart();
                        break;
                    case R.id.shopping_bag:
                        my_orders();
                        break;
                    case R.id.llCategory:
                        startActivity(new Intent(getApplicationContext(),CategoryActivity.class));
                        break;
                    case R.id.message:
                        break;
                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(),CustomerProfileActivity.class));
                        break;
                }
                return false;
            }});
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.home); // change to whichever id should be default
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.all_categories:
                startActivity(new Intent(getApplicationContext(),ChangePasswordActivity.class));
                //all_category();
                break;
            case R.id.orders:
               my_orders();
                break;
            case R.id.MyWishlist:
                startActivity(new Intent(getApplicationContext(),WishlistActivity.class));
                break;
            case R.id.Rate:
                goToMyApp();
                break;
            case R.id.offer:
                startActivity(new Intent(getApplicationContext(),OfferActivity.class));
               /* break;*/
            case R.id.notifications:
                startActivity(new Intent(getApplicationContext(),OfferActivity.class));
                break;
            case R.id.privacy_policy:
                String url = "http://shubhparidhaan.net/TermsAndCondition/PrivacyPolicy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
                break;
            case R.id.share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.logout:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage(getString(R.string.aresurelogout));
                alertDialogBuilder.setPositiveButton(getString(R.string.Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent I = new Intent(getApplicationContext(), SplashActivity.class);
                                startActivity(I);
                                arg0.dismiss();
                                onResume();
                            }});
                alertDialogBuilder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    void cart()
    {
        Intent intent = new Intent(this,CartActivity.class);
        startActivity(intent);
    }
    void my_orders()
    {
        Intent intent = new Intent(this, OrderListActivity.class);
        startActivity(intent);
    }
   /*

    void all_category()
    {
        Intent intent = new Intent(this, AllCategoryActivity.class);
        startActivity(intent);
    }

    void my_orders()
    {
        Intent intent = new Intent(this, MyOrdersActivity.class);
        startActivity(intent);
    }

  */
   void switchfragment(Fragment fragment)
    {
    FragmentManager manager = getSupportFragmentManager();
    manager.beginTransaction().replace(R.id.framelayout,fragment).commit();
   }

    public void goToMyApp() {//true if Google Play, false if Amazone Store
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));

    }

    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder ab = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        ab.setMessage("are you sure to exit?");
        ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //if you want to kill app . from other then your main avtivity.(Launcher)
              /*android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);*/
                finishAffinity();

            }
        });
        ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();
    }
}