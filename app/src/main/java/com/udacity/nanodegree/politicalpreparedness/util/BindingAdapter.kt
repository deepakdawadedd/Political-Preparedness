package com.udacity.nanodegree.politicalpreparedness.util

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter


/**
 * Created by @author Deepak Dawade on 2/22/2021 at 2:26 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
@BindingAdapter("toggleVisibility")
fun View.visibility(visible: Boolean?) {
    isVisible = visible == true
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = adapter as? ArrayAdapter<String>
    val position = adapter?.getPosition(value) ?: 0
    if (position >= 0)
        setSelection(position)
}
