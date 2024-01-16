package com.hika.myscent.base

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hika.myscent.widget.buildLoadingDialog
import com.musfickjamil.snackify.Snackify

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    lateinit var loadingDialog: Dialog

    private var _binding: VB? = null
    val binding get() = _binding

    lateinit var fragmentView: View

    abstract fun inflateViewBinding(container: ViewGroup?): VB
    abstract fun VB.bind()
    abstract fun determineScreenOrientation(): ScreenOrientation?

    open fun onCreateViewBehaviour(inflater: LayoutInflater, container: ViewGroup?) { }
    open fun onViewCreatedBehaviour() { }
    open fun onDestroyBehaviour() { }
    open fun onBackPressedBehaviour() { }
    fun doNothing() = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onCreateViewBehaviour(inflater, container)
        if(_binding == null) {
            _binding = inflateViewBinding(container)
        }
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(determineScreenOrientation() == ScreenOrientation.PORTRAIT) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else if (determineScreenOrientation() == ScreenOrientation.LANDSCAPE) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.fragmentView = view

        binding?.apply {
            bind()
        }

        loadingDialog = requireContext().buildLoadingDialog()

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedBehaviour()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onDestroyView() {
       super.onDestroyView()
        onDestroyBehaviour()
        _binding = null
    }

    fun showSuccessSnackBar(message: String) {
        Snackify.success(binding!!.root, message, Snackify.LENGTH_SHORT).show()
    }

    fun showErrorSnackBar(message: String) {
        Snackify.error(binding!!.root, message, Snackify.LENGTH_SHORT).show()
    }

    enum class ScreenOrientation {
        PORTRAIT, LANDSCAPE
    }

}