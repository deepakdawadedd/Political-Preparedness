package com.udacity.nanodegree.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass
import com.udacity.nanodegree.politicalpreparedness.network.models.Address

@JsonClass(generateAdapter = true)
data class AdministrationBody (
        val name: String? = null,
        val electionInfoUrl: String? = null,
        val votingLocationFinderUrl: String? = null,
        val ballotInfoUrl: String? = null,
        val correspondenceAddress: Address? = null
)