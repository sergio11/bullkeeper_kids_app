package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ConversationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.DaggerConversationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidGuardianEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.kidguardians.KidGuardiansActivity
import kotlinx.android.synthetic.main.app_translucent_toolbar_return.*
import javax.inject.Inject

/**
 * Conversation List Activity
 */
class ConversationListActivity : BaseActivity(),
        HasComponent<ConversationComponent>, IConversationListHandler {

    val TAG = "CONVERSATION_MESSAGE_LIST"


    /**
     * Conversation Component
     */
    private val conversationComponent: ConversationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerConversationComponent
                .builder()
                .applicationComponent((application as AndroidApplication).appComponent)
                .activityModule(ActivityModule(this))
                .build()
    }


    /**
     * App Component
     */
    override val component: ConversationComponent
        get() = conversationComponent


    /**
     * Dependencies
     * =================
     */

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository


    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        conversationComponent.inject(this)

        backIcon.setOnClickListener {
            onBackPressed()
        }

        appTitle.setOnClickListener {
            navigator.showHome(this)
        }
    }

    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.activity_conversation_list


    /**
     * Fragment
     */
    override fun fragment(): BaseFragment = ConversationListActivityFragment()

    /**
     * Show Conversation Message List
     */
    override fun showConversationMessageList(conversation: String) =
            navigator.showConversationMessageList(this, conversation)


    /**
     * Show Kid Guardian
     */
    override fun showKidGuardian() =
            navigator.showKidGuardians(this, KID_GUARDIAN_REQUEST_CODE)

    /**
     * On Activity Result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == KID_GUARDIAN_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK) {

            data?.extras?.getSerializable(KidGuardiansActivity.KID_GUARDIAN_SELECTED_ARG)?.let {

                if(it is KidGuardianEntity) {

                    it.guardian?.identity?.let {
                        val kidId = preferenceRepository.getPrefKidIdentity()
                        navigator.showConversationMessageList(this, kidId, it)
                    }

                }
            }
        }

    }

    companion object {

        const val KID_GUARDIAN_REQUEST_CODE = 12345

        /**
         * Calling Intent
         */
        fun callingIntent(context: Context): Intent
                = Intent(context, ConversationListActivity::class.java)
    }

}
