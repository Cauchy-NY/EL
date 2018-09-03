package application;

import java.io.*;
import java.util.ArrayList;

public class DataBase {
	
	static String filePath_UsersList = "Data/UsersList.txt";
	static String mulu = DataBase.class.getResource("/").getFile();
	//String filePath_UsersList = DataBase.class.getResource("Data/UsersList.txt").toExternalForm();
	//获取玩家列表
	public ArrayList<String> getPlayer() {
		ArrayList<String> players = new ArrayList<String>();
		//System.out.println(mulu);
		//InputStream fileIs = this.getClass().getResourceAsStream(mulu+filePath_UsersList);
		try{
			FileInputStream fileIs = new FileInputStream(mulu+filePath_UsersList);
			BufferedReader bf = new BufferedReader(new InputStreamReader(fileIs));
			String str = "";
		
			while((str = bf.readLine()) != null) {
				String[] names = str.split(",");
				for(int i = 0; i < names.length; i++) {
					if(!players.contains(names[i])) {
						players.add(names[i]);
					}
				}
			}
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return players;
	}
	
	//加载玩家，如果存在则加载数据，否则创建玩家数据
	public Player loadPlayer(String name) {
		ArrayList<String> players = this.getPlayer();
		Player restore = null;
		
		if(players.contains(name)) {
			String site = "Data/" + name + ".ser";
			try {
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(mulu+site));
				restore = (Player) is.readObject();
				is.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			players.add(name);
			restore = new Player(name);
			String site = "Data/" + name + ".ser";
			try {
				FileOutputStream fs = new FileOutputStream(mulu+site);
				ObjectOutputStream os = new ObjectOutputStream(fs);
				os.writeObject(restore);
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String str = "";
			for(int i = 0; i < players.size() - 1; i++) {
				str += players.get(i) + ",";
			}
			str += players.get(players.size() - 1);
			
			try {
				FileWriter writer = new FileWriter(mulu+filePath_UsersList);
				writer.write(str);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return restore;
		
	}
	
	//存储玩家数据
	public static void storePlayer(Player name) {
		String site = "Data/" + name.getName() + ".ser";
		Player store = name;
		try {
			FileOutputStream fs = new FileOutputStream(mulu+site);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(store);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//删除用户数据
	public static void deletePlayer(String playerName){
		String site = "Data/" + playerName + ".ser";

		//删除序列化文件
		File playerData = new File(mulu+site);
		playerData.delete();
		
		//删除usersList中对应名字
		try {
			FileInputStream fileIs = new FileInputStream(mulu+filePath_UsersList);
			BufferedReader bf = new BufferedReader(new InputStreamReader(fileIs));
			String str = "";

			while(bf.ready()){
				str += bf.readLine();
			}
			
			String[] tempList = str.split(",");
			
			
			str = "";
			for(String temp : tempList){
				if(!temp.equals(playerName)){
					str += temp + ",";
				}
			}
			
			
			System.out.println(str.substring(0, str.length()-1));
			bf.close();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(mulu+filePath_UsersList),false));

			bw.write(str.substring(0, str.length()-1));
			bw.flush();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
