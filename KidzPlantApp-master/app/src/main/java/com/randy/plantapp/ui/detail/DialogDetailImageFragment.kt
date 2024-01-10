package com.randy.plantapp.ui.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.randy.plantapp.R
import com.randy.plantapp.databinding.FragmentDialogDetailImageBinding

class DialogDetailImageFragment(
    private val imageUrl: String,
) : DialogFragment() {

    private var _binding: FragmentDialogDetailImageBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = (250 * resources.displayMetrics.density).toInt()
        dialog?.window?.apply {
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                width,
                height
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDialogDetailImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvImage.setBackgroundResource(R.drawable.bg_card_white)

        Glide.with(requireContext())
            .load(imageUrl)
            .into(binding.image)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}