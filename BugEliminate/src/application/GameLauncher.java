package application;
	
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameLauncher extends Application {
	
	public String loadname = "";
	Stage stage = new Stage();
	static Scene mainScene;
	static Scene mainscene1;
	static Group root = new Group();
	
	static boolean as1 = true;			//记录当前的checkBox状态
	static boolean as2 = true;
	
	static CheckBox audio = new CheckBox("       ");
	static CheckBox music = new CheckBox("       ");
	Image shengya = new Image(this.getClass().getResourceAsStream("image/button/生涯模式.png"),200,80,false,false);
	Image xianshi = new Image(this.getClass().getResourceAsStream("image/button/限时模式.png"),200,80,false,false);
	Image shezhi = new Image(this.getClass().getResourceAsStream("image/button/设置.png"),200,80,false,false);
	Image paihang = new Image(this.getClass().getResourceAsStream("image/button/排行榜.png"),200,80,false,false);
	Image tuichu = new Image(this.getClass().getResourceAsStream("image/button/退出游戏.png"),200,80,false,false);
	
	Image beijing = new Image(this.getClass().getResourceAsStream("image/主界面背景.png"), 800, 1000,false,false);
	
	Player player;
	DataBase dataBase = new DataBase();
	
	int width = 800;
	int height = 1000;
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("Bug消消乐");		//设置标题
		stage.initStyle(StageStyle.UNDECORATED);//设定窗口无边框
		
		//播放背景音乐
		BackgroundMusic.playMainBGM();
		
		//设置背景
		ImageView background = new ImageView(beijing);
		root.getChildren().add(background);
		
		//设置主菜单
		GridPane menuGrid = new GridPane();
		menuGrid.setAlignment(Pos.CENTER);
		menuGrid.setVgap(4);			//设置行距
		menuGrid.setPadding(new Insets(450, 0, 0, 300));
		
		ImageView p1 = new ImageView(shengya);
		Button Life = new Button("",p1);
		Life.setStyle("-fx-background-color:null");	
		Life.setMaxSize(200, 80);
		Life.setOnAction(new LifeHandler());
		menuGrid.add(Life, 0, 0);
		texiao(Life);
		
		ImageView p2 = new ImageView(xianshi);
		Button Time = new Button("",p2);
		Time.setStyle("-fx-background-color:null");	
		Time.setMinSize(200, 80);
		Time.setOnAction(new TimeHandler());
		menuGrid.add(Time, 0, 1);
		texiao(Time);
		
		ImageView p3 = new ImageView(paihang);
		Button Rank = new Button("",p3);
		Rank.setStyle("-fx-background-color:null");	
		Rank.setMinSize(200, 80);
		Rank.setOnAction(new RankHandler());
		menuGrid.add(Rank, 0, 2);
		texiao(Rank);
		
		ImageView p4 = new ImageView(shezhi);
		Button Help = new Button("",p4);
		Help.setStyle("-fx-background-color:null");	
		Help.setMinSize(200, 80);
		Help.setOnAction(new HelpHandler());
		menuGrid.add(Help, 0, 3);
		texiao(Help);
		
		
		ImageView p5 = new ImageView(tuichu);
		Button Exit = new Button("",p5);
		Exit.setStyle("-fx-background-color:null");	
		Exit.setMinSize(200, 80);
		menuGrid.add(Exit, 0, 4);
		Exit.setOnAction(new ExitHandler());
		texiao(Exit);
		
		ImageView sun =new ImageView(new Image(this.getClass().getResourceAsStream("image/decoration/太阳.gif")));
		root.getChildren().add(sun);
		sun.setLayoutX(11);  sun.setLayoutY(22);
		
		root.getChildren().add(menuGrid);
		
		mainScene = new Scene(root, width, height);
		menuGrid.getStylesheets().add(GameLauncher.class.getResource("MainMenu.css").toExternalForm());		//加载样式表
		
		stage.setScene(mainScene);
		
		//显示选择用户界面
		//dataBase.setUsers("test");
		
		
		Label gray = new Label();
		gray.setStyle("-fx-background-color:grey");
		gray.setMinSize(width,height);
		gray.setOpacity(0.7);
		root.getChildren().add(gray);
		
		GridPane login = new GridPane();
		login.setId("login");
		login.setMinSize(600, 490);
//		login.setGridLinesVisible(true);
		
		
		Text ss = new Text("      ");
		login.add(ss, 0, 7);
		
		Text ss1 = new Text("      ");
		login.add(ss1, 1, 7);
		
		//已存在用户的选择
	    Button sure =  new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/存在的用户.png"),200,80,false,false)));
			sure.setStyle("-fx-background-color:null");
			texiao(sure);
		    sure.setOnAction((ActionEvent aa) -> {
		    	Sound.playaftclicked();
		    	Label oldplayer1 = new Label("",new ImageView(new Image(this.getClass().getResourceAsStream("image/words/存在的用户.png"),120,48,false,false)));
		ChoiceBox<String> oldplayer = new ChoiceBox<String>(
			        FXCollections.observableArrayList(dataBase.getPlayer()));
		oldplayer.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
          public void changed(ObservableValue ov, Number value, Number new_value) {

        	  player = dataBase.loadPlayer(dataBase.getPlayer().get(new_value.intValue()));
        		root.getChildren().remove(root.getChildren().size()-1);
	  			root.getChildren().remove(root.getChildren().size()-1);
            
        	  
          }
        }); 
		login.add(oldplayer1,0, 7);
		login.add(oldplayer ,1, 7);
		    });
	

	    
	    login.add(sure,0, 4,2,3);
		
		

		
		Button newplayer = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/不存在的用户.png"),200,80,false,false)));
		newplayer.setStyle("-fx-background-color:null");
		newplayer.setOnAction(new newplayerHandler());
		texiao(newplayer);
		
		
		
		login.add(newplayer, 0, 1,2,3);
		login.setLayoutX(110);	login.setLayoutY(250);
		login.setAlignment(Pos.CENTER);
		login.setVgap(40);
		login.setHgap(10);
		root.getStylesheets().add(Game.class.getResource("MainMenu.css").toExternalForm());	
	
		root.getChildren().add(login);
		
		stage.show();
	}//start方法结束
	
	public static void main(String[] args) {
		launch(args);
	}
	
	class newplayerHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			Sound.playaftclicked();
			Label gray1 = new Label();
			gray1.setStyle("-fx-background-color:grey");
			gray1.setMinSize(width,height);
			gray1.setOpacity(0.7);
			root.getChildren().add(gray1);
			
			GridPane login1 = new GridPane();
			login1.setId("login");
			login1.setMinSize(600, 490);
