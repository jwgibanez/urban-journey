package io.github.jwgibanez.catfacts.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.jwgibanez.catfacts.database.dao.CatFactDao;
import io.github.jwgibanez.catfacts.database.model.CatFact;

@Database(entities = {CatFact.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static String DB_NAME = "app-db";
    private static final int NUMBER_OF_THREADS = 1;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public abstract CatFactDao catFactDao();
}