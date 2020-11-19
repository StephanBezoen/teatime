package nl.acidcats.teatimer.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import com.github.ajalt.timberkt.Timber

object SoundUtil {
    fun playSound(context:Context) {
        val player = MediaPlayer()

        val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .build()
        player.setAudioAttributes(attributes)
        player.setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        player.setOnCompletionListener {
            Timber.d { "Completed playing the sound" }
            player.reset()
            player.release()
        }

        player.setOnPreparedListener {
            Timber.d { "Prepared" }

            player.start()
        }
        player.prepareAsync()

    }
}