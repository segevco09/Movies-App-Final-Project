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

    emit(Resource.loading()) // ✅ הצגת מצב טעינה ל-UI

    val source = localDbFetch().map { Resource.success(it) }
    emitSource(source) // ✅ הצגת נתונים מקומיים תחילה

    val fetchResource = remoteDbFetch() // ✅ קריאה ל-API

    if (fetchResource is Resource.Success) {
        val newData = fetchResource.data!!
        localDbSave(newData) // ✅ שמירה ל-DB (כעת שומר נתונים קיימים)
    } else if (fetchResource is Resource.Error) {
        emit(Resource.error(fetchResource.message!!))
        emitSource(source) // ✅ הצגת נתונים מקומיים במקרה של שגיאה
    }
}