//			login1.setGridLinesVisible(true);
			
			Label newplayer1 = new Label("",new ImageView(new Image(this.getClass().getResourceAsStream("image/words/新建用户.png"),120,48,false,false)));
			TextField newplayer = new TextField();
			newplayer.setPromptText("取个名字");
			Button sure = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/确定.png"),100,40,false,false)));
			sure.setStyle("-fx-background-color:null");
			texiao(sure);
			sure.setOnAction((ActionEvent aa) -> {
				if (newplayer.getText().equals("")) {
					
				}else{
					Sound.playaftclicked();
				player = dataBase.loadPlayer(newplayer.getText());
				root.getChildren().remove(root.getChildren().size()-1);
	  			root.getChildren().remove(root.getChildren().size()-1);
	  			root.getChildren().remove(root.getChildren().size()-1);
	  			root.getChildren().remove(root.getChildren().size()-1);
				}
			});
			login1.add(newplayer1, 0, 1);
			login1.add(newplayer , 1, 1);
			login1.add(sure      , 1, 2,2,1);
			
			Button returntohelp = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/返回1.png"),50,50,false,false)));
			returntohelp.setStyle("-fx-background-color:null");
		    texiao(returntohelp);
		    login1.add(returntohelp, 0, 2);
		    returntohelp.setOnAction((ActionEvent aa) -> {
		    	Sound.playaftclicked();
		    	root.getChildren().remove(root.getChildren().size()-1);
	  			root.getChildren().remove(root.getChildren().size()-1);
		    });
			
			login1.setLayoutX(110);	login1.setLayoutY(250);
			login1.setAlignment(Pos.CENTER);
			login1.setVgap(40);
			login1.setHgap(10);
			root.getChildren().add(login1);
		}
	}
	
	class LifeHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			Sound.playaftclicked();
			Life lifePane = new Life(player);
			//mainScene.setRoot(Life.life);
			mainScene.setRoot(Life.mainGroup);
			lifePane.go();
		}
	}
	
	class TimeHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			Sound.playaftclicked();
			//Game endlessGame = new Game(0, dataBase);
			//mainScene.setRoot(Game.game);
			Game endlessGame = new Game(0,player);
			mainScene.setRoot(endlessGame.game);
			endlessGame.go();
		}
	}
	
    class RankHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			Sound.playaftclicked();
			Label gray = new Label();
			gray.setStyle("-fx-background-color:grey");
			gray.setMinSize(width,height);
			gray.setOpacity(0.7);
			root.getChildren().add(gray);
			
			GridPane rankgui = new GridPane();
			rankgui.setMinSize(400, 500);
			rankgui.setId("rankgui");
