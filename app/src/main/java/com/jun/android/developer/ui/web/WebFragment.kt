package com.jun.android.developer.ui.web

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jun.android.developer.R
import com.jun.android.developer.databinding.FragmentWebBinding
import com.jun.android.developer.ui.MainViewModel
import com.jun.android.developer.util.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WebFragment : Fragment() {

    private val args by navArgs<WebFragmentArgs>()
    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var networkConnection: NetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //onBackPressedCallback логика для нажание на кнопку назад
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack();
                    } else {
                        activity?.finish()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkConnection = NetworkConnection(requireContext())
        handleNetworkConnection()

        loadWebView()
    }

    private fun loadWebView() {
        binding.apply {
            webView.webViewClient = WebViewClient()
            args.uri?.let { webView.loadUrl(it) }
            webView.settings.javaScriptEnabled = true
            webView.settings.setSupportZoom(false)
        }
    }

    // Observe internet status
    private fun handleNetworkConnection() {
        networkConnection.observe(viewLifecycleOwner) { networkIsAvailable ->
            if (networkIsAvailable == null || networkIsAvailable == false) {
               findNavController().navigate(R.id.action_webFragment_to_splashFragment)
            }
        }
    }
    // ---
}