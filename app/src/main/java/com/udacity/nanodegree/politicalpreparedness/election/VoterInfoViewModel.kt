package com.udacity.nanodegree.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.nanodegree.politicalpreparedness.base.BaseViewModel
import com.udacity.nanodegree.politicalpreparedness.network.CivicsRepository
import com.udacity.nanodegree.politicalpreparedness.network.Result
import com.udacity.nanodegree.politicalpreparedness.network.models.Election
import com.udacity.nanodegree.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoterInfoViewModel(app: Application, private val repository: CivicsRepository) :
    BaseViewModel(app) {

    //Added live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    //Added var and methods to populate voter info
    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election


    //Added var and methods to support loading URLs
    fun getVoterInfo() {
        viewModelScope.launch {
            val result = repository.getVoterInfo(election.value ?: return@launch)
            when (result) {
                is Result.Success -> _voterInfo.postValue(result.data)
                is Result.Error -> showSnackBar.postValue(result.message)
            }
        }
    }

    fun setElection(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val election = repository.getElectionById(id)
            _election.postValue(election)
        }
    }

    //Added var and methods to save and remove elections to local database
    //cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    fun updateElection() {
        val election = _election.value ?: return
        election.isFollowed = !election.isFollowed
        viewModelScope.launch {
            repository.updateElection(election)
            _election.value = election
        }
    }

}