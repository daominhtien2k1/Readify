package com.tm2k1.readify.ui.main

import androidx.activity.viewModels
import com.tm2k1.readify.R
import com.tm2k1.readify.base.activity.BaseActivity
import com.tm2k1.readify.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    override fun observeViewModel() {
    }
}
