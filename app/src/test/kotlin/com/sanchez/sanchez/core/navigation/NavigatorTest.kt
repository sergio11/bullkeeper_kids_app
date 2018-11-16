/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sanchez.sanchez.core.navigation

import com.sanchez.sanchez.AndroidTest
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInActivity
import com.sanchez.sanchez.features.movies.MoviesActivity
import com.sanchez.sanchez.shouldNavigateTo
import com.sanchez.sanchez.bullkeeper_kids.presentation.SplashScreenActivity
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify


class NavigatorTest : AndroidTest() {

    private lateinit var navigator: Navigator

    @Mock private lateinit var authenticatorService: AuthenticatorService

    @Before fun setup() {
        navigator = Navigator(authenticatorService)
    }

    @Test fun `should forward user to login screen`() {
        whenever(authenticatorService.userLoggedIn()).thenReturn(false)

        navigator.showMain(activityContext())

        verify(authenticatorService).userLoggedIn()
        SplashScreenActivity::class shouldNavigateTo SignInActivity::class
    }

    @Test fun `should forward user to movies screen`() {
        whenever(authenticatorService.userLoggedIn()).thenReturn(true)

        navigator.showMain(activityContext())

        verify(authenticatorService).userLoggedIn()
        SplashScreenActivity::class shouldNavigateTo MoviesActivity::class
    }
}
