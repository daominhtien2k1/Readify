package com.tm2k1.readify.base.activity

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.NavHostFragment
import com.tm2k1.readify.base.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel<*, *>> : AppCompatActivity() {

    abstract val layoutId: Int

    abstract val viewModel: VM

    lateinit var binding: VB

    private var navHostFragment: NavHostFragment? = null /* compiled code */

    private var mMenuScreen: Menu? = null

    private var mPopupWindow: PopupWindow? = null

    private lateinit var coroutineScope: CoroutineScope


    abstract fun observeViewModel()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide default statusbar and systembar
        window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
        window.decorView.windowInsetsController?.hide(WindowInsets.Type.systemBars())
        window.decorView.windowInsetsController?.hide(WindowInsets.Type.navigationBars())
        window.setDecorFitsSystemWindows(false)

        // init base activity
        binding = DataBindingUtil.setContentView(this, layoutId)
        lifecycleScope.launch {
            withStarted {
                coroutineScope = this
                observeViewModel()
            }
        }

    }
    fun <F> addObserver(flow: Flow<F>, result: (F) -> Unit) {
        coroutineScope.launch {
            flow.collectLatest {
                result.invoke(it)
            }
        }
    }

}
