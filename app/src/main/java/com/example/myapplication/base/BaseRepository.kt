package com.example.myapplication.base

import com.example.myapplication.utils.Result
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseRepository {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): Result<T> {

        var result: Result<T> = Result.InProgress
        try {
            val response = call.invoke()

            response?.let {
                it.body()?.let { body ->
                    result = handleSuccess(body)
                }
                it.errorBody()?.let { responseErrorBody ->
                    if (responseErrorBody is HttpException) {
                        responseErrorBody.response()?.code()?.let { errorCode ->
                            result = handleException(errorCode)
                        }
                    } else result = handleException(GENERAL_ERROR_CODE)
                }
            }
        } catch (exception: Exception) {
            result = handleException(GENERAL_ERROR_CODE)
        } catch (httpException: HttpException) {
            result = handleException(httpException.code())
        }

        return result
    }

    companion object {
        private const val UNAUTHORIZED = "Unauthorized"
        private const val NOT_FOUND = "Not found"
        const val SOMETHING_WRONG = "Something went wrong"

        const val GENERAL_ERROR_CODE = 499

        fun <T : Any> handleSuccess(data: T): Result<T> {
            return Result.Success(data)
        }

        fun <T : Any> handleException(code: Int): Result<T> {
            val exception = getErrorMessage(code)
            return Result.Error(Exception(exception))
        }

        private fun getErrorMessage(httpCode: Int): String {
            return when (httpCode) {
                401 -> UNAUTHORIZED
                404 -> NOT_FOUND
                else -> SOMETHING_WRONG
            }
        }
    }
}