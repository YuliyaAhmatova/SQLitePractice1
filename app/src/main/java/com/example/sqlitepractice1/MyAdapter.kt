package com.example.sqlitepractice1

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyAdapter(private val context: Context, productList: MutableList<Product>) :
    ArrayAdapter<Product>(context, R.layout.list_item, productList) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val product = getItem(position)
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val nameText = view?.findViewById<TextView>(R.id.nameTV)
        val weightText = view?.findViewById<TextView>(R.id.weightTV)
        val costText = view?.findViewById<TextView>(R.id.costTV)

        nameText?.text = "Name: ${product?.productName}"
        weightText?.text = "Weight: ${product?.productWeight}"
        costText?.text = "Cost: ${product?.productCost}"

        return view!!
    }
}