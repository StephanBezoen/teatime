package nl.acidcats.teatimer.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager

object SoundUtil {
    fun playSound(context: Context) {
        val player = MediaPlayer()

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .build()
        player.setAudioAttributes(attributes)
        player.setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        player.setOnCompletionListener {
            player.reset()
            player.release()
        }

        player.setOnPreparedListener {
            player.start()
        }
        player.prepareAsync()
    }
}