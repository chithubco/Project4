package com.echithub.project4

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.echithub.project4.databinding.FragmentMainBinding
import com.echithub.project4.utils.ButtonState
import com.echithub.project4.utils.FILE_NAME
import com.echithub.project4.utils.NotificationsHelper
import com.echithub.project4.utils.URL_UDACITY


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var downloadStarted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container,false)

        binding.btnDownload.setOnClickListener {

            download(URL_UDACITY, FILE_NAME)
            NotificationsHelper(requireContext()).createNotification()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun download(url: String,fileName: String) {
        downloadStarted = true
        (activity as MainActivity).download(Uri.parse(url),fileName) // Download activity in Main actitivty
    }

    fun onPermissionResult(permissionGranted: Boolean){
        if (permissionGranted && downloadStarted){

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_main,menu)
    }

}