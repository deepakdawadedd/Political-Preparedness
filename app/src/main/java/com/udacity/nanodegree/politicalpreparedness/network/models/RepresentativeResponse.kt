package com.udacity.nanodegree.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass
import com.udacity.nanodegree.politicalpreparedness.representative.model.Representative


@JsonClass(generateAdapter = true)
data class RepresentativeResponse(
        val offices: List<Office>,
        val officials: List<Official>
)

fun RepresentativeResponse.mapResponseToRepresentatives(): List<Representative> {
    return officials.mapIndexed { index, official ->
        Representative(
                official,
                offices.first { it.officials.contains(index) }
        )
    }
}