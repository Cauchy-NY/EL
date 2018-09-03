package application;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BackgroundMusic {
	static boolean isMute = false;
	static boolean isPlaying = false;
	//static AudioClip mainBGM = new AudioClip(BackgroundMusic.class.getResource("sound/mainBGM.mp3").toExternalForm());
	static MediaPlayer mainBGM = new MediaPlayer(new Media(BackgroundMusic.class.getResource("sound/mainBGM.mp3").toString()));
	public static void playMainBGM(){
		/*
		mainBGM.setCycleCount(666);
		
		if(!isMute){
			if(mainBGM.isPlaying()){
				return;
			}
			mainBGM.play();
			mainBGM.stop();
			mainBGM.play();
			System.out.println(mainBGM.getCycleCount());
		}
		*/
		mainBGM.setCycleCount(666);
		if(!isMute){
			if(isPlaying){
				return;
			}
			mainBGM.play();
			isPlaying = true;
		}
	}
	public static void stopMainBGM(){
		if(!isMute){
			mainBGM.stop();
			isPlaying = false;
		}
	}
	
	public static void setMute(){
		isMute = true;
	}
	public static void setNotMute(){
		isMute = false;
	}
}
