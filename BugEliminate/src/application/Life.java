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
	static final int HEIGHT = 300;			//跳跃高度
	static final int SPEED = 3;				//速度
	
	Player player;
	int lastGame = 11;			//默认为11
	
	int chapterIndex = 0;		//0为选章节，记录处于第几章
	
	//static Group life = new Group();
	static Group mainGroup = new Group();			//主Group
	
	Chapter selectChapter;				//选章
	Chapter chapter;
	
	Group life = new Group();
	//Group landGroup = new Group();
	Group landGroup;
	Rectangle[] land;
	
	Media video1 = new Media(this.getClass().getResource("video/动画01.mp4").toString());
	MediaPlayer video1Player = new MediaPlayer(video1);
	MediaView video1View = new MediaView(video1Player);
	
	Image backGround0 = new Image(this.getClass().getResourceAsStream("image/生涯背景0.png"));
	Image backGround1 = new Image(this.getClass().getResourceAsStream("image/生涯背景1.png"));
	Image backGround2 = new Image(this.getClass().getResourceAsStream("image/生涯背景2.png"));
	Image backGround3 = new Image(this.getClass().getResourceAsStream("image/生涯背景3.png"));

	//Image backGround = new Image(this.getClass().getResourceAsStream("image/测试生涯背景.jpg"));
	Image back = new Image(this.getClass().getResourceAsStream("image/返回1.png"), 80, 80, false, false);
	Image wormLeft, wormRight;
	//Image mainLand = new Image(this.getClass().getResourceAsStream("image/遮挡.jpg"), backGround.getWidth(),30, false,false);
	//Image smallLand = new Image(this.getClass().getResourceAsStream("image/遮挡.jpg"), 200, 30,false, false);
	Image guanqia = new Image(this.getClass().getResourceAsStream("image/关卡.png"),120,120,false,false);
	Image suoguanqia = new Image(this.getClass().getResourceAsStream("image/锁关卡.png"),120,120,false,false);
	
	//ImageView bg = new ImageView(backGround);
	ImageView worm;
	//ImageView[] landView = new ImageView[5];
	ImageView[] level;
	
	Right right;				//→类
	Thread rightThread;
	Left left;					//←类
	Thread leftThread;
	
	Thread upThread;
	Thread checkDropThread;
	
	boolean isJumping = false;
	
	boolean isSelectingChapter = true;				//记录是否在选章界面
	
	double x ,y;			//用来保存当前虫子的坐标
	//构造函数
	public Life(Player player){
		this.player = player;
		//wormRight = new Image(this.getClass().getResourceAsStream("image/苹果1.png"), 80, 80,false,false);
		//worm = new ImageView(wormRight);
		//worm.setLayoutY(870-wormRight.getHeight());
		//x = 0;	y = worm.getLayoutY() + wormRight.getHeight();
	}
	
	public void go(){
		
		video1View.setFitWidth(GameLauncher.mainScene.getWidth());
		GridPane videoPane = new GridPane();
		videoPane.setStyle("-fx-background-color:black");
		
		//暂停背景音乐
		BackgroundMusic.stopMainBGM();
		
		video1Player.setAutoPlay(true);
		videoPane.setAlignment(Pos.CENTER);
		videoPane.add(video1View, 0, 0);
		
		Label tiaoguo = new Label("单击以跳过");
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
			
			//继续背景音乐
			BackgroundMusic.playMainBGM();
			
			video1Player.dispose();
		});
		
		video1Player.setOnStopped(()->{
			GameLauncher.mainScene.setRoot(mainGroup);
			
			//继续背景音乐
			BackgroundMusic.playMainBGM();
			
			video1Player.dispose();
		});
		
		/*
		Media video2 = new Media(this.getClass().getResource("video/动画02.mp4").toString());
		MediaPlayer video2Player = new MediaPlayer(video2);
		MediaView video2View = new MediaView(video2Player);
		
		//停止背景音乐
		BackgroundMusic.stopMainBGM();
		
		video2View.setFitWidth(GameLauncher.mainScene.getWidth());
		GridPane videoPane = new GridPane();
		videoPane.setStyle("-fx-background-color:black");
		video2Player.setAutoPlay(true);
		videoPane.setAlignment(Pos.CENTER);
		videoPane.add(video2View, 0, 0);
		
		Label tiaoguo = new Label("单击以跳过");
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
			
			//继续BGM
			BackgroundMusic.playMainBGM();
			
			video2Player.dispose();
		});
		
		video2Player.setOnStopped(()->{
			GameLauncher.mainScene.setRoot(mainGroup);
			
			//继续BGM
			BackgroundMusic.playMainBGM();
			
			video2Player.dispose();
		});
		 */
		
		//设置键盘监听
		right = new Right();
		left = new Left();
		
		KeyPressed kp = new KeyPressed();
		//life.setOnKeyPressed(kp);
		mainGroup.setOnKeyPressed(kp);
		KeyReleased kr = new KeyReleased();
		//life.setOnKeyReleased(kr);
		mainGroup.setOnKeyReleased(kr);
		
		/*
		//设置背景
		landGroup.getChildren().add(bg);
		
		//设置陆地
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
		
		//设置关卡图
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

		//新建选章的对象
		selectChapter = new Chapter(0);
		landGroup = selectChapter.landGroup;
		land = selectChapter.land;
		//将虫子设置为新的选章对象的虫子
		//wormRight = selectChapter.wormRight;
		//wormLeft = selectChapter.wormLeft;
		worm = selectChapter.worm;
		
		setChapter();				//设置章节
		
		//返回按钮
		ImageView b = new ImageView(back);
		selectChapter.backButton = new Button("", b);
		selectChapter.backButton.setMaxSize(90, 90);
		selectChapter.backButton.setLayoutX(0);
		selectChapter.backButton.setStyle("-fx-background-color:null");			//设置按钮透明
		selectChapter.backButton.setOnAction(new BackHandler());
		life.getChildren().add(selectChapter.backButton);
		
		//加入虫子
		life.getChildren().add(worm);
		
		mainGroup.getChildren().add(life);				//将life加入mainGroup
	}
	
	class BackHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			if(isSelectingChapter){
				life = null;
				//mainGroup = null;
				GameLauncher.back();
			}
			else{			//不是选章界面
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
	
	//键盘监听类
	class KeyPressed implements EventHandler<KeyEvent>{
		public void handle(KeyEvent e){
			if(e.getCode() == KeyCode.RIGHT){			//按下→时
				/*
				//判断是否到达边界
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
			}//判断是否按下→结束
			
			if(e.getCode() == KeyCode.LEFT){
				/*
				//判断是否到达边界
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
			}//判断是否按下←结束
			
			if(e.getCode() == KeyCode.UP){
				
				if(isJumping || (upThread != null && upThread.isAlive()) ){		//同时只能跳一次
					return;
				}
				
				isJumping = true;			//设置为在跳跃状态
				
				/*
				Timeline timeLine1 = new Timeline();
				KeyValue kv1 = new KeyValue(worm.yProperty(), worm.getY()-HEIGHT);
				KeyFrame kf1 = new KeyFrame(Duration.millis(HEIGHT*2), kv1);
				timeLine1.getKeyFrames().add(kf1);
								
				//设置下落动画
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
				
			}//↑结束
			
			if(e.getCode() == KeyCode.ESCAPE){
				if(isSelectingChapter){
					life = null;
					//mainGroup = null;
					GameLauncher.back();
				}
				else{			//不是选章界面
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
			}//ESC键返回
			
			if(e.getCode() == KeyCode.ENTER){
				select();
			}//按下空格键进行选择
			
		}//方法handle结束
	}//类KeyPressed结束
	
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
	//下落方法
	void drop(){
		//y = y - HEIGHT;
		//判断下一次的落点
		//double nextY = 550;
		double nextY = 0;
		
		//判断是否落在小地上
		for(int i = 1;i < 3;i++){
			//先判断横坐标
			if(y < landView[i].getLayoutY() && x + wormRight.getWidth() > landView[i].getLayoutX() && x < landView[i].getLayoutX() + smallLand.getWidth()){
				//if(landView[i].getLayoutY() < nextY){
				if(landView[i].getLayoutY() > y){
					nextY = landView[i].getLayoutY();		//设置下一次落点
				}
			}
		}
		
		//判断是否落在大地上
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
					isJumping = false;			//设置为未跳跃状态
					
					//检查是否会下落
					//checkDrop();
			}
		});
		timeLine2.play();
		y = nextY;
	}//方法drop结束
	*/
	
	//不用
	
	void checkDrop(){
		int onWhichOne = -1;
		for(int i = 0;i < land.length;i++){
			if(y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2){
				onWhichOne = i;			//记录处在哪一块上
				break;
			}
		}

		//如果在大地上,不去检查是否会下掉
		if(onWhichOne == -1){
			return;
		}
		//System.out.println(onWhichOne);
		//判断是否要掉落
		//if(x + wormRight.getWidth() <= landView[onWhichOne].getLayoutX() || x >= landView[onWhichOne].getLayoutX() + smallLand.getWidth()){
		if(x + wormRight.getWidth() <= land[onWhichOne].getLayoutX() || x >= land[onWhichOne].getLayoutX() + land[onWhichOne].getWidth()){
			//System.out.println("wc");
			isJumping = true;
			double nextY = 0;
			
			for(int i = 1;i < land.length;i++){
				//先判断横坐标
				//if(x + wormRight.getWidth() > landView[i].getLayoutX() && x < landView[i].getLayoutX() + smallLand.getWidth()){
				if(x + wormRight.getWidth() > land[i].getLayoutX() && x < land[i].getLayoutX() + land[onWhichOne].getWidth()){
					if(land[i].getLayoutY() > y){
						nextY = land[i].getLayoutY();		//设置下一次落点
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
					isJumping = false;			//设置为未跳跃状态
					
					checkDrop();			//递归检查是否还能继续下落
				}
			});
			timeLine2.play();
			y = nextY;
		}//if结束

	}//方法checkDrop
	
	
	class Right implements Runnable{
		boolean isPressed = false;			//记录→方向键是否被按下
		
		public void run(){
			loop:while(isPressed){
				//改变虫子方向
				worm.setImage(wormRight);
				
				//判断是否到达边界
				if(landGroup.getLayoutX() <= 4-(backGround1.getWidth() - GameLauncher.mainScene.getWidth())
						&& worm.getLayoutX() >= GameLauncher.mainScene.getWidth() - wormRight.getWidth()){
					return;
				}
				
				//判断是否撞墙
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
						//if(checkDropThread != null && !checkDropThread.isAlive() ){		//同时只能跳一次
						//}
							for(int i = 0;i < land.length;i++){
								if( (x + wormRight.getWidth() <= land[i].getLayoutX() || x >= land[i].getLayoutX() + land[i].getWidth() )
										&& (y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2)){
									if(!isJumping){				//防止二次加载线程加速下落
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
						//if(checkDropThread != null && !checkDropThread.isAlive() ){		//同时只能跳一次
						for(int i = 0;i < land.length;i++){
							if( (x + wormRight.getWidth() <= land[i].getLayoutX() || x >= land[i].getLayoutX() + land[i].getWidth() )
									&& (y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2)){
								if(!isJumping){				//防止二次加载线程加速下落
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
					Thread.sleep(5);			//设置延时
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}//run结束
	}//Right类结束
	
	class Left implements Runnable{
		boolean isPressed = false;		//用来记录←方向键是否被按下
		public void run(){
			loop:while(isPressed){
				//改变虫子方向
				worm.setImage(wormLeft);
				
				//判断是否到达边界
				if(landGroup.getLayoutX() >= -4
						&& worm.getLayoutX() <= 0){
					return;
				}
				
				//判断是否撞墙
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
						//if(checkDropThread != null && !checkDropThread.isAlive() ){		//同时只能跳一次
						for(int i = 0;i < land.length;i++){
							if( (x + wormRight.getWidth() <= land[i].getLayoutX() || x >= land[i].getLayoutX() + land[i].getWidth() )
									&& (y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2)){
								if(!isJumping){				//防止二次加载线程加速下落
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
						//if(checkDropThread != null && !checkDropThread.isAlive() ){		//同时只能跳一次
						for(int i = 0;i < land.length;i++){
							if( (x + wormRight.getWidth() <= land[i].getLayoutX() || x >= land[i].getLayoutX() + land[i].getWidth() )
									&& (y >= land[i].getLayoutY() && y <= land[i].getLayoutY()+2)){
								if(!isJumping){				//防止二次加载线程加速下落
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
					Thread.sleep(5);			//设置延时
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}//run结束
	}//Left类结束
	
	class Up implements Runnable{
		public void run(){
			try{
			int height = 0;			//记录当前跳了多高
			while(height < HEIGHT){		//起跳
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
			//下落
			while(isJumping){
				//检查是否落地
				for(int i = 0;i < land.length;i++){
					/*
					if(i == 0){		//判断是否落在大地上
						if(y == landView[0].getLayoutY()){
							isJumping = false;
							return;
						}
					}
					else{
						//先判断横坐标
						if(x + wormRight.getWidth() > landView[i].getLayoutX() 
								&& x < landView[i].getLayoutX() + smallLand.getWidth()){
							//再判断纵坐标是否相等
							if(y == landView[i].getLayoutY()){
								isJumping = false;
								return;
							}
						}
					}
					*/
					
					//先判断横坐标
					if(x + wormRight.getWidth() > land[i].getLayoutX() 
							&& x < land[i].getLayoutX() + land[i].getWidth()){
						//再判断纵坐标是否相等
						if(y >= land[i].getLayoutY() && y <= land[i].getLayoutY() + 2){
							isJumping = false;
							return;
						}
					}

				}//for结束
				//虫子坐标+2
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
			}//while结束
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}//run结束
	}//类Up结束
	
	
	//不用,删除
	
	class CheckDrop implements Runnable{
		public void run(){
			while(isJumping){
				//检查是否落地
				for(int i = 0;i < land.length;i++){
					//先判断横坐标
					if(x + wormRight.getWidth() > land[i].getLayoutX() 
							&& x < land[i].getLayoutX() + land[i].getWidth()){
						//再判断纵坐标是否相等
						if(y >= land[i].getLayoutY() && y <= land[i].getLayoutY() + 2){
							isJumping = false;
							return;
						}
					}

				}//for结束
				//虫子坐标+2
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
			}//while结束
		}//handle结束
	}//CheckDrop类结束
	
	
	void setChapter(){
		lastGame = player.lastGame;				//读取进度
		
		//加上关卡图
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
	}//方法setChapter结束
	
	void setLevel(int selection, Chapter chapter){
		
		if(selection == 1){
			chapter.level = new ImageView[5];		//初始化
			
			for(int i = 0;i < 5;i++){
				if(lastGame >= i+1+selection*10){
					chapter.level[i] = new ImageView(guanqia);
				}
				else{
					chapter.level[i] = new ImageView(suoguanqia);
				}
			}//设置图片结束
			
			//设置位置
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
			chapter.level = new ImageView[5];		//初始化
			
			for(int i = 0;i < 5;i++){
				if(lastGame >= i+1+selection*10){
					chapter.level[i] = new ImageView(guanqia);
				}
				else{
					chapter.level[i] = new ImageView(suoguanqia);
				}
			}//设置图片结束
			
			//设置位置
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
			chapter.level = new ImageView[5];		//初始化
			
			for(int i = 0;i < 5;i++){
				if(lastGame >= i+1+selection*10){
					chapter.level[i] = new ImageView(guanqia);
				}
				else{
					chapter.level[i] = new ImageView(suoguanqia);
				}
			}//设置图片结束
			
			//设置位置
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
	
	//判断是否选择了关卡
	void select(){
		//判断是否在关卡附近

		for(int i = 0;i < level.length;i++){
			if(x + wormRight.getWidth()/2 > level[i].getLayoutX() && x + wormRight.getWidth()/2 < level[i].getLayoutX() + guanqia.getWidth()
				&& y > level[i].getLayoutY() && y <= SPEED+level[i].getLayoutY() + guanqia.getHeight()){
				if(level[i].getImage() == suoguanqia){			//如果该关卡是锁的
					continue;
				}
				/*
				LifeGame lifeGame = new LifeGame(13);
				GameLauncher.mainScene.setRoot(lifeGame.game);
				lifeGame.go();
				*/
				if(isSelectingChapter){			//如果在选章节
					selectChapter.storeXY();
					//Chapter chapter = new Chapter(i+1);
					chapter = new Chapter(i+1);
					landGroup = chapter.landGroup;
					land = chapter.land;
					mainGroup.getChildren().remove(life);
					
					//将虫子设置为新的选章对象的虫子
					//wormRight = chapter.wormRight;
					//wormLeft = chapter.wormLeft;
					
					worm = chapter.worm;

					isSelectingChapter = false;
				}
				else{		//选关卡
					
					LifeGame lifeGame = new LifeGame(chapterIndex*10+i+1, player,this);
					GameLauncher.mainScene.setRoot(lifeGame.game);
					lifeGame.go();
					
				}
			}
		}
	}//select方法
	
	class Chapter{			//每一章
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

			if(selection == 0){			//选章
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
			
			if(selection == 1){			//第一章
				
				land = new Rectangle[8];
				
				//设置背景
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
				
				//设置关卡图
				setLevel(selection, this);
				
				//返回按钮
				ImageView b = new ImageView(back);
				backButton = new Button("", b);
				backButton.setMaxSize(90, 90);
				backButton.setLayoutX(0);
				backButton.setStyle("-fx-background-color:null");			//设置按钮透明
				backButton.setOnAction(new BackHandler());
				eachChapter.getChildren().add(backButton);

				//加入虫子
				//wormRight = new Image(this.getClass().getResourceAsStream("image/苹果1.png"), 80, 80,false,false);
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

				//计时
				int time = 4000;
				Timeline timeLine = new Timeline();
				KeyFrame kf = new KeyFrame(Duration.millis(time));
				timeLine.getKeyFrames().add(kf);
				timeLine.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e){
						//移除加载动画
						eachChapter.getChildren().remove(tempPane);
					}
				});
				timeLine.play();

			}

			if(selection == 2){			//第二章
				
				land = new Rectangle[6];
				
				//设置背景
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
				
				//设置关卡图
				setLevel(selection, this);
				
				//返回按钮
				ImageView b = new ImageView(back);
				backButton = new Button("", b);
				backButton.setMaxSize(90, 90);
				backButton.setLayoutX(0);
				backButton.setStyle("-fx-background-color:null");			//设置按钮透明
				backButton.setOnAction(new BackHandler());
				eachChapter.getChildren().add(backButton);

				//加入虫子
				//wormRight = new Image(this.getClass().getResourceAsStream("image/苹果1.png"), 80, 80,false,false);
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

				//计时
				int time = 4000;
				Timeline timeLine = new Timeline();
				KeyFrame kf = new KeyFrame(Duration.millis(time));
				timeLine.getKeyFrames().add(kf);
				timeLine.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e){
						//移除加载动画
						eachChapter.getChildren().remove(tempPane);
					}
				});
				timeLine.play();
			}
			
			if(selection == 3){			//第三章
				
				land = new Rectangle[10];
				
				//设置背景
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

				//设置关卡图
				setLevel(selection, this);
				
				//返回按钮
				ImageView b = new ImageView(back);
				backButton = new Button("", b);
				backButton.setMaxSize(90, 90);
				backButton.setLayoutX(0);
				backButton.setStyle("-fx-background-color:null");			//设置按钮透明
				backButton.setOnAction(new BackHandler());
				eachChapter.getChildren().add(backButton);

				//加入虫子
				//wormRight = new Image(this.getClass().getResourceAsStream("image/苹果1.png"), 80, 80,false,false);
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

				//计时
				int time = 4000;
				Timeline timeLine = new Timeline();
				KeyFrame kf = new KeyFrame(Duration.millis(time));
				timeLine.getKeyFrames().add(kf);
				timeLine.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e){
						//移除加载动画
						eachChapter.getChildren().remove(tempPane);
					}
				});
				timeLine.play();
			}

		}		//构造函数结束
		
		//保存坐标
		void storeXY(){
			this.eachX = x;
			this.eachY = y;
		}

	}//每一章
	
	//返回方法
	public static void back(){
		GameLauncher.mainScene.setRoot(mainGroup);
	}
	
	//更新地图上的关卡数据
	public void update(){
		if(lastGame == 35 && player.lastGame != lastGame){			//如果通关了,播放通关动画
			player.lastGame = lastGame;			//恢复成最后一关的状态
			DataBase.storePlayer(player);
			
			Media video2 = new Media(this.getClass().getResource("video/动画02.mp4").toString());
			MediaPlayer video2Player = new MediaPlayer(video2);
			MediaView video2View = new MediaView(video2Player);
			
			//停止背景音乐
			BackgroundMusic.stopMainBGM();
			
			video2View.setFitWidth(GameLauncher.mainScene.getWidth());
			GridPane videoPane = new GridPane();
			videoPane.setStyle("-fx-background-color:black");
			video2Player.setAutoPlay(true);
			videoPane.setAlignment(Pos.CENTER);
			videoPane.add(video2View, 0, 0);
			
			Label tiaoguo = new Label("单击以跳过");
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
				
				//继续BGM
				BackgroundMusic.playMainBGM();
				
				video2Player.dispose();
			});
			
			video2Player.setOnStopped(()->{
				GameLauncher.mainScene.setRoot(mainGroup);
				
				//继续BGM
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

}//类Life结束
