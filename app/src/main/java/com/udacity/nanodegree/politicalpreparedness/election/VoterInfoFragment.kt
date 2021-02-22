package com.udacity.nanodegree.politicalpreparedness.election

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.udacity.nanodegree.politicalpreparedness.R
import com.udacity.nanodegree.politicalpreparedness.base.BaseFragment
import com.udacity.nanodegree.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.udacity.nanodegree.politicalpreparedness.network.models.VoterInfoResponse
import org.koin.android.ext.android.inject

class VoterInfoFragment : BaseFragment() {

    override val viewModel: VoterInfoViewModel by inject()

    private var binding: FragmentVoterInfoBinding? = null
    private val args: VoterInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)
        val electionId = args.argElectionId
        //Added ViewModel values and create ViewModel
        viewModel.setElection(electionId)
        viewModel.getVoterInfo()

        //Added binding values
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        viewModel.election.observe(viewLifecycleOwner) {
            binding?.btnFollowElection?.text =
                if (!it.isFollowed) getString(R.string.voter_info_follow_election) else getString(R.string.voter_info_un_follow_election)
        }

        //Populated voter info -- hide views without provided data.
        viewModel.voterInfo.observe(viewLifecycleOwner, ::populateVoterInfo)

        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */


        //Handled loading of URLs

        //Handled save button UI state
        //cont'd Handle save button clicks

        return binding?.root
    }

    private fun populateVoterInfo(info: VoterInfoResponse) {
        val body = info.state?.firstOrNull()?.electionAdministrationBody

        binding?.apply {
            stateLocations.setOnClickListener {
                openUrl(body?.votingLocationFinderUrl)
            }

            stateBallot.setOnClickListener {
                openUrl(body?.ballotInfoUrl)
            }

            address.text = body?.correspondenceAddress?.toFormattedString()
        }
    }

    //Created method to load URL intents
    @SuppressLint("QueryPermissionsNeeded")
    private fun openUrl(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (intent.resolveActivity(requireContext().packageManager) != null)
            startActivity(intent)
        else viewModel.showSnackBarInt.value = R.string.can_not_open_url
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
