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

    emit(Resource.loading())

    val source = localDbFetch().map { Resource.success(it) }
    emitSource(source)

    val fetchResource = remoteDbFetch()

    if (fetchResource is Resource.Success) {
        val newData = fetchResource.data!!
        localDbSave(newData)
    } else if (fetchResource is Resource.Error) {
        emit(Resource.error(fetchResource.message!!))
        emitSource(source)
    }
}
