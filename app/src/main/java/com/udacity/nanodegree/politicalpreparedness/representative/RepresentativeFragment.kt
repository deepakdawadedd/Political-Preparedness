package com.udacity.nanodegree.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.udacity.nanodegree.politicalpreparedness.R
import com.udacity.nanodegree.politicalpreparedness.base.BaseFragment
import com.udacity.nanodegree.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.udacity.nanodegree.politicalpreparedness.network.models.Address
import com.udacity.nanodegree.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import org.koin.android.ext.android.inject
import java.util.*

class RepresentativeFragment : BaseFragment() {

    companion object {
        private val TAG: String = RepresentativeFragment::class.java.simpleName

        //Added Constant for Location request
        private const val REQUEST_CODE_LOCATION = 125
    }

    //Declared ViewModel
    override val viewModel: RepresentativeViewModel by inject()

    private var binding: FragmentRepresentativeBinding? = null

    private val LOCATION_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) else arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Established bindings
        binding = FragmentRepresentativeBinding.inflate(layoutInflater, container, false)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        //Defined and assign Representative adapter
        val listAdapter = RepresentativeListAdapter()
        binding?.representativeFragmentMyRepresentativesList?.adapter = listAdapter
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.states,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.representativeFragmentSelectorState?.adapter = adapter
        }

        binding?.representativeFragmentSelectorState?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    (parent?.getItemAtPosition(position) as String?)?.let { state ->
                        viewModel.state.value = state
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        //Populated Representative adapter
        viewModel.representatives.observe(viewLifecycleOwner, { representatives ->
            representatives?.apply {
                hideKeyboard()
                listAdapter.submitList(representatives)
            }
        })

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        //Established button listeners for field and location search
        binding?.representativeFragmentActionUseMyLocation?.setOnClickListener {
            hideKeyboard()
            checkLocationPermissions()
        }
        return binding?.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //Handled location permission result to get location on permission granted

        if (requestCode == REQUEST_CODE_LOCATION)
            if (grantResults.contains(PackageManager.PERMISSION_DENIED))
                viewModel.showSnackBarInt.value = R.string.permission_denied_explanation
            else getLocation()

    }

    private fun checkLocationPermissions() {
        if (isPermissionGranted()) {
            getLocation()
        } else {
            //Requested Location permissions
            requestPermissions(LOCATION_PERMISSIONS, REQUEST_CODE_LOCATION)
        }
    }

    private fun isPermissionGranted(): Boolean {
        //Checked if permission is already granted and return (true = granted, false = denied/other)
        val isForegroundLocationPermissionApproved = (
                PermissionChecker.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ))
        val isBackgroundLocationPermissionApproved =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                PermissionChecker.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) else true
        return isForegroundLocationPermissionApproved && isBackgroundLocationPermissionApproved
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //Get location from LocationServices
        val locationResult = fusedLocationProviderClient?.lastLocation
        locationResult?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val location = task.result
                val address = location?.geoCodeLocation()
                address?.let { viewModel.getAddressFromGeoLocation(it) }
            } else viewModel.showSnackBarInt.value =
                R.string.error_can_not_find_representative_for_location
        }
    }

    //The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    private fun Location.geoCodeLocation(): Address {
        val geocode = Geocoder(context, Locale.getDefault())
        return geocode.getFromLocation(latitude, longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroy() {
        fusedLocationProviderClient = null
        binding = null
        super.onDestroy()
    }
}
