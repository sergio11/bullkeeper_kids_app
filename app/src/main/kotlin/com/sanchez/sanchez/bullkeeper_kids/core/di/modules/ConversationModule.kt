package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.conversation.*
import com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.chat.ConversationMessageListViewModel
import com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list.ConversationListViewModel
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Conversation Module
 */
@Module
class ConversationModule {

    /**
     * Provide Conversation Service
     */
    @Provides
    @PerActivity
    fun provideConversationService(retrofit: Retrofit) : IConversationService
            = retrofit.create(IConversationService::class.java)

    /**
     * Provide Add Message Interact
     */
    @Provides
    @PerActivity
    fun provideAddMessageInteract(
            appContext: Context,
            apiEndPointsHelper: ApiEndPointsHelper,
            conversationService: IConversationService,
            retrofit: Retrofit
    ): AddConversationMessageInteract
        = AddConversationMessageInteract(appContext, apiEndPointsHelper, conversationService, retrofit)

    /**
     * Provide Delete Conversation Interact
     */
    @Provides
    @PerActivity
    fun provideDeleteConversationInteract(
            conversationService: IConversationService,
            retrofit: Retrofit
    ): DeleteConversationInteract
        = DeleteConversationInteract(conversationService, retrofit)


    /**
     * Provide Delete Conversation Messages Interact
     */
    @Provides
    @PerActivity
    fun provideDeleteConversationMessagesInteract(
            conversationService: IConversationService,
            retrofit: Retrofit
    ): DeleteConversationMessagesInteract
        = DeleteConversationMessagesInteract(conversationService, retrofit)

    /**
     * Provide Get Conversation Interact
     */
    @Provides
    @PerActivity
    fun provideGetConversationInteract(
            appContext: Context,
            apiEndPointsHelper: ApiEndPointsHelper,
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationInteract
            = GetConversationInteract(appContext, apiEndPointsHelper, conversationService, retrofit)


    /**
     * Provide Get Conversation Messages Interact
     */
    @Provides
    @PerActivity
    fun provideGetConversationMessagesInteract(
            appContext: Context,
            apiEndPointsHelper: ApiEndPointsHelper,
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationMessagesInteract
            = GetConversationMessagesInteract(appContext, apiEndPointsHelper, conversationService, retrofit)

    /**
     * Provide Get Conversation For Member Interact
     */
    @Provides
    @PerActivity
    fun provideGetConversationForMemberInteract(
            appContext: Context,
            apiEndPointsHelper: ApiEndPointsHelper,
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationsForMemberInteract
        = GetConversationsForMemberInteract(appContext, apiEndPointsHelper, conversationService, retrofit)


    /**
     * Provide Delete All Conversation For Member Interact
     */
    @Provides
    @PerActivity
    fun provideDeleteAllConversationForMemberInteract(
            conversationService: IConversationService,
            retrofit: Retrofit
    ): DeleteAllConversationForMemberInteract
        = DeleteAllConversationForMemberInteract(conversationService, retrofit)


    /**
     * Provide Get Conversation Messages For Members Interact
     */
    @Provides
    @PerActivity
    fun provideGetConversationMessagesForMembersInteract(
            appContext: Context,
            apiEndPointsHelper: ApiEndPointsHelper,
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationMessagesForMembersInteract
        = GetConversationMessagesForMembersInteract(appContext, apiEndPointsHelper, conversationService, retrofit)


    /**
     * Provide Delete Conversation Messages For Member Interact
     */
    @Provides
    @PerActivity
    fun provideDeleteConversationMessagesForMembersInteract(
            conversationService: IConversationService,
            retrofit: Retrofit
    ): DeleteConversationMessagesForMembersInteract
        = DeleteConversationMessagesForMembersInteract(conversationService, retrofit)

    /**
     * Provide Get Conversation For Members Interact
     */
    @Provides
    @PerActivity
    fun provideGetConversationForMembersInteract(
            appContext: Context,
            apiEndPointsHelper: ApiEndPointsHelper,
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationForMembersInteract
        = GetConversationForMembersInteract(appContext, apiEndPointsHelper, conversationService, retrofit)

    /**
     * Provide Create Conversation For Members Interact
     */
    @Provides
    @PerActivity
    fun provideCreateConversationForMembersInteract(
            appContext: Context,
            apiEndPointsHelper: ApiEndPointsHelper,
            conversationService: IConversationService,
            retrofit: Retrofit
    ): CreateConversationForMembersInteract
        = CreateConversationForMembersInteract(appContext, apiEndPointsHelper, conversationService, retrofit)

    /**
     * Provide Set Messages As Viewed Interact
     */
    @Provides
    @PerActivity
    fun provideSetMessagesAsViewedInteract(
            conversationService: IConversationService,
            retrofit: Retrofit
    ): SetMessagesAsViewedInteract
        = SetMessagesAsViewedInteract(conversationService, retrofit)

    /**
     * Provide Image Loader
     * @param picasso
     * @return
     */
    @Provides
    @PerActivity
    fun provideImageLoader(picasso: Picasso): ImageLoader {
        return ImageLoader { imageView, url, payload ->
            picasso.load(url).placeholder(R.drawable.user_default).error(R.drawable.user_default).into(imageView) }
    }


    /**
     * Provide Conversation Message List View Model
     */
    @Provides
    @PerActivity
    fun provideConversationMessageListViewModel(
            addMessageInteract: AddConversationMessageInteract,
            getConversationMessagesInteract: GetConversationMessagesInteract,
            deleteConversationMessagesInteract: DeleteConversationMessagesInteract,
            getConversationMessagesForMembersInteract: GetConversationMessagesForMembersInteract,
            deleteConversationMessagesForMembersInteract: DeleteConversationMessagesForMembersInteract,
            getConversationInteract: GetConversationInteract,
            getConversationForMembersInteract: GetConversationForMembersInteract,
            createConversationForMembersInteract: CreateConversationForMembersInteract,
            setMessagesAsViewedInteract: SetMessagesAsViewedInteract
    ): ConversationMessageListViewModel
        = ConversationMessageListViewModel(addMessageInteract, getConversationMessagesInteract,
            deleteConversationMessagesInteract, getConversationMessagesForMembersInteract,
            deleteConversationMessagesForMembersInteract, getConversationInteract, getConversationForMembersInteract,
            createConversationForMembersInteract, setMessagesAsViewedInteract)

    /**
     * Provide Conversation List View Model
     */
    @Provides
    @PerActivity
    fun provideConversationListViewModel(
            getConversationsForMemberInteract: GetConversationsForMemberInteract,
            deleteConversationInteract: DeleteConversationInteract,
            deleteAllConversationForMemberInteract: DeleteAllConversationForMemberInteract
    ): ConversationListViewModel
        = ConversationListViewModel(getConversationsForMemberInteract,
            deleteConversationInteract, deleteAllConversationForMemberInteract)


}