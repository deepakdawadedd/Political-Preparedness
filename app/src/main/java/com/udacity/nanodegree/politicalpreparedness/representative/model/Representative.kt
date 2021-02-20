package com.udacity.nanodegree.politicalpreparedness.representative.model

import com.udacity.nanodegree.politicalpreparedness.network.models.Office
import com.udacity.nanodegree.politicalpreparedness.network.models.Official


data class Representative (
        val official: Official,
        val office: Office
)