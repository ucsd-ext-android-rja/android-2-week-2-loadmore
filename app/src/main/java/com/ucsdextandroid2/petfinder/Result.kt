package com.ucsdextandroid2.petfinder

/**
 * Created by rjaylward on 2019-07-12
 */

sealed class ApiResult<T> {
    abstract fun <R> map(mapper: (T) -> R): ApiResult<R>
    abstract val data: T?
}
class ApiSuccess<T>(override val data: T): ApiResult<T>() {
    override fun <R> map(mapper: (T) -> R): ApiSuccess<R> = ApiSuccess(mapper(data))
}

class ApiFail<T>(val cause: Throwable): ApiResult<T>() {
    override val data: T?
        get() = null

   override fun <R> map(mapper: (T) -> R): ApiFail<R> = ApiFail(cause)

}