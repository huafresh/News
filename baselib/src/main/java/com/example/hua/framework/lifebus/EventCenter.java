package com.example.hua.framework.lifebus;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.v4.util.ArraySet;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 实现类似于LiveData，一样的生命周期感知，是简化的专用于发送Event的LiveData
 *
 * @author hua
 * @version 2018/8/29 9:29
 */

class EventCenter {

    private HashMap<LifecycleOwner, ObserverWithOwner> observersMap = new HashMap<>();

    static EventCenter getInstance() {
        return Holder.S_INSTANCE;
    }

    private static final class Holder {
        private static final EventCenter S_INSTANCE = new EventCenter();
    }

    private EventCenter() {
    }

    void observe(LifecycleOwner owner, ObserverWrap observer) {
        ObserverWithOwner observerWithOwner = null;
        if (observersMap.containsKey(owner)) {
            observerWithOwner = observersMap.get(owner);
        } else {
            observerWithOwner = new ObserverWithOwner(owner);
            owner.getLifecycle().addObserver(observerWithOwner);
            observersMap.put(owner, observerWithOwner);
        }
        observerWithOwner.addObserver(observer);
    }

    void removeObservers(LifecycleOwner owner) {
        ObserverWithOwner observerWithOwner = observersMap.remove(owner);
        if (observerWithOwner != null) {
            observerWithOwner.owner.getLifecycle().removeObserver(observerWithOwner);
        }
    }

    void sendEvent(EventWrap eventWrap) {
        for (LifecycleOwner owner : observersMap.keySet()) {
            ObserverWithOwner observerWithOwner = observersMap.get(owner);
            observerWithOwner.sendEvent(eventWrap);
        }
    }

    class ObserverWithOwner implements GenericLifecycleObserver {
        private LifecycleOwner owner;
        private ArraySet<ObserverWrap> observers = new ArraySet<>();

        ObserverWithOwner(LifecycleOwner owner) {
            this.owner = owner;
        }

        void addObserver(ObserverWrap newWrap) {
            ObserverWrap oldObserverWrap = getObserverWrap(newWrap.observer);
            if (oldObserverWrap != null) {
                oldObserverWrap.update(newWrap);
            } else {
                observers.add(newWrap);
                if (newWrap.isSticky()) {
                    LifecycleBus.sendStickyEvent(newWrap.eventType);
                }
            }
        }

        void removeObserver(ObserverWrap observer) {
            observers.remove(observer);
        }

        void clearObservers() {
            observers.clear();
        }

        ObserverWrap getObserverWrap(EventObserver observer){
            for (ObserverWrap wrap : observers) {
                if (wrap.observer.hashCode() == observer.hashCode()) {
                    return wrap;
                }
            }
            return null;
        }

        @Override
        public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
            Iterator<ObserverWrap> iterator = observers.iterator();
            while (iterator.hasNext()) {
                ObserverWrap observerWrap = iterator.next();
                observerWrap.onStateChanged(event);

                if (observerWrap.removed) {
                    iterator.remove();
                }
            }

            if (observers.isEmpty()) {
                EventCenter.this.removeObservers(owner);
            }


//            Object o = observers.toArray()[0];
//            String os = o != null ? o + "" : "null";
//            Log.e("@@@hua", "onStateChanged, source = " + source + ", event = " + event + ",observer=" + os);

        }

        @SuppressWarnings("unchecked")
        void sendEvent(EventWrap eventWrap) {
            for (ObserverWrap observer : observers) {
                if (isEventMatch(observer, eventWrap)) {
                    observer.onEvent(eventWrap);
                }
            }
        }

        boolean isEventMatch(ObserverWrap observerWrap, EventWrap eventWrap) {
            return observerWrap.eventType == eventWrap.eventType;
        }
    }

}
