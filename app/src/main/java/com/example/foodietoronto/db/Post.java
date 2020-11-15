package com.example.foodietoronto.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "food_name")
    public String foodName;

    @ColumnInfo(name = "rest_name")
    public String restName;

    @ColumnInfo(name = "food_price")
    public String foodPrice;

    @ColumnInfo(name = "rest_address")
    public String restAdr;
}
