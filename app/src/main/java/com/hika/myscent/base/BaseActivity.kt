package com.hika.myscent.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hika.myscent.widget.buildLoadingDialog
import com.musfickjamil.snackify.Snackify
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {

    lateinit var loadingDialog: Dialog

    private lateinit var _binding: VB
    val binding get() = _binding

    abstract fun inflateViewBinding(): VB
    abstract fun determineScreenOrientation(): ScreenOrientation?
    abstract fun VB.bind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateViewBinding()
        setContentView(binding.root)

        this.loadingDialog = buildLoadingDialog()
        val screenOrientation = determineScreenOrientation()

        requestedOrientation = if(screenOrientation != null) {
            if (screenOrientation == ScreenOrientation.PORTRAIT)
                android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            else
                android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        binding.apply {
            bind()
        }
    }

    fun showSuccessSnackBar(message: String) {
        Snackify.success(binding.root, message, Snackify.LENGTH_SHORT).show()
    }

    fun showErrorSnackBar(message: String) {
        Snackify.error(binding.root, message, Snackify.LENGTH_SHORT).show()
    }


    enum class ScreenOrientation {
        PORTRAIT, LANDSCAPE
    }

}