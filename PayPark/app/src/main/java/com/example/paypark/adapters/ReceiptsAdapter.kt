package com.example.paypark.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.paypark.R
import com.example.paypark.model.Parking
import kotlinx.android.synthetic.main.parking_receipt.view.*

/*
User: hoangjam
Name: Jameson Hoang
Student ID: 991548515
Date: 2020-11-17
*/
class ReceiptsAdapter(
        val context: Context,
        val receiptsList: MutableList<Parking>,
        val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ReceiptsAdapter.ReceiptViewHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ReceiptsAdapter.ReceiptViewHolder {

        val view: View = LayoutInflater.from(context).inflate(R.layout.parking_receipt, null)
        return ReceiptViewHolder(view)
    }

    override fun getItemCount(): Int {
        return receiptsList.size
    }

    override fun onBindViewHolder(holder: ReceiptsAdapter.ReceiptViewHolder, position: Int) {
        holder.bind(receiptsList[position], itemClickListener)
    }

    inner class ReceiptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvDateParked: TextView = itemView.tvDateParked
        var tvBuildingCode: TextView = itemView.tvBuildingCode
        var tvUnitNumber: TextView = itemView.tvUnitNum
        var tvHoursParked: TextView = itemView.tvHoursParked

        fun bind(receipt: Parking, clickListener: OnItemClickListener){
            tvDateParked.setText(receipt.parkingDate.toString())
            tvBuildingCode.setText(receipt.buildingCode.toString())
            tvUnitNumber.setText(receipt.unitNumber.toString())
            tvHoursParked.setText(receipt.duration.toString())

            itemView.setOnClickListener{
                clickListener.onItemClicked(receipt)
            }
        }
    }
}

interface OnItemClickListener{
    fun onItemClicked(receipt: Parking)
}

// takehome - implement item long click listener to display alert dialog to confirm the delete operation fo the item
// hint - call deleteParking() from parkingViewModel
// use removeAt(position) with receiptList
// notify the adpater of data set changed