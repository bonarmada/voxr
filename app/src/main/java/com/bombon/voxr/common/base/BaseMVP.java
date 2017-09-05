package com.bombon.voxr.common.base;

import java.lang.ref.WeakReference;

/**
 * Created by Vaughn on 8/29/17.
 */

public interface BaseMVP {
    interface View extends BaseView {
        void showProgress();

        void hideProgress();

        void showError();

        void showEmptyState();

        void showNetworkErrorNotification();
    }

    abstract class Presenter<V extends View> implements BasePresenter {
        private WeakReference<V> view = null;

        public final void setView(V view) {
            if (view == null) throw new NullPointerException("new view must not be null");

            if (this.view != null) dropView(this.view.get());

            this.view = new WeakReference<>(view);
        }

        public final void dropView(V view) {
            if (view == null) throw new NullPointerException("dropped view must not be null");
            this.view = null;
        }

        protected final V getView() {
            if (view == null) throw new NullPointerException("getView called when view is null. " +
                    "Ensure setView(View view) is called first.");
            return view.get();
        }
    }
}