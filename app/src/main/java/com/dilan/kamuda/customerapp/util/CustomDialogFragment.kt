package com.dilan.kamuda.customerapp.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.model.order.OrderItem

class CustomDialogFragment : DialogFragment() {
    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_POSITIVE_BUTTON_TEXT = "positive_button_text"
        private const val ARG_NEGATIVE_BUTTON_TEXT = "negative_button_text"
        private const val ARG_NEUTRAL_BUTTON_TEXT = "neutral_button_text"
        private const val ARG_CHECKED_ITEMS = "list_data"

        fun newInstance(
            title: String,
            message: String,
            positiveButtonText: String = "OK",
            negativeButtonText: String? = null,
            neutralButtonText: String? = null,
            checkedItems: List<OrderItem>,
        ): CustomDialogFragment {
            val fragment = CustomDialogFragment()
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
                putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
                putString(ARG_NEUTRAL_BUTTON_TEXT, neutralButtonText)
                putSerializable(ARG_CHECKED_ITEMS, ArrayList(checkedItems))
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = requireArguments().getString(ARG_TITLE, "")
        val message = requireArguments().getString(ARG_MESSAGE, "")
        val checkedItems = arguments?.getSerializable(ARG_CHECKED_ITEMS) as? List<OrderItem>

        val inflater = requireActivity().layoutInflater
        val customView = inflater.inflate(R.layout.dialog_layout_custom, null)

        customView.findViewById<TextView>(R.id.textViewTitle).text = title
        customView.findViewById<TextView>(R.id.textViewMessage).text = message

        val listViewCheckedItems = customView.findViewById<ListView>(R.id.listViewCheckedItems)
        val checkedItemNames = checkedItems?.map { it.name } ?: emptyList()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            checkedItemNames
        )
        listViewCheckedItems.adapter = adapter

        val builder = AlertDialog.Builder(requireContext())
            .setView(customView)
            .setPositiveButton(
                requireArguments().getString(ARG_POSITIVE_BUTTON_TEXT)
            ) { _, _ -> positiveActionListener?.invoke() }
            .setNegativeButton(
                requireArguments().getString(ARG_NEGATIVE_BUTTON_TEXT)
            ) { _, _ -> negativeActionListener?.invoke() }

        requireArguments().getString(ARG_NEUTRAL_BUTTON_TEXT)?.let {
            builder.setNeutralButton(it) { _, _ -> neutralActionListener?.invoke() }
        }

        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false) // Prevent dismissing by touching outside the dialog
        alertDialog.setCancelable(false) // Prevent dismissing by pressing the back button

        return alertDialog
    }

    private var positiveActionListener: (() -> Unit)? = null

    private var negativeActionListener: (() -> Unit)? = null

    private var neutralActionListener: (() -> Unit)? = null

    fun setPositiveActionListener(listener: () -> Unit) {
        positiveActionListener = listener
    }

    fun setNegativeActionListener(listener: () -> Unit) {
        negativeActionListener = listener
    }

    fun setNeutralActionListener(listener: () -> Unit) {
        neutralActionListener = listener
    }
}