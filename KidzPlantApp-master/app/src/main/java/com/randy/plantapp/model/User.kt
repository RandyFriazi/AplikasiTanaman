package com.randy.plantapp.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val avatarUrl: String,
)
