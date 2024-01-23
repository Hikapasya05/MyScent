package com.hika.myscent.base

abstract class BaseState<T> {
    abstract val isLoading: Boolean
    abstract val isError: Boolean
    abstract val errorMessage: String
    abstract val isSuccess: Boolean
    abstract val successData: T?
}