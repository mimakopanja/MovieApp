package com.mirjanakopanja.movieapp.data.cast

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Actor(
    val also_known_as: List<String>? = listOf(),
    val biography: String? = "",
    val birthday: String? = "",
    val id: Int? = 1,
    val known_for_department: String? = "",
    val name: String? = "",
    val place_of_birth: String? = "",
    val popularity: Double? = 5.5,
    val profile_path: String? = ""
): Parcelable