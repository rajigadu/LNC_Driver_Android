package com.lncdriver.dbh.base

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.lncdriver.R
import com.lncdriver.databinding.FragmentAlertMessageDialogBinding
import com.lncdriver.dbh.utils.FragmentCallback

/**
 * Create by Siru Malayil on 26-04-2023.
 */
class AlertMessageDialogFragment : DialogFragment() {
    private var binding: FragmentAlertMessageDialogBinding? = null
    private var message: String? = null
    private var negativeButton: Boolean = false
    private var showRetry: Boolean = false
    private var fragmentCallback: FragmentCallback? = null

    companion object {
        const val ACTION_OK = true
        const val ACTION_CANCEL = false
        const val ACTION_RETRY = true

        fun newInstance(
            message: String?,
            negativeButton: Boolean = false,
            showRetry: Boolean = false,
            fragmentCallBack: FragmentCallback? = null
        ) = AlertMessageDialogFragment().apply {
            this.message = message
            this.showRetry = showRetry
            this.negativeButton = negativeButton
            this.fragmentCallback = fragmentCallBack
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            setStyle(STYLE_NO_FRAME, android.R.style.Theme)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        isCancelable = false
        binding = FragmentAlertMessageDialogBinding.inflate(inflater, container, false)

        if (showRetry) {
            binding?.btnPositive?.visibility = View.GONE
            binding?.btnRetry?.visibility = View.VISIBLE
        }
        if (negativeButton) {
            binding?.btnNegative?.visibility = View.VISIBLE
        } else binding?.btnNegative?.visibility = View.GONE

        binding?.btnPositive?.setOnClickListener {
            fragmentCallback?.onResult(ACTION_OK)
            dismissAllowingStateLoss()
        }

        binding?.btnNegative?.setOnClickListener {
            fragmentCallback?.onResult(ACTION_CANCEL)
            dismissAllowingStateLoss()
        }

        binding?.btnRetry?.setOnClickListener {
            fragmentCallback?.onResult(ACTION_RETRY)
            dismissAllowingStateLoss()
        }

        return binding?.root
    }

    override fun dismiss() {
        try {
            super.dismissAllowingStateLoss()
        } catch (e: Exception) {
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message = if (message.isNullOrBlank() || message == "null") getString(R.string.something_went_wrong) else message
        binding?.tvMessage?.text = message.toString().trim()
        binding?.tvHeaderTitle?.text = getString(R.string.app_name_1)
    }

    override fun onResume() {
        super.onResume()
        val params: WindowManager.LayoutParams? = dialog?.window?.attributes
        params?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}