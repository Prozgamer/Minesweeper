package com.Bohdan;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    private Clip clip;
    private FloatControl volume_control;

    public SoundPlayer(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) throw new RuntimeException("Файл не знайдено: " + path);

            AudioInputStream audio_stream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audio_stream);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volume_control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play(boolean loop) {
        if (clip == null) return;
        clip.setFramePosition(0);
        clip.start();
        if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null && clip.isRunning()) clip.stop();
    }

    public void setVolume(float volume) {
        if (volume_control != null) {
            // volume: від 0.0 (тихо) до 1.0 (макс)
            float min = volume_control.getMinimum();
            float max = volume_control.getMaximum();
            float dB = min + (max - min) * volume;
            volume_control.setValue(dB);
        }
    }
}
