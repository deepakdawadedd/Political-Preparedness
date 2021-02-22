package com.udacity.nanodegree.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Division(
        val id: String,
        val country: String,
        val state: String
) : Parcelable {

    fun toFormattedString(): String {
        var output = country.plus("\n")
        if (state.isNotBlank()) {
            output = output.plus(state).plus("\n")
        }
        return output
    }
}