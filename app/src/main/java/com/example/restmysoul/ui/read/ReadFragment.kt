package com.example.restmysoul.ui.read

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restmysoul.R
import com.example.restmysoul.databinding.FragmentReadBinding


class ReadFragment : Fragment() {
    private var _binding: FragmentReadBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val readViewModel =
            ViewModelProvider(this)[ReadViewModel::class.java]

        _binding = FragmentReadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBibleUrl("https://biblehub.com/esv/genesis/1.htm")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadBibleUrl(url: String?) {
        if (url.isNullOrBlank()) return
        binding.webView.apply {
            settings.javaScriptEnabled = true
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }
                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                    activity?.runOnUiThread {
                        val dialog = AlertDialog.Builder(activity)
                            .setMessage(getString(R.string.error_invalid_ssl_cert))
                            .setPositiveButton(getString(R.string.continue_btn_label)) { _: DialogInterface?, _: Int -> handler.proceed() }
                            .setNegativeButton(getString(R.string.cancel)) { _: DialogInterface?, _: Int -> handler.cancel() }
                            .create()
                        if (!activity?.isFinishing!!)
                            dialog.show()
                    }
                }
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    Toast.makeText(activity, getString(R.string.error_generic), Toast.LENGTH_SHORT).show()
                }
            }
            loadUrl(url)
        }
    }

            }