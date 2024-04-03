package com.hika.myscent.features.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hika.common.widget.bind
import com.hika.common.widget.buildLoadingDialog
import com.hika.myscent.databinding.FragmentReviewBottomSheetBinding
import com.musfickjamil.snackify.Snackify
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewBottomSheetFragment(
    private val onDismiss: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentReviewBottomSheetBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModel<ReviewViewModel>()

    private val perfumeId: String by lazy {
        requireArguments().getString(PERFUME_ID).orEmpty()
    }

    private val loadingDialog by lazy {
        requireContext().buildLoadingDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var rating = 0

        binding?.apply {
            includeReview.bind(
                context = requireContext(),
                isClickable = true,
                starSize = 108f,
                onStarPressed = { rating = it }
            )

            btnSubmit.setOnClickListener {
                val review = tietReview.text.toString()
                viewModel.addReview(rating, review, perfumeId)
            }
        }

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (it.isSuccess) {
                    onDismiss.invoke()
                    dismiss()
                }
                if (it.isError) Toast.makeText(requireContext(), it.errorMessage, Snackify.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val PERFUME_ID = "perfume_id"

        fun newInstance(onDismiss: () -> Unit, perfumeId: String): ReviewBottomSheetFragment {
            val args = Bundle()
            args.putString(PERFUME_ID, perfumeId)
            return ReviewBottomSheetFragment(onDismiss).apply {
                arguments = args
            }
        }
    }

}