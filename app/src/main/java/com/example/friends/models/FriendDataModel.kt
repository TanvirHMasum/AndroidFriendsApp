package com.example.friends.models

data class FriendData(
    val results: List<FriendInfo>
)

data class FriendInfo(
    val name: FriendsFullName,
    val location: FriendsLocation,
    val email: String,
    val phone: String,
    val picture: FriendsPortrait
)

data class FriendsFullName(
    val title: String,
    val first: String,
    val last: String
)

data class FriendsPortrait(
    val large: String
)

data class FriendsLocation(
    val city: String,
    val state: String,
    val country: String
)