package com.udacity.nanodegree.politicalpreparedness.network

import androidx.lifecycle.LiveData
import com.udacity.nanodegree.politicalpreparedness.network.models.Address
import com.udacity.nanodegree.politicalpreparedness.network.models.Election
import com.udacity.nanodegree.politicalpreparedness.network.models.VoterInfoResponse
import com.udacity.nanodegree.politicalpreparedness.representative.model.Representative


/**
 * Created by @author Deepak Dawade on 2/22/2021 at 2:01 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
interface CivicsDataSource {
    val electionsFollowed: LiveData<List<Election>>
    val electionsUpcoming: LiveData<List<Election>>

    suspend fun getRepresentatives(address: Address): Result<List<Representative>>
    suspend fun refreshElectionsData()
    suspend fun updateElection(election: Election)
    suspend fun getElectionById(id: Int): Election
    suspend fun deleteElection(election: Election)
    suspend fun clear()
    suspend fun getVoterInfo(election: Election): Result<VoterInfoResponse>


}