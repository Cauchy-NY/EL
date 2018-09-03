package application;

import javafx.scene.media.AudioClip;

public class Sound {
	static boolean isMute = false;				//记录是否是静音
	
	static AudioClip clickSound = new AudioClip(Sound.class.getResource("sound/点击.mp3").toExternalForm());
	
	static AudioClip moveSound = new AudioClip(Sound.class.getResource("sound/掉落.mp3").toExternalForm());
	
	static AudioClip boomSound1 = new AudioClip(Sound.class.getResource("sound/消除.mp3").toExternalForm());
	static AudioClip boomSound2 = new AudioClip(Sound.class.getResource("sound/爆1.mp3").toExternalForm());
	static AudioClip boomSound3 = new AudioClip(Sound.class.getResource("sound/爆2.mp3").toExternalForm());
	static AudioClip boomSound4 = new AudioClip(Sound.class.getResource("sound/爆3.mp3").toExternalForm());
	static AudioClip boomSound5 = new AudioClip(Sound.class.getResource("sound/爆4.mp3").toExternalForm());
	static AudioClip boomSound6 = new AudioClip(Sound.class.getResource("sound/爆5.mp3").toExternalForm());
	static AudioClip boomSound7 = new AudioClip(Sound.class.getResource("sound/爆6.mp3").toExternalForm());
	static AudioClip boomSound8 = new AudioClip(Sound.class.getResource("sound/爆7.mp3").toExternalForm());
	static AudioClip boomSound9 = new AudioClip(Sound.class.getResource("sound/爆8.mp3").toExternalForm());
	static AudioClip boomSound10 = new AudioClip(Sound.class.getResource("sound/爆9.mp3").toExternalForm());

	static AudioClip lineBoom = new AudioClip(Sound.class.getResource("sound/爆列行.mp3").toExternalForm());
	static AudioClip NineBoom = new AudioClip(Sound.class.getResource("sound/消除九宫格.mp3").toExternalForm());
	static AudioClip FiveBoom = new AudioClip(Sound.class.getResource("sound/消除5连特效.mp3").toExternalForm());
	
	static AudioClip good = new AudioClip(Sound.class.getResource("sound/3good.mp3").toExternalForm());
	static AudioClip great = new AudioClip(Sound.class.getResource("sound/5great.mp3").toExternalForm());
	static AudioClip excellent = new AudioClip(Sound.class.getResource("sound/7excellent.mp3").toExternalForm());
	static AudioClip amazing = new AudioClip(Sound.class.getResource("sound/9amazing.mp3").toExternalForm());
	static AudioClip unblievable = new AudioClip(Sound.class.getResource("sound/11unbelieve.mp3").toExternalForm());

	static AudioClip befclicked = new AudioClip(Sound.class.getResource("sound/点前.wav").toExternalForm());
	static AudioClip aftclicked = new AudioClip(Sound.class.getResource("sound/点后.wav").toExternalForm());

	public static void setMute(){
		isMute = true;
	}
	public static void setNotMute(){
		isMute = false;
	}
	
	public static void playClickSound(){
		if(!isMute){
			clickSound.play();
		}
	}//方法playClickSound结束
	
	public static void playBoomSound(int eliminateCount){
		if(!isMute){
			switch(eliminateCount){
				case 1:{
					boomSound1.play();
					break;
				}
				case 2:{
					boomSound2.play();
					break;
				}
				case 3:{
					boomSound3.play();
					break;
				}
				case 4:{
					boomSound4.play();
					break;
				}
				case 5:{
					boomSound5.play();
					break;
				}
				case 6:{
					boomSound6.play();
					break;
				}
				case 7:{
					boomSound7.play();
					break;
				}
				case 8:{
					boomSound8.play();
					break;
				}
				case 9:{
					boomSound9.play();
					break;
				}

			}//switch结束
			
			if(eliminateCount >= 10){
				boomSound10.play();
			}

		}//if结束
	}//方法playBoomSound结束
	
	public static void playMoveSound(){
		if(!isMute){
			moveSound.play();
		}
	}//方法playMoveSound结束
	
	public static void playLineBoomSound(){
		if(!isMute){
			lineBoom.play();
		}
	}//方法playLineBoomSound结束
	
	public static void playNineBoomSound(){
		if(!isMute){
			NineBoom.play();
		}
	}//方法playNineBoomSound结束
	
	public static void playFiveBoomSound(){
		if(!isMute){
			FiveBoom.play();
		}
	}//方法playFiveBoomSound结束
	
	public static void playPraiseSound(int eliminateCount){
		if(!isMute){
			if(eliminateCount >= 11){
				unblievable.play();
			}
			else if(eliminateCount >= 9){
				amazing.play();
			}
			else if(eliminateCount >= 7){
				excellent.play();
			}
			else if(eliminateCount >= 5){
				great.play();
			}
			else if(eliminateCount >= 3){
				good.play();
			}
		}
	}//方法playPraiseSound结束
	
	public static void playbefclicked(){
		if(!isMute){
			befclicked.setVolume(0.15);
			befclicked.play();
		}
	}
	public static void playaftclicked(){
		if(!isMute){
			aftclicked.setVolume(0.15);
			aftclicked.play();
		}
	}

}
