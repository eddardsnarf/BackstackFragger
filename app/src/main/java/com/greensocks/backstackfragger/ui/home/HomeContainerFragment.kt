package com.greensocks.backstackfragger.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.greensocks.backstackfragger.BackstackContainerFragment

class HomeContainerFragment : BackstackContainerFragment() {
    override fun getFragmentInstance(bundle: Bundle?): Fragment {
        //TODO: decide according to intent bundle to which fragment we need to show
        return HomeFragment.newInstance(bundle)
    }

    companion object {
        fun newInstance(bundle: Bundle? = null): Fragment {
            val f = HomeContainerFragment()
            f.arguments = bundle
            return f
        }
    }
}
