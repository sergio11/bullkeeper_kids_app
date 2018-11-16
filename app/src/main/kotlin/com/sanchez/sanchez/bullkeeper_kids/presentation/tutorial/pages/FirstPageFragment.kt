package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.cleveroad.slidingtutorial.Direction
import com.cleveroad.slidingtutorial.TransformItem
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.AppTutorialComponent
import com.sanchez.sanchez.bullkeeper_kids.presentation.legalcontent.LegalContentActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.IAppTutorialHandler
import kotlinx.android.synthetic.main.first_page_fragment_layout.*
import timber.log.Timber
import java.lang.IllegalStateException
import java.util.*

/**
 * First Page Fragment
 */
class FirstPageFragment: AbstractPageFragment<AppTutorialComponent>() {


    /**
     * App Tutorial Handler
     */
    lateinit var appTutorialHandler: IAppTutorialHandler

    /**
     * Get Layout Res Id
     */
    override fun getLayoutResId(): Int = R.layout.first_page_fragment_layout

    /**
     * Initialize Injector
     */
    override fun initializeInjector(): AppTutorialComponent? {
        val appTutorialComponent = AppTutorialComponent::class.java
                .cast((activity as HasComponent<AppTutorialComponent>)
                        .component)
        appTutorialComponent.inject(this)
        return appTutorialComponent
    }

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        /**
         * Check Context
         */
        if(context !is IAppTutorialHandler)
            throw IllegalStateException("The context does not implement the handler IAppTutorialHandler")

        appTutorialHandler = context

    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureTermsOfServiceAndPrivacyPolicy()
    }

    /**
     * When Phase Is Hidden
     */
    override fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int) {
        Timber.d("Phase Is Hidden")
    }

    /**
     * When Phase Is Showed
     */
    override fun whenPhaseIsShowed() {
        Timber.d("Phase Is Showed")
    }

    /**
     * Configure Terms Of Service And Privacy Policy
     */
    private fun configureTermsOfServiceAndPrivacyPolicy() {

        val termsOfService = getString(R.string.terms_of_service)
        val privacyPolicy = getString(R.string.privacy_policy)

        val acceptTermsText = String.format(Locale.getDefault(),
                getString(R.string.first_page_registration_terms_and_conditions),
                termsOfService, privacyPolicy)

        val termsOfServicesStart = acceptTermsText.indexOf(termsOfService)
        val privacyPolicyStart = acceptTermsText.indexOf(privacyPolicy)

        val spannable = SpannableStringBuilder(acceptTermsText)


        // Terms of Service Deep Link
        spannable.setSpan(object : DeepLinkSpan(acceptTerms, termsOfService, context) {
            override fun onClick(widget: View) {
                super.onClick(widget)
                appTutorialHandler.showLegalContent(
                        LegalContentActivity.LegalTypeEnum.TERMS_OF_SERVICE)
            }
        }, termsOfServicesStart,
                termsOfServicesStart + termsOfService.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        // Privacy Policy Deep Link
        spannable.setSpan(object : DeepLinkSpan(acceptTerms, privacyPolicy, context) {
            override fun onClick(widget: View) {
                super.onClick(widget)
                appTutorialHandler.showLegalContent(
                        LegalContentActivity.LegalTypeEnum.PRIVACY_POLICY)
            }
        }, privacyPolicyStart,
                privacyPolicyStart + privacyPolicy.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        acceptTerms.text = spannable
        acceptTerms.movementMethod = LinkMovementMethod.getInstance()

    }

    /**
     * Get Transform Items
     */
    override fun getTransformItems(): Array<TransformItem> {
        return arrayOf(
                TransformItem.create(R.id.titleText, Direction.LEFT_TO_RIGHT, 0.2f),
                TransformItem.create(R.id.appIcon, Direction.RIGHT_TO_LEFT, 0.7f),
                TransformItem.create(R.id.acceptTerms, Direction.LEFT_TO_RIGHT, 0.7f)
        )
    }

    /**
     * Deep Link Span
     */
    abstract inner class DeepLinkSpan constructor(private val textView: TextView, private val clickableText: String,
                                                          private val appContext: Context?) : ClickableSpan() {

        /**
         * On Click
         * @param widget
         */
        override fun onClick(widget: View) {

            widget.cancelPendingInputEvents()
        }


        /**
         * Update Draw State
         * @param ds
         */
        override fun updateDrawState(ds: TextPaint) {

            ds.isUnderlineText = true

            appContext?.let {

                if (textView.isPressed && textView.selectionStart != -1 && textView.text
                                .toString()
                                .substring(textView.selectionStart, textView.selectionEnd) == clickableText) {
                    ds.color = ContextCompat.getColor(it, R.color.commonWhite)
                } else {
                    ds.color = ContextCompat.getColor(it, R.color.commonWhite)
                }
            }


        }

    }
}