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
	public static final int MARGIN1=100;     //�߾�  
	public static final int MARGIN2=200;
	public static final int GRID_SPAN=75;   //������  

	boolean isTimeMode;
	
	static final int MOVETIME = 150;

	//��ʱ
	Clock clock = new Clock();
	Thread clockThread = new Thread(clock);
	
	BasePlay basePlay = new BasePlay();
	Player player;
	//DataBase dataBase;
	
	//static Group game = new Group();
	Group game = new Group();
	//Image background = new Image(this.getClass().getResourceAsStream("image/����.jpg"));
	Image background = new Image(this.getClass().getResourceAsStream("image/�ֱ���.png"), GameLauncher.mainScene.getWidth(), GameLauncher.mainScene.getHeight(),false,false);
	//Image background = new Image(this.getClass().getResourceAsStream("image/����1.png"), GameLauncher.mainScene.getWidth(),GameLauncher.mainScene.getHeight(),false,false);
	Image pause = new Image(this.getClass().getResourceAsStream("image/greyPause.png"), 80, 80, false, false);
	//Image pause = new Image(this.getClass().getResourceAsStream("image/button/��ͣ��ťfinal.png"), 80, 80, false, false);
	//Image pause = new Image(this.getClass().getResourceAsStream("image/��ͣ.png"), 80, 80, false, false);
	Image zhedang = new Image(this.getClass().getResourceAsStream("image/�ڵ�.jpg"));			//�ڵ����ص�ˮ�����Ӷ�ʵ�ֵ�����Ч
	Image fanhuiyouxi = new Image(this.getClass().getResourceAsStream("image/button/������Ϸ.png"),200,80,false,false);
	Image tuichubenju = new Image(this.getClass().getResourceAsStream("image/button/�˳�����.png"),200,80,false,false);
	Image chongxinkaishi = new Image(this.getClass().getResourceAsStream("image/button/���¿�ʼ.png"),200,80,false,false);
	
	GridPane jifenban;
	Label fenshu, shijian, something;			//��ʾ����,ʱ��
	
	ImageView fanhui = new ImageView(fanhuiyouxi);
	ImageView tuichu = new ImageView(tuichubenju);
	ImageView chongxin = new ImageView(chongxinkaishi);

	ImageView base;
	ImageView zheDang = new ImageView(zhedang);
	ImageView[] hiddenFood = new ImageView[8];		//���ص�ˮ��ͼ��
	ImageView[][] food = new ImageView[8][10];
	ImageView[] checkedBox = new ImageView[2];

	Map[] hiddenMap = new Map[8];			//���صĿ�
	Map[][] map = new Map[8][10];
	ArrayList<Map> selected;
	ArrayList<Map> needMoved;
	Exchange exc = new Exchange();
	
	String[][] data = new String[10][8];		//�����Ƿ������ʾ
	
	boolean floor = false;			//�жϻ�ǳɫ������ɫ�װ�
	
	int grades, time;				//��¼�ɼ�,ʱ��
	int direction = 0;		//����任����,0��,1�ң�2�ϣ�3��
	int xIndex,yIndex;			//�������浱ǰ�����λ��
	
	int eliminateCount = 0;			//������������
	
	int count = 1; 				//�����м�������һ����
	int selectedCount = 0;		//�����м�����ѡ����
	boolean existLinked;		//���ڼ���Ƿ���3������������һ���
	boolean isExchanging = false;		//�����ж��Ƿ��п��ڽ��л���
	boolean eliminable = false;			//�ж��Ƿ��������
	boolean isElimenating = false;		//�ж��Ƿ�������
	boolean isMoving = false;		//�ж��Ƿ����ƶ�
	boolean isPausing = false;		//�ж��Ƿ���ͣ
	boolean isGameOver;		//�ж���Ϸ�Ƿ����

	//ͼƬ
	Image fruit;
	Image box = new Image(this.getClass().getResourceAsStream("image/����.png"), GRID_SPAN, GRID_SPAN, false, false);
	Image floor1 = new Image(this.getClass().getResourceAsStream("image/�װ�4.png"), GRID_SPAN, GRID_SPAN, false, false);
	Image floor2 = new Image(this.getClass().getResourceAsStream("image/�װ�5.png"), GRID_SPAN, GRID_SPAN, false, false);
	
	/*
	Image fruit01 = new Image(this.getClass().getResourceAsStream("image/����.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit02 = new Image(this.getClass().getResourceAsStream("image/����.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit03 = new Image(this.getClass().getResourceAsStream("image/ƻ��1.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit04 = new Image(this.getClass().getResourceAsStream("image/�㽶.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit05 = new Image(this.getClass().getResourceAsStream("image/����.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit06 = new Image(this.getClass().getResourceAsStream("image/��ݮ.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	*/
	//Image fruit01 = new Image(this.getClass().getResourceAsStream("image/022.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit01 = new Image(this.getClass().getResourceAsStream("image/��.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit02 = new Image(this.getClass().getResourceAsStream("image/��.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit03 = new Image(this.getClass().getResourceAsStream("image/��.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit04 = new Image(this.getClass().getResourceAsStream("image/ƻ.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit05 = new Image(this.getClass().getResourceAsStream("image/��.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit06 = new Image(this.getClass().getResourceAsStream("image/��.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	
	Image fruit11 = new Image(this.getClass().getResourceAsStream("image/��1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit12 = new Image(this.getClass().getResourceAsStream("image/��1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit13 = new Image(this.getClass().getResourceAsStream("image/��1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit14 = new Image(this.getClass().getResourceAsStream("image/ƻ1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit15 = new Image(this.getClass().getResourceAsStream("image/��1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit16 = new Image(this.getClass().getResourceAsStream("image/��1.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);

	Image fruit21 = new Image(this.getClass().getResourceAsStream("image/��2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit22 = new Image(this.getClass().getResourceAsStream("image/��2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit23 = new Image(this.getClass().getResourceAsStream("image/��2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit24 = new Image(this.getClass().getResourceAsStream("image/ƻ2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit25 = new Image(this.getClass().getResourceAsStream("image/��2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit26 = new Image(this.getClass().getResourceAsStream("image/��2.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);

	Image fruit31 = new Image(this.getClass().getResourceAsStream("image/��3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit32 = new Image(this.getClass().getResourceAsStream("image/��3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit33 = new Image(this.getClass().getResourceAsStream("image/��3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit34 = new Image(this.getClass().getResourceAsStream("image/ƻ3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit35 = new Image(this.getClass().getResourceAsStream("image/��3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit36 = new Image(this.getClass().getResourceAsStream("image/��3.gif"), GRID_SPAN -12, GRID_SPAN - 12,false,false);
	Image fruit40 = new Image(this.getClass().getResourceAsStream("image/5����Ч��.png"), GRID_SPAN -12, GRID_SPAN - 12,false,false);

	//����
	/*
	Media clickSound = new Media(this.getClass().getResource("sound/���.mp3").toString());
	//Media boomSound1 = new Media(this.getClass().getResource("sound/����1.mp3").toString());
	Media moveSound = new Media(this.getClass().getResource("sound/����.mp3").toString());
	
	Media boomSound1 = new Media(this.getClass().getResource("sound/��1.mp3").toString());
	Media boomSound2 = new Media(this.getClass().getResource("sound/��2.mp3").toString());
	Media boomSound3 = new Media(this.getClass().getResource("sound/��3.mp3").toString());
	Media boomSound4 = new Media(this.getClass().getResource("sound/��4.mp3").toString());
	Media boomSound5 = new Media(this.getClass().getResource("sound/��5.mp3").toString());
	Media boomSound6 = new Media(this.getClass().getResource("sound/��6.mp3").toString());
	Media boomSound7 = new Media(this.getClass().getResource("sound/��7.mp3").toString());
	Media boomSound8 = new Media(this.getClass().getResource("sound/��8.mp3").toString());
	Media boomSound9 = new Media(this.getClass().getResource("sound/��9.mp3").toString());
	*/
	
	//public Game(int selection){
	public Game(int selection, Player p){
		//dataBase = db;
		player = p;
		
		if(selection == 0){		//�ж��Ƿ�����ʱģʽ
			isTimeMode = true;
		}
		else{
			isTimeMode = false;
		}
		//��ʼ����ͼ����
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
			}//���ѭ������
			
			fr.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
				
		selected = new ArrayList<Map>();
		
		//����������
		MouseDone md = new MouseDone();
		game.setOnMouseClicked(md);
		game.setOnMousePressed(md);
		game.setOnMouseReleased(md);

		clockThread.start();
		isPausing = true;
	}//���캯������
	
	public void go(){
		isGameOver = false;
		
		existLinked = true;
		
		grades = 0;			//�ɼ���ʼ��
		time = 60;
		
		//���clock�Ƿ�alive
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
		pause.setStyle("-fx-background-color:null");			//���ð�ť͸��
		pause.setOnAction(new PauseHandler());
		game.getChildren().add(pause);
		//���õ�ͼ
		SetMap setMap = new SetMap();
		setMap.Set();
		
		for(int i = 7;i >= 0;i--){
			floor = !floor;
			for(int j = 9;j >= 0;j--){
				//���װ�
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
				
				
				//����ͼƬ
				if(!setPicture(i, j)){
					continue;
				}
				
				//����û�б�ѡ�У����豻�ƶ��Ŀ�
				if( (!map[i][j].isSelected && !map[i][j].willBeMoved) || !isExchanging){
					food[i][j] = new ImageView(fruit);
					food[i][j].setX(MARGIN1 + GRID_SPAN * map[i][j].x + 6);
					food[i][j].setY(MARGIN2 + GRID_SPAN * map[i][j].y + 6);
					food[i][j].setSmooth(true);
					game.getChildren().add(food[i][j]);
				}
					
			}//�ڲ�ѭ������
		}//���ѭ������
		
		//��ʼ��������Ʒְ�
		if(isTimeMode){		//����ʱģʽ
			jifenban = new GridPane();
			shijian = new Label("ʱ�䣺"+time);
			shijian.setMinSize(200, 75);
			jifenban.add(shijian, 0, 0);
			
			something = new Label();
			something.setMinSize(200, 75);
			jifenban.add(something, 1, 0);
		}
		else{				//������ʱģʽ
			
		}
		fenshu = new Label(""+grades);
		fenshu.setAlignment(Pos.CENTER_RIGHT);
		fenshu.setMinSize(200, 75);
		jifenban.add(fenshu, 2, 0);
		jifenban.setMinSize(600, 75);
		jifenban.setLayoutX(MARGIN1);	jifenban.setLayoutY(MARGIN2-GRID_SPAN);
		jifenban.getStylesheets().add(this.getClass().getResource("game.css").toExternalForm());	//������ʽ��
		//game.getChildren().add(jifenban);
		
		for(int i = 0;i < 8;i++){
			setHiddenFood(i);
		}
		
		//��ʼ��ʱ
		/*
		Clock clock = new Clock();
		Thread clockThread = new Thread(clock);
		clockThread.start();
		*/
		isPausing = false;
		
	}//����go()����
	
	/*ͨ�������ص�ʳ������������õ�ʳ����µ���ʱ�����ص�ʳ����뵽������
	 * ���ҽ����õ�ʳ��ͨ���뱳����ͬ��ͼƬ�ڵ�ס
	 * ����Ŀǰ�뵽���ܹ��������������Ψһ������ȱ���ǵ�ͼ�Ϸ���Ҫ�ճ�һ�е�λ��
	 * ��ֻ�������Ϸ�ʹ��
	 */
	//�����������ص�ʳ��
	void setHiddenFood(int i){
		
		//ÿ�����Ƴ��ڵ�����ʵ��֮��������Ժ��ܼ����ڵ�
		
		game.getChildren().remove(jifenban);
		game.getChildren().remove(zheDang);

		//�������صĵ�ͼ����
		hiddenMap[i] = new Map(i , -1);
		hiddenMap[i].state = (int)(Math.random()*6 + 1);
		//��������ʳ���ͼ��
		setPicture(i, -1);
		hiddenFood[i] = new ImageView(fruit);
		hiddenFood[i].setX(MARGIN1 + GRID_SPAN * i + 6);
		hiddenFood[i].setY(MARGIN2 - GRID_SPAN + 6);
		game.getChildren().add(hiddenFood[i]);

		//�����ڵ�
		zheDang.setX(MARGIN1);	zheDang.setY(MARGIN2 - GRID_SPAN);
		game.getChildren().add(zheDang);

		game.getChildren().add(jifenban);
	}
	
	//���ؼ���
	
	//��ͣ����
	class PauseHandler implements EventHandler<ActionEvent>{
		/*������ͣ����,��ť�Ͱ�ť����
		 */
		public void handle(ActionEvent e){
			if(isMoving){			//�����ƶ�ʱ�����Ч,��ֹ����bug
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
			game.getStylesheets().add(Game.class.getResource("pauseOption.css").toExternalForm());		//������ʽ��
			game.getChildren().add(pauseOption);
		}
	}//��PauseHandler����
	
	class continueHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			game.getChildren().remove(game.getChildren().size()-1);
			game.getChildren().remove(game.getChildren().size()-1);
			isPausing = false;
		}
	}//��continueHandler����
	
	class restartHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			game.getChildren().remove(game.getChildren().size()-1);
			game.getChildren().remove(game.getChildren().size()-1);
			isPausing = false;
			go();
		}
	}//��restartHandler����
	
	class backHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			game = null;
			GameLauncher.back();
		}
	}//��backHandler����
	
	class SetMap{
		void Set(){
			for(int i = 0;i < 8;i++){
				for(int j = 0;j < 10;j++){
					if(map[i][j].isShow){
						map[i][j].state = (int)(Math.random()*6) + 1;
					}
				}
			}
			//����Ƿ�ʼǰ��������һ���
			//����
			while(existLinked){
				existLinked = false;		//����û������һ���
				for(int i = 0;i < 8;i++){
					count = 1;
					for(int j = 0;j < 9;j++){
						if(map[i][j].state == map[i][j+1].state){
							count++;			//����һ����ɫ��ͬ���������һ
							if(count == 3){
								existLinked = true;
								Set();
							}
						}
						else{
							count = 1;			//����һ����ɫ��ͬ�����������
						}
					}
				}//for����
				//����
				for(int j = 0;j < 10;j++){
					count = 1;
					for(int i = 0;i < 7;i++){
						if(map[i][j].state == map[i+1][j].state){
							count++;			//����һ����ɫ��ͬ���������һ
							if(count == 3){
								existLinked = true;
								Set();
							}
						}
						else{
							count = 1;			//����һ����ɫ��ͬ�����������
						}
					}
				}//for����
				
				//���û�з���������
				if(!existLinked){
					break;
				}
			}//while����

		}//set()��������
		
	}//SetMap�������
	
	boolean setPicture(int x, int y){
		if(y == -1){			//����ʳ�������ͼƬ
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

		}//switch����
		if(map[x][y].state >= 40){
			fruit = fruit40;
		}
		return true;
	}

	public class MouseDone implements EventHandler<MouseEvent>{
		int index = 0;			//��¼��ѡ�е�

		public void handle(MouseEvent e) {
			
			if(isExchanging || isMoving || isPausing || isGameOver){
				return;
			}
			//�����������ϱ߽�����Ч
			if(e.getX() < MARGIN1 || e.getY() < MARGIN2){
				return;
			}
			//ת��Ϊ��������ֵ
			xIndex = (int) ((e.getX() - MARGIN1)/GRID_SPAN);
			yIndex = (int) ((e.getY() - MARGIN2)/GRID_SPAN);
		    //�ڵ�ͼ�ⲻ��Ч  
			if(xIndex<0||xIndex>7||yIndex<0||yIndex>9)  
		        return;
			//�ж��Ƿ�ѡ�й� ����Ϊ��
			if(map[xIndex][yIndex].isSelected || map[xIndex][yIndex].state == 0)
				return;
			//���ñ�ѡ��
			map[xIndex][yIndex].isSelected = true;
			selected.add(map[xIndex][yIndex]);
			
			//������Ч
			
			if(e.getEventType() != MouseEvent.MOUSE_RELEASED){			//������϶�  ����ɿ���ʱ�򲻴�������
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
			
			//ѡ�б�ʶ
			game.getChildren().remove(food[xIndex][yIndex]);
			checkedBox[index] = new ImageView(box);
			checkedBox[index].setX(MARGIN1 + GRID_SPAN * xIndex);
			checkedBox[index].setY(MARGIN2 + GRID_SPAN * yIndex);
			game.getChildren().add(checkedBox[index]);
			game.getChildren().add(food[xIndex][yIndex]);
			selectedCount++;
			if(index == 0){
				index = 1;			//�ı䵽��һ����ѡ�е�
			}
			else{
				index = 0;
			}
			
			//�Ƿ���ѡ��������
			if(selectedCount == 2){
				exc.exchange(index);			//�ƶ�
			}
			
		}//handle��������
		
	}//��mouseClicked����
	
	class Exchange{				//�����������ڿ�
		void exchange(int index){
			if( ( (selected.get(0)).x == (selected.get(1)).x && (((selected.get(0)).y == (selected.get(1)).y-1)|| (selected.get(0)).y == ((Map)selected.get(1)).y+1) )
					|| ( ((Map)selected.get(0)).y == (selected.get(1)).y && (((selected.get(0)).x == (selected.get(1)).x-1)|| ((Map)selected.get(0)).x == (selected.get(1)).x+1) )
					|| ( (selected.get(0).x == selected.get(1).x+1 || selected.get(0).x == selected.get(1).x-1) && (selected.get(0).y == selected.get(1).y+1 || selected.get(0).y == selected.get(1).y-1) ) ){
				isExchanging = true;			//�����ѡ�����������ͽ���
			}
			else{			//��������ڣ���ȡ����һ��ѡ���
				(selected.get(0)).isSelected = false;
				selected.remove(0);
				game.getChildren().remove(checkedBox[index]);
				selectedCount--;
				return;
			}
			//����������
			if(isExchanging){
				//�жϽ�������
				if((selected.get(0)).y == (selected.get(1)).y){
					if((selected.get(0)).x < (selected.get(1)).x){		//0����
						direction = 0;
					}
					else{
						direction = 1;								//0����
					}
				}
				if((selected.get(0)).x == (selected.get(1)).x){
					if((selected.get(0)).y < (selected.get(1)).y){		//0����
						direction = 2;
					}
					else{
						direction = 3;					//0����
					}
				}
				if(selected.get(0).x == selected.get(1).x+1){			//0����
					if(selected.get(0).y == selected.get(1).y +1){		//0������
						direction = 4;
					}
					else if(selected.get(0).y == selected.get(1).y - 1){
						direction = 5;				//0������
					}
				}
				if(selected.get(0).x == selected.get(1).x-1){			//0����
					if(selected.get(0).y == selected.get(1).y +1){		//0������
						direction = 6;
					}
					else if(selected.get(0).y == selected.get(1).y - 1){
						direction = 7;				//0������
					}
				}

				
				//�Ƴ�ѡ�б��
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


				}//����switch
				
				//�����������״̬
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
				
				//���ý���������������
				timeLine.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent t){
						if(eliminable){			//����
							
							//int[][] data1 = basePlay.remove(eliminateState, data,  selected.get(0).x , selected.get(0).y , selected.get(1).x , selected.get(1).y);
							
							
							//map = basePlay.remove(mark, map);
							basePlay.isStarRemove(mark, map, stars, starsState);			//����Ƿ�����Ч������
							basePlay.remove(mark, map);
							
							ImageView tempView;
							tempView = food[selected.get(1).x][selected.get(1).y];
							food[selected.get(1).x][selected.get(1).y] = food[selected.get(0).x][selected.get(0).y];
							food[selected.get(0).x][selected.get(0).y] = tempView;
							
							eliminate(stars, starsState);
							//�����ƶ�
							//move();
							

						}//����if
						
						//����״̬
						isExchanging = false;		//����Ϊû�н���״̬
						selectedCount = 0;		//�Ѿ��ƶ��ˣ�����������
						((Map)selected.get(0)).isSelected = false;
						((Map)selected.get(1)).isSelected = false;
						selected = new ArrayList<Map>();

					}
				});
				
				//��������������������
				if(eliminable){
					timeLine.play();
					
				}
				else{		//����������������״̬
					(selected.get(1)).state = (selected.get(0)).state;
					(selected.get(0)).state = tempVal;
					timeLine.setAutoReverse(true);
					timeLine.setCycleCount(2);
					timeLine.play();
				}
			}//����if
						
		}//exchange��������
	}//��Exchange����
	
	void eliminate(ArrayList<Map> stars, ArrayList<Integer> starsState){
		int time = 300;
		Timeline timeLine = new Timeline();
		KeyFrame kf = new KeyFrame(Duration.millis(time));
		timeLine.getKeyFrames().add(kf);
		timeLine.setOnFinished(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				//������һ���ƶ�
				eliminable = false;
				move();
				if(isGameOver){				//��Ϸ����ʱ���⸽�Ӽ���Ƿ������Ч��
					checkStar();		//�ݹ����Ƿ���
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
					
					//��ʾ����
					
					//��������
					//���ݲ�ͬ����ɫ��ʾ��ͬ��ɫ��������,Ŀǰûʵ��
					Image boom = new Image(this.getClass().getResourceAsStream("image/��ը��Ч3.gif"));
					//Image boom = new Image(this.getClass().getResourceAsStream("image/��ը��Ч1.gif"));
					ImageView boomView = new ImageView(boom);
					boomView.setX(MARGIN1 + GRID_SPAN * map[i][j].x);
					boomView.setY(MARGIN2 + GRID_SPAN * map[i][j].y);
					game.getChildren().add(boomView);

					//���źͽ���
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
															
					//�������÷���
					if(stars.contains(map[i][j])){		//��Ч�����
						if(starsState.get(stars.indexOf(map[i][j])) > 40){		//5����Ч��
							grades = grades + 107*6;
							fenshu.setText(""+grades);
						}
						else{		//������Ч��
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
					
					//�������÷���
					grades = grades + 107;
					fenshu.setText(""+grades);

				}
			}
		}//for����
		
		eliminateCount++;		//����������1
		
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

		}//switch����
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
		//��ը��Ч	�Ժ���ܸ������������в�ͬ��Ч	Ŀǰһ��
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
			//for(Map temp : stars){				//�ҳ�������Ч��״̬
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
	}//����eliminate����
	
	boolean[][] hadMoved = new boolean[8][10];		//��¼��Щ���ӱ��ƶ���
	void move(){
		isMoving = true;		//��ʼ��Ϊ���ƶ�
		needMoved = new ArrayList<Map>();
		for(int row = 9;row >= 0;row--){		//������һ�ſ�ʼ����Ƿ�����Ҫ�ƶ��Ŀ�
			for(int col = 0;col < 8;col++){		//����
				if(needMoved.contains(map[col][row])){		//���������˵���ÿ��Ѿ�����ӹ�Ҫ�ƶ�
					continue;
				}
				if(map[col][row].state == 0){		//�пտ�˵������Ҫ���ƶ���
					if(map[col][row].y == 0){			//����ǵ�һ�ţ�ֱ��rebirth��ǰ�������λ��
						/*
						rebirth(col, row);
						move();
						*/
						needMoved.add(hiddenMap[col]);
					}
					else if(row-1 >= 0 && map[col][row-1].state != 0){
						for(int k = row-1;k >= 0;k--){		//��������Ҫ�ƶ��Ŀ�֮�ϵ����п�װ��needMoved
							map[col][k].willBeMoved = true;
							needMoved.add(map[col][k]);
							if(k == 0){
								needMoved.add(hiddenMap[col]);
							}
						}
					}//if-else����
				}//if����
				
			}//for����
		}//for����
		if(needMoved.size() == 0 || needMoved.isEmpty()){			//���û����Ҫ�ƶ��ģ�����
			//����Ƿ��ƶ���ĵ�ͼ�Ƿ��п���������
			ArrayList<Map> mark = basePlay.checkAfterMove(hadMoved, map);
			hadMoved = new boolean[8][10];		//״̬����

			if(!mark.isEmpty()){		//���mark��Ϊ��	������
				eliminable = true;
				//map = basePlay.remove(mark, map);
				ArrayList<Map> stars = new ArrayList<Map>();
				ArrayList<Integer> starsState = basePlay.isStarRemove(mark, map, stars, new ArrayList<Integer>());			//����Ƿ�����Ч������
				basePlay.remove(mark, map);
				eliminate(stars, starsState);
				
				//move();
			}
			else{
				isMoving = false;
				
				Sound.playPraiseSound(eliminateCount);			//���ų�����Ч

				//��0����������
				eliminateCount = 0;

			}

			//�����ƶ���Ч
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
		}//�ж��Ƿ���Է��ؽ���
		
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
				//�ƶ�һ������ݸ���
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
					map[(needMoved.get(i)).x][(needMoved.get(i)).y] = needMoved.get(i);		//�����������ƶ�һ��
					
					if(needMoved.get(i).y == 0){
						setHiddenFood(needMoved.get(i).x);
					}
					hadMoved[needMoved.get(i).x][needMoved.get(i).y] = true;
				}//for����
				
				
				move();		//�ݹ�
				
			}//handle����
		});
		
		timeLine.play();					
	}//����move����
	
	class Clock implements Runnable{
		public void run(){
			while(true){
				//û����ͣ������Ϸû����ʱ
				try{
					Thread.sleep(1000);
					if(isPausing || isGameOver){
						continue;
					}
					if(time != 0){
						time--;
						Platform.runLater(() -> {
							shijian.setText("ʱ�䣺"+time);
						});
					}
					if(time == 0){
						//��鲢������Ч��
						

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
					//shijian.setText("ʱ�䣺"+time);
				}catch(Exception ex){
					ex.printStackTrace();
				}

			}
		}//go����
	}//��Clock����
	
	
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
				ArrayList<Integer> starsState = basePlay.isStarRemove(mark, map, stars, new ArrayList<Integer>());			//����Ƿ�����Ч������
				basePlay.remove(mark, map);
				eliminable = true;
				eliminate(stars, starsState);
				
			}
		//});
	}//����checkStar����
	
	
	//����Ϸ����ʱչʾ��������
	void gameOver(){
		Platform.runLater(() -> {
			//��¼���÷���
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
			
			Label jieshu = new Label("",new ImageView(new Image(this.getClass().getResourceAsStream("image/words/��Ϸ����.png"),200,80,false,false)));
			jieshu.setId("jieshu");
			jieshu.setAlignment(Pos.CENTER);
			GridPane.setHalignment(jieshu, HPos.CENTER);
			overPane.add(jieshu, 0, 0,2,1);
			
			Label finalGrades = new Label(""+grades,new ImageView(new Image(this.getClass().getResourceAsStream("image/words/���շ���.png"),200,80,false,false)));
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
	
}//��Game����
