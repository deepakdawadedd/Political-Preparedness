package com.udacity.nanodegree.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.nanodegree.politicalpreparedness.R
import com.udacity.nanodegree.politicalpreparedness.base.BaseViewModel
import com.udacity.nanodegree.politicalpreparedness.network.CivicsRepository
import com.udacity.nanodegree.politicalpreparedness.network.Result
import com.udacity.nanodegree.politicalpreparedness.network.models.Address
import com.udacity.nanodegree.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    application: Application,
    private val repository: CivicsRepository
) : BaseViewModel(application) {

    //Established live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> get() = _representatives

    private val address = MutableLiveData<Address?>()
    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()
    init {
        showNoData.value = false
        showLoading.value = false
    }
    //Created function to fetch representatives from API from a provided address
    private fun getRepresentativesFromAddress(address: Address) {
        viewModelScope.launch {
            showLoading.value = true
            val result = repository.getRepresentatives(address)
            when (result) {
                is Result.Success -> {
                    val data = result.data
                    _representatives.postValue(data)
                    showLoading.value = false
                    showNoData.postValue(data.isEmpty())
                }
                is Result.Error -> {
                    showSnackBar.value = result.message
                    showLoading.value = false
                    _representatives.postValue(emptyList())
                    showNoData.postValue(true)
                }
            }

        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //Created function get address from geo location
    fun getAddressFromGeoLocation(address: Address) {
        this.address.value = address
        addressLine1.value = address.line1
        address.line2.let { addressLine2.value = it }
        city.value = address.city
        state.value = address.state
        zip.value = address.zip
        getRepresentativesFromAddress(address)
    }

    //Created function to get address from individual fields
    fun fetchRepresentatives() {
        val addressLine1 = addressLine1.value ?: kotlin.run {
            showSnackBarInt.value = R.string.error_enter_address_line1
            return
        }
        val city = city.value ?: kotlin.run {
            showSnackBarInt.value = R.string.error_enter_city
            return
        }

        val state = state.value ?: kotlin.run {
            showSnackBarInt.value = R.string.error_enter_state
            return
        }

        val zip = zip.value ?: kotlin.run {
            showSnackBarInt.value = R.string.error_enter_zip
            return
        }


        val address = Address(addressLine1, addressLine2.value, city, state, zip)
        getRepresentativesFromAddress(address)
    }


}
