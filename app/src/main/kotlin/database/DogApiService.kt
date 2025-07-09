package com.equiposeis.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

data class BreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)

data class RandomImageResponse(
    val message: String,  // the image URL
    val status: String
)

interface DogApiService {
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): BreedsResponse

    @GET
    suspend fun getRandomImageForBreed(@Url url: String): RandomImageResponse
}