package com.udacity.nanodegree.politicalpreparedness.network.models

import com.udacity.nanodegree.politicalpreparedness.network.models.AdministrationBody

data class State (
    val name: String,
    val electionAdministrationBody: AdministrationBody
)