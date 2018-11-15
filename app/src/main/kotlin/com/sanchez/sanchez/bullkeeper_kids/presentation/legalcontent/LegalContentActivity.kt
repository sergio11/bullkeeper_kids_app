package com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent

import android.annotation.TargetApi
import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sanchez.sanchez.bullkeeper_kids.R
import kotlinx.android.synthetic.main.activity_legal_content.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Legal Content Activity
 */
class LegalContentActivity : Activity() {

    /**
     *
     */
    companion object {

        val LEGAL_CONTENT_TYPE_ARG = "LEGAL_CONTENT_TYPE_ARG"

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context, legalTypeEnum: LegalContentActivity.LegalTypeEnum): Intent {
            val intent = Intent(context, LegalContentActivity::class.java)
            intent.putExtra(LEGAL_CONTENT_TYPE_ARG, legalTypeEnum)
            return intent
        }
    }



    /**
     * Legal Type Enum
     */
    enum class LegalTypeEnum {
        TERMS_OF_SERVICE, PRIVACY_POLICY
    }

    /**
     * Legal Type Enum
     */
    private var legalTypeEnum = LegalTypeEnum.TERMS_OF_SERVICE

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legal_content)

        val extras = intent.extras
        if (extras != null && !extras.isEmpty && extras.containsKey(LEGAL_CONTENT_TYPE_ARG))
            legalTypeEnum = extras.getSerializable(LEGAL_CONTENT_TYPE_ARG) as LegalTypeEnum

        if (legalTypeEnum == LegalTypeEnum.TERMS_OF_SERVICE)
            legalContentTitle.setText(R.string.terms_of_service)
        else
            legalContentTitle.setText(R.string.privacy_policy)

        acceptButton.setOnClickListener { finish() }

        // Configure Web View.
        configureWebView()

    }


    /**
     * Attach Base Context
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    /**
     * Configure WebView
     */
    private fun configureWebView() {

        // Disable JavaScript
        textLicenceWebView.settings.javaScriptEnabled = false

        // Enable the caching for web view
        textLicenceWebView.settings.setAppCacheEnabled(true)
        // Specify the app cache path
        textLicenceWebView.settings.setAppCachePath(cacheDir.path)

        // Set the cache mode
        textLicenceWebView.settings.cacheMode = WebSettings.LOAD_DEFAULT

        // Terms Of Service
        if (legalTypeEnum == LegalTypeEnum.TERMS_OF_SERVICE)
            textLicenceWebView.loadUrl(getString(R.string.terms_of_service_url))
        else
            textLicenceWebView.loadUrl(getString(R.string.privacy_policy_url))

    }

}
