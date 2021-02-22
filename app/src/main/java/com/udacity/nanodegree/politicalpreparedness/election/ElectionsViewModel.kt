package com.udacity.nanodegree.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.udacity.nanodegree.politicalpreparedness.base.BaseViewModel
import com.udacity.nanodegree.politicalpreparedness.base.NavigationCommand
import com.udacity.nanodegree.politicalpreparedness.network.CivicsRepository
import com.udacity.nanodegree.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
    app: Application,
    private val repository: CivicsRepository
) : BaseViewModel(app) {

    //Created live data val for upcoming elections
    val upcomingElections: LiveData<List<Election>>
        get() = repository.electionsUpcoming

    //Created live data val for saved elections
    val followedElections: LiveData<List<Election>>
        get() = repository.electionsFollowed

    //Created val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun refreshElections() {
        viewModelScope.launch {
            repository.refreshElectionsData()
        }
    }

    //Created functions to navigate to saved or upcoming election voter info
    fun navigateToVoterInfo(election: Election) {
        navigationCommand.value = NavigationCommand.To(
            ElectionsFragmentDirections.toVoterInfoFragment(
                election.id,
                election.division
            )
        )
    }

    init {
        refreshElections()
    }

}