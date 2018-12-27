package org.bitspilani.ssms.messapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.act_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)

        setupBottomNav()
    }

    private fun setupBottomNav() {
        navHostFRA.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            val currentItem = when(destination.id) {
                R.id.menuFragment   -> 2
                R.id.noticeFragment -> 3
                R.id.moreFragment   -> 4
                else                -> 2
            }
            if(bottomNavAHB.currentItem != currentItem) {
                bottomNavAHB.setCurrentItem(currentItem, false)
            }
        }

        bottomNavAHB.apply {
            mapOf(
                "Profile" to R.drawable.ic_profile_24dp_24dp,
                "Grubs" to R.drawable.ic_grubs_24dp_24dp,
                "Menu" to R.drawable.ic_menu_24dp_24dp,
                "Notices" to R.drawable.ic_notice_24dp_24dp,
                "More" to R.drawable.ic_more_24dp_24dp
            )
                .forEach {
                    addItem(AHBottomNavigationItem(it.key, it.value))
                }

            setCurrentItem(2, false)

            titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

            accentColor = resources.getColor(R.color.pnk01)
            inactiveColor = resources.getColor(R.color.gry01)

            setDefaultBackgroundResource(R.drawable.sh_rounded_rectangle_wht01_16dp)

            setTitleTypeface(ResourcesCompat.getFont(context, R.font.cabin))

            setOnTabSelectedListener { position, wasSelected ->
                if(wasSelected) {
                    return@setOnTabSelectedListener false
                }

                val options = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.menuFragment, false).build()

                val destination = when(position) {
                    0    -> R.id.menuFragment
                    1    -> R.id.menuFragment
                    2    -> R.id.menuFragment
                    3    -> R.id.noticeFragment
                    4    -> R.id.moreFragment
                    else -> throw IllegalStateException("BottomNav with position: $position")
                }

                navHostFRA.findNavController().navigate(destination, null, options)

                return@setOnTabSelectedListener true
            }
        }
    }

}