//			rankgui.setGridLinesVisible(true);
			
			ImageView rank1 = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/战绩榜.png"),200,60,false,false));
			
			rankgui.add(rank1 , 2,0,2,1);
			
//			ArrayList<String> players = DataBase.getPlayer();

			int a[] = player.getTopGrades();
			ImageView first = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/1.png"),80,40,false,false));
		    rankgui.add(first, 1, 2);
		    ImageView second = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/2.png"),80,40,false,false));
		    rankgui.add(second, 1, 3);
		    ImageView third = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/3.png"),80,40,false,false));
		    rankgui.add(third,1, 4);
		    ImageView forth = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/4.png"),80,40,false,false));
		    rankgui.add(forth, 1, 5);
		    ImageView fifth = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/5.png"),80,40,false,false));
		    rankgui.add(fifth, 1, 6);
		    ImageView sixth = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/6.png"),80,40,false,false));
		    rankgui.add(sixth, 1, 7);
		    ImageView seventh = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/7.png"),80,40,false,false));
		    rankgui.add(seventh, 1, 8);
		    ImageView eighth = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/8.png"),80,40,false,false));
		    rankgui.add(eighth,1, 9);
		    ImageView ninth = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/9.png"),80,40,false,false));
		    rankgui.add(ninth, 1, 10);
		    
			Text first1 = new Text(""+a[0]);
			rankgui.add(first1, 2, 2);
			ziti(first1);
			Text second1 = new Text(""+a[1]);
			rankgui.add(second1, 2, 3);
			ziti(second1);
			Text third1 = new Text(""+a[2]);
			rankgui.add(third1, 2, 4);
			ziti(third1);
			Text forth1 = new Text(""+a[3]);
			rankgui.add(forth1, 2, 5);
			ziti(forth1);
			Text fifth1 = new Text(""+a[4]);
			rankgui.add(fifth1, 2, 6);
			ziti(fifth1);
			Text sixth1 = new Text(""+a[5]);
			rankgui.add(sixth1, 2, 7);
			ziti(sixth1);
			Text seventh1 = new Text(""+a[6]);
			rankgui.add(seventh1, 2, 8);
			ziti(seventh1);
			Text eighth1 = new Text(""+a[7]);
			rankgui.add(eighth1,2, 9);
			ziti(eighth1);
			Text ninth1 = new Text(""+a[8]);
			rankgui.add(ninth1,2, 10);
			ziti(ninth1);
		
		    
		    Button returnstart = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/关闭.png"),50,50,false,false)));
			returnstart.setStyle("-fx-background-color:null");
			returnstart.setOnAction(new returnHandler());
			rankgui.add(returnstart, 1, 0);
			texiao(returnstart);
			
			rankgui.setLayoutX(200);	rankgui .setLayoutY(250);
			rankgui .setAlignment(Pos.CENTER);
			rankgui .setVgap(10);
			rankgui .setHgap(10);
			root.getStylesheets().add(Game.class.getResource("rankgui.css").toExternalForm());		
			root.getChildren().add(rankgui);
			
			
			
		}
		 public void ziti(Text s) { 
				s.setFill(Color.YELLOW);
				s.setFont(Font.font(null, FontWeight.BOLD, 40));
	    }
	}
	
	class HelpHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			Sound.playaftclicked();
			Label gray = new Label();
			gray.setStyle("-fx-background-color:grey");
			gray.setMinSize(width,height);
			gray.setOpacity(0.7);
			root.getChildren().add(gray);
			
			GridPane HelpOption = new GridPane();
			HelpOption.setMinSize(510, 498);
			HelpOption.setId("HelpOption");
