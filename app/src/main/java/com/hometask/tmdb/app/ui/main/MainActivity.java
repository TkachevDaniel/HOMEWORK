package com.hometask.tmdb.app.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.hometask.tmdb.app.R;
import com.hometask.tmdb.app.adapter.MoviesAdapter;
import com.hometask.tmdb.app.models.MovieResponse;

///////////
//////////

public class MainActivity extends AppCompatActivity implements MainViewInterface {

    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    //Added in Part 2 of the series
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String TAG = "MainActivity";
    RecyclerView.Adapter adapter;
    MainPresenter mainPresenter;

    private Dialog dialog;

    RadioButton TopButton;
    RadioButton PopularButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ////////////
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_sort_dialog);


        TopButton = dialog.findViewById(R.id.TopRadioButton);
        PopularButton = dialog.findViewById(R.id.PopularRadioButton);


        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            ////Toast.makeText(MainActivity.this, "!!!!", Toast.LENGTH_LONG).show();
            rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rvMovies.setLayoutManager(new GridLayoutManager(this, 4));
        }

        rvMovies.smoothScrollToPosition(0);
        //rvMovies.setItemAnimator(new DefaultItemAnimator());
        //rvMovies.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        ///////////
        setupMVP();
        setupViews();
        getMovieList();
    }


    /////////////////
    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }
    /////////////////


    private boolean haveNetwork(){
        boolean WIFI = false;
        boolean MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info:networkInfos)
        {
            if(info.getTypeName().equalsIgnoreCase("WIFI"))
                if(info.isConnected())
                    WIFI=true;
            if(info.getTypeName().equalsIgnoreCase("MOBILE"))
                if(info.isConnected())
                    MobileData=true;
        }
        return MobileData || WIFI;
    }


    private void setupMVP() {
        mainPresenter = new MainPresenter(this);
    }

    private void setupViews(){
        //Added in Part 2 of the series
        setSupportActionBar(toolbar);
        //rvMovies.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMovieList() {

     mainPresenter.getTopMovies();

    }



    @Override
    public void showToast(String str) {
        Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void displayMovies(MovieResponse movieResponse) {
        if(movieResponse!=null) {
            Log.d(TAG,movieResponse.getResults().get(1).getTitle());
            adapter = new MoviesAdapter(movieResponse.getResults(), MainActivity.this);
            rvMovies.setAdapter(adapter);
        }else{
            Log.d(TAG,"Movies response null");
        }
    }

    @Override
    public void displayError(String e) {

        //showToast(e);
        mainPresenter.getTopMovies();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        dialog.show();
        dialog.setCancelable(false);

        Button button = (Button) dialog.findViewById(R.id.buttonOK);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                if(TopButton.isChecked()){
                    mainPresenter.getTopMovies();
                }
                else if(PopularButton.isChecked()){
                    mainPresenter.getPopularMovies();
                }
                dialog.hide();
            }
        });

        return false;
    }
}
