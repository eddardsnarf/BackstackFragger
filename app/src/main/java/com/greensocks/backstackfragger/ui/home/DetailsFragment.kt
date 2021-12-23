package com.greensocks.backstackfragger.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.greensocks.backstackfragger.databinding.FragmentHomeBinding

class DetailsFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        arguments?.getString(KEY_DETAILS_TEXT_PARAM)?.let {
            textView.text = it
        }

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

}

const val KEY_DETAILS_TEXT_PARAM: String = "key_details_text_param"
