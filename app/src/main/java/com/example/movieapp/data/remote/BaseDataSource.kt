package com.example.movieapp.data.remote

import android.content.Context
import com.example.movieapp.R
import com.example.movieapp.utils.Resource
import retrofit2.Response

abstract class BaseDataSource(private val context: Context) {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val result = call()
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) return Resource.success(body)
            }
            Resource.error(context.getString(R.string.network_call_failed))
        } catch (e: Exception) {
            Resource.error(context.getString(R.string.network_call_failed))
        }
    }
}
