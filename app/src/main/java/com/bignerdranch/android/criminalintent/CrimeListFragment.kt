package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "CrimeListFragment"

class CrimeListFragment: Fragment() {
    private val crimeListViewModel: CrimeListViewModel by lazy{
        ViewModelProviders.of(this).get(crimeListViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    companion object {
        fun newInstance(): CrimeListFragment{
            return CrimeListFragment()
        }
    }
}