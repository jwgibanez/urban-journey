package io.github.jwgibanez.catfacts.di;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import io.github.jwgibanez.catfacts.database.AppDatabase;

import static io.github.jwgibanez.catfacts.database.AppDatabase.DB_NAME;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Singleton
    @Provides
    public static AppDatabase provideCatDatabase(@ApplicationContext Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DB_NAME).build();
    }
}
