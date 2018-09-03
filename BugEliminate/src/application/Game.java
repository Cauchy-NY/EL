package application;

import java.io.*;
import java.util.ArrayList;

import application.LifeGame.backHandler;
import application.LifeGame.restartHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.*;
import javafx.geometry.HPos;
import javafx.geometry.Pos;

public class Game{
	public static final int MARGIN1=100;     //边距  
	public static final int MARGIN2=200;
	public static final int GRID_SPAN=75;   //网格间距  

	boolean isTimeMode;
	
	static final int MOVETIME = 150;

	//计时
	Clock clock = new Clock();
	Thread clockThread = new Thread(clock);
	
	BasePlay basePlay = new BasePlay();
	Player player;
	//DataBase dataBase;
	
	//static Group game = new Group();
	Group game = new Group();
	//Image background = new Image(this.getClass().getResourceAsStream("image/背景.jpg"));
	Image background = new Image(this.getClass().getResourceAsStream("image/林背景.png"), GameLauncher.mainScene.getWidth(), GameLauncher.mainScene.getHeight(),false,false);
	//Image background = new Image(this.getClass().getResourceAsStream("image/背景1.png"), GameLauncher.mainScene.getWidth(),GameLauncher.mainScene.getHeight(),false,false);
	Image pause = new Image(this.getClass().getResourceAsStream("image/greyPause.png"), 80, 80, false, false);
	//Image pause = new Image(this.getClass().getResourceAsStream("image/button/暂停按钮final.png"), 80, 80, false, false);
	//Image pause = new Image(this.getClass().getResourceAsStream("image/暂停.png"), 80, 80, false, false);
	Image zhedang = new Image(this.getClass().getResourceAsStream("image/遮挡.jpg"));			//遮挡隐藏的水果，从而实现掉落特效
	Image fanhuiyouxi = new Image(this.getClass().getResourceAsStream("image/button/返回游戏.png"),200,80,false,false);
	Image tuichubenju = new Image(this.getClass().getResourceAsStream("image/button/退出本局.png"),200,80,false,false);
	Image chongxinkaishi = new Image(this.getClass().getResourceAsStream("image/button/重新开始.png"),200,80,false,false);
	
	GridPane jifenban;
	Label fenshu, shijian, something;			//显示分数,时间
	
	ImageView fanhui = new ImageView(fanhuiyouxi);
	ImageView tuichu = new ImageView(tuichubenju);
	ImageView chongxin = new ImageView(chongxinkaishi);

	ImageView base;
	ImageView zheDang = new ImageView(zhedang);
	ImageView[] hiddenFood = new ImageView[8];		//隐藏的水果图像
	ImageView[][] food = new ImageView[8][10];
	ImageView[] checkedBox = new ImageView[2];

	Map[] hiddenMap = new Map[8];			//隐藏的块
	Map[][] map = new Map[8][10];
	ArrayList<Map> selected;
	ArrayList<Map> needMoved;
	Exchange exc = new Exchange();
	
	String[][] data = new String[10][8];		//储存是否可以显示
	
	boolean floor = false;			//判断画浅色还是深色底板
	
	int grades, time;				//记录成绩,时间
	int direction = 0;		//储存变换方向,0左,1右，2上，3下
	int xIndex,yIndex;			//用来保存当前点击的位置
	
	int eliminateCount = 0;			//计数多少连击
	
	int count = 1; 				//计数有几个连在一起了
	int selectedCount = 0;		//计数有几个被选中了
	boolean existLinked;		//用于检查是否有3个或以上连在一起的
	boolean isExchanging = false;		//用于判断是否有块在进行互换
	boolean eliminable = false;			//判断是否可以消除
	boolean isElimenating = false;		//判断是否在消除
	boolean isMoving = false;		//判断是否在移动
	boolean isPausing = false;		//判断是否暂停
	boolean isGameOver;		//判断游戏是否结束

	//图片
	Image fruit;
	Image box = new Image(this.getClass().getResourceAsStream("image/泡泡.png"), GRID_SPAN, GRID_SPAN, false, false);
	Image floor1 = new Image(this.getClass().getResourceAsStream("image/底板4.png"), GRID_SPAN, GRID_SPAN, false, false);
	Image floor2 = new Image(this.getClass().getResourceAsStream("image/底板5.png"), GRID_SPAN, GRID_SPAN, false, false);
	
