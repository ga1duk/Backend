package ru.netology.nmedia.model

data class SignUpModelState(
    val networkError: Boolean = false,
    val emptyFieldsError: Boolean = false,
    val loginOrPassError: Boolean = false,
    val unknownError: Boolean = false,
    val passwordsNotMatchError: Boolean = false
)
