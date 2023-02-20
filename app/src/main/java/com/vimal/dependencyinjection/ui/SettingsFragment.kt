package com.vimal.dependencyinjection.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vimal.dependencyinjection.MainViewModel
import com.vimal.dependencyinjection.R
import com.vimal.dependencyinjection.adapter.ArtistAdapter
import com.vimal.dependencyinjection.adapter.TvShowAdapter
import com.vimal.dependencyinjection.databinding.FragmentSettingsBinding
import com.vimal.dependencyinjection.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding?=null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var adapter: TvShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        viewModel.getAllTvShowList()

        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView(){
        binding.tvShowRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TvShowAdapter()
        binding.tvShowRecyclerView.adapter = adapter
        observerData()

    }
    private fun observerData() {
        val response = viewModel.tvShowLiveData
        response.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success ->{
                  adapter.setList(it.data!!.tvShows)
                    adapter.notifyDataSetChanged()
                }
                is NetworkResult.Error ->{

                }
                is NetworkResult.Loading ->{

                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}