//		    HelpOption.setGridLinesVisible(true);//显示网格线
//		    
			//返回按钮
			Button returnstart = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/返回2.png"),70,50,false,false)));
			returnstart.setStyle("-fx-background-color:null");
			returnstart.setId("returnstart");
			returnstart.setOnAction(new returnHandler());
			HelpOption.add(returnstart, 0, 0);
			texiao(returnstart);
			
			//音乐开关
			VBox musicduiqi1 = new VBox();
			Text kong1 = new Text("      " );
			Text kong = new Text("      " );
			HBox musicduiqi = new HBox();
			musicduiqi.getChildren().addAll(kong,music);
			musicduiqi1.getChildren().addAll(kong1,musicduiqi);
 			music.setId("music");
			music.setMinSize(80, 30);
			//music.setSelected(true);
			music.setSelected(as1);
			
		    music.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    public void changed(ObservableValue<? extends Boolean> ov,
		             Boolean old_val, Boolean new_val) {
		            if (music.isSelected()) {
						//System.out.println("打开音乐的方法");
		            	BackgroundMusic.setNotMute();
		            	BackgroundMusic.playMainBGM();
					}else {
						//System.out.println("关闭音乐的办法");
						BackgroundMusic.stopMainBGM();
						BackgroundMusic.setMute();
					}
		            
		            as1 = music.isSelected();
		          }
		        });
			HelpOption.add(musicduiqi1, 1, 0);
			 
			//音效开关
			VBox audioduiqi1 = new VBox();
			HBox audioduiqi = new HBox();
			Text kong2 = new Text("      " );
			Text kong22 = new Text("      " );
			audioduiqi.getChildren().addAll(kong2,audio);
			audioduiqi1.getChildren().addAll(kong22,audioduiqi);
			audio.setId("audio");
			audio.setMinSize(80, 30);
			//audio.setSelected(true);
			audio.setSelected(as2);
			
	        audio.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov,
			   Boolean old_val, Boolean new_val) {
			           if (audio.isSelected()) {
							Sound.setNotMute();
			           }else {
							Sound.setMute();
			           }
			           as2 = audio.isSelected();
			          }
			        });
	        HelpOption.add(audioduiqi1, 2, 0);
	        
	        //规则介绍
		
			Button Rule = new Button("", new ImageView(new Image(this.getClass().getResourceAsStream("image/button/规则介绍.png"),100,40,false,false)));
			Rule.setStyle("-fx-background-color:null");
			HelpOption.add(Rule, 1,2);
			Rule.setOnAction(new RuleHandler()); 
			texiao(Rule);
			
			
			//选择用户	 
			Button  choose= new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/选择用户.png"),100,40,false,false)));
			choose.setStyle("-fx-background-color:null");
			HelpOption.add(choose, 1, 1);
			texiao(choose);
			choose.setOnAction((ActionEvent a) -> {
				Sound.playaftclicked();
				Label gray1 = new Label();
				gray1.setStyle("-fx-background-color:grey");
				gray1.setMinSize(width,height);
				gray1.setOpacity(0.7);
				root.getChildren().add(gray1);
				
				GridPane login = new GridPane();
				login.setId("login");
				login.setMinSize(600, 490);
//				login.setGridLinesVisible(true);
				
				
				Text ss = new Text("      ");
				login.add(ss, 0, 7);
				
				Text ss1 = new Text("      ");
				login.add(ss1, 1, 7);
				
				Button sure =  new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/存在的用户.png"),200,80,false,false)));
				sure.setStyle("-fx-background-color:null");
				texiao(sure);
			    sure.setOnAction((ActionEvent aa) -> {
				    	Sound.playaftclicked();
				Label oldplayer1 = new Label("",new ImageView(new Image(this.getClass().getResourceAsStream("image/words/存在的用户.png"),120,48,false,false)));
				ChoiceBox<String> oldplayer = new ChoiceBox<String>(
					        FXCollections.observableArrayList(dataBase.getPlayer()));
				oldplayer.getSelectionModel().selectedIndexProperty()
		        .addListener(new ChangeListener<Number>() {
		          public void changed(ObservableValue ov, Number value, Number new_value) {

		        	  player = dataBase.loadPlayer(dataBase.getPlayer().get(new_value.intValue()));
		        		root.getChildren().remove(root.getChildren().size()-1);
			  			root.getChildren().remove(root.getChildren().size()-1);
		            
		        	  
		          }
		        }); 
				login.add(oldplayer1,0, 7);
				login.add(oldplayer ,1, 7);
				    });
		  
			    login.add(sure,0, 4,2,3);
	
				Button newplayer = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/不存在的用户.png"),200,80,false,false)));
				newplayer.setStyle("-fx-background-color:null");
				newplayer.setOnAction(new newplayerHandler());
				texiao(newplayer);
				
				
				login.add(newplayer, 0, 1,2,3);
				login.setLayoutX(110);	login.setLayoutY(250);
				login.setAlignment(Pos.CENTER);
				login.setVgap(40);
				login.setHgap(10);
				root.getStylesheets().add(Game.class.getResource("MainMenu.css").toExternalForm());	
			
				root.getChildren().add(login);
				
			});

			//删除用户
			Button delete = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/删除当前用户.png"),130,40,false,false)));
			delete.setStyle("-fx-background-color:null");
			HelpOption.add(delete, 2,1);
			texiao(delete);
			delete.setOnAction((ActionEvent a) -> {
				//删除用户的方法调用
				Sound.playaftclicked();
				
				DataBase.deletePlayer(player.name);
				
				Label gray1 = new Label();
				gray1.setStyle("-fx-background-color:grey");
				gray1.setMinSize(width,height);
				gray1.setOpacity(0.7);
				root.getChildren().add(gray1);
				
				GridPane login = new GridPane();
				login.setId("login");
				login.setMinSize(600, 490);
//				login.setGridLinesVisible(true);
				
				Text ss = new Text("      ");
				login.add(ss, 0, 7);
				
				Text ss1 = new Text("      ");
				login.add(ss1, 1, 7);
				
				Button sure =  new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/存在的用户.png"),200,80,false,false)));
				sure.setStyle("-fx-background-color:null");
				texiao(sure);
			    sure.setOnAction((ActionEvent aa) -> {
			    	
					Sound.playaftclicked();
					Label oldplayer1 = new Label("",new ImageView(new Image(this.getClass().getResourceAsStream("image/words/存在的用户.png"),120,48,false,false)));
					ChoiceBox<String> oldplayer = new ChoiceBox<String>(
						        FXCollections.observableArrayList(dataBase.getPlayer()));
					oldplayer.getSelectionModel().selectedIndexProperty()
			        .addListener(new ChangeListener<Number>() {
			          public void changed(ObservableValue ov, Number value, Number new_value) {
	
			        	  player = dataBase.loadPlayer(dataBase.getPlayer().get(new_value.intValue()));
			        		root.getChildren().remove(root.getChildren().size()-1);
				  			root.getChildren().remove(root.getChildren().size()-1);
			          }
			        }); 
					login.add(oldplayer1,0, 7);
					login.add(oldplayer ,1, 7);
				
				});
			

			    
			    login.add(sure,0, 4,2,3);
	
				Button newplayer = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/不存在的用户.png"),200,80,false,false)));
				newplayer.setStyle("-fx-background-color:null");
				newplayer.setOnAction(new newplayerHandler());
				texiao(newplayer);
				
				
				login.add(newplayer, 0, 1,2,3);
				login.setLayoutX(110);	login.setLayoutY(250);
				login.setAlignment(Pos.CENTER);
				login.setVgap(40);
				login.setHgap(10);
				root.getStylesheets().add(Game.class.getResource("MainMenu.css").toExternalForm());		
				root.getChildren().add(login);
			
				
			});
			
		//设置里larva的图片
		ImageView larva1 =new ImageView(new Image(this.getClass().getResourceAsStream("image/设置图片.png"),120,200,false,false));
		HelpOption.add(larva1, 0, 1,1,2);
	
			
			
			
			
			
			//开发人员介绍按钮
			Button appdeveloper = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/开发者.png"),120,40,false,false)));
			appdeveloper.setStyle("-fx-background-color:null");
			appdeveloper.setOnAction(new AppdeveloperHandler());
			HelpOption.add(appdeveloper, 2,2);
			texiao(appdeveloper);
			
			HelpOption.setLayoutX(150);	HelpOption.setLayoutY(250);
			HelpOption.setAlignment(Pos.CENTER);
			HelpOption.setVgap(40);
			HelpOption.setHgap(2);
			root.getStylesheets().add(Game.class.getResource("HelpOption.css").toExternalForm());		
			root.getChildren().add(HelpOption);
		}
	}

	//设置按钮的监听
	class SetHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			
		}
	}
    
    class RuleHandler implements EventHandler<ActionEvent>{
			public void handle(ActionEvent e){
//				Sound.playaftclicked();
//				Label gray = new Label();
//				gray.setStyle("-fx-background-color:grey");
//				gray.setMinSize(width,height);
//				gray.setOpacity(0.7);
//				root.getChildren().add(gray);
//				
//				GridPane rulegui = new GridPane();
//				rulegui.setMinSize(500, 500);
//				rulegui.setId("rulegui");
//				rulegui.setGridLinesVisible(true);
//				
//				ImageView ruleintroduction = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/规则.png"),250,60,false,false));
//				rulegui.add(ruleintroduction , 2, 0);
//				
//				//滚动面板
//				ScrollPane sp = new ScrollPane();
//				Image roses = new Image(getClass().getResourceAsStream("image/规则.png"),400,800,false,false);
//			    sp.setContent(new ImageView(roses));
//		        sp.minWidth(roses.getWidth());;
//		        sp.setMaxSize(400, 300);
////				
////				VBox ruleintrod= new VBox();
////				ImageView as =  new ImageView(new Image(this.getClass().getResourceAsStream("image/left1.png")));
////              Text ss = new Text("            ds\na\n\n\n\n\n\n\n\n\nn\n\n\n\n\n\n\nn\n\\n\naaaaaaaaaaaaaa\naaaaaaaaaaaaaaaa\naaaaaaaaaaaaaaaaaaaaa");
////				ruleintrod.getChildren().addAll(as,ss);
////				ScrollBar s1 = new ScrollBar();
////				ruleintrod.setMinSize(20, 50);
////				ruleintrod.setSpacing(0);
////			  
////			  
////			    s1.setMin(0);
////		        s1.setOrientation(Orientation.VERTICAL);
////		        s1.setPrefHeight(180);
////		        s1.setMax(360);
////	      	    s1.setOrientation(Orientation.VERTICAL);
////			    rulegui.add(s1,3,1);
////			    rulegui.add(ruleintrod,2,1);
////			    s1.valueProperty().addListener((ObservableValue<? extends Number> ov, 
////				            Number old_val, Number new_val) -> {
////				                ruleintrod.setLayoutY(-new_val.doubleValue());
////				        });        
//			    rulegui.add(sp,2,1);
//				sp.setHbarPolicy(ScrollBarPolicy.NEVER);
//				sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
//				
//				//排版用
//				Text paiban = new Text("             ");
//				rulegui.add(paiban,4, 1);
//				
//				Button returnstart = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/返回1.png"),50,50,false,false)));
//				returnstart.setStyle("-fx-background-color:null");
//				returnstart.setOnAction(new returnHandler());
//				rulegui.add(returnstart, 2, 0);
//				texiao(returnstart);
				Sound.playaftclicked();
				Label gray = new Label();
				gray.setStyle("-fx-background-color:grey");
				gray.setMinSize(width,height);
				gray.setOpacity(0.7);
				root.getChildren().add(gray);
				
				GridPane rulegui = new GridPane();
				rulegui.setMinSize(400, 800);
				rulegui.setId("rulegui");
//				rulegui.setGridLinesVisible(true);
				
				
				Button guanbi = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/关闭.png"),40,40,false,false)));
				guanbi.setStyle("-fx-background-color:null");
				texiao(guanbi);
				guanbi.setOnAction(new returnHandler());
				rulegui.add(guanbi, 2, 0);
				//排版用
				Text paiban1 = new Text("         ");
				
				rulegui.add(paiban1, 0, 0);
				Text paiban = new Text(" ");
				rulegui.add(paiban, 1, 0);
 				ImageView larva2 = new ImageView(new Image(this.getClass().getResourceAsStream("image/larva2.png"),120,168,false,false));
				rulegui.add(larva2, 1, 23);
				
//				
				rulegui.setLayoutX(200);	rulegui .setLayoutY(100);
				rulegui .setAlignment(Pos.CENTER);
				rulegui.setVgap(20);
				rulegui.setHgap(40);
				root.getStylesheets().add(Game.class.getResource("rulegui.css").toExternalForm());
				root.getChildren().add(rulegui);
				
				//具体的调用排行榜的方法并显示到stage
				
			}
		}

    class ExitHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			System.exit(0);
		}
	}
    
    class returnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			Sound.playaftclicked();
			root.getChildren().remove(root.getChildren().size()-1);
			root.getChildren().remove(root.getChildren().size()-1);
		}
	}
	
    class AppdeveloperHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			Sound.playaftclicked();
			Label gray = new Label();
			gray.setStyle("-fx-background-color:grey");
			gray.setMinSize(width,height);
			gray.setOpacity(0.7);
			root.getChildren().add(gray);
			
			GridPane developgui = new GridPane();
			developgui.setMinSize(550, 500);
			developgui.setId("developgui");
