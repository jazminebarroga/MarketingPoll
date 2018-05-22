package com.jpbbarroga.marketingpoll

import android.app.Activity
import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SeekBar

/**
 * Created by jeppie on 15/05/2018.
 */

class PollAdapter(private var activity: Activity, private var items: ArrayList<Person>): BaseAdapter() {

    private class ViewHolder(row: View?) {
        var seekBar: SeekBar? = null

        init {
            this.seekBar = row?.findViewById<SeekBar>(R.id.seekBar)

        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_layout, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder

        }

        var personDto = items[position]
      //  viewHolder.txtName?.text = userDto.name

        viewHolder.seekBar!!.max = 10


        val layerDrawable = activity?.getDrawable(R.drawable.custom_thumb) as? LayerDrawable
        val replace = activity?.getDrawable(personDto.thumb)

        val test = layerDrawable!!.setDrawableByLayerId(R.id.thumb_image, replace)



        viewHolder.seekBar!!.thumb = layerDrawable
        viewHolder.seekBar!!.progress = personDto.numVotes

        return view as View
    }



    override fun getItem(i: Int): Person {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}