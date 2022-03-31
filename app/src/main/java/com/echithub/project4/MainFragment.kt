package com.echithub.project4


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log

import android.view.*

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.echithub.project4.databinding.FragmentMainBinding
import com.echithub.project4.utils.*


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var downloadStarted = false
    private var downloadLink: String = ""
    private var downloadCompleted: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container,false)
        setupBroadcastReceiver()
        binding.btnDownload.setOnClickListener {
            binding.btnDownload.text = "DOWNLOAD IN PROGRESS...."
            download(downloadLink, FILE_NAME)
            runAnimation()
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

    private fun runAnimation(){
//        val animator = ObjectAnimator.ofFloat(binding.btnDownload,View.ALPHA,0.2f)
        val animator = ObjectAnimator.ofArgb(binding.btnDownload,"backgroundColor", Color.BLUE, Color.YELLOW)
        animator.duration = 1000
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                animator.repeatCount = 2
                animator.repeatMode = ObjectAnimator.RESTART
            }
            override fun onAnimationEnd(animation: Animator?) {

                if (!downloadCompleted) {
                    animator.start()

                }else{
                    animator.repeatCount = 0
                    animator.cancel()
                    binding.btnDownload.setBackgroundColor(Color.GREEN)

                }
            }
        })
        animator.start()
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

    private fun setupBroadcastReceiver(){
        val br = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.i("Fragment Receiver",intent.toString())
                downloadCompleted = true
                binding.btnDownload.text = "DOWNLOAD COMPETED"
                binding.btnDownload.setBackgroundColor(Color.GREEN)
            }
        }
        context?.registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


}