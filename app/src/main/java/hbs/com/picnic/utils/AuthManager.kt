package hbs.com.picnic.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.lang.Exception
import java.util.concurrent.TimeUnit

class AuthManager {
    interface OnLoginAnonymousAuth {
        fun onCompleted(firebaseUser: FirebaseUser?)
        fun onFail(exception: Exception?)
    }

    companion object {
        fun login(context: Context, onLoginAnonymousAuth: OnLoginAnonymousAuth): Disposable {
            return Observable.interval(0, 3000, TimeUnit.MILLISECONDS)
                .takeUntil {
                    checkNetworkState(context)
                }
                .subscribe {
                    FirebaseAuth
                        .getInstance()
                        .signInAnonymously()
                        .addOnCompleteListener {
                            if (it.isComplete) {
                                onLoginAnonymousAuth.onCompleted(it.result?.user)
                            } else {
                                onLoginAnonymousAuth.onFail(it.exception)
                            }
                        }
                }
        }

        fun getUserId() = FirebaseAuth
            .getInstance()
            .currentUser?.uid

        private fun checkNetworkState(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}