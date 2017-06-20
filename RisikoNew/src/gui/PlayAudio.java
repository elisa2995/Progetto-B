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
     * Reads the file corresponding to the source and it passes it to the
     * audioSystem output.
     *
     * @param source
     */
    static void play(String source) {
        AudioFormat audioFormat;
        AudioInputStream audioInputStream;
        SourceDataLine sourceDataLine;
        DataLine.Info dataLineInfo;
        try {
            File soundFile = new File(source);
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            audioFormat = audioInputStream.getFormat();
            dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            /* AudioSystem: permettere di accedere ai mixers installati nel sistema. 
               sourceDataLine = oggetto usato per feddare l'audio negli speakers.*/
            (new Thread() {
                @Override
                public void run() {
                    byte tempBuffer[] = new byte[10000];
                    /* Buffer usato per trasferire i dati dallo stream
                       del file audio all'oggetto SourceDataLine*/
                    try {
                        sourceDataLine.open(audioFormat);
                        /* Apre la linea del formato specificato, facendole 
                           acquisire le risorse di sistema necessarie e quindi
                           diventare operativa. */
                        sourceDataLine.start();
                        /* Permette alla linea di fare inviare/ricevere dati. A
                           questo punto la linea è pronta per trasferire l'audio
                           negli speaker.*/
                        int cnt;

                        while ((cnt = audioInputStream.read(
                                tempBuffer, 0, tempBuffer.length)) != -1) { // non ha raggiunto l'end of file
                            if (cnt > 0) {
                                sourceDataLine.write(
                                        tempBuffer, 0, cnt);
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
