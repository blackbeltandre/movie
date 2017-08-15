package com.tulisandigital.movie.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.tulisandigital.movie.AppVar;
import com.tulisandigital.movie.BaseActivity;
import com.tulisandigital.movie.R;
import com.tulisandigital.movie.adapter.ListAdapter;
import com.tulisandigital.movie.adapter.viewholders.MovieViewHolder;
import com.tulisandigital.movie.app.AppMovie;
import com.tulisandigital.movie.model.Movies;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RatedActivity extends BaseActivity {
    AlertDialogManager alert = new AlertDialogManager();
    private static final String TAG = "MovieActivity";
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ArrayList<Movies> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        initRecycler();
        initAdapterMovies();
       /* View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
      */  showDialog("Loading...");
        if (isInternetConnectionAvailable())
            GetMovieByRating();
        else {
            Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
            hideDialog();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.header,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.popular) {
            getData();
        }
        if (id == R.id.rated) {
            GetMovieByRating();
        }
        if (id == R.id.refresh) {
            refresh();
        }
        if (id == R.id.exitapp) {
            exit();
        }
        return super.onOptionsItemSelected(item);
    }
    private void refresh(){
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }
    private void exit(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Anda ingin menutup aplikasi?");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        alert.showAlertDialog(RatedActivity.this, "Menutup Program....", "Silahkan Tunggu...", false);
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void initRecycler() {
        // bind recyclerview from layout
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //set layout orientation (Linear/GRID)
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2)); //2 itu jumlah column
        //menambahkan divider peritem
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
    }
    private void initAdapterMovies() {
        //setting adapter generic class <class_object,viewholder>(item_list,viewholder.class,object.class,list)
        listAdapter = new ListAdapter<Movies//object
                , MovieViewHolder> //view holder
                (R.layout.item_list_movie // layout item list
                        , MovieViewHolder.class // class View Holder
                        , Movies.class // class object
                        , list) {
            @Override
            protected void bindView(MovieViewHolder holder, final Movies model, int position) {
                //color background
//                if (position%2 == 0)
//                    holder.getItem().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_orange_light));
//                else
//                    holder.getItem().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_blue_bright));
                //glide
                Picasso.with(getApplicationContext())
                        .load(AppVar.BASE_IMAGE+model.getPoster_path()) // url nya
                        .into(holder.gambar_movie);//item view image nya
                /*
                //picasso
                Picasso.with(getApplicationContext())
                        .load(URLs.BASE_IMAGE+model.getPoster_path()) // url nya
                        .into(holder.gambar_movie);//item view image nya*/

               /* holder.title.setText(model.getOriginal_title());
                //parsing to String
                holder.vote_average.setText(String.valueOf(model.getVote_average()));
                holder.release_date.setText(model.getRelease_date());
*/
                holder.getItem().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(MovieActivity.this, model.getOriginal_title(), Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(RatedActivity.this,DetailMovieActivity.class);
                        in.putExtra("movie",model);
                        startActivity(in);
                    }
                });
            }
        };
        recyclerView.setAdapter(listAdapter);
    }
    public void GetMovieByRating() {
        //toolbar title automatic changed
        getSupportActionBar().setTitle("Top Rated Movies");
        //init set request api
        StringRequest request = new StringRequest(Request.Method.GET //method request (POST,GET,DELETE,PUT)
                , AppVar.URL_MOVIE_TOP_RATED //url api
                , new Response.Listener<String>() { //response api
            @Override
            public void onResponse(String response) {//response string butuh convert ke json
                try {
                    //convert string ke json
                    JSONObject parent = new JSONObject(response);
                    //contoh buat ambil data array
                    JSONArray results = parent.getJSONArray("results");
                    //kosongin list company
                    list = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject sourceParam = results.getJSONObject(i);
                        Movies datajson = new Movies();
                        datajson.setId(sourceParam.getInt("id"));
                        datajson.setTitle(sourceParam.getString("title"));
                        datajson.setOriginal_language(sourceParam.getString("original_language"));
                        datajson.setOriginal_title(sourceParam.getString("original_title"));
                        datajson.setOverview(sourceParam.getString("overview"));
                        datajson.setPopularity(sourceParam.getDouble("popularity"));
                        datajson.setPoster_path(sourceParam.getString("poster_path"));
                        datajson.setRelease_date(sourceParam.getString("release_date"));
                        datajson.setVote_average(sourceParam.getDouble("vote_average"));
                        datajson.setVote_count(sourceParam.getInt("vote_count"));
                        list.add(datajson);
                    }
                    listAdapter.swapData(list);
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { // error response
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hideDialog();
            }
        });
        //add to que request on appMovie
        AppMovie.getInstance().addToRequestQueue(request, TAG);
    }
    public void getData() {
        //toolbar title automatic changed
        getSupportActionBar().setTitle("Pop Movies");
        //init set request api
        StringRequest request = new StringRequest(Request.Method.GET //method request (POST,GET,DELETE,PUT)
                , AppVar.URL_MOVIE_550 //url api
                , new Response.Listener<String>() { //response api
            @Override
            public void onResponse(String response) {//response string butuh convert ke json
                try {
                    //convert string ke json
                    JSONObject parent = new JSONObject(response);
                    //contoh buat ambil data array
                    JSONArray results = parent.getJSONArray("results");
                    //kosongin list company
                    list = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject sourceParam = results.getJSONObject(i);
                        Movies datajson = new Movies();
                        datajson.setId(sourceParam.getInt("id"));
                        datajson.setTitle(sourceParam.getString("title"));
                        datajson.setOriginal_language(sourceParam.getString("original_language"));
                        datajson.setOriginal_title(sourceParam.getString("original_title"));
                        datajson.setOverview(sourceParam.getString("overview"));
                        datajson.setPopularity(sourceParam.getDouble("popularity"));
                        datajson.setPoster_path(sourceParam.getString("poster_path"));
                        datajson.setRelease_date(sourceParam.getString("release_date"));
                        datajson.setVote_average(sourceParam.getDouble("vote_average"));
                        datajson.setVote_count(sourceParam.getInt("vote_count"));
                        list.add(datajson);
                    }
                    listAdapter.swapData(list);
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { // error response
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hideDialog();
            }
        });
        //add to que request on appMovie
        AppMovie.getInstance().addToRequestQueue(request, TAG);
    }
}
