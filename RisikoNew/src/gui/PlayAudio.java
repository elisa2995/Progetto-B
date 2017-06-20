package gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Interface that allows to execute audio tracks thanks to the static method
 * play. It creates a file from the source from which the play method is called;
 * it reads the input audio stream from this file and writes it in the output of
 * the audioSystem.
 */
public interface PlayAudio {

    /**
     * Plays the audio <code>source</code>.
     *
     * @param source
     */
    static void play(String source) {
        try {
            File soundFile = new File(source);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat audioFormat = audioInputStream.getFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            /* AudioSystem: allowds querying and accessing the mixers that are installed on the system 
            sourceDataLine: feeds the audio into the mixers.*/
            (new Thread() {
                @Override
                public void run() {
                    byte tempBuffer[] = new byte[10000];
                    try {
                        sourceDataLine.open(audioFormat);
                        sourceDataLine.start();
                        int cnt;
                        while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                            if (cnt > 0) {
                                sourceDataLine.write(tempBuffer, 0, cnt);
                            }
                        }
                        sourceDataLine.drain();
                        sourceDataLine.close();
                    } catch (IOException | LineUnavailableException ex) {
                        Logger.getLogger(PlayAudio.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
            Logger.getLogger(PlayAudio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
