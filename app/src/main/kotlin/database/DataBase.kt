package com.equiposeis.database
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Pet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
}