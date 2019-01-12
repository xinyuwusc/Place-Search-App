package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class DetailsActivity extends AppCompatActivity implements Info.OnFragmentInteractionListener, Photos.OnFragmentInteractionListener, Maps.OnFragmentInteractionListener,Reviews.OnFragmentInteractionListener{

    private PagerAdapter adapter;
    private ViewPager mViewPager;
    private String placeid;
    private Info infodetail = new Info();
    private Photos photosdetail = new Photos();
    private Maps mapsdetail = new Maps();
    private Reviews reviewsdetail = new Reviews();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        adapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(4);
        setupViewPager(mViewPager);
        TabLayout detailstabLayout = (TabLayout)findViewById(R.id.detailstablayout);
        detailstabLayout.setupWithViewPager(mViewPager);


        Intent detailintent = getIntent();
        placeid = detailintent.getStringExtra("placeid");
        System.out.println("placeid:"+placeid);
        infodetail.SetId(placeid);
        photosdetail.SetId(placeid);
        reviewsdetail.SetId(placeid);

    }

    private void setupViewPager(ViewPager viewPager){
        PagerAdapter madapter = new PagerAdapter(getSupportFragmentManager());
        madapter.addFragment(infodetail,"INFO");
        madapter.addFragment(photosdetail,"PHOTOS");
        madapter.addFragment(mapsdetail,"MAPS");
        madapter.addFragment(reviewsdetail,"REVIEWS");
        viewPager.setAdapter(madapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
