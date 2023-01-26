package com.gmfrontier.paguelofacilchallenge.navigation

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.gmfrontier.paguelofacilchallenge.R

private val options = NavOptions.Builder()
    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
    .build()

fun Activity.findNavController(): NavController =
    this.findNavController(R.id.nav_host_fragment_content_main)

fun NavController.navigateWithAnimation(request: NavDeepLinkRequest) {
    this.navigate(request, options)
}

