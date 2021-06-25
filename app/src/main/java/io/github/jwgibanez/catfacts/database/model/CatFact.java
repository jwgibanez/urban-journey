package io.github.jwgibanez.catfacts.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "catfact")
public class CatFact {
    @NonNull
    @PrimaryKey
    public String id;
    public String url;
    public String fact;
    @ColumnInfo(name = "creation_date")
    public Date creationDate = new Date(System.currentTimeMillis());
}
