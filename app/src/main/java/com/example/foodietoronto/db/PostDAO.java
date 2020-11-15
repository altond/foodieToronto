package com.example.foodietoronto.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDAO {
    @Query("SELECT * FROM Post")
    List<Post> getAllUsers();

    @Insert
    void insertUser(Post... posts);

    @Delete
    void delete(Post post);
}