	/*
	Image fruit01 = new Image(this.getClass().getResourceAsStream("image/桔子.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit02 = new Image(this.getClass().getResourceAsStream("image/葡萄.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit03 = new Image(this.getClass().getResourceAsStream("image/苹果1.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit04 = new Image(this.getClass().getResourceAsStream("image/香蕉.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit05 = new Image(this.getClass().getResourceAsStream("image/西瓜.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit06 = new Image(this.getClass().getResourceAsStream("image/草莓.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	*/
	//Image fruit01 = new Image(this.getClass().getResourceAsStream("image/022.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit01 = new Image(this.getClass().getResourceAsStream("image/橙.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit02 = new Image(this.getClass().getResourceAsStream("image/梨.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit03 = new Image(this.getClass().getResourceAsStream("image/弥.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit04 = new Image(this.getClass().getResourceAsStream("image/苹.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit05 = new Image(this.getClass().getResourceAsStream("image/西.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit06 = new Image(this.getClass().getResourceAsStream("image/黄.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	
	Image fruit11 = new Image(this.getClass().getResourceAsStream("image/橙1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit12 = new Image(this.getClass().getResourceAsStream("image/梨1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit13 = new Image(this.getClass().getResourceAsStream("image/弥1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit14 = new Image(this.getClass().getResourceAsStream("image/苹1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit15 = new Image(this.getClass().getResourceAsStream("image/西1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit16 = new Image(this.getClass().getResourceAsStream("image/黄1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);

	Image fruit21 = new Image(this.getClass().getResourceAsStream("image/橙2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit22 = new Image(this.getClass().getResourceAsStream("image/梨2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit23 = new Image(this.getClass().getResourceAsStream("image/弥2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit24 = new Image(this.getClass().getResourceAsStream("image/苹2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit25 = new Image(this.getClass().getResourceAsStream("image/西2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit26 = new Image(this.getClass().getResourceAsStream("image/黄2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);

	Image fruit31 = new Image(this.getClass().getResourceAsStream("image/橙3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit32 = new Image(this.getClass().getResourceAsStream("image/梨3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit33 = new Image(this.getClass().getResourceAsStream("image/弥3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit34 = new Image(this.getClass().getResourceAsStream("image/苹3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit35 = new Image(this.getClass().getResourceAsStream("image/西3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit36 = new Image(this.getClass().getResourceAsStream("image/黄3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit40 = new Image(this.getClass().getResourceAsStream("image/5连特效块.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);

	//声音
	/*
	Media clickSound = new Media(this.getClass().getResource("sound/点击.mp3").toString());
	//Media boomSound1 = new Media(this.getClass().getResource("sound/消除1.mp3").toString());
	Media moveSound = new Media(this.getClass().getResource("sound/掉落.mp3").toString());
	
	Media boomSound1 = new Media(this.getClass().getResource("sound/爆1.mp3").toString());
	Media boomSound2 = new Media(this.getClass().getResource("sound/爆2.mp3").toString());
	Media boomSound3 = new Media(this.getClass().getResource("sound/爆3.mp3").toString());
	Media boomSound4 = new Media(this.getClass().getResource("sound/爆4.mp3").toString());
	Media boomSound5 = new Media(this.getClass().getResource("sound/爆5.mp3").toString());
	Media boomSound6 = new Media(this.getClass().getResource("sound/爆6.mp3").toString());
	Media boomSound7 = new Media(this.getClass().getResource("sound/爆7.mp3").toString());
	Media boomSound8 = new Media(this.getClass().getResource("sound/爆8.mp3").toString());
	Media boomSound9 = new Media(this.getClass().getResource("sound/爆9.mp3").toString());
	*/
	
	//public Game(int selection){
	public Game(int selection, Player p){
		//dataBase = db;
		player = p;
		
		if(selection == 0){		//判断是否是限时模式
			isTimeMode = true;
		}
		else{
			isTimeMode = false;
		}
		//初始化地图数据
		String path = "map/" + selection +".txt";
		try{
			InputStream fileIS = this.getClass().getResourceAsStream(path);
			BufferedReader fr = new BufferedReader(new InputStreamReader(fileIS));
			for(int i = 0; i < 10; i++){
				data[i]= fr.readLine().split(" ");
			}
			
			for(int i = 0;i < 8;i++){
				for(int j = 0;j < 10;j++){
					map[i][j] = new Map(i, j);
					if(data[j][i].charAt(0) == '0'){
						map[i][j].isShow = false;
					}
				}
			}//外层循环结束
			
			fr.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
				
		selected = new ArrayList<Map>();
		
		//设置鼠标监听
		MouseDone md = new MouseDone();
		game.setOnMouseClicked(md);
		game.setOnMousePressed(md);
		game.setOnMouseReleased(md);

		clockThread.start();
		isPausing = true;
	}//构造函数结束
	
	public void go(){
		isGameOver = false;
		
		existLinked = true;
		
		grades = 0;			//成绩初始化
		time = 60;
		
		//检查clock是否alive
		if(!clockThread.isAlive()){
			clockThread = new Thread(clock);
			clockThread.start();
		}

		ImageView bg = new ImageView(background);
		game.getChildren().add(bg);
		
		ImageView p = new ImageView(pause);
		Button pause = new Button("", p);
		pause.setMaxSize(90, 90);
		pause.setLayoutX(800-pause.getMaxWidth());
		pause.setStyle("-fx-background-color:null");			//设置按钮透明
		pause.setOnAction(new PauseHandler());
		game.getChildren().add(pause);
		//设置地图
		SetMap setMap = new SetMap();
		setMap.Set();
		
		for(int i = 7;i >= 0;i--){
			floor = !floor;
			for(int j = 9;j >= 0;j--){
				//画底板
				if(floor && map[i][j].isShow){
					base = new ImageView(floor1);
					base.setX(MARGIN1 + GRID_SPAN * map[i][j].x);
					base.setY(MARGIN2 + GRID_SPAN * map[i][j].y);
					floor = !floor;
				}
				else if(!floor && map[i][j].isShow){
					base = new ImageView(floor2);
					base.setX(MARGIN1 + GRID_SPAN * map[i][j].x);
					base.setY(MARGIN2 + GRID_SPAN * map[i][j].y);
					floor = !floor;
				}
				else{
					floor = !floor;
					continue;
				}
				game.getChildren().add(base);
				
				
				//设置图片
				if(!setPicture(i, j)){
					continue;
				}
				
				//画出没有被选中，不需被移动的块
				if( (!map[i][j].isSelected && !map[i][j].willBeMoved) || !isExchanging){
					food[i][j] = new ImageView(fruit);
					food[i][j].setX(MARGIN1 + GRID_SPAN * map[i][j].x + 6);
					food[i][j].setY(MARGIN2 + GRID_SPAN * map[i][j].y + 6);
					food[i][j].setSmooth(true);
					game.getChildren().add(food[i][j]);
				}
					
			}//内层循环结束
		}//外层循环结束
		
		//初始化并加入计分板
		if(isTimeMode){		//是限时模式
			jifenban = new GridPane();
			shijian = new Label("时间："+time);
			shijian.setMinSize(200, 75);
			jifenban.add(shijian, 0, 0);
			
			something = new Label();
			something.setMinSize(200, 75);
			jifenban.add(something, 1, 0);
		}
		else{				//不是限时模式
			
		}
		fenshu = new Label(""+grades);
		fenshu.setAlignment(Pos.CENTER_RIGHT);
		fenshu.setMinSize(200, 75);
		jifenban.add(fenshu, 2, 0);
		jifenban.setMinSize(600, 75);
		jifenban.setLayoutX(MARGIN1);	jifenban.setLayoutY(MARGIN2-GRID_SPAN);
		jifenban.getStylesheets().add(this.getClass().getResource("game.css").toExternalForm());	//加载样式表
		//game.getChildren().add(jifenban);
		
		for(int i = 0;i < 8;i++){
			setHiddenFood(i);
		}
		
		//开始计时
		/*
		Clock clock = new Clock();
		Thread clockThread = new Thread(clock);
		clockThread.start();
		*/
		isPausing = false;
		
	}//结束go()方法
	
	/*通过用隐藏的食物，事先生产备用的食物，往下掉落时将隐藏的食物参与到掉落中
	 * 并且将备用的食物通过与背景相同的图片遮挡住
	 * 这是目前想到的能够产生这个动画的唯一方法，缺点是地图上方需要空出一行的位置
	 * 且只能在最上方使用
	 */
	//用来设置隐藏的食物
	void setHiddenFood(int i){
		
		//每次先移除遮挡，以实现之后添加了以后还能继续遮挡
		
		game.getChildren().remove(jifenban);
		game.getChildren().remove(zheDang);

		//设置隐藏的地图属性
		hiddenMap[i] = new Map(i , -1);
		hiddenMap[i].state = (int)(Math.random()*6 + 1);
		//设置隐藏食物的图像
		setPicture(i, -1);
		hiddenFood[i] = new ImageView(fruit);
		hiddenFood[i].setX(MARGIN1 + GRID_SPAN * i + 6);
		hiddenFood[i].setY(MARGIN2 - GRID_SPAN + 6);
		game.getChildren().add(hiddenFood[i]);

		//设置遮挡
		zheDang.setX(MARGIN1);	zheDang.setY(MARGIN2 - GRID_SPAN);
		game.getChildren().add(zheDang);

		game.getChildren().add(jifenban);
	}
	
	//返回监听
	
	//暂停监听
	class PauseHandler implements EventHandler<ActionEvent>{
		/*设置暂停界面,按钮和按钮监听
		 */
		public void handle(ActionEvent e){
			if(isMoving){			//当在移动时点击无效,防止出现bug
				return;
			}
			Sound.playaftclicked();
			
			isPausing = true;
			Label gray = new Label();
			gray.setStyle("-fx-background-color:gray");
			gray.setMinSize(GameLauncher.mainScene.getWidth(), GameLauncher.mainScene.getHeight());
			gray.setOpacity(0.7);
			game.getChildren().add(gray);
			GridPane pauseOption = new GridPane();
			pauseOption.setId("pauseOption");
			//pauseOption.setMinSize(400, 500);
			pauseOption.setMinSize(486, 532);

			
			Button continueGame = new Button("",fanhui);
			continueGame.setOnAction(new continueHandler());
			GameLauncher.texiao(continueGame);
			continueGame.setId("continueGame");
			pauseOption.add(continueGame, 0, 0);
			Button restartGame = new Button("",chongxin);
			restartGame.setOnAction(new restartHandler());
			GameLauncher.texiao(restartGame);
			restartGame.setId("restartGame");
			pauseOption.add(restartGame, 0, 1);
			Button back = new Button("",tuichu);
			GameLauncher.texiao(back);
			back.setOnAction(new backHandler());
			back.setId("back");
			pauseOption.add(back, 0, 2);
			
			pauseOption.setLayoutX(157);	pauseOption.setLayoutY(234);
			pauseOption.setAlignment(Pos.CENTER);
			pauseOption.setVgap(10);
			game.getStylesheets().add(Game.class.getResource("pauseOption.css").toExternalForm());		//加载样式表
			game.getChildren().add(pauseOption);
		}
	}//类PauseHandler结束
	
	class continueHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			game.getChildren().remove(game.getChildren().size()-1);
			game.getChildren().remove(game.getChildren().size()-1);
			isPausing = false;
		}
	}//类continueHandler结束
	
	class restartHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			game.getChildren().remove(game.getChildren().size()-1);
			game.getChildren().remove(game.getChildren().size()-1);
			isPausing = false;
			go();
		}
	}//类restartHandler结束
	
	class backHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			game = null;
			GameLauncher.back();
		}
	}//类backHandler结束
	
	class SetMap{
		void Set(){
			for(int i = 0;i < 8;i++){
				for(int j = 0;j < 10;j++){
					if(map[i][j].isShow){
						map[i][j].state = (int)(Math.random()*6) + 1;
					}
				}
			}
			//检查是否开始前就有连在一起的
			//横向
			while(existLinked){
				existLinked = false;		//假设没有连在一起的
				for(int i = 0;i < 8;i++){
					count = 1;
					for(int j = 0;j < 9;j++){
						if(map[i][j].state == map[i][j+1].state){
							count++;			//若下一个颜色相同则计数器加一
							if(count == 3){
								existLinked = true;
								Set();
							}
						}
						else{
							count = 1;			//若下一个颜色不同则计数器归零
						}
					}
				}//for结束
				//竖向
				for(int j = 0;j < 10;j++){
					count = 1;
					for(int i = 0;i < 7;i++){
						if(map[i][j].state == map[i+1][j].state){
							count++;			//若下一个颜色相同则计数器加一
							if(count == 3){
								existLinked = true;
								Set();
							}
						}
						else{
							count = 1;			//若下一个颜色不同则计数器归零
						}
					}
				}//for结束
				
				//如果没有发现相连的
				if(!existLinked){
					break;
				}
			}//while结束

		}//set()方法结束
		
	}//SetMap子类结束
	
	boolean setPicture(int x, int y){
		if(y == -1){			//隐藏食物的设置图片
			switch(hiddenMap[x].state){
			case 1:
				fruit = fruit01;
				break;
			case 2:
				fruit = fruit02;
				break;
			case 3:
				fruit = fruit03;
				break;
			case 4:
				fruit = fruit04;
				break;
			case 5:
				fruit = fruit05;
				break;
			case 6:
				fruit = fruit06;
				break;
			}
			return true;
		}
		
		switch(map[x][y].state){
		case 0:
			return false;
		case 1:
			fruit = fruit01;
			break;
		case 2:
			fruit = fruit02;
			break;
		case 3:
			fruit = fruit03;
			break;
		case 4:
			fruit = fruit04;
			break;
		case 5:
			fruit = fruit05;
			break;
		case 6:
			fruit = fruit06;
			break;
		
		case 11:
			fruit = fruit11;
			break;
		case 12:
			fruit = fruit12;
			break;
		case 13:
			fruit = fruit13;
			break;
		case 14:
			fruit = fruit14;
			break;
		case 15:
			fruit = fruit15;
			break;
		case 16:
			fruit = fruit16;
			break;
			
		case 21:
			fruit = fruit21;
			break;
		case 22:
			fruit = fruit22;
			break;
		case 23:
			fruit = fruit23;
			break;
		case 24:
			fruit = fruit24;
			break;
		case 25:
			fruit = fruit25;
			break;
		case 26:
			fruit = fruit26;
			break;

		case 31:
			fruit = fruit31;
			break;
		case 32:
			fruit = fruit32;
			break;
		case 33:
			fruit = fruit33;
			break;
		case 34:
			fruit = fruit34;
			break;
		case 35:
			fruit = fruit35;
			break;
		case 36:
			fruit = fruit36;
			break;

		}//switch结束
		if(map[x][y].state >= 40){
			fruit = fruit40;
		}
		return true;
	}

	public class MouseDone implements EventHandler<MouseEvent>{
		int index = 0;			//记录被选中的

		public void handle(MouseEvent e) {
			
			if(isExchanging || isMoving || isPausing || isGameOver){
				return;
			}
			//如果点在左和上边界外无效
			if(e.getX() < MARGIN1 || e.getY() < MARGIN2){
				return;
			}
			//转化为网格索引值
			xIndex = (int) ((e.getX() - MARGIN1)/GRID_SPAN);
			yIndex = (int) ((e.getY() - MARGIN2)/GRID_SPAN);
		    //在地图外不生效  
			if(xIndex<0||xIndex>7||yIndex<0||yIndex>9)  
		        return;
			//判断是否被选中过 或者为空
			if(map[xIndex][yIndex].isSelected || map[xIndex][yIndex].state == 0)
				return;
			//设置被选中
			map[xIndex][yIndex].isSelected = true;
			selected.add(map[xIndex][yIndex]);
			
			//播放音效
			
			if(e.getEventType() != MouseEvent.MOUSE_RELEASED){			//如果是拖动  鼠标松开的时候不触发声音
				//MediaPlayer clickSoundPlayer = new MediaPlayer(clickSound);
				
				/*
				clickSoundPlayer.setOnEndOfMedia(new Runnable(){
					public void run(){
						Platform.runLater(() -> {
							clickSoundPlayer.dispose();
						});
					}
				});
				*/
				//clickSoundPlayer.play();
				
				Sound.playClickSound();
			}
			
			//选中标识
			game.getChildren().remove(food[xIndex][yIndex]);
			checkedBox[index] = new ImageView(box);
			checkedBox[index].setX(MARGIN1 + GRID_SPAN * xIndex);
			checkedBox[index].setY(MARGIN2 + GRID_SPAN * yIndex);
			game.getChildren().add(checkedBox[index]);
			game.getChildren().add(food[xIndex][yIndex]);
			selectedCount++;
			if(index == 0){
				index = 1;			//改变到下一个被选中的
			}
			else{
				index = 0;
			}
			
			//是否已选中了两个
			if(selectedCount == 2){
				exc.exchange(index);			//移动
			}
			
		}//handle方法结束
		
	}//类mouseClicked结束
	
	class Exchange{				//交换两个相邻块
		void exchange(int index){
			if( ( (selected.get(0)).x == (selected.get(1)).x && (((selected.get(0)).y == (selected.get(1)).y-1)|| (selected.get(0)).y == ((Map)selected.get(1)).y+1) )
					|| ( ((Map)selected.get(0)).y == (selected.get(1)).y && (((selected.get(0)).x == (selected.get(1)).x-1)|| ((Map)selected.get(0)).x == (selected.get(1)).x+1) )
					|| ( (selected.get(0).x == selected.get(1).x+1 || selected.get(0).x == selected.get(1).x-1) && (selected.get(0).y == selected.get(1).y+1 || selected.get(0).y == selected.get(1).y-1) ) ){
				isExchanging = true;			//如果所选是相邻两个就交换
			}
			else{			//如果不相邻，则取消上一次选择的
				(selected.get(0)).isSelected = false;
				selected.remove(0);
				game.getChildren().remove(checkedBox[index]);
				selectedCount--;
				return;
			}
			//画交换动画
			if(isExchanging){
				//判断交换方向
				if((selected.get(0)).y == (selected.get(1)).y){
					if((selected.get(0)).x < (selected.get(1)).x){		//0向右
						direction = 0;
					}
					else{
						direction = 1;								//0向左
					}
				}
				if((selected.get(0)).x == (selected.get(1)).x){
					if((selected.get(0)).y < (selected.get(1)).y){		//0向下
						direction = 2;
					}
					else{
						direction = 3;					//0向上
					}
				}
				if(selected.get(0).x == selected.get(1).x+1){			//0在右
					if(selected.get(0).y == selected.get(1).y +1){		//0向左上
						direction = 4;
					}
					else if(selected.get(0).y == selected.get(1).y - 1){
						direction = 5;				//0向左下
					}
				}
				if(selected.get(0).x == selected.get(1).x-1){			//0在左
					if(selected.get(0).y == selected.get(1).y +1){		//0向右上
						direction = 6;
					}
					else if(selected.get(0).y == selected.get(1).y - 1){
						direction = 7;				//0向右下
					}
				}

				
				//移除选中标记
				game.getChildren().removeAll(checkedBox[0], checkedBox[1]);
								
				final Timeline timeLine = new Timeline();
				final KeyValue kv0,kv1;
				final KeyFrame kf0,kf1;
				
				switch(direction){
				case 0:{
					kv0 = new KeyValue(food[selected.get(0).x][selected.get(0).y].xProperty(), MARGIN1 + GRID_SPAN * selected.get(0).x + GRID_SPAN + 6);
					kv1 = new KeyValue(food[selected.get(1).x][selected.get(1).y].xProperty(), MARGIN1 + GRID_SPAN * selected.get(1).x - GRID_SPAN + 6);
					kf0 = new KeyFrame(Duration.millis(300), kv0);
					kf1 = new KeyFrame(Duration.millis(300), kv1);
					timeLine.getKeyFrames().addAll(kf0, kf1);

					break;
				}
				case 1:{
					kv0 = new KeyValue(food[selected.get(0).x][selected.get(0).y].xProperty(), MARGIN1 + GRID_SPAN * selected.get(0).x - GRID_SPAN + 6);
					kv1 = new KeyValue(food[selected.get(1).x][selected.get(1).y].xProperty(), MARGIN1 + GRID_SPAN * selected.get(1).x + GRID_SPAN + 6);
					kf0 = new KeyFrame(Duration.millis(300), kv0);
					kf1 = new KeyFrame(Duration.millis(300), kv1);
					timeLine.getKeyFrames().addAll(kf0, kf1);

					break;
				}
				case 2:{
					kv0 = new KeyValue(food[selected.get(0).x][selected.get(0).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(0).y + GRID_SPAN + 6);
					kv1 = new KeyValue(food[selected.get(1).x][selected.get(1).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(1).y - GRID_SPAN + 6);
					kf0 = new KeyFrame(Duration.millis(300), kv0);
					kf1 = new KeyFrame(Duration.millis(300), kv1);
					timeLine.getKeyFrames().addAll(kf0, kf1);					//timeLine1.getKeyFrames().add(kf1);

					break;
				}
				case 3:{
					kv0 = new KeyValue(food[selected.get(0).x][selected.get(0).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(0).y - GRID_SPAN + 6);
					kv1 = new KeyValue(food[selected.get(1).x][selected.get(1).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(1).y + GRID_SPAN + 6);
					kf0 = new KeyFrame(Duration.millis(300), kv0);
					kf1 = new KeyFrame(Duration.millis(300), kv1);
					timeLine.getKeyFrames().addAll(kf0, kf1);

					break;
				}
				case 4:{
					kv0 = new KeyValue(food[selected.get(0).x][selected.get(0).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(0).y - GRID_SPAN + 6);
					KeyValue kv00 = new KeyValue(food[selected.get(0).x][selected.get(0).y].xProperty(), MARGIN1 + GRID_SPAN * selected.get(0).x - GRID_SPAN + 6);
					kv1 = new KeyValue(food[selected.get(1).x][selected.get(1).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(1).y + GRID_SPAN + 6);
					KeyValue kv11 = new KeyValue(food[selected.get(1).x][selected.get(1).y].xProperty(),MARGIN1 + GRID_SPAN * selected.get(1).x + GRID_SPAN + 6);
					kf0 = new KeyFrame(Duration.millis(300), kv0);
					KeyFrame kf00 = new KeyFrame(Duration.millis(300), kv00);
					kf1 = new KeyFrame(Duration.millis(300), kv1);
					KeyFrame kf11 = new KeyFrame(Duration.millis(300), kv11);
					timeLine.getKeyFrames().addAll(kf0, kf1, kf00,kf11);

					break;
				}
				case 5:{
					kv0 = new KeyValue(food[selected.get(0).x][selected.get(0).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(0).y + GRID_SPAN + 6);
					KeyValue kv00 = new KeyValue(food[selected.get(0).x][selected.get(0).y].xProperty(), MARGIN1 + GRID_SPAN * selected.get(0).x - GRID_SPAN + 6);
					kv1 = new KeyValue(food[selected.get(1).x][selected.get(1).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(1).y - GRID_SPAN + 6);
					KeyValue kv11 = new KeyValue(food[selected.get(1).x][selected.get(1).y].xProperty(),MARGIN1 + GRID_SPAN * selected.get(1).x + GRID_SPAN + 6);
					kf0 = new KeyFrame(Duration.millis(300), kv0);
					KeyFrame kf00 = new KeyFrame(Duration.millis(300), kv00);
					kf1 = new KeyFrame(Duration.millis(300), kv1);
					KeyFrame kf11 = new KeyFrame(Duration.millis(300), kv11);
					timeLine.getKeyFrames().addAll(kf0, kf1, kf00,kf11);

					break;
				}
				case 6:{
					kv0 = new KeyValue(food[selected.get(0).x][selected.get(0).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(0).y - GRID_SPAN + 6);
					KeyValue kv00 = new KeyValue(food[selected.get(0).x][selected.get(0).y].xProperty(), MARGIN1 + GRID_SPAN * selected.get(0).x + GRID_SPAN + 6);
					kv1 = new KeyValue(food[selected.get(1).x][selected.get(1).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(1).y + GRID_SPAN + 6);
					KeyValue kv11 = new KeyValue(food[selected.get(1).x][selected.get(1).y].xProperty(),MARGIN1 + GRID_SPAN * selected.get(1).x - GRID_SPAN + 6);
					kf0 = new KeyFrame(Duration.millis(300), kv0);
					KeyFrame kf00 = new KeyFrame(Duration.millis(300), kv00);
					kf1 = new KeyFrame(Duration.millis(300), kv1);
					KeyFrame kf11 = new KeyFrame(Duration.millis(300), kv11);
					timeLine.getKeyFrames().addAll(kf0, kf1, kf00,kf11);

					break;
				}
				case 7:{
					kv0 = new KeyValue(food[selected.get(0).x][selected.get(0).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(0).y + GRID_SPAN + 6);
					KeyValue kv00 = new KeyValue(food[selected.get(0).x][selected.get(0).y].xProperty(), MARGIN1 + GRID_SPAN * selected.get(0).x + GRID_SPAN + 6);
					kv1 = new KeyValue(food[selected.get(1).x][selected.get(1).y].yProperty(), MARGIN2 + GRID_SPAN * selected.get(1).y - GRID_SPAN + 6);
					KeyValue kv11 = new KeyValue(food[selected.get(1).x][selected.get(1).y].xProperty(),MARGIN1 + GRID_SPAN * selected.get(1).x - GRID_SPAN + 6);
					kf0 = new KeyFrame(Duration.millis(300), kv0);
					KeyFrame kf00 = new KeyFrame(Duration.millis(300), kv00);
					kf1 = new KeyFrame(Duration.millis(300), kv1);
					KeyFrame kf11 = new KeyFrame(Duration.millis(300), kv11);
					timeLine.getKeyFrames().addAll(kf0, kf1, kf00,kf11);

					break;
				}


				}//结束switch
				
				//交换两个块的状态
				int tempVal;
				tempVal = (selected.get(0)).state;
				(selected.get(0)).state = ((Map)selected.get(1)).state;
				(selected.get(1)).state = tempVal;
				
				/*
				int eliminateState = basePlay.check(data, selected.get(0).x , selected.get(0).y , selected.get(1).x , selected.get(1).y);
				if(eliminateState != 0){
					eliminable = true;
				}
				*/
				
				/*
				ArrayList<Map> mark = basePlay.check(map);
				if(mark.size() != 0){
					eliminable = true;
				}
				*/
				
				ArrayList<Map> stars = new ArrayList<Map>();
				ArrayList<Integer> starsState = new ArrayList<Integer>();
				ArrayList<Map> mark = basePlay.checkExchange(selected.get(0).x , selected.get(0).y , selected.get(1).x , selected.get(1).y, map, stars, starsState);
				if(mark.size() != 0){
					eliminable = true;
				}
				
				//设置交换动画结束动作
				timeLine.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent t){
						if(eliminable){			//消除
							
							//int[][] data1 = basePlay.remove(eliminateState, data,  selected.get(0).x , selected.get(0).y , selected.get(1).x , selected.get(1).y);
							
							
							//map = basePlay.remove(mark, map);
							basePlay.isStarRemove(mark, map, stars, starsState);			//检查是否有特效块消除
							basePlay.remove(mark, map);
							
							ImageView tempView;
							tempView = food[selected.get(1).x][selected.get(1).y];
							food[selected.get(1).x][selected.get(1).y] = food[selected.get(0).x][selected.get(0).y];
							food[selected.get(0).x][selected.get(0).y] = tempView;
							
							eliminate(stars, starsState);
							//进行移动
							//move();
							

						}//结束if
						
						//归置状态
						isExchanging = false;		//设置为没有交换状态
						selectedCount = 0;		//已经移动了，计数器清零
						((Map)selected.get(0)).isSelected = false;
						((Map)selected.get(1)).isSelected = false;
						selected = new ArrayList<Map>();

					}
				});
				
				//若可以消除，进行消除
				if(eliminable){
					timeLine.play();
					
				}
				else{		//不可以消除，返回状态
					(selected.get(1)).state = (selected.get(0)).state;
					(selected.get(0)).state = tempVal;
					timeLine.setAutoReverse(true);
					timeLine.setCycleCount(2);
					timeLine.play();
				}
			}//结束if
						
		}//exchange方法结束
	}//类Exchange结束
	
	void eliminate(ArrayList<Map> stars, ArrayList<Integer> starsState){
		int time = 300;
		Timeline timeLine = new Timeline();
		KeyFrame kf = new KeyFrame(Duration.millis(time));
		timeLine.getKeyFrames().add(kf);
		timeLine.setOnFinished(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				//继续下一次移动
				eliminable = false;
				move();
				if(isGameOver){				//游戏结束时额外附加检查是否存在特效块
					checkStar();		//递归检查是否还有
				}

			}
		});
		timeLine.play();
		
		for(int i = 0;i < 8;i++){
			for(int j = 0;j < 10;j++){
				//if(newMap[i][j].state == 0){
				//if(map[i][j].state != 0 && newMap[i][j].state == 0){
				if(map[i][j].state == 0){
				//if(data1[i][j] == 0){
					//game.getChildren().remove(food[i][j]);
					
					//显示分数
					
					//消除动画
					//根据不同的颜色显示不同颜色消除动画,目前没实现
					Image boom = new Image(this.getClass().getResourceAsStream("image/爆炸特效3.gif"));
					//Image boom = new Image(this.getClass().getResourceAsStream("image/爆炸特效1.gif"));
					ImageView boomView = new ImageView(boom);
					boomView.setX(MARGIN1 + GRID_SPAN * map[i][j].x);
					boomView.setY(MARGIN2 + GRID_SPAN * map[i][j].y);
					game.getChildren().add(boomView);

					//缩放和渐变
					ScaleTransition st = ScaleTransitionBuilder.create()
							.node(food[i][j])
							.duration(Duration.millis(time))
							.toX(0.1)
							.toY(0.1)
							.build();
					st.setOnFinished(new EventHandler<ActionEvent>(){
						public void handle(ActionEvent e){
							game.getChildren().remove(st.getNode());
							game.getChildren().remove(boomView);
						}
					});
					FadeTransition ft = new FadeTransition(Duration.millis(time), food[i][j]);
					ft.setToValue(0.1);
					
					st.play();
					ft.play();
															
					//加上所得分数
					if(stars.contains(map[i][j])){		//特效块分数
						if(starsState.get(stars.indexOf(map[i][j])) > 40){		//5连特效块
							grades = grades + 107*6;
							fenshu.setText(""+grades);
						}
						else{		//其他特效块
							grades = grades + 107*3;
							fenshu.setText(""+grades);
						}
					}
					else{
						grades = grades + 107;
						fenshu.setText(""+grades);
					}
				}
				else if(map[i][j].state/10 != 0){
				//else if(map[i][j].state/10 == 1 || map[i][j].state/10 == 2 || map[i][j].state/10 == 3){
					setPicture(i, j);
					game.getChildren().remove(food[i][j]);
					food[i][j] = new ImageView(fruit);
					food[i][j].setX(MARGIN1 + GRID_SPAN * map[i][j].x + 6);
					food[i][j].setY(MARGIN2 + GRID_SPAN * map[i][j].y + 6);
					food[i][j].setSmooth(true);
					game.getChildren().add(food[i][j]);
					
					//加上所得分数
					grades = grades + 107;
					fenshu.setText(""+grades);

				}
			}
		}//for结束
		
		eliminateCount++;		//连击次数加1
		
		/*
		Media boomSound = null;
		
		switch(eliminateCount){
		case 1:{
			boomSound = boomSound1;
			break;
		}
		case 2:{
			boomSound = boomSound2;
			break;
		}
		case 3:{
			boomSound = boomSound3;
			break;
		}
		case 4:{
			boomSound = boomSound4;
			break;
		}
		case 5:{
			boomSound = boomSound5;
			break;
		}
		case 6:{
			boomSound = boomSound6;
			break;
		}
		case 7:{
			boomSound = boomSound7;
			break;
		}
		case 8:{
			boomSound = boomSound8;
			break;
		}

		}//switch结束
		if(eliminateCount >= 9){
			boomSound = boomSound9;
		}
		
		
		MediaPlayer boomSoundPlayer = new MediaPlayer(boomSound);
		/*
		boomSoundPlayer.setOnEndOfMedia(() -> {
			Platform.runLater(() -> {
				boomSoundPlayer.dispose();
			});
		});
		*/
		//boomSoundPlayer.play();
				
		/*
		//爆炸音效	以后可能根据连击次数有不同音效	目前一种
		MediaPlayer boomSound1Player = new MediaPlayer(boomSound1);
		
		boomSound1Player.setOnEndOfMedia(new Runnable(){
			public void run(){
				Platform.runLater(() -> {
					boomSound1Player.dispose();
				});
			}
		});
		
		boomSound1Player.play();
		*/
		
		if(stars.isEmpty()){
			Sound.playBoomSound(eliminateCount);
		}
		else{
			int maxStar = 0;
			for(int temp : starsState){
				if(temp/10 > maxStar){
					maxStar = temp/10;
				}
			}
			//for(Map temp : stars){				//找出最大的特效块状态
				//if(temp.state/10 > maxStar){
					//maxStar = temp.state/10;
				//}
			//}

			if(maxStar == 1 || maxStar == 2){
				Sound.playLineBoomSound();
			}
			if(maxStar == 3){
				Sound.playNineBoomSound();
			}
			if(maxStar == 4){
				Sound.playFiveBoomSound();
			}
		}

		try {
			this.finalize();
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//System.gc();
	}//方法eliminate结束
	
	boolean[][] hadMoved = new boolean[8][10];		//记录哪些格子被移动过
	void move(){
		isMoving = true;		//初始化为在移动
		needMoved = new ArrayList<Map>();
		for(int row = 9;row >= 0;row--){		//从最下一排开始检查是否有需要移动的块
			for(int col = 0;col < 8;col++){		//逐列
				if(needMoved.contains(map[col][row])){		//如果包含了说明该块已经被添加过要移动
					continue;
				}
				if(map[col][row].state == 0){		//有空块说明有需要被移动的
					if(map[col][row].y == 0){			//如果是第一排，直接rebirth当前消除后的位置
						/*
						rebirth(col, row);
						move();
						*/
						needMoved.add(hiddenMap[col]);
					}
					else if(row-1 >= 0 && map[col][row-1].state != 0){
						for(int k = row-1;k >= 0;k--){		//将该列需要移动的块之上的所有块装进needMoved
							map[col][k].willBeMoved = true;
							needMoved.add(map[col][k]);
							if(k == 0){
								needMoved.add(hiddenMap[col]);
							}
						}
					}//if-else结束
				}//if结束
				
			}//for结束
		}//for结束
		if(needMoved.size() == 0 || needMoved.isEmpty()){			//如果没有需要移动的，返回
			//检查是否移动后的地图是否有可以消除的
			ArrayList<Map> mark = basePlay.checkAfterMove(hadMoved, map);
			hadMoved = new boolean[8][10];		//状态归零

			if(!mark.isEmpty()){		//如果mark不为空	则消除
				eliminable = true;
				//map = basePlay.remove(mark, map);
				ArrayList<Map> stars = new ArrayList<Map>();
				ArrayList<Integer> starsState = basePlay.isStarRemove(mark, map, stars, new ArrayList<Integer>());			//检查是否有特效块消除
				basePlay.remove(mark, map);
				eliminate(stars, starsState);
				
				//move();
			}
			else{
				isMoving = false;
				
				Sound.playPraiseSound(eliminateCount);			//播放称赞音效

				//清0连击计数器
				eliminateCount = 0;

			}

			//播放移动音效
			//MediaPlayer moveSoundPlayer = new MediaPlayer(moveSound);
			/*
			moveSoundPlayer.setOnEndOfMedia(new Runnable(){
				public void run(){
					Platform.runLater(() -> {
						moveSoundPlayer.dispose();
					});	
				}
			});
			*/
			//moveSoundPlayer.play();
			Sound.playMoveSound();
			
			try {
				this.finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.gc();
			
			return;
		}//判断是否可以返回结束
		
		Timeline timeLine = new Timeline();
		KeyValue[] kv = new KeyValue[needMoved.size()];
		KeyFrame[] kf = new KeyFrame[needMoved.size()];
		for(int i = 0 ; i < needMoved.size();i++){
			if(needMoved.get(i).y == -1){
				kv[i] = new KeyValue(hiddenFood[needMoved.get(i).x].yProperty(), MARGIN2 + GRID_SPAN * needMoved.get(i).y + GRID_SPAN + 6);
			}
			else{
				kv[i] = new KeyValue(food[needMoved.get(i).x][needMoved.get(i).y].yProperty(), MARGIN2 + GRID_SPAN * needMoved.get(i).y + GRID_SPAN + 6);
			}
			kf[i] = new KeyFrame(Duration.millis(MOVETIME), kv[i]);
			timeLine.getKeyFrames().add(kf[i]);
		}
		
		timeLine.setOnFinished(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent t){
				//移动一格后将数据更新
				for(int i = 0;i < needMoved.size();i++){
					if(needMoved.get(i).y == -1){
						food[needMoved.get(i).x][needMoved.get(i).y+1] = hiddenFood[needMoved.get(i).x];
						hiddenFood[needMoved.get(i).x] = null;
					}
					else{
						food[needMoved.get(i).x][needMoved.get(i).y+1] = food[needMoved.get(i).x][needMoved.get(i).y];
					}
					needMoved.get(i).willBeMoved = false;
					needMoved.get(i).y = needMoved.get(i).y + 1;
					map[(needMoved.get(i)).x][(needMoved.get(i)).y] = needMoved.get(i);		//将该列往下移动一格
					
					if(needMoved.get(i).y == 0){
						setHiddenFood(needMoved.get(i).x);
					}
					hadMoved[needMoved.get(i).x][needMoved.get(i).y] = true;
				}//for结束
				
				
				move();		//递归
				
			}//handle结束
		});
		
		timeLine.play();					
	}//方法move结束
	
	class Clock implements Runnable{
		public void run(){
			while(true){
				//没有暂停或者游戏没结束时
				try{
					Thread.sleep(1000);
					if(isPausing || isGameOver){
						continue;
					}
					if(time != 0){
						time--;
						Platform.runLater(() -> {
							shijian.setText("时间："+time);
						});
					}
					if(time == 0){
						//检查并消除特效块
						

						if(isExchanging || isMoving || isElimenating || eliminable){
							continue;
						}
						
						/*
						Platform.runLater(() -> {
							isGameOver = true;

							eliminable = true;
							checkStar();

						});
						*/
						
						
						isGameOver = true;
						gameOver();
						return;
						
					}
					//shijian.setText("时间："+time);
				}catch(Exception ex){
					ex.printStackTrace();
				}

			}
		}//go结束
	}//类Clock结束
	
	
	void checkStar(){
		//Platform.runLater(() -> {
			ArrayList<Map> mark = new ArrayList<Map>();
			for(int i = 9;i >= 0;i--){
				for(int j = 0;j < 8;j++){
					if(map[j][i].state > 10){
						mark.add(map[j][i]);
					}
				}
			}
			if(!mark.isEmpty()){
				ArrayList<Map> stars = new ArrayList<Map>();
				ArrayList<Integer> starsState = basePlay.isStarRemove(mark, map, stars, new ArrayList<Integer>());			//检查是否有特效块消除
				basePlay.remove(mark, map);
				eliminable = true;
				eliminate(stars, starsState);
				
			}
		//});
	}//方法checkStar结束
	
	
	//当游戏结束时展示结束界面
	void gameOver(){
		Platform.runLater(() -> {
			//记录所得分数
			//dataBase.setGrades(grades);
			player.setGrade(grades);
			DataBase.storePlayer(player);
			//System.out.println(player.getTopGrades()[0]);
			
			Label gray = new Label();
			gray.setStyle("-fx-background-color:gray");
			gray.setMinSize(GameLauncher.mainScene.getWidth(), GameLauncher.mainScene.getHeight());
			gray.setOpacity(0.7);
			game.getChildren().add(gray);

			GridPane overPane = new GridPane();
			overPane.setId("overPane");
			overPane.setMinSize(490, 500);
//			overPane.setGridLinesVisible(true);
			
			Label jieshu = new Label("",new ImageView(new Image(this.getClass().getResourceAsStream("image/words/游戏结束.png"),200,80,false,false)));
			jieshu.setId("jieshu");
			jieshu.setAlignment(Pos.CENTER);
			GridPane.setHalignment(jieshu, HPos.CENTER);
			overPane.add(jieshu, 0, 0,2,1);
			
			Label finalGrades = new Label(""+grades,new ImageView(new Image(this.getClass().getResourceAsStream("image/words/最终分数.png"),200,80,false,false)));
			finalGrades.setId("finalGrades");
			finalGrades.setAlignment(Pos.CENTER);
			GridPane.setHalignment(finalGrades, HPos.CENTER);
			overPane.add(finalGrades, 0, 1,2,1);

			
			Button restartGame = new Button("",chongxin);
			//restartGame.setMinWidth(200);
			GameLauncher.texiao(restartGame);
			GridPane.setHalignment(restartGame, HPos.CENTER);
			restartGame.setOnAction(new restartHandler());
			restartGame.setId("restartGame");
			overPane.add(restartGame, 0, 2);
			Button back = new Button("",tuichu);
			//back.setMinWidth(200);
			GameLauncher.texiao(back);
			GridPane.setHalignment(back, HPos.CENTER);
			back.setOnAction(new backHandler());
			back.setId("back");
			overPane.add(back, 1, 2);

			overPane.setLayoutX(160);	overPane.setLayoutY(250);
			overPane.setAlignment(Pos.CENTER);
			overPane.setVgap(5);
			overPane.getStylesheets().add(this.getClass().getResource("gameOver.css").toExternalForm());
			
			game.getChildren().add(overPane);
		});
	}
	
}//类Game结束
