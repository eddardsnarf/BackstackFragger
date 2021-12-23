package com.greensocks.backstackfragger

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Wrapper over the SDK's FragmentManager.
 * Its basic role is to swap in between "fragment-holding" fragments,
 * while also maintaining the state of the layouts inside these fragments
 * i.e. say  we are on fragment(index 0) , navigating deeper in the model hierarchy.
 * after swapping from fragment(index 0) to fragment(index 1) and then back to fragment(index 0),
 * we would have the last viewed layout of fragment(index 0).
 */
abstract class BottomNavFragmentManager(
    private var container: ViewGroup,
    private val mFragmentManager: FragmentManager
) {

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position - index of the item inside the [BottomNavigationView]
     */
    abstract fun getNewItem(position: Int, bundle: Bundle?): Fragment

    /**
     * Return a unique identifier for the item at the given position.
     *
     * The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.
     *
     * @param position Position within this adapter
     * @return Unique identifier for the item at position
     */
    private fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Creates the fragment tag in the following format: "android:switcher:$viewId:$id" .
     * This tag will be used to save the fragment in the FragmentManager.
     */
    private fun getFragmentTag(viewId: Int, id: Long): String {
        return "android:switcher:$viewId:$id"
    }

    fun getFragmentAt(position: Int): Fragment? {
        val tag = getFragmentTag(container.id, getItemId(position))
        return mFragmentManager.findFragmentByTag(tag)
    }

    fun getCurrentFragmentAt(position: Int): Fragment? {
        val taggedFrag = getFragmentAt(position)
        val curFrag = mFragmentManager.primaryNavigationFragment
        if (curFrag == taggedFrag) {
            return taggedFrag
        }
        return null
    }

    /**
     * Swaps the current-fragment with the fragment
     * found for the given [position].
     * If fragment is found in fragment manager at [position],
     * it is reattached, if not it's gonna be added.
     *
     * @param position
     * @return newly attached/added fragment at position
     */
    fun swapFragment(position: Int, bundle: Bundle? = null): Fragment {
        val tag = getFragmentTag(container.id, getItemId(position))
        val fragmentTransaction = mFragmentManager.beginTransaction()
        // Hide existing primary fragment
        val curFrag = mFragmentManager.primaryNavigationFragment
        if (curFrag != null) {
            fragmentTransaction.hide(curFrag)
        }

        // If fragment manager doesn't have an instance of the fragment,
        // get an instance and add it to the transaction.
        // Else, attach the instance to transaction.
        var fragment: Fragment? = mFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = getNewItem(position, bundle)
            fragmentTransaction.add(container.id, fragment, tag)
        } else {
            fragmentTransaction.show(fragment)
        }
        // Set fragment as primary navigator for child manager back stack to be handled by system
        fragmentTransaction.setPrimaryNavigationFragment(fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNowAllowingStateLoss()

        return fragment
    }
}
