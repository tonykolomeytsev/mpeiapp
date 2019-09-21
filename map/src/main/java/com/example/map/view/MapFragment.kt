package com.example.map.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.map.MapFragmentPresenter
import com.example.map.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MapFragment : DaggerFragment(), MapFragmentView {

    @Inject
    lateinit var presenter: MapFragmentPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


}
