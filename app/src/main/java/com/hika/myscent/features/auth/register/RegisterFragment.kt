package com.hika.myscent.features.auth.register

import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.hika.myscent.base.BaseFragment
import com.hika.myscent.databinding.FragmentRegisterBinding
import com.hika.myscent.model.User
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel by viewModel<RegisterViewModel>()

    override fun inflateViewBinding(container: ViewGroup?): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(layoutInflater, container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun FragmentRegisterBinding.bind() {
        btnRegister.setOnClickListener {
            val username = edtUsername.text.toString()
            val email = edtEmail.text.toString()
            val phone = edtPhoneNumber.text.toString()
            val address = edtAddress.text.toString()
            val password = edtPassword.text.toString()

            val user = User(email, username, address, phone)
            viewModel.register(user, password)
        }

        tvAlreadyHaveAccounts.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                state?.let {
                    if (it.isLoading) loadingDialog.show() else loadingDialog.hide()
                    if (it.isSuccess) {
                        showSuccessSnackBar("Register success")
                        parentFragmentManager.popBackStack()
                    }
                    if (it.isError) {
                        showErrorSnackBar(it.errorMessage)
                    }
                }
            }
        }
    }

    override fun onBackPressedBehaviour() {
        parentFragmentManager.popBackStack()
    }
}