package com.gelirgwenntintur.rxjava

data class Contact(
    val id: Int,
    val name: String,
    val status: ContactStatus
)

enum class ContactStatus {
    DEFAULT, LOADED
}
