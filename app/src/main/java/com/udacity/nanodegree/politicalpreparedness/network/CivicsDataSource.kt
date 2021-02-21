package com.udacity.nanodegree.politicalpreparedness.network

import com.udacity.nanodegree.politicalpreparedness.network.models.Address
import com.udacity.nanodegree.politicalpreparedness.representative.model.Representative


/**
 * Created by @author Deepak Dawade on 2/22/2021 at 2:01 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
interface CivicsDataSource {

    suspend fun getRepresentatives(address: Address): Result<List<Representative>>


}