//			developgui.setGridLinesVisible(true);
//			
			ImageView developer = new ImageView(new Image(this.getClass().getResourceAsStream("image/words/开发组成员介绍.png"),250,60,false,false));
			
		    developgui.add(developer, 1,0);
		    
		    Text developer1 = new Text("杨袁瑞（队长）\n王一力（大佬）\n周益冰（大佬）\n周正伟（划水菜鸡）");
			developer1.setId("developer1-text");
			developgui.add(developer1, 1, 1);
			
			Button support = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/支持设计者.png"),150,60,false,false)));
			support.setStyle("-fx-background-color:null");
			texiao(support);
			support.setOnAction(new supportHandler());
		    developgui.add(support, 1,2);
		   
		    //排版用
		    Text paiban = new Text("       ");
		    developgui.add(paiban , 1, 4);
		   
		    
			Button returntohelp = new Button("",new ImageView(new Image(this.getClass().getResourceAsStream("image/button/返回1.png"),30,30,false,false)));
			returntohelp.setStyle("-fx-background-color:null");
		    texiao(returntohelp);
		    returntohelp.setOnAction(new returnHandler());
		    developgui.add(returntohelp, 0, 1);
			
			developgui.setLayoutX(120);	developgui.setLayoutY(240);
			developgui.setAlignment(Pos.CENTER);
			developgui.setVgap(30);
			developgui.setHgap(10);
			root.getStylesheets().add(Game.class.getResource("developergui.css").toExternalForm());	
		    root.getChildren().add(developgui);
			
			
		}
	}
	
    /*或许搞个支付宝的链接O.O*/
    class supportHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			Sound.playaftclicked();
			
			try {
				java.awt.Desktop.getDesktop().browse(new URI("https://user.qzone.qq.com/1094114079"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
	}
	
    /*按钮的小特效*/	
    public static void texiao(Button s){
		 s.setOnMouseEntered(new EventHandler<MouseEvent>() {
		      @Override
		      public void handle(MouseEvent e) {
		        s.setScaleX(1.1);
		        s.setScaleY(1.1);
		        Sound.playbefclicked();
		      }
		    });

		    s.setOnMouseExited(new EventHandler<MouseEvent>() {
		      @Override
		      public void handle(MouseEvent e) {
		        s.setScaleX(1.0);
		        s.setScaleY(1.0);
		      }
		    });
	}


    public static void back(){
		mainScene.setRoot(root);
		
		return;
	}
}
