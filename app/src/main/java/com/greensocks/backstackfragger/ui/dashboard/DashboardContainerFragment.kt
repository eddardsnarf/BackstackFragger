package com.greensocks.backstackfragger.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.greensocks.backstackfragger.BackstackContainerFragment
import com.greensocks.backstackfragger.ui.home.HomeContainerFragment

class DashboardContainerFragment : BackstackContainerFragment() {
    override fun getFragmentInstance(bundle: Bundle?): Fragment {
        //TODO: decide according to intent bundle to which fragment we need to show
        return DashboardFragment.newInstance()
    }
    companion object{
        fun newInstance(bundle: Bundle? = null): Fragment {
            val f = DashboardContainerFragment()
            f.arguments = bundle
            return f
        }
    }
}
