package com.hika.admin.features.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hika.admin.R

class OrderAdminFragment : Fragment() {

    companion object {
        fun newInstance() = OrderAdminFragment()
    }

    private lateinit var viewModel: OrderAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_admin, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderAdminViewModel::class.java)
        // TODO: Use the ViewModel
    }

}