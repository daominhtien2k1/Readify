package com.tm2k1.readify.base.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tm2k1.readify.base.activity.BaseActivity
import com.tm2k1.readify.utils.ext.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VB : ViewDataBinding, VM : ViewModel> : Fragment() {

    private val TAG by lazy { this::class.simpleName }

    protected var binding by autoCleared<VB>()
        private set

    abstract val viewModel: VM

    private var jobEventReceiver: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    /***
     * init data
     */
    abstract fun initData()

    abstract fun initView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        binding = method.invoke(null, layoutInflater, container, false) as VB
        return binding.root
    }

    private lateinit var coroutineScope: CoroutineScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewLifecycleOwner.lifecycleScope.launch(SupervisorJob()) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                coroutineScope = this
                observerViewModel()
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

    open fun getMenuResId(): Int? {
        return null
    }

    open fun showToast(content: String, showLong: Boolean = false) {
        requireContext().showToast(content, showLong)
    }

    open fun navigateToDestination(actionId: Int, bundle: Bundle = bundleOf()) {
        findNavController().navigate(actionId, bundle)
    }

    open fun replaceFragment(containerViewId: Int, fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerViewId, fragment)
        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        jobEventReceiver?.cancel()
        super.onDestroyView()
    }

    fun onClearViewModelInScopeActivity() {
        activity?.viewModelStore?.clear()
    }

    open fun getBaseActivity(): BaseActivity<*, *> {
        return requireActivity() as BaseActivity<*, *>
    }

    abstract fun observerViewModel()

    open fun onMenuItemClick(item: MenuItem?) {

    }

    fun setupUI(view: View) {
        Log.d(TAG, "Not yet implemented setupUI")
    }
}
