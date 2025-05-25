package com.equiposeis.api

import retrofit2.http.GET

data class BreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)

interface DogApiService {
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): BreedsResponse
}