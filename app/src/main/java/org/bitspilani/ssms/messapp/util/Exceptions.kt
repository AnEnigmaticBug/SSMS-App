package org.bitspilani.ssms.messapp.util

/**
 * Thrown when the cache is empty and the remote data source can't be reached.
 * */
class NoDataSourceException : Exception()

/**
 * Thrown when the user's details can't be retrieved from the cache.
 * */
class NoLoggedUserException : Exception()

/**
 * Thrown when the device doesn't have a connection to the internet.
 * */
class NoConnectionException : Exception()

/**
 * Thrown when  the backend-server  responds with a 5XX status code.
 * */
class ErrorCode5XXException : Exception()

/**
 * Thrown when  the backend-server  responds with a 4XX status code.
 * */
class ErrorCode4XXException(message: String) : Exception(message)

/**
 * Thrown when the app  should be restarted to avoid inconsistent information.
 * */
class UnknownStateException : Exception()