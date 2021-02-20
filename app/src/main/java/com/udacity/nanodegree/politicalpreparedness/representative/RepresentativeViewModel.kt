package com.udacity.nanodegree.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.nanodegree.politicalpreparedness.base.BaseViewModel
import com.udacity.nanodegree.politicalpreparedness.network.models.Address
import kotlinx.coroutines.launch

class RepresentativeViewModel(application: Application): BaseViewModel(application) {

    //Established live data for representatives and address
    private val address = MutableLiveData<Address?>()
    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location
    fun refreshRepresentatives(address: Address) {
        this.address.value = address
        addressLine1.value = address.line1
        address.line2.let { addressLine2.value = it }
        city.value = address.city
        state.value = address.state
        zip.value = address.zip

        viewModelScope.launch {
//            repository.refreshRepresentatives(address.toFormattedString())
        }
    }
    //TODO: Create function to get address from individual fields
    fun refreshRepresentatives() {
        val addressLine1 = addressLine1.value ?: return
        val city = city.value ?: return
        val state = state.value ?: return
        val zip = zip.value ?: return

        val address = Address(addressLine1, addressLine2.value, city, state, zip)
        refreshRepresentatives(address)
    }


}
