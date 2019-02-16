package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import android.widget.ImageView
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IConversationService
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
            conversationService: IConversationService,
            retrofit: Retrofit
    ): AddConversationMessageInteract
        = AddConversationMessageInteract(appContext, conversationService, retrofit)

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
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationInteract
            = GetConversationInteract(appContext, conversationService, retrofit)


    /**
     * Provide Get Conversation Messages Interact
     */
    @Provides
    @PerActivity
    fun provideGetConversationMessagesInteract(
            appContext: Context,
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationMessagesInteract
            = GetConversationMessagesInteract(appContext, conversationService, retrofit)

    /**
     * Provide Get Conversation For Member Interact
     */
    @Provides
    @PerActivity
    fun provideGetConversationForMemberInteract(
            appContext: Context,
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationForMemberInteract
        = GetConversationForMemberInteract(appContext, conversationService, retrofit)


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
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationMessagesForMembersInteract
        = GetConversationMessagesForMembersInteract(appContext, conversationService, retrofit)


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
            conversationService: IConversationService,
            retrofit: Retrofit
    ): GetConversationForMembersInteract
        = GetConversationForMembersInteract(appContext, conversationService, retrofit)

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
            getConversationForMembersInteract: GetConversationForMembersInteract
    ): ConversationMessageListViewModel
        = ConversationMessageListViewModel(addMessageInteract, getConversationMessagesInteract,
            deleteConversationMessagesInteract, getConversationMessagesForMembersInteract,
            deleteConversationMessagesForMembersInteract, getConversationInteract, getConversationForMembersInteract)

    /**
     * Provide Conversation List View Model
     */
    @Provides
    @PerActivity
    fun provideConversationListViewModel(
            getConversationForMemberInteract: GetConversationForMemberInteract,
            deleteConversationInteract: DeleteConversationInteract,
            deleteAllConversationForMemberInteract: DeleteAllConversationForMemberInteract
    ): ConversationListViewModel
        = ConversationListViewModel(getConversationForMemberInteract,
            deleteConversationInteract, deleteAllConversationForMemberInteract)


}