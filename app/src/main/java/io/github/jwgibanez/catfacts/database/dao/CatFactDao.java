package io.github.jwgibanez.catfacts.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.github.jwgibanez.catfacts.database.model.CatFact;

@Dao
public interface CatFactDao {
    @Query("SELECT * FROM catfact ORDER BY creation_date DESC")
    LiveData<List<CatFact>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CatFact... facts);

    @Delete
    void delete(CatFact fact);
}
