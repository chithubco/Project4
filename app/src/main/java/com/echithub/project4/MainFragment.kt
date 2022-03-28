package com.echithub.project4

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.echithub.project4.databinding.FragmentMainBinding
import com.echithub.project4.utils.*


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var downloadStarted = false
    private var downloadLink: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container,false)

        binding.btnDownload.setOnClickListener {

            download(downloadLink, FILE_NAME)
        }

        binding.rgSelectDownloadSource.setOnCheckedChangeListener { group, checkedId ->
            downloadLink = when(checkedId){
                R.id.rb_glide -> URL_GLIDE
                R.id.rb_udacity -> URL_UDACITY
                R.id.rb_retrofit -> URL_RETROFIT
                else -> URL_RETROFIT
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun download(url: String,fileName: String) {
        downloadStarted = true
        if (downloadLink.isNullOrEmpty() or  downloadLink.isNullOrBlank()){
            Toast.makeText(context,"Please Select a Download File",Toast.LENGTH_LONG).show()
        }else{
            (activity as MainActivity).download(Uri.parse(url),fileName) // Download activity in Main actitivty
        }

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