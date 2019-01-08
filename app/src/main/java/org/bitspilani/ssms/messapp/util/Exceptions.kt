package org.bitspilani.ssms.messapp.util

/**
 * Thrown when the cache is empty and the remote data source can't be reached.
 * */
class NoDataSourceException(message: String) : Exception(message)

/**
 * Thrown when the user's details can't be retrieved from the cache.
 * */
class NoLoggedUserException(message: String) : Exception(message)

/**
 * Thrown when the device doesn't have a connection to the internet.
 * */
class NoConnectionException(message: String) : Exception(message)

/**
 * Thrown when the user can't be logged in with his/her credentials.
 * */
class InvalidCredentialsException(message: String) : Exception(message)