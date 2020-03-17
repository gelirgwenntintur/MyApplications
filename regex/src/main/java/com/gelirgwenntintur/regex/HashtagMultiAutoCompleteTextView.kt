package com.gelirgwenntintur.regex

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import androidx.core.content.ContextCompat
import java.util.regex.Pattern

class HashtagMultiAutoCompleteTextView(context: Context, attrs: AttributeSet?) :
    MultiAutoCompleteTextView(context, attrs) {

    private val TAG = HashtagMultiAutoCompleteTextView::class.qualifiedName

    init {
        threshold = 1
        setTokenizer(HashtagSpaceTokenizer())

        highlightHashtag()
    }

    fun highlightHashtag() {
        Log.d(TAG, "highlightHashtag text = $text")
        // There are two groups:
        // The first group is (^[#]{1}[A-Za-z0-9-_]*). Explanation: at the begining of the string (it is indicated like ^) the symbol # occurs exactly one time,
        //and it can be followed by 0 or more characters from the range [A-Za-z0-9-_].
        // The second group is  (\s{1}[#]+[A-Za-z0-9-_]*). Explanation: : at the begining of the group the space (equivalent to [\ t \ n \ r \ f])
        //occurs exactly one time,  and it can be followed by only one symbol #, and that it can be followed by 0 or more characters from the range [A-Za-z0-9-_]
        //For example, #1 hello #tag1tag2 #tag. The hashtags are #1, #tag1, #tag
        //For example, #1 hello ##tag1tag2 #tag.  The hashtags are #1, #, #tag
        val tagPattern =
            Pattern.compile("(^[#]{1}[A-Za-z0-9-_]*)|(\\s{1}[#]{1}[A-Za-z0-9-_]*)")
        val hashtagMatcher = tagPattern.matcher(text)
        while (hashtagMatcher.find()) {
            val hashtag = hashtagMatcher.group()
            val startIndex = hashtagMatcher.start()
            val endIndex = hashtagMatcher.end()
            Log.d(TAG, "hashtag = $hashtag, startIndex = $startIndex, endIndex = $endIndex")

            setBoldStyleSpan(startIndex, endIndex)

            setForegroundColorSpan(
                startIndex,
                endIndex,
                ContextCompat.getColor(context, R.color.colorAccent)
            )
        }
    }

    private fun setBoldStyleSpan(startIndex: Int, endIndex: Int) {
        editableText.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }

    private fun setForegroundColorSpan(startIndex: Int, endIndex: Int, color: Int) {
        editableText.setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }
}