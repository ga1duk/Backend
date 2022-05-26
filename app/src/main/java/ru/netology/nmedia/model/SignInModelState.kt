package ru.netology.nmedia.model

data class SignInModelState(
    val networkError: Boolean = false,
    val emptyFieldsError: Boolean = false,
    val loginOrPassError: Boolean = false,
    val unknownError: Boolean = false
)