package com.vimal.dependencyinjection.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vimal.dependencyinjection.MainViewModel
import com.vimal.dependencyinjection.R
import com.vimal.dependencyinjection.adapter.ArtistAdapter
import com.vimal.dependencyinjection.databinding.FragmentArtiestBinding
import com.vimal.dependencyinjection.model.artist.toArtist
import com.vimal.dependencyinjection.utils.NetworkResult
import com.vimal.dependencyinjection.utils.getCurrentPosition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtiestFragment : Fragment() {

    private var _binding:FragmentArtiestBinding?=null
    private val viewModel by activityViewModels<MainViewModel>()
    private val binding get() = _binding!!
    private lateinit var adapter: ArtistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentArtiestBinding.inflate(inflater,container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllPopularArtistList()

        initRecyclerView()
        observerData()
        binding.fab.setOnClickListener {
            observerData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView(){
        binding.recyclerViewArtist.layoutManager = LinearLayoutManager(requireContext())
        adapter = ArtistAdapter()
        binding.recyclerViewArtist.adapter = adapter
        observerData()

    }

    private fun observerData(){
        val response = viewModel.artistList
        response.observe(viewLifecycleOwner, Observer {
//            binding.progressBar.isVisible = false
//            binding.parentLayout.isVisible = true
            when(it){
                is NetworkResult.Success ->{
                    adapter.setList(it.data!!.results.toArtist())
                    binding.recyclerViewArtist.scrollToPosition(viewModel.currentIndexOnRecyclerView)
                }
                is NetworkResult.Error ->{

                }
                is NetworkResult.Loading ->{
                    adapter.setShimmerList()
//                    binding.progressBar.isVisible = true
//                    binding.parentLayout.isVisible = false
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.currentIndexOnRecyclerView = binding.recyclerViewArtist.getCurrentPosition()
    }
}