package com.github.onotoliy.opposite.treasure

import com.github.onotoliy.opposite.treasure.di.AppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App: DaggerApplication() {

    val appComponent: AppComponent by lazy {
        AppComponent.Initializer
            .init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent.inject(this)

        return appComponent
    }

}
