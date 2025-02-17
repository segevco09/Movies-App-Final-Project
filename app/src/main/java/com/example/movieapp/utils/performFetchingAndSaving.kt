package com.example.movieapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

fun <T, A> performFetchingAndSaving(
    localDbFetch: () -> LiveData<T>,
    remoteDbFetch: suspend () -> Resource<A>,
    localDbSave: suspend (A) -> Unit
): LiveData<Resource<T>> = liveData(Dispatchers.IO) {

    emit(Resource.loading()) // ✅ Notify UI that loading started

    val source = localDbFetch().map { Resource.success(it) }
    emitSource(source) // ✅ Show local data first

    val fetchResource = remoteDbFetch() // ✅ Fetch remote data

    if (fetchResource is Resource.Success) {
        localDbSave(fetchResource.data!!) // ✅ Save data if successful
    } else if (fetchResource is Resource.Error) {
        emit(Resource.error(fetchResource.message!!))
        emitSource(source) // ✅ Keep showing local data
    }
}
