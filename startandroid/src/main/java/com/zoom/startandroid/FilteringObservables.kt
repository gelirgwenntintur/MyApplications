package com.zoom.startandroid

import android.util.Log
import android.widget.EditText
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Операторы фильтрации
 * Официальная документация http://reactivex.io/documentation/operators.html
 */
class FilteringObservables {

    private val TAG = FilteringObservables::class.qualifiedName
    private val compositeDisposable = CompositeDisposable()

    init {
        take()
        skip()

        filter()

        distinct()
        distinctUntilChanged()
    }

    /**
     *  Оператор debounce будет отсеивать передаваемые ему данные,
     *  если между ними пауза меньше, чем указанное время.
     *  Например, чтобы отсеять излишние срабатывания поиска.
     */
    fun debounce(searchEditText: EditText) {
        Log.d(TAG, "debounce")
        val key = "Kate"
        compositeDisposable.add(
            searchEditText.textChanges()
                .skip(1)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map {
                    val result = it.contains(key)
                    Log.d(TAG, "Search result: $result")
                }.subscribe()
        )

    }

    /**
     * Оператор filter может отсеять только НУЖНЫЕ элементы.
     */
    private fun filter() {
        Log.d(TAG, "filter")
        val filterDisposable = Observable.fromArray("1", "25", "3", "4", "55", "6", "65", "1")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .filter {
                it.contains("5")
            }
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onNext: $it")
                },
                onError = {
                    Log.d(TAG, "onError: $it")
                },

                onComplete = {
                    Log.d(TAG, "onComplete")
                }
            )
        compositeDisposable.add(filterDisposable)
    }

    /**
     * Оператор distinct отсеет дубликаты
     */
    private fun distinct() {
        Log.d(TAG, "distinct")
        val distinctDisposable = Observable.fromArray("1", "2", "3", "4", "5", "6", "6", "1")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .distinct()
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onNext: $it")
                },
                onError = {
                    Log.d(TAG, "onError: $it")
                },

                onComplete = {
                    Log.d(TAG, "onComplete")
                }
            )
        compositeDisposable.add(distinctDisposable)

    }

    /**
     * Оператор distinctUntilChanged  будет отображать значение, только если оно отличаются от предыдущего.
     * Для определения разницы между значениями по умолчанию используется обычное сравнение (то есть «===»).
     *
     * Используется для того, чтобы избежать дублирования сетевых вызовов.
     * Например, последний поисковой запрос был «abc», затем пользователь удалил «c» и заново ввел «c».
     * Результат снова «abc». Таким образом, оператор distinctUntilChanged отсеивает повторяющиеся последовательно переданные ему элементы.
     * !!! То есть нет никакого смысла снова искать "abc", так как предыдцщий поиск уже был с таким же ключем.
     */
    private fun distinctUntilChanged() {
        Log.d(TAG, "distinctUntilChanged")
        val distinctDisposable = Observable.fromArray(1, 1, 2, 2, 2, 1, 1, 2, 3, 3, 4)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onNext: $it")
                },
                onError = {
                    Log.d(TAG, "onError: $it")
                },

                onComplete = {
                    Log.d(TAG, "onComplete")
                }
            )
        compositeDisposable.add(distinctDisposable)

    }

    /**
     * Оператор skip пропустит первые элементы.
     */
    private fun skip() {
        Log.d(TAG, "skip")
        val skipDisposable = Observable.fromArray("1", "2", "3", "4", "5", "6", "7")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .skip(3)
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onNext: $it")
                },
                onError = {
                    Log.d(TAG, "onError: $it")
                },

                onComplete = {
                    Log.d(TAG, "onComplete")
                }
            )
        compositeDisposable.add(skipDisposable)
    }

    /**
     * take возьмет только указанное количество первых элементов из переданной ему последовательности
     * и сформирует из них новую последовательность.
     */
    private fun take() {
        Log.d(TAG, "take")
        val takeDisposable = Observable.fromArray("1", "2", "3", "4", "5", "6", "7")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .take(3)
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onNext: $it")
                },
                onError = {
                    Log.d(TAG, "onError: $it")
                },

                onComplete = {
                    Log.d(TAG, "onComplete")
                }
            )
        compositeDisposable.add(takeDisposable)
    }
}