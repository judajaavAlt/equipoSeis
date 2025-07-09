package com.equiposeis.database
import androidx.room.*

@Dao
interface PetDao {

    @Insert
    suspend fun insert(pet: Pet): Long

    @Update
    suspend fun update(pet: Pet)

    @Query("SELECT * FROM pet_table")
    suspend fun getAllPets(): List<Pet>

    @Query("SELECT * FROM pet_table WHERE id = :petId")
    suspend fun getPetById(petId: Int): Pet?

    @Delete
    suspend fun delete(pet: Pet)
}