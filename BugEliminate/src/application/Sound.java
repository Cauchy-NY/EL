package application;

import javafx.scene.media.AudioClip;

public class Sound {
	static boolean isMute = false;				//��¼�Ƿ��Ǿ���
	
	static AudioClip clickSound = new AudioClip(Sound.class.getResource("sound/���.mp3").toExternalForm());
	
	static AudioClip moveSound = new AudioClip(Sound.class.getResource("sound/����.mp3").toExternalForm());
	
	static AudioClip boomSound1 = new AudioClip(Sound.class.getResource("sound/����.mp3").toExternalForm());
	static AudioClip boomSound2 = new AudioClip(Sound.class.getResource("sound/��1.mp3").toExternalForm());
	static AudioClip boomSound3 = new AudioClip(Sound.class.getResource("sound/��2.mp3").toExternalForm());
	static AudioClip boomSound4 = new AudioClip(Sound.class.getResource("sound/��3.mp3").toExternalForm());
	static AudioClip boomSound5 = new AudioClip(Sound.class.getResource("sound/��4.mp3").toExternalForm());
	static AudioClip boomSound6 = new AudioClip(Sound.class.getResource("sound/��5.mp3").toExternalForm());
	static AudioClip boomSound7 = new AudioClip(Sound.class.getResource("sound/��6.mp3").toExternalForm());
	static AudioClip boomSound8 = new AudioClip(Sound.class.getResource("sound/��7.mp3").toExternalForm());
	static AudioClip boomSound9 = new AudioClip(Sound.class.getResource("sound/��8.mp3").toExternalForm());
	static AudioClip boomSound10 = new AudioClip(Sound.class.getResource("sound/��9.mp3").toExternalForm());

	static AudioClip lineBoom = new AudioClip(Sound.class.getResource("sound/������.mp3").toExternalForm());
	static AudioClip NineBoom = new AudioClip(Sound.class.getResource("sound/�����Ź���.mp3").toExternalForm());
	static AudioClip FiveBoom = new AudioClip(Sound.class.getResource("sound/����5����Ч.mp3").toExternalForm());
	
	static AudioClip good = new AudioClip(Sound.class.getResource("sound/3good.mp3").toExternalForm());
	static AudioClip great = new AudioClip(Sound.class.getResource("sound/5great.mp3").toExternalForm());
	static AudioClip excellent = new AudioClip(Sound.class.getResource("sound/7excellent.mp3").toExternalForm());
	static AudioClip amazing = new AudioClip(Sound.class.getResource("sound/9amazing.mp3").toExternalForm());
	static AudioClip unblievable = new AudioClip(Sound.class.getResource("sound/11unbelieve.mp3").toExternalForm());

	static AudioClip befclicked = new AudioClip(Sound.class.getResource("sound/��ǰ.wav").toExternalForm());
	static AudioClip aftclicked = new AudioClip(Sound.class.getResource("sound/���.wav").toExternalForm());

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
	}//����playClickSound����
	
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

			}//switch����
			
			if(eliminateCount >= 10){
				boomSound10.play();
			}

		}//if����
	}//����playBoomSound����
	
	public static void playMoveSound(){
		if(!isMute){
			moveSound.play();
		}
	}//����playMoveSound����
	
	public static void playLineBoomSound(){
		if(!isMute){
			lineBoom.play();
		}
	}//����playLineBoomSound����
	
	public static void playNineBoomSound(){
		if(!isMute){
			NineBoom.play();
		}
	}//����playNineBoomSound����
	
	public static void playFiveBoomSound(){
		if(!isMute){
			FiveBoom.play();
		}
	}//����playFiveBoomSound����
	
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
	}//����playPraiseSound����
	
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
