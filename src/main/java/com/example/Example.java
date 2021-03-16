package com.example;

import dev.JustRed23.ListenDotMoe.ListenDotMoe;
import dev.JustRed23.ListenDotMoe.Utils.Logger;

import java.util.concurrent.TimeUnit;

public class Example {

    public static void main(String[] args) throws InterruptedException {
        ListenDotMoe ldm = new ListenDotMoe();
        ldm.enableDebug(false);
        ldm.disableLogger(false);
        ldm.addSongEventHandler(song -> Logger.info("Song title: " + song.getTitle()));
        ldm.start();

        TimeUnit.SECONDS.sleep(3);
        ldm.stop();
    }
}
