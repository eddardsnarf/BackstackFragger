package com.greensocks.backstackfragger

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.greensocks.backstackfragger.databinding.ActivityMainBinding
import com.greensocks.backstackfragger.ui.dashboard.DashboardContainerFragment
import com.greensocks.backstackfragger.ui.home.HomeContainerFragment
import com.greensocks.backstackfragger.ui.home.KEY_HOME_TEXT_PARAM
import com.greensocks.backstackfragger.ui.notifications.NotificationsContainerFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mBottomNavFragmentManager: BottomNavFragmentManager
    private val menuItemList: MutableList<BottomTabItem> by lazy {
        BottomTabItem.values().toCollection(ArrayList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mBottomNavFragmentManager =
            object :
                BottomNavFragmentManager(binding.fragmentContainerView, supportFragmentManager) {
                override fun getNewItem(position: Int, bundle: Bundle?): Fragment =
                    when (position) {
                        BottomTabItem.HOME.ordinal -> HomeContainerFragment.newInstance(bundle)
                        BottomTabItem.DASHBOARD.ordinal -> DashboardContainerFragment.newInstance(
                            bundle
                        )
                        BottomTabItem.NOTIFICATION.ordinal -> NotificationsContainerFragment.newInstance(
                            bundle
                        )
                        else -> HomeContainerFragment.newInstance()
                    }
            }
        val navView: BottomNavigationView = binding.navView.apply {
            addMenuItems()
            setupItemSelectionListener()
        }

        val bundle = intent?.data?.toBundle()
        navView.navigateTo(bundle)
    }

    private fun Uri?.toBundle(): Bundle? {
        if (this == null) return null
        val bundle = Bundle()
        bundle.putString(KEY_BOTTOM_TAB_LABEL, host)
        bundle.putString(KEY_SELECTED_PAGE, pathSegments?.get(0))
        bundle.putString(KEY_HOME_TEXT_PARAM, pathSegments?.get(1))
        return bundle
    }

    private fun BottomNavigationView.setSelectedItemIndex(index: Int) {
        menu.getItem(index).isChecked = true
    }

    private fun BottomNavigationView.setupItemSelectionListener() {
        setOnItemSelectedListener { item ->
            val selectedIndex = item.itemId - Menu.FIRST
            mBottomNavFragmentManager.swapFragment(selectedIndex)
            true
        }
    }

    private fun BottomNavigationView.navigateTo(bundle: Bundle?) {
        //TODO: decide according to intent bundle to which tab we should go.
        val selectedIndex = when (bundle?.getString(KEY_BOTTOM_TAB_LABEL)) {
            BottomTabItem.HOME.name.lowercase() -> BottomTabItem.HOME.ordinal
            else -> BottomTabItem.HOME.ordinal
        }
        setSelectedItemIndex(selectedIndex)
        mBottomNavFragmentManager.swapFragment(selectedIndex, bundle)
    }

    private fun BottomNavigationView.addMenuItems() {
        // we could add the menuItems automatically using the xml pattern,
        // but this manual way of doing things gives us more control
        // regarding the way they are added, and also a bit of optimisation
        // for the moment when a tab item is selected.
        for ((index, item) in menuItemList.withIndex()) {
            menu.add(
                Menu.NONE,
                Menu.FIRST + index,
                Menu.NONE,
                item.labelResId
            )
                .setCheckable(true)
                .setIcon(item.iconResId)
        }
    }
}

enum class BottomTabItem(
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int
) {
    HOME(R.string.title_home, R.drawable.ic_home_black_24dp),
    DASHBOARD(R.string.title_dashboard, R.drawable.ic_dashboard_black_24dp),
    NOTIFICATION(R.string.title_notifications, R.drawable.ic_notifications_black_24dp)
}

const val KEY_BOTTOM_TAB_LABEL = "key_bottom_tab_label"
const val KEY_SELECTED_PAGE = "key_selected_page"
