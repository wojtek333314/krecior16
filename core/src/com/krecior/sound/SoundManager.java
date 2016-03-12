package com.krecior.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.krecior.utils.Data;

/**
 * Created by Wojciech Osak on 2015-09-30.
 */
public class SoundManager {
    public static boolean isMuted = false;
    private final String sharedIsMutedKey = "soundIsMuted";

    public static Sound click_final;
    public static Sound eraser_final;
    public static Sound fire_final;
    public static Sound hammer_hit_final;
    public static Sound screwdriver_hit_final;
    public static Sound poison_project;
    public static Sound shot_pistol_final;
    public static Sound splash_final;
    public static Sound thunder_final;
    public static Sound water_serving_final;
    public static Music mainSound;



    public SoundManager() {
        isMuted = Data.isAppMuted();

        createSounds();
    }

    private void createSounds() {
        click_final = Gdx.audio.newSound(Gdx.files.internal("sfx/click_final.wav"));
        eraser_final = Gdx.audio.newSound(Gdx.files.internal("sfx/eraser_final.mp3"));
        fire_final = Gdx.audio.newSound(Gdx.files.internal("sfx/fire_final.wav"));
        hammer_hit_final = Gdx.audio.newSound(Gdx.files.internal("sfx/hammer_hit_final.wav"));
        screwdriver_hit_final = click_final;
        poison_project = Gdx.audio.newSound(Gdx.files.internal("sfx/poison_project.wav"));
        shot_pistol_final = Gdx.audio.newSound(Gdx.files.internal("sfx/shot_pistol_final.wav"));
        splash_final = Gdx.audio.newSound(Gdx.files.internal("sfx/splash_final.wav"));
        thunder_final = Gdx.audio.newSound(Gdx.files.internal("sfx/thunder_final.wav"));
        water_serving_final = Gdx.audio.newSound(Gdx.files.internal("sfx/water_serving_final.wav"));
        mainSound = Gdx.audio.newMusic(Gdx.files.internal("sfx/mainSong.mp3"));
        mainSound.setLooping(true);
    }

    public static void mute(){
        Data.setAppMuted(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                float time = 0;
                float muteTime = 1f;
                while(time<muteTime){
                    try {
                        Thread.sleep(100);
                        time+=0.1f;
                        mainSound.setVolume(1-(time/muteTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mainSound.pause();

            }
        }).start();

        click_final.stop();
        eraser_final.stop();
        fire_final.stop();
        hammer_hit_final.stop();
        poison_project.stop();
        shot_pistol_final.stop();
        splash_final.stop();
        thunder_final.stop();
        water_serving_final.stop();
        isMuted = true;
    }

    public static void unMute(){
        Data.setAppMuted(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mainSound.setVolume(0);
                mainSound.play();
                float time = 0;
                float muteTime = 1f;
                while(time<muteTime){
                    try {
                        Thread.sleep(100);
                        time+=0.1f;
                        mainSound.setVolume((time/muteTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        isMuted = false;
    }

    public static void play(Sound s) {
        if(!isMuted) {
            s.play();
        }
    }

    public static void play(Music s) {
        if(!isMuted) {
            s.play();
        }
    }

}