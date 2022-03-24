package nl.acidcats.teatimer.di

import nl.acidcats.teatimer.helpers.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val helperModule = module {
    single<StorageHelper> { StorageHelperImpl(androidContext()) }

    single<AlarmHelper> { AlarmHelperImpl(androidContext()) }

    single<NotificationHelper> { NotificationHelperImpl(androidContext()) }

    single<ScreenHelper> { ScreenHelperImpl(androidContext()) }

    single<AppShortcutHelper> { AppShortcutHelperImpl(context = androidContext(), configHelper = get()) }

    single<SoundHelper> { SoundHelperImpl(androidContext()) }

    single<ToastHelper> { ToastHelperImpl(androidContext()) }

    single<ConfigHelper> { ConfigHelperImpl(androidContext()) }

    single<WidgetHelper> { WidgetHelperImpl(androidContext()) }
}
