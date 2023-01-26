package com.gmfrontier.paguelofacilchallenge

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.gmfrontier.paguelofacilchallenge.databinding.ActivityMainBinding
import com.gmfrontier.paguelofacilchallenge.navigation.findNavController
import com.gmfrontier.paguelofacilchallenge.navigation.navigateWithAnimation
import com.gmfrontier.transaction_presentation.navigation.Route
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val request = NavDeepLinkRequest.Builder
                        .fromUri(Route.HOME.toUri())
                        .build()
                    findNavController().navigateWithAnimation(request)
                    true
                }
                R.id.send_money -> {
                    val title = "Pagar"
                    val titleArgument = URLEncoder.encode(title, "utf-8")
                    val route = getString(com.gmfrontier.transaction_presentation.R.string.route_fragments) + titleArgument
                    val request = NavDeepLinkRequest.Builder
                        .fromUri(route.toUri())
                        .build()
                    findNavController().navigateWithAnimation(request)
                    true
                }
                R.id.receive_money -> {
                    val title = "Cobrar"
                    val titleArgument = URLEncoder.encode(title, "utf-8")
                    val route = getString(com.gmfrontier.transaction_presentation.R.string.route_fragments) + titleArgument
                    val request = NavDeepLinkRequest.Builder
                        .fromUri(route.toUri())
                        .build()
                    findNavController().navigateWithAnimation(request)
                    true
                }
                R.id.balance -> {
                    val title = "Balance"
                    val titleArgument = URLEncoder.encode(title, "utf-8")
                    val route = getString(com.gmfrontier.transaction_presentation.R.string.route_fragments) + titleArgument
                    val request = NavDeepLinkRequest.Builder
                        .fromUri(route.toUri())
                        .build()
                    findNavController().navigateWithAnimation(request)
                    true
                }
                else -> false
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}