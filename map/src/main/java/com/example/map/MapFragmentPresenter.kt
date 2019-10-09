package com.example.map

import com.example.map.model.MapFragmentModel
import com.example.map.view.MapFragmentView
import kekmech.ru.core.Presenter
import javax.inject.Inject

class MapFragmentPresenter @Inject constructor(
    private val model: MapFragmentModel
) : Presenter<MapFragmentView>()