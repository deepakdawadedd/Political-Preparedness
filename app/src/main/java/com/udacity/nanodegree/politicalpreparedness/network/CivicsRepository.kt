package com.udacity.nanodegree.politicalpreparedness.network

import androidx.lifecycle.LiveData
import com.udacity.nanodegree.politicalpreparedness.database.ElectionDao
import com.udacity.nanodegree.politicalpreparedness.network.models.Address
import com.udacity.nanodegree.politicalpreparedness.network.models.Election
import com.udacity.nanodegree.politicalpreparedness.network.models.VoterInfoResponse
import com.udacity.nanodegree.politicalpreparedness.network.models.mapResponseToRepresentatives
import com.udacity.nanodegree.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Created by @author Deepak Dawade on 2/22/2021 at 2:03 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
class CivicsRepository(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CivicsDataSource {
    override val electionsFollowed: LiveData<List<Election>>  = electionDao.getFollowedElections()
    override val electionsUpcoming: LiveData<List<Election>> = electionDao.getAllElections()

    override suspend fun getRepresentatives(address: Address): Result<List<Representative>> =
        withContext(ioDispatcher) {
            try {
                val response =
                    CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
                val representatives = response.mapResponseToRepresentatives()
                Result.Success(representatives)
            } catch (ex: Exception) {
                Result.Error(ex.message)
            }

        }

    override suspend fun refreshElectionsData() {
        withContext(ioDispatcher) {
            try {
                val response = CivicsApi.retrofitService.getElections()
                val elections = response.elections
                electionDao.insertAll(*elections.toTypedArray())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun updateElection(election: Election) = withContext(ioDispatcher) {
        try {
            electionDao.updateElection(election)
        } catch (e: Exception) {
        }

    }

    override suspend fun getElectionById(id: Int): Election = electionDao.getElectionById(id)

    override suspend fun deleteElection(election: Election) = withContext(ioDispatcher) {
        try {
            electionDao.deleteElection(election)
        } catch (ex: Exception) {
        }
    }

    override suspend fun clear() = withContext(ioDispatcher) {
        try {
            electionDao.clear()
        } catch (ex: Exception) {
        }
    }

    override suspend fun getVoterInfo(election: Election): Result<VoterInfoResponse> =
        withContext(ioDispatcher) {
            try {
                val response = CivicsApi.retrofitService.getVoterInfo(
                    election.division.toFormattedString(),
                    election.id
                )
                Result.Success(response)
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }


}