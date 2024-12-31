package io.github.westbot.craftsaber.client.audio;

import net.minecraft.client.sound.OggAudioStream;
import org.lwjgl.openal.AL11;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;


/// Credit for basically all of this class' code goes to [Swifter1243](https://github.com/Swifter1243/BeatCraft)
public class AudioSource {
    private int buffer;
    private final int source;
    private boolean isPlaying = false;
    private boolean isLoaded = false;

    public AudioSource() {
        this.source = AL11.alGenSources();
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void pause() {
        if (this.isPlaying && this.isLoaded) {
            AL11.alSourcePause(this.source);
            this.isPlaying = false;
        }
    }

    public void stop() {
        if (this.isPlaying && this.isLoaded) {
            AL11.alSourceStop(this.source);
            this.isPlaying = false;
        }
    }

    public void play() {
        if (!this.isPlaying && this.isLoaded) {
            AL11.alSourcePlay(this.source);
            this.isPlaying = true;
        }
    }

    public void playFrom(float time) {
        this.seek(time);
        this.play();
    }

    public void seek(float time) {
        if (this.isLoaded) {
            AL11.alSourcef(this.source, AL11.AL_SEC_OFFSET, time);
        }
    }

    public void setSpeed(float speed) {
        AL11.alSourcef(this.source, AL11.AL_PITCH, speed);
    }

    private int getFormatID(AudioFormat format) {
        AudioFormat.Encoding encoding = format.getEncoding();
        int channels = format.getChannels();
        int sampleSize = format.getSampleSizeInBits();

        if (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED) || encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            if (channels == 1) {
                if (sampleSize == 8) {
                    return AL11.AL_FORMAT_MONO8;
                }
                if (sampleSize == 16) {
                    return AL11.AL_FORMAT_MONO16;
                }
            } else if (channels == 2) {
                if (sampleSize == 8) {
                    return AL11.AL_FORMAT_STEREO8;
                }
                if (sampleSize == 16) {
                    return AL11.AL_FORMAT_STEREO16;
                }
            }
        }

        throw new IllegalArgumentException("Invalid audio format: " + format);
    }

    public void loadAudioFromFile(String path) throws IOException {
        this.closeBuffer();

        InputStream inputStream = Files.newInputStream(Path.of(path));
        OggAudioStream oggAudioStream = new OggAudioStream(inputStream);

        AudioFormat format = oggAudioStream.getFormat();
        this.buffer = AL11.alGenBuffers();

        int formatID = getFormatID(format);
        int sampleRate = (int) format.getSampleRate();

        ByteBuffer audioData = oggAudioStream.readAll();
        AL11.alBufferData(this.buffer, formatID, audioData, sampleRate);
        AL11.alSourcei(this.source, AL11.AL_BUFFER, this.buffer);

        this.isLoaded = true;
        inputStream.close();
        oggAudioStream.close();
    }

    public void closeBuffer() {
        if (this.isLoaded) {
            stop();
            AL11.alDeleteBuffers(this.buffer);
            this.isLoaded = false;
        }
    }


}
