package com.greensocks.backstackfragger.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.greensocks.backstackfragger.BackstackContainerFragment
import com.greensocks.backstackfragger.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        textView.setOnClickListener {
            arguments?.putString(KEY_DETAILS_TEXT_PARAM,"detailed_perp")
            (parentFragment as BackstackContainerFragment).childNavigatesTo(arguments)
        }
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        handleArgBundle(arguments)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(bundle: Bundle?): Fragment {
            val f = HomeFragment()
            f.arguments = bundle
            return f
        }
    }

    private fun handleArgBundle(bundle: Bundle?) {
        bundle?.getString(KEY_HOME_TEXT_PARAM)?.let {
            homeViewModel.getText(it)
        }
    }
}

const val KEY_HOME_TEXT_PARAM: String = "key_home_text_param"
