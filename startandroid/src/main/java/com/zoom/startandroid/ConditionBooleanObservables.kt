package com.zoom.startandroid

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Операторы условий
 *  Официальная документация http://reactivex.io/documentation/operators.
 */
class ConditionAndBooleanObservables {

    private val TAG = ConditionAndBooleanObservables::class.qualifiedName
    private val compositeDisposable = CompositeDisposable()

    init {
        //takeUntil()
        //all()
        amb()
    }

    /**
     * Оператор takeUntil будет брать элементы пока не попадется элемент,
     * удовлетворяющий определенному условию.
     */
    fun takeUntil() {
        Log.d(TAG, "takeUntil")
        compositeDisposable.add(Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .takeUntil { it == 5 }
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
            ))
    }

    /**
     * Оператор all позволяет узнать все ли элементы удовлетворяют указанному условию.
     */
    private fun all() {
        Log.d(TAG, "all")
        compositeDisposable.add(Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .takeUntil { it < 10 }
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
            ))
    }

    /**
     * Берет несколько Observable и ждет, кто из них первый пришлет данные.
     * Далее, оператор будет возвращать элементы только из этого первого Observable.
     */
    private fun amb() {
        val observable1 = Observable.interval(1000, 300, TimeUnit.MILLISECONDS)
            .take(10)
        val observable2 = Observable.interval(700, 500, TimeUnit.MILLISECONDS)
            .take(10)
            .map {
                it+100
            }

        val ambDisposable = Observable.ambArray(observable1, observable2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
        compositeDisposable.add(ambDisposable)
    }

    fun onClear() {
        compositeDisposable.clear()
    }
}