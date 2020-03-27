package com.gelirgwenntintur.rxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RxAppActivity : AppCompatActivity() {

    private val TAG = RxAppActivity::class.qualifiedName

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_app)

        loadContactsOneByOne()

        loadAllContacts()
    }

    /** Задача #1  получить список контактов из интернета, затем для кадого контакта выполнить запрос на сервер для получение статуса
     * и вывести в список по мере получение данных о каждом контакте, то есть друг за другом.
     * Для имитации запроса на сервер будут использоваться заглушки.
     * Главное понять как работаю операторы.
     */
    private fun loadContactsOneByOne() {
        Log.d(TAG, "=== loadContactsOneByOne===")

        val contactsDisposable = getContacts()
            .subscribeOn(Schedulers.io())
            .flatMap { contacts ->
                Observable.fromIterable(contacts)
                    .map { item ->
                        item.copy(
                            status = ContactStatus.LOADED
                        )
                    }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { contact ->
                    Log.d(TAG, "loadContactsOneByOne onNext:  contact info: $contact")
                },

                onComplete = {
                    Log.d(TAG, "loadContactsOneByOne onComplete")
                },

                onError = { ex ->
                    Log.e(TAG, "loadContactsOneByOne onError: Error in getting contacts $ex")
                }
            )

        compositeDisposable.add(contactsDisposable)
    }

    /** Задача #2 отличается от задачи #1 тем, что нужно заполнить статус для всех элементов вместо, а потом их отобразить в списке
     * Для этого используется оператор преобразования toList()
     */
    private fun loadAllContacts() {
        Log.d(TAG, "==== loadAllContacts ====")

        val contactsDisposable = getContacts()
            .subscribeOn(Schedulers.io())
            .flatMap { contacts ->
                Observable.fromIterable(contacts)
                    .map { item ->
                        item.copy(
                            status = ContactStatus.LOADED
                        )
                    }.toList().toObservable()
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { contact ->
                    Log.d(TAG, "loadAllContacts onNext:  all contacts: $contact")
                },

                onComplete = {
                    Log.d(TAG, "loadAllContacts onComplete")
                },

                onError = { ex ->
                    Log.e(TAG, "loadAllContacts onError: Error in getting contacts $ex")
                }
            )

        compositeDisposable.add(contactsDisposable)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun getContacts(): Observable<List<Contact>> =
        Observable.just(true).subscribeOn(Schedulers.io()).flatMap {
            val contacts = mutableListOf<Contact>()
            contacts.add(Contact(1, "Name 1", ContactStatus.DEFAULT))
            contacts.add(Contact(2, "Name 2", ContactStatus.DEFAULT))
            contacts.add(Contact(3, "Name 3", ContactStatus.DEFAULT))
            contacts.add(Contact(4, "Name 4", ContactStatus.DEFAULT))
            contacts.add(Contact(5, "Name 5", ContactStatus.DEFAULT))
            contacts.add(Contact(6, "Name 6", ContactStatus.DEFAULT))
            Observable.just(contacts)
        }

}
