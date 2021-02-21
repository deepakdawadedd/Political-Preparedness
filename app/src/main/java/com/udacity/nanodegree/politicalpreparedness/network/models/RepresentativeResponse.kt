package com.udacity.nanodegree.politicalpreparedness.network.models

import com.udacity.nanodegree.politicalpreparedness.representative.model.Representative

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