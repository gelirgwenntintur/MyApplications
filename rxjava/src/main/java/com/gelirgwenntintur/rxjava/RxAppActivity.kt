package com.gelirgwenntintur.rxjava

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class RxAppActivity : AppCompatActivity() {

    private val TAG = RxAppActivity::class.qualifiedName

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_app)

        //loadContactsOneByOne()

        //loadAllContacts()

        loadContactsWithAnalytics()
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

    /**
     * Отправить аналитику
     * 1. Все контакты загрузились успешно - CONTACTS_LOAD_SUCCESS
     * 2. Ошибка при загрузке всех контактов - CONTACTS_LOAD_FAILED
     * 3. При получении статуса о конкретном  - контакте произошла ошибка CONTACT_MATCH_FAILED
     * Идея, для ошибки при получении статуса контакта пробрасывать исключение и в onError делать обработку
     */

    class LookupContactException(errorMessage: String?): Exception(errorMessage)

    private fun loadContactsWithAnalytics() {
        Log.d(TAG, "=== loadContactsWithAnalytics ===")
        val getContactsDisposable = getContacts()
            .flatMap { contacts ->
                getContactsStatus(contacts)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { contacts ->
                    Log.d(TAG, "Contact Lookup Result:  contacts =  ${contacts.size}")
                    //add items to the common list
                },
                onError = { ex ->
                    handleError(ex)
                },
                onComplete = {
                    Log.d(TAG, "sendEvent(CONTACTS_LOAD_SUCCESS)")
                }
            )
        compositeDisposable.add(getContactsDisposable)
    }

    private fun getContactsStatus(contacts: List<Contact>): Observable<List<Contact>> {
        return Observable.fromIterable(contacts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { item ->
                try {
                    Integer.parseInt("a")//Test error
                    item.copy(
                        status = ContactStatus.LOADED
                    )
                }catch (ex: Exception){
                    throw LookupContactException(ex.message)
                }

            }.buffer(2)
            //Не будет работать так как вернеться CompositeException http://reactivex.io/RxJava/javadoc/io/reactivex/exceptions/CompositeException.html
            //Из-за того, что 1 ошибка возвращается в  Integer.parseInt(“a”) (эмитация ошибки), а вторая -  throw LookupContactException(it.message), поэтому получается две ошибки
            /*.onErrorReturn {
                throw LookupContactException(it.message)
            }*/
    }

    private fun handleError(th: Throwable) {
        when(th){
            is LookupContactException -> {
              Log.e(TAG,"Lookup Contact Failed: $th")
              Log.e(TAG, "sendEvent CONTACT_MATCH_FAILED")
            }
            else -> {
                Log.e(TAG, "Contacts Load Failed: $th")
                Log.e(TAG, "sendEvent CONTACTS_LOAD_FAILED")
            }

        }
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
            //throw  IllegalAccessException("Get Contacts Error")
            contacts.add(Contact(3, "Name 3", ContactStatus.DEFAULT))
            contacts.add(Contact(4, "Name 4", ContactStatus.DEFAULT))
            contacts.add(Contact(5, "Name 5", ContactStatus.DEFAULT))
            contacts.add(Contact(6, "Name 6", ContactStatus.DEFAULT))
            Observable.just(contacts)
        }

}
