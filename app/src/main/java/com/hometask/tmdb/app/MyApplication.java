package com.hometask.tmdb.app;

import android.app.Application;

import com.hometask.tmdb.app.di.ApplicationComponent;
import com.hometask.tmdb.app.di.DaggerApplicationComponent;
import com.hometask.tmdb.app.di.ApplicationModule;


public class MyApplication extends Application {

    private static ApplicationComponent applicationComponent;

      public MyApplication(){

      }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                                .builder()
                                .applicationModule(new ApplicationModule(this))
                                .build();

    }

    public ApplicationComponent getApplicationComponent(){
          return applicationComponent;
      }

}
