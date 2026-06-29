package com.orbix.ui.navigation

import android.widget.Toast

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.orbix.ui.model.AuthResponse
import com.orbix.ui.screen.LoginScreen
import com.orbix.ui.screen.SignUpScreen
import com.orbix.ui.util.AuthNavigation
import com.orbix.ui.viewmodel.SessionState
import com.orbix.ui.viewmodel.SessionViewModel
import com.orbix.ui.screen.TermsAndConditionsScreen
import com.orbix.ui.viewmodel.SignUpViewModel
import androidx.activity.ComponentActivity
import androidx.navigation.toRoute

@Composable
fun AppNavigation() {
    val sessionViewModel: SessionViewModel = viewModel()
    val navController = rememberNavController()
    val context = LocalContext.current

    val onAuthSuccess: (AuthResponse, Boolean) -> Unit = { response, isRegistration ->
        sessionViewModel.onLoginSuccessFromResponse(response)
        Toast.makeText(
            context,
            AuthNavigation.authSuccessMessage(response.roles, isRegistration),
            Toast.LENGTH_LONG
        ).show()
    }

    when (val state = sessionViewModel.sessionState) {
        SessionState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        SessionState.Unauthenticated -> {
            val activity = LocalContext.current as ComponentActivity
            val signUpViewModel: SignUpViewModel = viewModel(viewModelStoreOwner = activity)
            NavHost(
                navController = navController,
                startDestination = Login
            ) {
                composable<Login> {
                    LoginScreen(
                        onLogin = { response -> onAuthSuccess(response, false) },
                        onNavigateToSignUp = {
                            signUpViewModel.clearForm()
                            navController.navigate(SignUp)
                        }
                    )
                }

                composable<SignUp> {
                    SignUpScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToTerms = {
                            navController.navigate(TermsAndConditions(isSignUpFlow = true))
                        },
                        viewModel = signUpViewModel
                    )
                }

                composable<TermsAndConditions> { backStackEntry ->
                    val route: TermsAndConditions = backStackEntry.toRoute()
                    TermsAndConditionsScreen(
                        onBack = { navController.popBackStack() },
                        onAccept = {
                            navController.popBackStack()
                        },
                        signUpViewModel = if (route.isSignUpFlow) signUpViewModel else null,
                        onRegisterSuccess = { response ->
                            onAuthSuccess(response, true)
                        }
                    )
                }
            }
        }

        is SessionState.Authenticated -> {
            NavHost(
                navController = navController,
                startDestination = AuthNavigation.homeRoute(state.session.roles)
            ) {
                authenticatedRoutes(
                    navController = navController,
                    session = state.session,
                    sessionViewModel = sessionViewModel
                )
            }
        }
    }
}
