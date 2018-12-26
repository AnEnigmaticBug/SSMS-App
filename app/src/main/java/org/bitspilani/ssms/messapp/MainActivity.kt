package org.bitspilani.ssms.messapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
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

                when(position) {
                    0 -> Toast.makeText(this@MainActivity, "Profile", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(this@MainActivity, "Grubs", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this@MainActivity, "Menu", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(this@MainActivity, "Notices", Toast.LENGTH_SHORT).show()
                    4 -> Toast.makeText(this@MainActivity, "More", Toast.LENGTH_SHORT).show()
                }
                return@setOnTabSelectedListener true
            }
        }
    }

}
