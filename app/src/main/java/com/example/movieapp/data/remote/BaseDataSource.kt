package com.example.movieapp.data.remote

import com.example.movieapp.utils.Resource
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val result = call()
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) return Resource.success(body)
            }
            Resource.error("Network call failed: ${result.message()} ${result.code()}")
        } catch (e: Exception) {
            Resource.error("Network call failed: ${e.localizedMessage ?: e.toString()}")
        }
    }
}
