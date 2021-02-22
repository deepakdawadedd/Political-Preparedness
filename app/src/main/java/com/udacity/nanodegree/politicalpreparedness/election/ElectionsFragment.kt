package com.udacity.nanodegree.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.udacity.nanodegree.politicalpreparedness.base.BaseFragment
import com.udacity.nanodegree.politicalpreparedness.databinding.FragmentElectionBinding
import com.udacity.nanodegree.politicalpreparedness.election.adapter.ElectionListAdapter
import org.koin.android.ext.android.inject

class ElectionsFragment : BaseFragment() {

    //Declared ViewModel
    override val viewModel: ElectionsViewModel by inject()
    private var binding: FragmentElectionBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Added ViewModel values and create ViewModel
        binding = FragmentElectionBinding.inflate(inflater, container, false)
        //Added binding values
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        //TODO: Link elections to voter info

        //Initiated recycler adapters
        val upComingAdapter = ElectionListAdapter {
            viewModel.navigateToVoterInfo(it)
        }

        val followedAdapter = ElectionListAdapter {
            viewModel.navigateToVoterInfo(it)
        }

        //Populated recycler adapters
        binding?.electionFragmentUpcomingElections?.adapter = upComingAdapter
        binding?.electionFragmentFollowedElection?.adapter = followedAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, { elections ->
            upComingAdapter.submitList(elections)
        })

        viewModel.followedElections.observe(viewLifecycleOwner, { elections ->
            followedAdapter.submitList(elections)
        })

        return binding?.root
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}