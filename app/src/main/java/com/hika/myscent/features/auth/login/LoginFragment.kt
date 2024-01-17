package com.hika.myscent.features.auth.login

import android.content.Intent
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.hika.myscent.R
import com.hika.myscent.base.BaseFragment
import com.hika.myscent.databinding.FragmentLoginBinding
import com.hika.myscent.features.MainActivity
import com.hika.myscent.features.auth.register.RegisterFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel by viewModel<LoginViewModel>()

    override fun inflateViewBinding(container: ViewGroup?): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater, container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation? {
        return ScreenOrientation.PORTRAIT
    }

    override fun FragmentLoginBinding.bind() {
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            viewModel.login(email, password)
        }

        tvDoNotHaveAccount.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.auth_container, RegisterFragment())
                addToBackStack(null)
            }
        }

        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                state?.let {
                    if (it.isLoading) loadingDialog.show() else loadingDialog.hide()
                    if (it.isSuccess) {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    if (it.isError) {
                        showErrorSnackBar(it.errorMessage)
                    }
                }
            }
        }
    }
}