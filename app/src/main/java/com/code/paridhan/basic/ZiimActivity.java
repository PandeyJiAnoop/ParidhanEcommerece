package com.code.paridhan.basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.code.paridhan.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ZiimActivity extends AppCompatActivity {
    //Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private Matrix matrix = new Matrix();
    ViewPager ivProductPic;
    public static String Path="";
    ImageView ivcross;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziim);

        ivcross=findViewById(R.id.imageView40);
        Path=getIntent().getStringExtra("Path");
        Picasso.get().load(Path).into(ivcross);

        ivcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoViewAttacher photoAttacher;
                photoAttacher= new PhotoViewAttacher(ivcross);
                photoAttacher.update();
            }
        });




    }


}
