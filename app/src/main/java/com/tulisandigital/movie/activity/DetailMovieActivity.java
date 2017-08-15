package com.tulisandigital.movie.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tulisandigital.movie.AppVar;
import com.tulisandigital.movie.BaseActivity;
import com.tulisandigital.movie.R;
import com.tulisandigital.movie.model.Movies;

public class DetailMovieActivity extends BaseActivity {
    AlertDialogManager alert = new AlertDialogManager();
    private Movies movies;
    private TextView title;
    private TextView overview;
    private TextView vote_average;
    private TextView release_date;
    private TextView popularity;
    private TextView vote_count;
    private ImageView gambar_movies;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* this.setTitle(String.format(title.toString()));*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        movies = (Movies) getIntent().getSerializableExtra("movie");
         popularity = (TextView) findViewById(R.id.popularity);
         title = (TextView) findViewById(R.id.title);
         vote_count = (TextView) findViewById(R.id.vote_count);
         release_date = (TextView) findViewById(R.id.release_date);
         vote_average = (TextView) findViewById(R.id.vote_average);
         overview = (TextView) findViewById(R.id.overview);
         title.setText(movies.getTitle());
         /*release_date.setText(movies.getRelease_date());*/
         String [] dateParts = movies.getRelease_date().split("-");
         String day = dateParts[0];
         release_date.setText(day);
         int param_count = (int) movies.getVote_count();
         String convertcount = String.valueOf(param_count);
         vote_count.setText(convertcount);
         double param_popularity = (double) movies.getPopularity();
         String convertpopularity = String.valueOf(param_popularity);
         popularity.setText(convertpopularity);
         float param = (float) movies.getVote_average();
         String convertparam = String.valueOf(param);
         vote_average.setText(convertparam);
         overview.setText(movies.getOverview());
        gambar_movies = (ImageView) findViewById(R.id.gambar_movie);
        /*show back button on toolbar area*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/
        Picasso.with(this).load(AppVar.BASE_IMAGE+movies.getPoster_path()).into(gambar_movies);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.header,menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.popular) {
            backtofrontend();
        }
        if (id == R.id.rated) {
            Intent Rated = new Intent(this, RatedActivity.class);
            startActivity(Rated);
        }
        if (id == R.id.exitapp) {
            exit();
        }
        return super.onOptionsItemSelected(item);
    }
    private void backtofrontend(){
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
                        alert.showAlertDialog(DetailMovieActivity.this, "Menutup Program....", "Silahkan Tunggu...", false);
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
}
