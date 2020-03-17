package com.gelirgwenntintur.regex.entity

import java.io.Serializable

data class Hashtag (
    val hashtagId : String = "",
    val hashtag : String = "",
    val followCount: Int = 0
) : Serializable