package com.example.myfirstapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchresultsActivity extends AppCompatActivity{

    private JSONObject nearbyobj;
    private JSONArray nearbyarray;
    private JSONArray firstpageresults;
    private JSONArray nextpageresults;
    private JSONArray lastpageresults;
    private int setpage = 0;
    private int totalpage = 1;
    private String nextpage;
    private String nextpage1;
    private Button nextBtn;
    private Button preBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        nextBtn = (Button)findViewById(R.id.next);
        preBtn = (Button) findViewById(R.id.previous);

        try {
            if(setpage == 0){
                setpage = 1;
                Intent intent = getIntent();
                String nearbyjson = intent.getStringExtra("nearbyjson");
                nearbyobj = new JSONObject(nearbyjson);
                nearbyarray = nearbyobj.getJSONArray("nearbyresults");
                firstpageresults = nearbyarray;
                nextpage = nearbyobj.getString("nextpage");
                if(nextpage.equals("none")){
                    nextBtn.setEnabled(false);
                }else{
                    nextBtn.setEnabled(true);
                    totalpage = 2;
                }
            }
            resTable();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("setpage:"+setpage);
        System.out.println("totalpage:"+totalpage);

        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setpage == 2 && totalpage == 3){
                    nearbyarray = firstpageresults;
                    setpage = 1;
                    totalpage = 2;
                    System.out.println("setpage:"+setpage);
                    System.out.println("totalpage:"+totalpage);
                    try {
                        resTable();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(setpage == 3) {
                    nearbyarray = nextpageresults;
                    setpage = 2;
                    totalpage = 3;
                    System.out.println("setpage:"+setpage);
                    System.out.println("totalpage:"+totalpage);
                    try {
                        resTable();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setpage == 1 && totalpage == 2){
                    final String nextpageurl = "http://xinyuw.us-east-2.elasticbeanstalk.com/?next=true&pagetoken="+ URLEncoder.encode(nextpage);
                    RequestQueue nextqueue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest nextRequest = new JsonObjectRequest
                            (Request.Method.GET, nextpageurl, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject responsenext) {
                                    System.out.println("nextpageurl:" + nextpageurl);
                                    try {
                                        setpage = 2;
                                        nearbyarray = responsenext.getJSONArray("nextpageresults");
                                        nextpageresults = nearbyarray;
                                        nextpage1 = responsenext.getString("nextpage");
                                        System.out.println("nextpage1:"+nextpage1);
                                        resTable();
                                        if(nextpage1.equals("none")){
                                            nextBtn.setEnabled(false);
                                        }else{
                                            nextBtn.setEnabled(true);
                                            totalpage = 3;
                                        }
                                        System.out.println("setpage:"+setpage);
                                        System.out.println("totalpage:"+totalpage);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("WRONG");
                                }
                            });
                    nextqueue.add(nextRequest);
                }else if(setpage == 2 && totalpage == 3){
                    final String lastpageurl = "http://xinyuw.us-east-2.elasticbeanstalk.com/?next=true&pagetoken="+ URLEncoder.encode(nextpage1);
                    RequestQueue lastqueue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest lastRequest = new JsonObjectRequest
                            (Request.Method.GET, lastpageurl, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject responselast) {
                                    System.out.println("lastpageurl:" + lastpageurl);
                                    try {
                                        setpage = 3;
                                        nextBtn.setEnabled(false);
                                        nearbyarray = responselast.getJSONArray("nextpageresults");
                                        lastpageresults = nearbyarray;
                                        resTable();
                                        System.out.println("setpage:"+setpage);
                                        System.out.println("totalpage:"+totalpage);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("WRONG");
                                }
                            });
                    lastqueue.add(lastRequest);
                }
            }
        });
    }

    private void resTable() throws JSONException {
        ArrayList<Table> mlist = new ArrayList<Table>();
        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;
        TableAdapter mAdapter;

        for(int i=0; i<nearbyarray.length(); i++){
            JSONObject near = (JSONObject)nearbyarray.get(i);
            String nearicon = near.getString("icon");
            String nearname = near.getString("name");
            String nearvicinity = near.getString("vicinity");
            String placeid = near.getString("place_id");
            mlist.add(new Table());
            mlist.get(i).setIcon(nearicon);
            mlist.get(i).setName(nearname);
            mlist.get(i).setVicinity(nearvicinity);
            mlist.get(i).setPlaceid(placeid);
        }

        mRecyclerView = (RecyclerView)findViewById(R.id.tablelist);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TableAdapter(mlist);
        mRecyclerView.setAdapter(mAdapter);

        if(setpage == 1 || setpage == 0){
            preBtn.setEnabled(false);
        }else{
            preBtn.setEnabled(true);
        }
        if(totalpage>=2){
            nextBtn.setEnabled(true);
        }
        if(setpage == 3){
           nextBtn.setEnabled(false);
        }
    }

}
