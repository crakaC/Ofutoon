package com.crakac.ofutoon.db

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        val DATABASE_NAME = "ofutodon.db"
        private var mInstance: AppDatabase? = null
        val instance get() = mInstance!!
        fun init(context: Context) {
            mInstance = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }

        private val executor = Executors.newSingleThreadExecutor()
        private val handler = Handler(Looper.getMainLooper())
        fun execute(runnable: () -> Unit) {
            executor.execute(runnable)
        }

        fun uiThread(runnable: () -> Unit) {
            handler.post(runnable)
        }
    }

    abstract fun userDao(): UserDao
}