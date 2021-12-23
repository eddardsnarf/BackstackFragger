package com.greensocks.backstackfragger.ui.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.greensocks.backstackfragger.BackstackContainerFragment
import com.greensocks.backstackfragger.ui.home.HomeContainerFragment

class NotificationsContainerFragment : BackstackContainerFragment() {
    override fun getFragmentInstance(bundle: Bundle?): Fragment {
        //TODO: decide according to intent bundle to which fragment we need to show
        return NotificationsFragment.newInstance()
    }

    companion object {
        fun newInstance(bundle: Bundle? = null): Fragment {
            val f = NotificationsContainerFragment()
            f.arguments = bundle
            return f
        }
    }
}
