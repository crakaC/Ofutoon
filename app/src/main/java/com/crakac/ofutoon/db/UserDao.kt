package com.crakac.ofutoon.db

import androidx.room.*


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Delete
    fun delete(user: User)

    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM user WHERE id = :id")
    fun get(id: Int): User

    @Query("SELECT * FROM user WHERE name = :name AND domain = :domain")
    fun select(name: String, domain: String): User?

    @Update
    fun update(user: User)
}