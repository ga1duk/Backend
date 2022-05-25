package ru.netology.nmedia.model

data class SignUpModelState(
    val networkError: Boolean = false,
    val emptyFieldsError: Boolean = false,
    val loginIsOccupiedError: Boolean = false
)
