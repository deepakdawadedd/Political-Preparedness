package com.udacity.nanodegree.politicalpreparedness.network

import com.udacity.nanodegree.politicalpreparedness.database.ElectionDao
import com.udacity.nanodegree.politicalpreparedness.network.models.Address
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
    override suspend fun getRepresentatives(address: Address): Result<List<Representative>> =
        withContext(ioDispatcher) {
            try {
                val response =
                    CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
                val representatives = response.mapResponseToRepresentatives()
                Result.Success(representatives)
            } catch (exception: java.lang.Exception) {
                Result.Error("Error getting representatives")
            }

        }
}