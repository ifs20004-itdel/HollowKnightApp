package com.example.hollowknightapp.view.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hollowknightapp.data.remote.response.HeroResponse
import com.example.hollowknightapp.databinding.FragmentMainBinding
import com.example.hollowknightapp.model.UserPreference
import com.example.hollowknightapp.view.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? =null
    private lateinit var mainFragmentViewModel: MainFragmentViewModel

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        setupViewModel()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        val heroAdapter = MainAdapter()

        mainFragmentViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        mainFragmentViewModel.getToken().observe(viewLifecycleOwner){
            mainFragmentViewModel.getHero(it.token)
        }
        mainFragmentViewModel.user.observe(viewLifecycleOwner){
            result ->
            if(result !=null){
                setDataHero(result)
            }
        }

        binding?.rvHero?.apply {
            layoutManager = LinearLayoutManager(context)
            val itemDecoration =
                DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation)
            binding!!.rvHero.addItemDecoration(itemDecoration)
            setHasFixedSize(true)
            adapter = heroAdapter
        }
    }

    private fun setDataHero(result: List<HeroResponse>) {
        val adapter =  HeroAdapter(result)
        binding?.rvHero?.adapter = adapter
    }

    private fun showLoading(it: Boolean) {
        if (it) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun setupViewModel() {
        mainFragmentViewModel =  ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore), requireContext())
        )[MainFragmentViewModel::class.java]
    }
}