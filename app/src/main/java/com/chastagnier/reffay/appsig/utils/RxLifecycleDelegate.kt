package com.chastagnier.reffay.appsig.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Author:        oronot
 * Creation date: 23/04/2018
 */
class RxLifecycleDelegate {
    enum class ActivityEvent {
        PAUSE,
        STOP,
        DESTROY
    }

    enum class FragmentEvent {
        PAUSE,
        STOP,
        DESTROY_VIEW,
        DESTROY,
        DETACH
    }

    private val activityLifecycleSubject: PublishSubject<ActivityEvent> = PublishSubject.create()
    private val fragmentLifecycleSubject: PublishSubject<FragmentEvent> = PublishSubject.create()

    fun onActivityPause() {
        activityLifecycleSubject.onNext(ActivityEvent.PAUSE)
    }

    fun onActivityStop() {
        activityLifecycleSubject.onNext(ActivityEvent.STOP)
    }

    fun onActivityDestroy() {
        activityLifecycleSubject.onNext(ActivityEvent.DESTROY)
    }

    fun lifecycle(event: ActivityEvent): Observable<ActivityEvent> {
        return activityLifecycleSubject.filter { it == event }
    }

    fun onFragmentPause() {
        fragmentLifecycleSubject.onNext(FragmentEvent.PAUSE)
    }

    fun onFragmentStop() {
        fragmentLifecycleSubject.onNext(FragmentEvent.STOP)
    }

    fun onFragmentDestroyView() {
        fragmentLifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
    }

    fun onFragmentDestroy() {
        fragmentLifecycleSubject.onNext(FragmentEvent.DESTROY)
    }

    fun onFragmentDetach() {
        fragmentLifecycleSubject.onNext(FragmentEvent.DETACH)
    }

    fun lifecycle(event: FragmentEvent): Observable<FragmentEvent> {
        return fragmentLifecycleSubject.filter { it == event }
    }
}