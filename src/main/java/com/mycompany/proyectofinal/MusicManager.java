/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author geral
 */
public class MusicManager {
    private static MediaPlayer mediaPlayer;
    private static final String BACKGROUND_MUSIC = "/musica/musica.mp3"; // Ruta única para todas las vistas

    public static void playBackgroundMusic() {
        if (mediaPlayer != null) {
            // Si ya está reproduciendo, no hacer nada
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                return;
            }
            // Si está pausado, reanudar
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
                return;
            }
        }
        
        // Crear nuevo reproductor si no existe
        try {
            URL resource = MusicManager.class.getResource(BACKGROUND_MUSIC);
            if (resource == null) {
                System.err.println("Archivo de música no encontrado: " + BACKGROUND_MUSIC);
                return;
            }
            
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error al reproducir música: " + e.getMessage());
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}
