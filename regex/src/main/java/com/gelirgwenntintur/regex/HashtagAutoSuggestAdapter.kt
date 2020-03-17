package com.gelirgwenntintur.regex

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.gelirgwenntintur.regex.entity.Hashtag
import java.util.regex.Pattern

private val TAG = HashtagAutoSuggestAdapter::class.java.simpleName

class HashtagAutoSuggestAdapter(context: Context, val filterSharpSymbol: Boolean = true, val showFollowersCount: Boolean = true) :
    ArrayAdapter<Hashtag>(context, 0), Filterable {

    private var data: MutableList<Hashtag> = mutableListOf()

    fun setData(list: List<Hashtag>) {
        data.clear()
        data.addAll(list)
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Hashtag {
        return data[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.hashtag_item_view,
            parent,
            false
        )

        val hashtagNameTextView = view.findViewById<TextView>(R.id.hashtagName)
        val hashtagFollowersCountTextView = view.findViewById<TextView>(R.id.hashtagFollowersCount)

        val item = data.get(position)
        hashtagNameTextView.text = item.hashtag
        if (showFollowersCount) {
            hashtagFollowersCountTextView.text =
                context.getString(
                    R.string.hashtag_followers_count,
                    toFollowerCountString(item.followCount)
                )
        }
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                Log.d(TAG, "performFiltering constraint = $constraint")

                val tagPattern =
                    if (filterSharpSymbol) Pattern.compile("[#]+[A-Za-z0-9-_]+")
                    else Pattern.compile("[A-Za-z0-9-_]+")

                val tagMatcher = tagPattern.matcher(constraint)

                if (tagMatcher.find()) {

                    Log.d(TAG, "performFiltering found hashtag: $constraint")

                    filterResults.values = data
                    filterResults.count = data.size
                }
                return filterResults
            }

            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?
            ) {
                Log.d(
                    TAG,
                    "publishResults constraint = $constraint, results count = ${results?.count}"
                )

                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as Hashtag).hashtag
            }

        }
    }


    private fun toFollowerCountString(number: Int): String {
        return when (number) {
            in 0..1000 ->
                String.format("%d", number)
            in 1000..10_000 -> {
                val res = number / 1000.0
                String.format("%.1f", res)
            }
            in 10_000..1_000_000 -> {
                val res = number / 1000
                String.format("%dK", res)
            }

            in 1_000_000..10_000_000 -> {
                val res = number / 1_000_000.0
                String.format("%.1fM", res)
            }
            else -> {
                val res = number / 1_000_000
                String.format("%dM", res)
            }
        }
    }
}