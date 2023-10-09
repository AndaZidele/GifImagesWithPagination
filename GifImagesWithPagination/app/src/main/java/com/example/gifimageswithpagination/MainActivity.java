package com.example.gifimageswithpagination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gifimageswithpagination.Adapters.SearchedImagesAdapter;
import com.example.gifimageswithpagination.Models.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchedImagesAdapter.OnItemClickListener {

    String url;
    SearchView searchView;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SearchedImagesAdapter adapter;
    List<Image> imageList = new ArrayList<>();

    //for pagination
    Boolean scrolling = false;
    int currentItems, totalItems, firstVisibleItemPosition;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = "https://api.giphy.com/v1/gifs/trending?api_key=3ghe3gbyFL4LbYLdYSSgjWFpNCxA39nr";// + page;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_search);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        searchView = (SearchView) findViewById(R.id.search_view);

        progressBar = (ProgressBar) findViewById(R.id.progressB);

        getAllImages();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                scrollingFunc(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1500);
                return true;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    scrolling = true;
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();


                if(scrolling && (currentItems+firstVisibleItemPosition==totalItems)){ //ja ekrānā redzamo att skaits plus pirmā redzamā attēla pozīcija ir vienāda ar kopējo att skaitu, tad:
                    if(searchView.getQuery().toString()==""){
                        scrollingFunc("");
                    }
                    else {
                        scrollingFunc((String) searchView.getQuery().toString());
                    }
                    scrolling = false;
                }
            }
        });
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String newText = searchView.getQuery().toString();
            filter(newText);
        }
    };

    private void scrollingFunc(String newText) {
        progressBar.setVisibility(View.VISIBLE);
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String gifName = newText.toLowerCase();
                        boolean boo = newText == gifName;

                        try {
                            JSONArray dataArray = response.getJSONArray("data");


                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);

                                JSONObject obj1 = obj.getJSONObject("images");
                                JSONObject obj2 = obj1.getJSONObject("downsized_medium");
                                String sourceUrl = obj2.getString("url");
                                String imgTitle = obj.getString("title");

                                if (imgTitle.toString().toLowerCase().contains(gifName)) {
                                    imageList.add(new Image(sourceUrl));
                                }
                            }
                            adapter = new SearchedImagesAdapter(MainActivity.this, imageList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);
                            adapter.setOnItemClickListener(MainActivity.this::onItemClick);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Kluda " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Requests.getInstance(getApplicationContext()).addToRequestQueue(objectRequest);
    }


    private void filter(String newText) {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                imageList = new ArrayList<>();
                String gifName = newText.toLowerCase();
                try {
                    JSONArray dataArray = response.getJSONArray("data");


                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject obj = dataArray.getJSONObject(i);
                        JSONObject obj1 = obj.getJSONObject("images");
                        JSONObject obj2 = obj1.getJSONObject("downsized_medium");
                        String sourceUrl = obj2.getString("url");
                        String imgTitle = obj.getString("title");
                        if (imgTitle.toString().toLowerCase().contains(gifName)) {
                            imageList.add(new Image(sourceUrl));
                        }
                    }
                    adapter = new SearchedImagesAdapter(MainActivity.this, imageList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    adapter.setOnItemClickListener(MainActivity.this::onItemClick);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Kluda " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Requests.getInstance(this).addToRequestQueue(objectRequest);
    }


    private void getAllImages(){
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("data");

                    for (int i=0; i<dataArray.length();i++){
                        JSONObject obj = dataArray.getJSONObject(i);
                        JSONObject obj1 = obj.getJSONObject("images");
                        JSONObject obj2 = obj1.getJSONObject("downsized_medium");
                        String sourceUrl = obj2.getString("url");
                        imageList.add(new Image(sourceUrl));
                    }
                    adapter = new SearchedImagesAdapter(MainActivity.this, imageList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    adapter.setOnItemClickListener(MainActivity.this::onItemClick);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Kluda "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Requests.getInstance(this).addToRequestQueue(objectRequest);
    }


    @Override
    public void onItemClick(int pos) {
        Intent oneImg = new Intent(this, OneImageActivity.class);
        Image clickedItem = imageList.get(pos);
        oneImg.putExtra("oneImg",clickedItem.getImgUrl());
        startActivity(oneImg);
    }
}

