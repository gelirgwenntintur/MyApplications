package com.gelirgwenntintur.regex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.gelirgwenntintur.regex.entity.Hashtag
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_regex.*
import java.util.concurrent.TimeUnit

const val HASHTAG_PATTER_REGEX = "(^[#]{1}[A-Za-z0-9-_]+)|(\\s{1}[#]{1}[A-Za-z0-9-_]+)"

class RegexActivity : AppCompatActivity() {

    private val TAG = RegexActivity::class.qualifiedName

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regex)

        validHashTagTextView.text = String.format("Is hashtag inputted?: %b", isValidHashTags(hashtagMultiCompleteTextView.toString()))

        hashtagMultiCompleteTextView.highlightHashtag()

        hashtagMultiCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                Log.d(TAG, "afterTextChanged text: ${editable?.toString()}")
                hashtagMultiCompleteTextView.highlightHashtag()
                validHashTagTextView.text = String.format("Is hashtag inputted?: %b", isValidHashTags(editable.toString()))
            }
        })

    }

    private fun isValidHashTags(description: String): Boolean =
        description.isNotEmpty() && description.contains(HASHTAG_PATTER_REGEX.toRegex())

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}
