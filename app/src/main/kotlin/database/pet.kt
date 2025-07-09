package com.equiposeis.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_table")
data class Pet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petName: String,
    val petBreed: String,
    val ownerName: String,
    val phoneNumber: String,
    val symptoms: String
)
