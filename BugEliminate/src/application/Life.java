package application;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.*;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Life {
	static final int HEIGHT = 300;			//��Ծ�߶�
	static final int SPEED = 3;				//�ٶ�
	
	Player player;
	int lastGame = 11;			//Ĭ��Ϊ11
	
	int chapterIndex = 0;		//0Ϊѡ�½ڣ���¼���ڵڼ���
	
	//static Group life = new Group();
	static Group mainGroup = new Group();			//��Group
	
	Chapter selectChapter;				//ѡ��
	Chapter chapter;
	
	Group life = new Group();
	//Group landGroup = new Group();
	Group landGroup;
	Rectangle[] land;
	
	Media video1 = new Media(this.getClass().getResource("video/����01.mp4").toString());
	MediaPlayer video1Player = new MediaPlayer(video1);
	MediaView video1View = new MediaView(video1Player);
	
	Image backGround0 = new Image(this.getClass().getResourceAsStream("image/���ı���0.png"));
	Image backGround1 = new Image(this.getClass().getResourceAsStream("image/���ı���1.png"));
	Image backGround2 = new Image(this.getClass().getResourceAsStream("image/���ı���2.png"));
	Image backGround3 = new Image(this.getClass().getResourceAsStream("image/���ı���3.png"));

	//Image backGround = new Image(this.getClass().getResourceAsStream("image/�������ı���.jpg"));
	Image back = new Image(this.getClass().getResourceAsStream("image/����1.png"), 80, 80, false, false);
	Image wormLeft, wormRight;
	//Image mainLand = new Image(this.getClass().getResourceAsStream("image/�ڵ�.jpg"), backGround.getWidth(),30, false,false);
	//Image smallLand = new Image(this.getClass().getResourceAsStream("image/�ڵ�.jpg"), 200, 30,false, false);
	Image guanqia = new Image(this.getClass().getResourceAsStream("image/�ؿ�.png"),120,120,false,false);
	Image suoguanqia = new Image(this.getClass().getResourceAsStream("image/���ؿ�.png"),120,120,false,false);
	
	//ImageView bg = new ImageView(backGround);
	ImageView worm;
	//ImageView[] landView = new ImageView[5];
	ImageView[] level;
	
	Right right;				//����
	Thread rightThread;
	Left left;					//����
	Thread leftThread;
	
	Thread upThread;
	Thread checkDropThread;
	
	boolean isJumping = false;
	
	boolean isSelectingChapter = true;				//��¼�Ƿ���ѡ�½���
	
	double x ,y;			//�������浱ǰ���ӵ�����
	//���캯��
	public Life(Player player){
		this.player = player;
		//wormRight = new Image(this.getClass().getResourceAsStream("image/ƻ��1.png"), 80, 80,false,false);
		//worm = new ImageView(wormRight);
		//worm.setLayoutY(870-wormRight.getHeight());
		//x = 0;	y = worm.getLayoutY() + wormRight.getHeight();
	}
	
	public void go(){
		
		video1View.setFitWidth(GameLauncher.mainScene.getWidth());
		GridPane videoPane = new GridPane();
		videoPane.setStyle("-fx-background-color:black");
		
		//��ͣ��������
		BackgroundMusic.stopMainBGM();
		
		video1Player.setAutoPlay(true);
		videoPane.setAlignment(Pos.CENTER);
		videoPane.add(video1View, 0, 0);
		
		Label tiaoguo = new Label("����������");
		tiaoguo.setStyle("-fx-font-fill:white");
		tiaoguo.setStyle("-fx-font-size:30");

		HBox temp = new HBox();
		temp.setAlignment(Pos.TOP_RIGHT);
		temp.getChildren().add(tiaoguo);
		videoPane.add(temp, 0, 1);
		
		GameLauncher.mainScene.setRoot(videoPane);
		
		videoPane.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				video1Player.stop();
			}
		});
		
		video1Player.setOnEndOfMedia(()->{
			GameLauncher.mainScene.setRoot(mainGroup);
			
			//������������
			BackgroundMusic.playMainBGM();
			
			video1Player.dispose();
		});
		
		video1Player.setOnStopped(()->{
			GameLauncher.mainScene.setRoot(mainGroup);
			
			//������������
			BackgroundMusic.playMainBGM();
			
			video1Player.dispose();
		});
		
		/*
		Media video2 = new Media(this.getClass().getResource("video/����02.mp4").toString());
		MediaPlayer video2Player = new MediaPlayer(video2);
		MediaView video2View = new MediaView(video2Player);
		
		//ֹͣ��������
		BackgroundMusic.stopMainBGM();
		
		video2View.setFitWidth(GameLauncher.mainScene.getWidth());
		GridPane videoPane = new GridPane();
		videoPane.setStyle("-fx-background-color:black");
		video2Player.setAutoPlay(true);
		videoPane.setAlignment(Pos.CENTER);
		videoPane.add(video2View, 0, 0);
		
		Label tiaoguo = new Label("����������");
		tiaoguo.setStyle("-fx-font-fill:white");
		tiaoguo.setStyle("-fx-font-size:30");

		HBox temp = new HBox();
		temp.setAlignment(Pos.TOP_RIGHT);
		temp.getChildren().add(tiaoguo);
		videoPane.add(temp, 0, 1);
		
		GameLauncher.mainScene.setRoot(videoPane);
		
		videoPane.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				video2Player.stop();
			}
		});
		
		video2Player.setOnEndOfMedia(()->{
			GameLauncher.mainScene.setRoot(mainGroup);
			
			//����BGM
			BackgroundMusic.playMainBGM();
			
			video2Player.dispose();
		});
		
		video2Player.setOnStopped(()->{
			GameLauncher.mainScene.setRoot(mainGroup);
			
			//����BGM
			BackgroundMusic.playMainBGM();
			
			video2Player.dispose();
		});
		 */
		
		//���ü��̼���
		right = new Right();
		left = new Left();
		
		KeyPressed kp = new KeyPressed();
		//life.setOnKeyPressed(kp);
		mainGroup.setOnKeyPressed(kp);
		KeyReleased kr = new KeyReleased();
		//life.setOnKeyReleased(kr);
		mainGroup.setOnKeyReleased(kr);
		
		/*
		//���ñ���
		landGroup.getChildren().add(bg);
		
		//����½��
		landView[0] = new ImageView(mainLand);
		landView[0].setLayoutY(550);
		landGroup.getChildren().add(landView[0]);
		landView[1] = new ImageView(smallLand);
		landView[1].setLayoutX(300);	landView[1].setLayoutY(420);
		landGroup.getChildren().add(landView[1]);
		landView[2] = new ImageView(smallLand);
		landView[2].setLayoutX(400);	landView[2].setLayoutY(300);
		landGroup.getChildren().add(landView[2]);
		
		life.getChildren().add(landGroup);
		
		//���ùؿ�ͼ
		level = new ImageView(guanqia);
		level.setLayoutX(landView[1].getLayoutX());		level.setLayoutY(landView[1].getLayoutY()-guanqia.getHeight());
		landGroup.getChildren().add(level);
		*/
		
		/*
		Chapter chapter = new Chapter(0);
		landGroup = chapter.landGroup;
		land = chapter.land;
		*/
		
		//landGroup = new Group();

		if(player.lastGame > 30){
			wormRight = new Image(this.getClass().getResourceAsStream("image/right3.png"), 65, 100,false,false);
			wormLeft = new Image(this.getClass().getResourceAsStream("image/left3.png"), 65, 100,false,false);
		}
		else if(player.lastGame > 20){
			wormRight = new Image(this.getClass().getResourceAsStream("image/right2.png"), 65, 100,false,false);
			wormLeft = new Image(this.getClass().getResourceAsStream("image/left2.png"), 65, 100,false,false);
		}
		else{
			wormRight = new Image(this.getClass().getResourceAsStream("image/right1.png"), 65, 100,false,false);
			wormLeft = new Image(this.getClass().getResourceAsStream("image/left1.png"), 65, 100,false,false);
		}

		//�½�ѡ�µĶ���
		selectChapter = new Chapter(0);
		landGroup = selectChapter.landGroup;
		land = selectChapter.land;
		//����������Ϊ�µ�ѡ�¶���ĳ���
		//wormRight = selectChapter.wormRight;
		//wormLeft = selectChapter.wormLeft;
		worm = selectChapter.worm;
		
		setChapter();				//�����½�
		
		//���ذ�ť
		ImageView b = new ImageView(back);
		selectChapter.backButton = new Button("", b);
		selectChapter.backButton.setMaxSize(90, 90);
		selectChapter.backButton.setLayoutX(0);
		selectChapter.backButton.setStyle("-fx-background-color:null");			//���ð�ť͸��
		selectChapter.backButton.setOnAction(new BackHandler());
		life.getChildren().add(selectChapter.backButton);
		
		//�������
		life.getChildren().add(worm);
		
		mainGroup.getChildren().add(life);				//��life����mainGroup
	}
	
	class BackHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			if(isSelectingChapter){
				life = null;
				//mainGroup = null;
				GameLauncher.back();
			}
			else{			//����ѡ�½���
				mainGroup.getChildren().remove(mainGroup.getChildren().size()-1);
				mainGroup.getChildren().add(life);
				landGroup = selectChapter.landGroup;
				land = selectChapter.land;
				x = selectChapter.eachX;	y = selectChapter.eachY;
				
				//wormRight = selectChapter.wormRight;
				//wormLeft = selectChapter.wormLeft;
				worm = null;
				worm = selectChapter.worm;
				
				level = null;
				level = selectChapter.level;
				//worm.setLayoutY(y-wormRight.getHeight());	worm.setLayoutX(selectChapter.layoutX);
				
				chapterIndex = 0;
				isSelectingChapter = true;
			}
		}
	}
	
	//���̼�����
	class KeyPressed implements EventHandler<KeyEvent>{
		public void handle(KeyEvent e){
			if(e.getCode() == KeyCode.RIGHT){			//���¡�ʱ
				/*
				//�ж��Ƿ񵽴�߽�
				if(land.getLayoutX() == -(backGround.getWidth() - GameLauncher.mainScene.getWidth())
						&& worm.getLayoutX() == GameLauncher.mainScene.getWidth() - wormRight.getWidth()){
					return;
				}
				if(worm.getLayoutX() < GameLauncher.mainScene.getWidth()/2.5
						|| land.getLayoutX() == -(backGround.getWidth() - GameLauncher.mainScene.getWidth())){
					worm.setLayoutX(worm.getLayoutX() + 10);
					x = x + 10;
					if(!isJumping){
						checkDrop();
					}
				}
				else{
					land.setLayoutX(land.getLayoutX() - 10);
					x = x + 10;
					if(!isJumping){
						checkDrop();
					}
				}
				*/
				if(right.isPressed){
					return;
				}
				right.isPressed = true;
				rightThread = new Thread(right);
				rightThread.start();
			}//�ж��Ƿ��¡�����
			
			if(e.getCode() == KeyCode.LEFT){
				/*
				//�ж��Ƿ񵽴�߽�
				if(land.getLayoutX() == 0
						&& worm.getLayoutX() == 0){
					return;
				}
				if(worm.getLayoutX() > GameLauncher.mainScene.getWidth()/2
						|| land.getLayoutX() == 0){
					worm.setLayoutX(worm.getLayoutX() - 10);
					x = x - 10;
					if(!isJumping){
						checkDrop();
					}
				}
				else{
					land.setLayoutX(land.getLayoutX() + 10);
					x = x - 10;
					if(!isJumping){
						checkDrop();
					}
				}
				*/
				
				if(left.isPressed){
					return;
				}
				left.isPressed = true;
				leftThread = new Thread(left);
				leftThread.start();
			}//�ж��Ƿ��¡�����
			
			if(e.getCode() == KeyCode.UP){
				
				if(isJumping || (upThread != null && upThread.isAlive()) ){		//ͬʱֻ����һ��
					return;
				}
				
				isJumping = true;			//����Ϊ����Ծ״̬
				
				/*
				Timeline timeLine1 = new Timeline();
				KeyValue kv1 = new KeyValue(worm.yProperty(), worm.getY()-HEIGHT);
				KeyFrame kf1 = new KeyFrame(Duration.millis(HEIGHT*2), kv1);
				timeLine1.getKeyFrames().add(kf1);
								
				//�������䶯��
				timeLine1.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e){
						y = y - HEIGHT;
						drop();
					}
				});
				
				timeLine1.play();
				*/
				
				upThread = new Thread(new Up());
				upThread.start();
				
			}//������
			
			if(e.getCode() == KeyCode.ESCAPE){
				if(isSelectingChapter){
					life = null;
					//mainGroup = null;
					GameLauncher.back();
				}
				else{			//����ѡ�½���
					mainGroup.getChildren().remove(mainGroup.getChildren().size()-1);
					mainGroup.getChildren().add(life);
					landGroup = selectChapter.landGroup;
					land = selectChapter.land;
					x = selectChapter.eachX;	y = selectChapter.eachY;
					
					//wormRight = selectChapter.wormRight;
					//wormLeft = selectChapter.wormLeft;
					worm = null;
					worm = selectChapter.worm;
					
					level = null;
					level = selectChapter.level;

					//worm.setLayoutY(y-wormRight.getHeight());	worm.setLayoutX(selectChapter.layoutX);
					
					chapterIndex = 0;
					isSelectingChapter = true;
				}
			}//ESC������
			
			if(e.getCode() == KeyCode.ENTER){
				select();
			}//���¿ո������ѡ��
			
		}//����handle����
	}//��KeyPressed����
	
	class KeyReleased implements EventHandler<KeyEvent>{
		public void handle(KeyEvent ke){
			if(ke.getCode() == KeyCode.RIGHT){
				right.isPressed  = false;
				right = new Right();
			}
			if(ke.getCode() == KeyCode.LEFT){
				left.isPressed = false;
				left = new Left();
			}
		}
	}
	
	/*
	//���䷽��
	void drop(){
		//y = y - HEIGHT;
		//�ж���һ�ε����
		//double nextY = 550;
		double nextY = 0;
		
		//�ж��Ƿ�����С����
		for(int i = 1;i < 3;i++){
			//���жϺ�����
			if(y < landView[i].getLayoutY() && x + wormRight.getWidth() > landView[i].getLayoutX() && x < landView[i].getLayoutX() + smallLand.getWidth()){
				//if(landView[i].getLayoutY() < nextY){
				if(landView[i].getLayoutY() > y){
					nextY = landView[i].getLayoutY();		//������һ�����
				}
			}
		}
		
		//�ж��Ƿ����ڴ����
		if(nextY == 0 && y < landView[0].getLayoutY()){
			nextY = landView[0].getLayoutY();
		}
		

		Timeline timeLine2 = new Timeline();
		double temp1 = nextY - wormRight.getWidth();
		double temp2 = nextY - y - HEIGHT;
		KeyValue kv2 = new KeyValue(worm.yProperty(), temp2);
		//KeyValue kv2 = new KeyValue(worm.yProperty(), y-nextY);
		KeyFrame kf2 = new KeyFrame(Duration.millis((nextY - y)*2), kv2);
		timeLine2.getKeyFrames().add(kf2);
		timeLine2.setOnFinished(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent et){
				worm.yProperty().set(0);
				worm.setLayoutY(temp1);
					isJumping = false;			//����Ϊδ��Ծ״̬
					
					//����Ƿ������
					//checkDrop();
			}
		});
		timeLine2.play();
		y = nextY;
	}//����drop����
	*/
	
	//����
	
	void checkDrop(){
		int onWhichOne = -1;
		for(int i = 0;i < land.length;i++){
			if(y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2){
				onWhichOne = i;			//��¼������һ����
				break;
			}
		}

		//����ڴ����,��ȥ����Ƿ���µ�
		if(onWhichOne == -1){
			return;
		}
		//System.out.println(onWhichOne);
		//�ж��Ƿ�Ҫ����
		//if(x + wormRight.getWidth() <= landView[onWhichOne].getLayoutX() || x >= landView[onWhichOne].getLayoutX() + smallLand.getWidth()){
		if(x + wormRight.getWidth() <= land[onWhichOne].getLayoutX() || x >= land[onWhichOne].getLayoutX() + land[onWhichOne].getWidth()){
			//System.out.println("wc");
			isJumping = true;
			double nextY = 0;
			
			for(int i = 1;i < land.length;i++){
				//���жϺ�����
				//if(x + wormRight.getWidth() > landView[i].getLayoutX() && x < landView[i].getLayoutX() + smallLand.getWidth()){
				if(x + wormRight.getWidth() > land[i].getLayoutX() && x < land[i].getLayoutX() + land[onWhichOne].getWidth()){
					if(land[i].getLayoutY() > y){
						nextY = land[i].getLayoutY();		//������һ�����
					}

				}
			}
			if(nextY == 0 && y < land[0].getLayoutY()){
				nextY = land[0].getLayoutY();
			}
			else if(nextY == 0){
				return;
			}

			Timeline timeLine2 = new Timeline();
			double temp = nextY - wormRight.getWidth();
			KeyValue kv2 = new KeyValue(worm.yProperty(), nextY - y);
			KeyFrame kf2 = new KeyFrame(Duration.millis((nextY - y)*1.5), kv2);
			timeLine2.getKeyFrames().add(kf2);
			timeLine2.setOnFinished(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent et){
					worm.yProperty().set(0);
					worm.setLayoutY(temp);
					isJumping = false;			//����Ϊδ��Ծ״̬
					
					checkDrop();			//�ݹ����Ƿ��ܼ�������
				}
			});
			timeLine2.play();
			y = nextY;
		}//if����

	}//����checkDrop
	
	
	class Right implements Runnable{
		boolean isPressed = false;			//��¼��������Ƿ񱻰���
		
		public void run(){
			loop:while(isPressed){
				//�ı���ӷ���
				worm.setImage(wormRight);
				
				//�ж��Ƿ񵽴�߽�
				if(landGroup.getLayoutX() <= 4-(backGround1.getWidth() - GameLauncher.mainScene.getWidth())
						&& worm.getLayoutX() >= GameLauncher.mainScene.getWidth() - wormRight.getWidth()){
					return;
				}
				
				//�ж��Ƿ�ײǽ
				for(int j = 0;j < land.length;j++){
					if(x+wormRight.getWidth() >= land[j].getLayoutX() && x+wormRight.getWidth() <= 2+land[j].getLayoutX()
							&& y > land[j].getLayoutY()+SPEED+1 && y < land[j].getLayoutY() + land[j].getHeight()){
						continue loop;
					}
				}
				
				
				if(worm.getLayoutX() < GameLauncher.mainScene.getWidth()/2.5
						|| landGroup.getLayoutX() <= 4-(backGround1.getWidth() - GameLauncher.mainScene.getWidth())){
					//worm.setLayoutX(worm.getLayoutX() + 2);
					Platform.runLater(() -> {
						worm.setLayoutX(worm.getLayoutX() + SPEED);
					});
					x = x + SPEED;
					if(!isJumping){
						//checkDrop();
						//if(checkDropThread != null && !checkDropThread.isAlive() ){		//ͬʱֻ����һ��
						//}
							for(int i = 0;i < land.length;i++){
								if( (x + wormRight.getWidth() <= land[i].getLayoutX() || x >= land[i].getLayoutX() + land[i].getWidth() )
										&& (y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2)){
									if(!isJumping){				//��ֹ���μ����̼߳�������
										isJumping = true;
										
										checkDropThread = null;

										checkDropThread = new Thread(new CheckDrop());
										checkDropThread.start();

									}

								}
							}

					}
				}
				else{
					Platform.runLater(() -> {
						landGroup.setLayoutX(landGroup.getLayoutX() - SPEED);
					});

					//landGroup.setLayoutX(land.getLayoutX() - SPEED);
					x = x + SPEED;
					if(!isJumping){
						//checkDrop();
						//if(checkDropThread != null && !checkDropThread.isAlive() ){		//ͬʱֻ����һ��
						for(int i = 0;i < land.length;i++){
							if( (x + wormRight.getWidth() <= land[i].getLayoutX() || x >= land[i].getLayoutX() + land[i].getWidth() )
									&& (y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2)){
								if(!isJumping){				//��ֹ���μ����̼߳�������
									isJumping = true;
									
									checkDropThread = null;

									checkDropThread = new Thread(new CheckDrop());
									checkDropThread.start();

								}

							}
						}
						//}

					}
				}
				try{
					Thread.sleep(5);			//������ʱ
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}//run����
	}//Right�����
	
	class Left implements Runnable{
		boolean isPressed = false;		//������¼��������Ƿ񱻰���
		public void run(){
			loop:while(isPressed){
				//�ı���ӷ���
				worm.setImage(wormLeft);
				
				//�ж��Ƿ񵽴�߽�
				if(landGroup.getLayoutX() >= -4
						&& worm.getLayoutX() <= 0){
					return;
				}
				
				//�ж��Ƿ�ײǽ
				for(int j = 0;j < land.length;j++){
					if(x <= land[j].getLayoutX()+land[j].getWidth() && x >= -2+land[j].getLayoutX()+land[j].getWidth()
							&& y > land[j].getLayoutY()+SPEED+1 && y < land[j].getLayoutY() + land[j].getHeight()){
						continue loop;
					}
				}

				if(worm.getLayoutX() > GameLauncher.mainScene.getWidth()/2
						|| landGroup.getLayoutX() >= -4){
					Platform.runLater(() -> {
						worm.setLayoutX(worm.getLayoutX() - SPEED);
					});

					//worm.setLayoutX(worm.getLayoutX() - SPEED);
					x = x - SPEED;
					if(!isJumping){
						//checkDrop();
						//if(checkDropThread != null && !checkDropThread.isAlive() ){		//ͬʱֻ����һ��
						for(int i = 0;i < land.length;i++){
							if( (x + wormRight.getWidth() <= land[i].getLayoutX() || x >= land[i].getLayoutX() + land[i].getWidth() )
									&& (y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2)){
								if(!isJumping){				//��ֹ���μ����̼߳�������
									isJumping = true;
									
									checkDropThread = null;

									checkDropThread = new Thread(new CheckDrop());
									checkDropThread.start();

								}

							}
						}
						//}
					}
				}
				else{
					Platform.runLater(() -> {
						landGroup.setLayoutX(landGroup.getLayoutX() + SPEED);
					});

					//landGroup.setLayoutX(landGroup.getLayoutX() + SPEED);
					x = x - SPEED;
					if(!isJumping){
						//checkDrop();
						//if(checkDropThread != null && !checkDropThread.isAlive() ){		//ͬʱֻ����һ��
						for(int i = 0;i < land.length;i++){
							if( (x + wormRight.getWidth() <= land[i].getLayoutX() || x >= land[i].getLayoutX() + land[i].getWidth() )
									&& (y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2)){
								if(!isJumping){				//��ֹ���μ����̼߳�������
									isJumping = true;
									
									checkDropThread = null;

									checkDropThread = new Thread(new CheckDrop());
									checkDropThread.start();

								}

							}
						}
						//}
					}
				}
				try{
					Thread.sleep(5);			//������ʱ
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}//run����
	}//Left�����
	
	class Up implements Runnable{
		public void run(){
			try{
			int height = 0;			//��¼��ǰ���˶��
			while(height < HEIGHT){		//����
				Platform.runLater(() -> {
					worm.setLayoutY(worm.getLayoutY() - SPEED);
				});

				//worm.setLayoutY(worm.getLayoutY() - 5);
				y = y - SPEED;
				height += SPEED;
				try{
					Thread.sleep(4);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			//����
			while(isJumping){
				//����Ƿ����
				for(int i = 0;i < land.length;i++){
					/*
					if(i == 0){		//�ж��Ƿ����ڴ����
						if(y == landView[0].getLayoutY()){
							isJumping = false;
							return;
						}
					}
					else{
						//���жϺ�����
						if(x + wormRight.getWidth() > landView[i].getLayoutX() 
								&& x < landView[i].getLayoutX() + smallLand.getWidth()){
							//���ж��������Ƿ����
							if(y == landView[i].getLayoutY()){
								isJumping = false;
								return;
							}
						}
					}
					*/
					
					//���жϺ�����
					if(x + wormRight.getWidth() > land[i].getLayoutX() 
							&& x < land[i].getLayoutX() + land[i].getWidth()){
						//���ж��������Ƿ����
						if(y >= land[i].getLayoutY() && y <= land[i].getLayoutY() + 2){
							isJumping = false;
							return;
						}
					}

				}//for����
				//��������+2
				Platform.runLater(() -> {
					worm.setLayoutY(worm.getLayoutY() + SPEED);
				});

				//worm.setLayoutY(worm.getLayoutY() + 5);
				y = y + SPEED;
				
				try{
					Thread.sleep(4);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}//while����
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}//run����
	}//��Up����
	
	
	//����,ɾ��
	
	class CheckDrop implements Runnable{
		public void run(){
			while(isJumping){
				//����Ƿ����
				for(int i = 0;i < land.length;i++){
					//���жϺ�����
					if(x + wormRight.getWidth() > land[i].getLayoutX() 
							&& x < land[i].getLayoutX() + land[i].getWidth()){
						//���ж��������Ƿ����
						if(y >= land[i].getLayoutY() && y <= land[i].getLayoutY() + 2){
							isJumping = false;
							return;
						}
					}

				}//for����
				//��������+2
				Platform.runLater(() -> {
					worm.setLayoutY(worm.getLayoutY() + SPEED);
				});

				//worm.setLayoutY(worm.getLayoutY() + 5);
				y = y + SPEED;
				
				try{
					Thread.sleep(4);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}//while����
		}//handle����
	}//CheckDrop�����
	
	
	void setChapter(){
		lastGame = player.lastGame;				//��ȡ����
		
		//���Ϲؿ�ͼ
		selectChapter.level = new ImageView[3];
		
		for(int i = 0;i < 3;i++){
			if(lastGame >= (i+1)*10){
				selectChapter.level[i] = new ImageView(guanqia);
			}
			else{
				selectChapter.level[i] = new ImageView(suoguanqia);
			}
			//selectChapter.level[i].setLayoutX(400+300*i);		selectChapter.level[i].setLayoutY(870-guanqia.getHeight());
			//selectChapter.landGroup.getChildren().add(selectChapter.level[i]);
		}

		selectChapter.level[0].setLayoutX(953);		selectChapter.level[0].setLayoutY(174-guanqia.getHeight());
		selectChapter.landGroup.getChildren().add(selectChapter.level[0]);
		selectChapter.level[1].setLayoutX(1193);		selectChapter.level[1].setLayoutY(363-guanqia.getHeight());
		selectChapter.landGroup.getChildren().add(selectChapter.level[1]);
		selectChapter.level[2].setLayoutX(1499);		selectChapter.level[2].setLayoutY(522-guanqia.getHeight());
		selectChapter.landGroup.getChildren().add(selectChapter.level[2]);

		level = selectChapter.level;
	}//����setChapter����
	
	void setLevel(int selection, Chapter chapter){
		
		if(selection == 1){
			chapter.level = new ImageView[5];		//��ʼ��
			
			for(int i = 0;i < 5;i++){
				if(lastGame >= i+1+selection*10){
					chapter.level[i] = new ImageView(guanqia);
				}
				else{
					chapter.level[i] = new ImageView(suoguanqia);
				}
			}//����ͼƬ����
			
			//����λ��
			chapter.level[0].setLayoutX(550);	chapter.level[0].setLayoutY(765-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[0]);
			chapter.level[1].setLayoutX(602);	chapter.level[1].setLayoutY(350-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[1]);
			chapter.level[2].setLayoutX(972);	chapter.level[2].setLayoutY(590-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[2]);
			chapter.level[3].setLayoutX(1315);	chapter.level[3].setLayoutY(748-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[3]);
			chapter.level[4].setLayoutX(1402);	chapter.level[4].setLayoutY(353-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[4]);

			level = null;
			level = chapter.level;

		}
		
		if(selection == 2){
			chapter.level = new ImageView[5];		//��ʼ��
			
			for(int i = 0;i < 5;i++){
				if(lastGame >= i+1+selection*10){
					chapter.level[i] = new ImageView(guanqia);
				}
				else{
					chapter.level[i] = new ImageView(suoguanqia);
				}
			}//����ͼƬ����
			
			//����λ��
			chapter.level[0].setLayoutX(770);	chapter.level[0].setLayoutY(630-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[0]);
			chapter.level[1].setLayoutX(194);	chapter.level[1].setLayoutY(480-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[1]);
			chapter.level[2].setLayoutX(580);	chapter.level[2].setLayoutY(267-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[2]);
			chapter.level[3].setLayoutX(1190);	chapter.level[3].setLayoutY(174-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[3]);
			chapter.level[4].setLayoutX(1665);	chapter.level[4].setLayoutY(570-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[4]);

			level = null;
			level = chapter.level;
		}
		
		if(selection == 3){
			chapter.level = new ImageView[5];		//��ʼ��
			
			for(int i = 0;i < 5;i++){
				if(lastGame >= i+1+selection*10){
					chapter.level[i] = new ImageView(guanqia);
				}
				else{
					chapter.level[i] = new ImageView(suoguanqia);
				}
			}//����ͼƬ����
			
			//����λ��
			chapter.level[0].setLayoutX(476);	chapter.level[0].setLayoutY(748-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[0]);
			chapter.level[1].setLayoutX(665);	chapter.level[1].setLayoutY(425-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[1]);
			chapter.level[2].setLayoutX(1050);	chapter.level[2].setLayoutY(526-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[2]);
			chapter.level[3].setLayoutX(1367);	chapter.level[3].setLayoutY(590-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[3]);
			chapter.level[4].setLayoutX(1698);	chapter.level[4].setLayoutY(636-guanqia.getHeight());
			chapter.landGroup.getChildren().add(chapter.level[4]);

			level = null;
			level = chapter.level;

		}

	}
	
	//�ж��Ƿ�ѡ���˹ؿ�
	void select(){
		//�ж��Ƿ��ڹؿ�����

		for(int i = 0;i < level.length;i++){
			if(x + wormRight.getWidth()/2 > level[i].getLayoutX() && x + wormRight.getWidth()/2 < level[i].getLayoutX() + guanqia.getWidth()
				&& y > level[i].getLayoutY() && y <= SPEED+level[i].getLayoutY() + guanqia.getHeight()){
				if(level[i].getImage() == suoguanqia){			//����ùؿ�������
					continue;
				}
				/*
				LifeGame lifeGame = new LifeGame(13);
				GameLauncher.mainScene.setRoot(lifeGame.game);
				lifeGame.go();
				*/
				if(isSelectingChapter){			//�����ѡ�½�
					selectChapter.storeXY();
					//Chapter chapter = new Chapter(i+1);
					chapter = new Chapter(i+1);
					landGroup = chapter.landGroup;
					land = chapter.land;
					mainGroup.getChildren().remove(life);
					
					//����������Ϊ�µ�ѡ�¶���ĳ���
					//wormRight = chapter.wormRight;
					//wormLeft = chapter.wormLeft;
					
					worm = chapter.worm;

					isSelectingChapter = false;
				}
				else{		//ѡ�ؿ�
					
					LifeGame lifeGame = new LifeGame(chapterIndex*10+i+1, player,this);
					GameLauncher.mainScene.setRoot(lifeGame.game);
					lifeGame.go();
					
				}
			}
		}
	}//select����
	
	class Chapter{			//ÿһ��
		Group eachChapter = new Group();
		Group landGroup = new Group();
		Rectangle[] land;
		
		//Image wormRight,wormLeft;
		ImageView worm;
		
		ImageView[] level;
		
		Button backButton;
		
		double eachX,eachY;
		public Chapter(int selection){
			chapterIndex = selection;

			if(selection == 0){			//ѡ��
				ImageView bg = new ImageView(backGround0);

				landGroup.getChildren().add(bg);
				land = new Rectangle[5];
				
				land[0] = new Rectangle();
				land[0].setLayoutX(765);	land[0].setLayoutY(730);
				land[0].setWidth(1168);		land[0].setHeight(0.1);
				land[1] = new Rectangle();
				land[1].setLayoutX(0);		land[1].setLayoutY(430);
				land[1].setWidth(765);		land[1].setHeight(320);
				land[2] = new Rectangle();
				land[2].setLayoutX(909);	land[2].setLayoutY(174);
				land[2].setWidth(208);		land[2].setHeight(0.1);
				land[3] = new Rectangle();
				land[3].setLayoutX(1149);	land[3].setLayoutY(363);
				land[3].setWidth(208);		land[3].setHeight(0.1);
				land[4] = new Rectangle();
				land[4].setLayoutX(1455);	land[4].setLayoutY(522);
				land[4].setWidth(208);		land[4].setHeight(0.1);
				
				life.getChildren().add(landGroup);
				
				//wormRight = new Image(this.getClass().getResourceAsStream("image/right.png"), 65, 100,false,false);
				worm = new ImageView(wormRight);

				worm.setLayoutY(430-wormRight.getHeight());
				//x = 0;	y = worm.getLayoutY() + wormRight.getHeight();
				eachX = 0;		eachY = worm.getLayoutY() + wormRight.getHeight();
				
				x = eachX;		y = eachY;
			}
			
			if(selection == 1){			//��һ��
				
				land = new Rectangle[8];
				
				//���ñ���
				ImageView bg = new ImageView(backGround1);

				landGroup.getChildren().add(bg);

				//
				land[0] = new Rectangle();
				land[0].setLayoutX(310);		land[0].setLayoutY(917);
				land[0].setWidth(1570);		land[0].setHeight(0.1);
				//landGroup.getChildren().add(land[0]);
				
				land[1] = new Rectangle();
				land[1].setLayoutX(409);	land[1].setLayoutY(765);
				land[1].setWidth(402);		land[1].setHeight(0.1);
				//landGroup.getChildren().add(land[1]);
				
				land[2] = new Rectangle();
				land[2].setLayoutX(558);	land[2].setLayoutY(350);
				land[2].setWidth(208);		land[2].setHeight(0.1);
				//landGroup.getChildren().add(land[2]);
				
				land[3] = new Rectangle();
				land[3].setLayoutX(928);	land[3].setLayoutY(590);
				land[3].setWidth(208);		land[3].setHeight(0.1);
				//landGroup.getChildren().add(land[3]);

				land[4] = new Rectangle();
				land[4].setLayoutX(1271);	land[4].setLayoutY(748);
				land[4].setWidth(208);		land[4].setHeight(0.1);
				//landGroup.getChildren().add(land[4]);
				
				land[5] = new Rectangle();
				land[5].setLayoutX(1358);	land[5].setLayoutY(353);
				land[5].setWidth(208);		land[5].setHeight(0.1);
				//landGroup.getChildren().add(land[5]);
				
				land[6] = new Rectangle();
				land[6].setLayoutX(0);	land[6].setLayoutY(465);
				land[6].setWidth(310);		land[6].setHeight(460);

				land[7] = new Rectangle();
				land[7].setLayoutX(1741);	land[7].setLayoutY(465);
				land[7].setWidth(180);		land[7].setHeight(500);
				
				eachChapter.getChildren().add(landGroup);
				
				//���ùؿ�ͼ
				setLevel(selection, this);
				
				//���ذ�ť
				ImageView b = new ImageView(back);
				backButton = new Button("", b);
				backButton.setMaxSize(90, 90);
				backButton.setLayoutX(0);
				backButton.setStyle("-fx-background-color:null");			//���ð�ť͸��
				backButton.setOnAction(new BackHandler());
				eachChapter.getChildren().add(backButton);

				//�������
				//wormRight = new Image(this.getClass().getResourceAsStream("image/ƻ��1.png"), 80, 80,false,false);
				worm = new ImageView(wormRight);

				worm.setLayoutX(310);		worm.setLayoutY(917-wormRight.getHeight());
				//x = 0;	y = worm.getLayoutY() + wormRight.getHeight();
				eachX = 310;		eachY = worm.getLayoutY() + wormRight.getHeight();
				
				x = eachX;		y = eachY;
				
				eachChapter.getChildren().add(worm);
				
				Image chapter1 = new Image(this.getClass().getResourceAsStream("image/chapter_1.gif"));
				ImageView chapter1View = new ImageView(chapter1);
				
				GridPane tempPane = new GridPane();
				tempPane.setStyle("-fx-background-color:black");
				tempPane.setAlignment(Pos.CENTER);
				tempPane.setMinSize(GameLauncher.mainScene.getWidth(), GameLauncher.mainScene.getHeight());
				tempPane.add(chapter1View, 0, 0);
				eachChapter.getChildren().add(tempPane);
				
				mainGroup.getChildren().add(eachChapter);

				//��ʱ
				int time = 4000;
				Timeline timeLine = new Timeline();
				KeyFrame kf = new KeyFrame(Duration.millis(time));
				timeLine.getKeyFrames().add(kf);
				timeLine.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e){
						//�Ƴ����ض���
						eachChapter.getChildren().remove(tempPane);
					}
				});
				timeLine.play();

			}

			if(selection == 2){			//�ڶ���
				
				land = new Rectangle[6];
				
				//���ñ���
				ImageView bg = new ImageView(backGround2);

				landGroup.getChildren().add(bg);

				//
				land[0] = new Rectangle();
				land[0].setLayoutX(0);		land[0].setLayoutY(870);
				land[0].setWidth(1520);		land[0].setHeight(0.1);
				//landGroup.getChildren().add(land[0]);
				
				land[1] = new Rectangle();
				land[1].setLayoutX(690);	land[1].setLayoutY(630);
				land[1].setWidth(280);		land[1].setHeight(0.1);
				//landGroup.getChildren().add(land[1]);
				
				land[2] = new Rectangle();
				land[2].setLayoutX(160);	land[2].setLayoutY(480);
				land[2].setWidth(188);		land[2].setHeight(0.1);
				//landGroup.getChildren().add(land[2]);
				
				land[3] = new Rectangle();
				land[3].setLayoutX(486);	land[3].setLayoutY(267);
				land[3].setWidth(310);		land[3].setHeight(0.1);
				//landGroup.getChildren().add(land[3]);

				land[4] = new Rectangle();
				land[4].setLayoutX(1064);	land[4].setLayoutY(174);
				land[4].setWidth(375);		land[4].setHeight(0.1);
				//landGroup.getChildren().add(land[4]);
				
				land[5] = new Rectangle();
				land[5].setLayoutX(1514);	land[5].setLayoutY(570);
				land[5].setWidth(422);		land[5].setHeight(450);
				//landGroup.getChildren().add(land[5]);

				eachChapter.getChildren().add(landGroup);
				
				//���ùؿ�ͼ
				setLevel(selection, this);
				
				//���ذ�ť
				ImageView b = new ImageView(back);
				backButton = new Button("", b);
				backButton.setMaxSize(90, 90);
				backButton.setLayoutX(0);
				backButton.setStyle("-fx-background-color:null");			//���ð�ť͸��
				backButton.setOnAction(new BackHandler());
				eachChapter.getChildren().add(backButton);

				//�������
				//wormRight = new Image(this.getClass().getResourceAsStream("image/ƻ��1.png"), 80, 80,false,false);
				worm = new ImageView(wormRight);

				worm.setLayoutX(0);		worm.setLayoutY(870-wormRight.getHeight());
				//x = 0;	y = worm.getLayoutY() + wormRight.getHeight();
				eachX = 0;		eachY = worm.getLayoutY() + wormRight.getHeight();
				
				x = eachX;		y = eachY;
				
				eachChapter.getChildren().add(worm);
								
				Image chapter2 = new Image(this.getClass().getResourceAsStream("image/chapter_2.gif"));
				ImageView chapter1View = new ImageView(chapter2);
				
				GridPane tempPane = new GridPane();
				tempPane.setStyle("-fx-background-color:black");
				tempPane.setAlignment(Pos.CENTER);
				tempPane.setMinSize(GameLauncher.mainScene.getWidth(), GameLauncher.mainScene.getHeight());
				tempPane.add(chapter1View, 0, 0);
				eachChapter.getChildren().add(tempPane);
				
				mainGroup.getChildren().add(eachChapter);

				//��ʱ
				int time = 4000;
				Timeline timeLine = new Timeline();
				KeyFrame kf = new KeyFrame(Duration.millis(time));
				timeLine.getKeyFrames().add(kf);
				timeLine.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e){
						//�Ƴ����ض���
						eachChapter.getChildren().remove(tempPane);
					}
				});
				timeLine.play();
			}
			
			if(selection == 3){			//������
				
				land = new Rectangle[10];
				
				//���ñ���
				ImageView bg = new ImageView(backGround3);

				landGroup.getChildren().add(bg);

				//
				land[0] = new Rectangle();
				land[0].setLayoutX(0);		land[0].setLayoutY(748);
				land[0].setWidth(639);		land[0].setHeight(0.1);
				//landGroup.getChildren().add(land[0]);
				
				land[1] = new Rectangle();
				land[1].setLayoutX(425);	land[1].setLayoutY(567);
				land[1].setWidth(222);		land[1].setHeight(0.1);
				//landGroup.getChildren().add(land[1]);
				
				land[2] = new Rectangle();
				land[2].setLayoutX(632);	land[2].setLayoutY(425);
				land[2].setWidth(185);		land[2].setHeight(440);
				//landGroup.getChildren().add(land[2]);
				
				land[3] = new Rectangle();
				land[3].setLayoutX(811);	land[3].setLayoutY(590);
				land[3].setWidth(216);		land[3].setHeight(0.1);
				//landGroup.getChildren().add(land[3]);

				land[4] = new Rectangle();
				land[4].setLayoutX(1020);	land[4].setLayoutY(526);
				land[4].setWidth(180);		land[4].setHeight(330);
				//landGroup.getChildren().add(land[4]);
				
				land[5] = new Rectangle();
				land[5].setLayoutX(1192);	land[5].setLayoutY(674);
				land[5].setWidth(153);		land[5].setHeight(0.1);
				//landGroup.getChildren().add(land[5]);
				
				land[6] = new Rectangle();
				land[6].setLayoutX(1334);	land[6].setLayoutY(590);
				land[6].setWidth(185);		land[6].setHeight(210);

				land[7] = new Rectangle();
				land[7].setLayoutX(1515);	land[7].setLayoutY(673);
				land[7].setWidth(155);		land[7].setHeight(0);
				
				land[8] = new Rectangle();
				land[8].setLayoutX(1663);	land[8].setLayoutY(636);
				land[8].setWidth(190);		land[8].setHeight(340);
				
				land[9] = new Rectangle();
				land[9].setLayoutX(1848);	land[9].setLayoutY(695);
				land[9].setWidth(77);		land[9].setHeight(0.1);
				
				eachChapter.getChildren().add(landGroup);

				//���ùؿ�ͼ
				setLevel(selection, this);
				
				//���ذ�ť
				ImageView b = new ImageView(back);
				backButton = new Button("", b);
				backButton.setMaxSize(90, 90);
				backButton.setLayoutX(0);
				backButton.setStyle("-fx-background-color:null");			//���ð�ť͸��
				backButton.setOnAction(new BackHandler());
				eachChapter.getChildren().add(backButton);

				//�������
				//wormRight = new Image(this.getClass().getResourceAsStream("image/ƻ��1.png"), 80, 80,false,false);
				worm = new ImageView(wormRight);

				worm.setLayoutX(0);		worm.setLayoutY(748-wormRight.getHeight());
				//x = 0;	y = worm.getLayoutY() + wormRight.getHeight();
				eachX = 0;		eachY = worm.getLayoutY() + wormRight.getHeight();
				
				x = eachX;		y = eachY;
				
				eachChapter.getChildren().add(worm);
								
				Image chapter3 = new Image(this.getClass().getResourceAsStream("image/chapter_3.gif"));
				ImageView chapter1View = new ImageView(chapter3);
				
				GridPane tempPane = new GridPane();
				tempPane.setStyle("-fx-background-color:black");
				tempPane.setAlignment(Pos.CENTER);
				tempPane.setMinSize(GameLauncher.mainScene.getWidth(), GameLauncher.mainScene.getHeight());
				tempPane.add(chapter1View, 0, 0);
				eachChapter.getChildren().add(tempPane);
				
				mainGroup.getChildren().add(eachChapter);

				//��ʱ
				int time = 4000;
				Timeline timeLine = new Timeline();
				KeyFrame kf = new KeyFrame(Duration.millis(time));
				timeLine.getKeyFrames().add(kf);
				timeLine.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e){
						//�Ƴ����ض���
						eachChapter.getChildren().remove(tempPane);
					}
				});
				timeLine.play();
			}

		}		//���캯������
		
		//��������
		void storeXY(){
			this.eachX = x;
			this.eachY = y;
		}

	}//ÿһ��
	
	//���ط���
	public static void back(){
		GameLauncher.mainScene.setRoot(mainGroup);
	}
	
	//���µ�ͼ�ϵĹؿ�����
	public void update(){
		if(lastGame == 35 && player.lastGame != lastGame){			//���ͨ����,����ͨ�ض���
			player.lastGame = lastGame;			//�ָ������һ�ص�״̬
			DataBase.storePlayer(player);
			
			Media video2 = new Media(this.getClass().getResource("video/����02.mp4").toString());
			MediaPlayer video2Player = new MediaPlayer(video2);
			MediaView video2View = new MediaView(video2Player);
			
			//ֹͣ��������
			BackgroundMusic.stopMainBGM();
			
			video2View.setFitWidth(GameLauncher.mainScene.getWidth());
			GridPane videoPane = new GridPane();
			videoPane.setStyle("-fx-background-color:black");
			video2Player.setAutoPlay(true);
			videoPane.setAlignment(Pos.CENTER);
			videoPane.add(video2View, 0, 0);
			
			Label tiaoguo = new Label("����������");
			tiaoguo.setStyle("-fx-font-fill:white");
			tiaoguo.setStyle("-fx-font-size:30");

			HBox temp = new HBox();
			temp.setAlignment(Pos.TOP_RIGHT);
			temp.getChildren().add(tiaoguo);
			videoPane.add(temp, 0, 1);
			
			GameLauncher.mainScene.setRoot(videoPane);
			
			videoPane.setOnMouseClicked(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent e){
					video2Player.stop();
				}
			});
			
			video2Player.setOnEndOfMedia(()->{
				GameLauncher.mainScene.setRoot(mainGroup);
				
				//����BGM
				BackgroundMusic.playMainBGM();
				
				video2Player.dispose();
			});
			
			video2Player.setOnStopped(()->{
				GameLauncher.mainScene.setRoot(mainGroup);
				
				//����BGM
				BackgroundMusic.playMainBGM();
				
				video2Player.dispose();
			});
		}
		
		lastGame = player.lastGame;
		
		chapter.eachChapter.getChildren().remove(chapter.backButton);
		chapter.eachChapter.getChildren().remove(chapter.worm);
		chapter.eachChapter.getChildren().remove(chapter.landGroup);
		for(int i = 0;i < chapter.level.length;i++){
			if(player.lastGame >= chapterIndex*10 + i+1 && chapter.level[i].getImage() == suoguanqia){
				chapter.landGroup.getChildren().remove(chapter.level[i]);
				chapter.level[i].setImage(guanqia);
				chapter.landGroup.getChildren().add(chapter.level[i]);
			}
		}
		chapter.eachChapter.getChildren().add(chapter.landGroup);
		chapter.eachChapter.getChildren().add(chapter.worm);
		chapter.eachChapter.getChildren().add(chapter.backButton);

		life.getChildren().remove(selectChapter.backButton);
		life.getChildren().remove(selectChapter.worm);
		life.getChildren().remove(selectChapter.landGroup);
		for(int i = 0;i < selectChapter.level.length;i++){
			if(player.lastGame >= (i+1)*10 && selectChapter.level[i].getImage() == suoguanqia){
				selectChapter.landGroup.getChildren().remove(selectChapter.level[i]);
				selectChapter.level[i].setImage(guanqia);
				selectChapter.landGroup.getChildren().add(selectChapter.level[i]);
			}
		}
		life.getChildren().add(selectChapter.landGroup);
		life.getChildren().add(selectChapter.worm);
		life.getChildren().add(selectChapter.backButton);

	}

}//��Life����
