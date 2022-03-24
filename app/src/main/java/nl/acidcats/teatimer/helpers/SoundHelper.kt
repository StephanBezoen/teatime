package nl.acidcats.teatimer.helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager

interface SoundHelper {
    fun playSound()
}

class SoundHelperImpl(
    private val context: Context
) : SoundHelper {

    override fun playSound() {
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