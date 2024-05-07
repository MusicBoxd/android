package musicboxd.android.data.remote.api

sealed class APIResult<T> {
    data class Success<T>(val data: T) : APIResult<T>()
    data class Failure<T>(val message: String) : APIResult<T>()
}