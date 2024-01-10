package com.randy.plantapp.response

import com.google.gson.annotations.SerializedName
import com.randy.plantapp.model.User

data class BasePlantResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: UserResponse,

	@field:SerializedName("avatars")
	val avatars: List<AvatarResponse>,
)

data class AvatarResponse (
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("avatarUrl")
	val avatarUrl: String,
)

data class UserResponse(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("avatarUrl")
	val avatarUrl: String,

)

fun UserResponse.asExternalModel() = User(
    id = id,
    username = username,
    email = email,
    password = password,
    avatarUrl = avatarUrl

)