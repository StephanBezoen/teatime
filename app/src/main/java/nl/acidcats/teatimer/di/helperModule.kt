package nl.acidcats.teatimer.di

import nl.acidcats.teatimer.alarm.AlarmHelper
import nl.acidcats.teatimer.alarm.AlarmHelperImpl
import nl.acidcats.teatimer.util.StorageHelper
import nl.acidcats.teatimer.util.StorageHelperImpl
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
val helperModule = module {
    single<StorageHelper> { StorageHelperImpl(get()) }

    single<AlarmHelper> { AlarmHelperImpl(get(), get()) }
}
