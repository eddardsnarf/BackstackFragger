package com.greensocks.backstackfragger

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Fade
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.greensocks.backstackfragger.databinding.FragmentBackstackContainerBinding

abstract class BackstackContainerFragment : Fragment() {
    protected var containerName: String? = null
    private val KEY_CLEAR_CHILD_FRAG_MANAGER_BACKSTACK: String =
        "key_clear_child_frag_manager_backstack"
    lateinit var binding: FragmentBackstackContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBackstackContainerBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToFragment(arguments)
    }

    fun childNavigatesTo(bundle: Bundle?) {
        val bundleTabName = bundle?.getString(KEY_BOTTOM_TAB_LABEL)
        if (bundleTabName != null
            && bundleTabName == containerName
        ) {
            navigateToFragment(bundle)
        } else {
            //TODO: send navigation request to mainActivity
        }
    }

    abstract fun getFragmentInstance(bundle: Bundle?): Fragment

    private fun navigateToFragment(bundle: Bundle?) {
        val fragment: Fragment = getFragmentInstance(bundle)
        val shouldClearBackstack =
            bundle?.getBoolean(KEY_CLEAR_CHILD_FRAG_MANAGER_BACKSTACK) ?: true
        clearBackstackIfWanted(shouldClearBackstack)
        prepareFragmentForTransitions(fragment)

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.backstack_fragment_container_view, fragment)
        transaction.setPrimaryNavigationFragment(fragment)
        // we shouldn't add the transaction to the backStack
        if (!shouldClearBackstack)
            transaction.addToBackStack(null)
        transaction.setReorderingAllowed(true)
        transaction.commitAllowingStateLoss()
    }

    private fun prepareFragmentForTransitions(fragment: Fragment) {
        val transitionSet = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
        fragment.sharedElementEnterTransition = transitionSet
        fragment.sharedElementReturnTransition = transitionSet
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
    }

    private fun clearBackstackIfWanted(shouldClear: Boolean) {
        // go to root of childFragmentManager's backStack
        if (shouldClear && childFragmentManager.backStackEntryCount > 0) {
            val firstFrag = childFragmentManager.getBackStackEntryAt(0)
            childFragmentManager.popBackStack(
                firstFrag.id,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }


}
