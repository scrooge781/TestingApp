package com.jun.android.developer.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils.isEmpty
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jun.android.developer.R
import com.jun.android.developer.databinding.FragmentSplashBinding
import com.jun.android.developer.ui.MainViewModel
import com.jun.android.developer.util.Constants
import com.jun.android.developer.util.NetworkConnection
import com.jun.android.developer.util.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var networkConnection: NetworkConnection
    private var network: Boolean? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkConnection = NetworkConnection(requireContext())
        handleNetworkConnection()

        start()
        observeViewModel()

        binding.btnError .setOnClickListener { start() }
    }

    private fun start() {
        viewModel.getDataStorage(Constants.HOME)
    }

    private fun observeViewModel() {
        viewModel.itemDataStorage.observe(viewLifecycleOwner) { uri ->
            if(network == true) {
                when (uri.isNullOrEmpty()) {
                    true -> viewModel.getResponse()
                    else -> navigation(uri)
                }
            }
        }

        viewModel.responseItem.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    visibilityProgress()
                    response.data?.let {
                        viewModel.saveDateStorage(it)
                    }
                }
                Status.LOADING -> {
                    visibilityProgress()
                }
                Status.ERROR -> {
                    visibilityError()
                }
            }
        }

        viewModel.firstWebStartStr.observe(viewLifecycleOwner) { navigation(it) }
    }

    private fun navigation(uri: String) {
            val action = SplashFragmentDirections.actionSplashFragmentToWebFragment(uri)

            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(action)
            }, 1000)
    }

    // Observe internet status
    private fun handleNetworkConnection() {
        networkConnection.observe(viewLifecycleOwner) { networkIsAvailable ->
            network = networkIsAvailable
            if (networkIsAvailable != null) {
                if (networkIsAvailable) {
                    visibilityProgress()
                    start()
                } else {
                    informNoNetwork()
                }
            } else {
                informNoNetwork()
            }
        }
    }

    private fun informNoNetwork() {
        visibilityError()
        binding.txtError.text = "No Network Connection"
    }
    // ---

    private fun visibilityProgress(){
        binding.apply {
            progress.visibility = View.VISIBLE
            error.visibility = View.GONE
        }
    }

    private fun visibilityError() {
        binding.apply {
            progress.visibility = View.GONE
            error.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
