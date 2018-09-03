package application;

import java.util.ArrayList;

public class BasePlay {
	
	private ArrayList<Map> checkEach(int x, int y, Map[][] data){
		ArrayList<Map> mark = new ArrayList<Map>();
		int count1 = 1;		//横向计数器
		int count2 = 1;		//纵向计数器
		
		for(int i = 1; x-i >= 0;i++){		//先向左检查
			if(BasePlay.isSame(data[x-i][y], data[x][y])){
				count1++;
				mark.add(data[x-i][y]);
			}
			else{		//如果不等则继续执行之后的向右检查
				break;
			}
		}
		for(int i = 1;x+i < 8;i++){		//向右检查
			if(BasePlay.isSame(data[x+i][y], data[x][y])){
				count1++;
				mark.add(data[x+i][y]);
			}
			else{
				break;
			}
		}
		
		if(count1 >= 3){			//当横向大于等于3个时
			mark.add(data[x][y]);		//将本身也加入mark
			
			boolean isMark = false;
			int[] count3 = new int[count1];			//声明一个count1大小的数组用来计数
			//对刚刚横向检查所标记的每一个都进行纵向检查
			
			for(int i = 0;i < count1;i++){
				Map temp = mark.get(i);			//暂时变量	方便表示
				count3[i] = 1;		//初始化计数器
				for(int j = 1;temp.y - j >= 0 ;j++){		//向上
					if(BasePlay.isSame(data[temp.x][temp.y - j].state, temp.state)){
						count3[i]++;
						mark.add(data[temp.x][temp.y - j]);
					}
					else{
						break;
					}
				}//向上检查的for结束
				for(int j = 1;temp.y + j < 10;j++){
					if(BasePlay.isSame(data[temp.x][temp.y + j].state, temp.state)){
						count3[i]++;
						mark.add(data[temp.x][temp.y + j]);
					}
					else{
						break;
					}
				}//向下检查的for结束
				
				
				//特效块的检查
				
				if(count3[i]<3)
					for(int k = 0;k < count3[i]-1;k++){		
						mark.remove(mark.size()-1);
					}
				//count3[i]小于3时
				//将刚才多加入mark的移除
				//因为count3[i]初值为1	所以多减去1
				
				if(!isMark){
					if((count1>=5||count3[i]>=5)){
						//原位置已有特效块就不生成新的特效块
						if(data[x][y].state/10==0){
							data[x][y].state=data[x][y].state+40;
						}
						isMark = true;
					}//生成五连特效块
					else if(count3[i] >= 3){
					
						if(data[x][y].state/10==0){
							data[x][y].state=data[x][y].state+30;
						}
						isMark = true;
					}//生成十字特效块		
				}
			}//对每一个进行纵向检查的for结束
			
			if(data[x][y].state/10==0&&count1==4){
				data[x][y].state=data[x][y].state+20;
				mark.remove(data[x][y]);
			}
			//生成4连特效块
			if(isMark){
				mark.remove(data[x][y]);
			}
		}
		else{			//横行不足3个
			boolean isMark = false;
			mark = new ArrayList<Map>();		//横行不足3个	将刚才的加入的标记块清空
			for(int i = 1;y-i >= 0;i++){		//向上检查
				if(BasePlay.isSame(data[x][y-i].state, data[x][y].state)){
					count2++;
					mark.add(data[x][y-i]);
				}
				else{
					break;
				}
			}
			for(int i = 1;y+i < 10;i++){			//向下检查
				if(BasePlay.isSame(data[x][y+i].state, data[x][y].state)){
					count2++;
					mark.add(data[x][y+i]);
				}
				else{
					break;
				}
			}
			if(count2 >= 3){			//竖向大于等于3时
				mark.add(data[x][y]);		//将本身也加入mark
				int[] count4 = new int[count2];		//声明一个count2大小的数组计数
				//对刚刚纵向检查的每一个进行横向检查
				for(int i = 0;i < count2;i++){
					Map temp = mark.get(i);		//暂时变量
					count4[i] = 1;		//初始化计数器
					for(int j = 1;temp.x -j >= 0;j++){		//向左检查
						if(BasePlay.isSame(data[temp.x-j][temp.y].state, temp.state)){
							count4[i]++;
							mark.add(data[temp.x - j][temp.y]);
						}
						else{
							break;
						}
					}//向左检查for结束
					for(int j = 1;temp.x + j < 8;j++){
						if(BasePlay.isSame(data[temp.x+j][temp.y].state, temp.state)){
							count4[i]++;
							mark.add(data[temp.x + j][temp.y]);
						}
						else{
							break;
						}
					}//向右检查for结束
					
					if(count4[i]<3){
						for(int k = 0;k < count4[i]-1;k++){		//因为count4[i]初值为1	所以多减去1
							mark.remove(mark.size()-1);
						}
					}
					
					if(!isMark){
						if((count2>=5||count4[i]>=5)){
							//原位置已有特效块就不生成新的特效块
							if(data[x][y].state/10==0){
								data[x][y].state=data[x][y].state+40;
							}
							isMark=true;
						}//生成五连特效块
						else if(count4[i] >= 3){
							
							if(data[x][y].state/10==0){
								data[x][y].state=data[x][y].state+30;
							}
							isMark=true;
						}//生成十字特效块
					}
				}//对每一个横向检查的for结束
				if(data[x][y].state/10==0&&count2==4){
					data[x][y].state=data[x][y].state+10;
					mark.remove(data[x][y]);
				}//生成4连特效块
				
				if(isMark){
					mark.remove(data[x][y]);
				}
			}
			else{			//竖向也不足3
				mark = new ArrayList<Map>();		//清空加入的标记块
			}
		}//if-else结束
		return mark;
	}//方法checkEach结束
	
	private static boolean isSame(int state1, int state2) {
		
		if(state1 == state2||state1%10==state2||state1==state2%10||state1%10==state2%10)
			return true;
		else
			return false;
	}
	
	private static boolean isSame(Map a,Map b){
		
		if(a.state == b.state||a.state%10==b.state||a.state==b.state%10||a.state%10==b.state%10)
			return true;
		else
			return false;
	}

	ArrayList<Map> checkExchange(int x1,int y1,int x2, int y2,Map[][] data, ArrayList<Map> stars, ArrayList<Integer> starsState){
		
		ArrayList<Map> mark = new ArrayList<Map>();
		
		
		//		if(data[x1][y1].state/10==0)
		//			data[x1][y1].state=data[x1][y1].state+20;
		//		if(data[x2][y2].state/10==0)
		//			data[x2][y2].state=data[x2][y2].state+20;	
		//双特效大量测试代码，删掉
		//双特效大量测试代码，删掉
		
		if(data[x1][y1].state>40&&data[x2][y2].state>40){
			for(int i=0;i<8;i++){
				for(int j=0;j<10;j++){
					mark.add(data[i][j]);
				}
			}
		}//两个五连特效交换,全部消除
		
		/*
		 * else if((data[x1][y1].state>40&&data[x2][y2].state>30)||(data[x1][y1].state>30&&data[x2][y2].state>40)){
			
			int tempState = 0;
			if(data[x1][y1].state>data[x2][y2].state){
				tempState=data[x2][y2].state;
				data[x1][y1].state=0;
				mark.add(data[x1][y1]);
			}
			else{
				tempState=data[x1][y1].state;
				data[x2][y2].state=0;
				mark.add(data[x2][y2]);
			}
			
			for(int i=0;i<8;i++){
				for(int j=0;j<10;j++){
					if(data[i][j].state%10==tempState%10){
						
						if(data[i][j].state/10==0)
							data[i][j].state+=30;
						else
							data[i][j].state=(data[i][j].state%10)+30;
						
						mark.add(data[i][j]);
					}
				}
			}		
		}//一个五连+9宫格交换，同类水果全部变为9宫格爆炸
		else if((data[x1][y1].state>40&&data[x2][y2].state>10)||(data[x1][y1].state>10&&data[x2][y2].state>40)){
			
			int tempState = 0;
			if(data[x1][y1].state>data[x2][y2].state){
				tempState=data[x2][y2].state;
				data[x1][y1].state=0;
				mark.add(data[x1][y1]);
			}
			else{
				tempState=data[x1][y1].state;
				data[x2][y2].state=0;
				mark.add(data[x2][y2]);
			}
			
			int nextState = 10;
			for(int i=0;i<8;i++){
				for(int j=0;j<10;j++){
					if(data[i][j].state%10==tempState%10){
						
						if(nextState==10)
							nextState=20;
						else
							nextState=10;
						
						if(data[i][j].state/10==0)
							data[i][j].state+=nextState;
						else
							data[i][j].state=(data[i][j].state%10)+nextState;
						
						mark.add(data[i][j]);
					}					
				}
			}		
		}//一个五连+横竖向特效交换，同类水果全部变为横竖向爆炸
		*/
		else if(data[x1][y1].state>40||data[x2][y2].state>40){
			
			int tempState = 0;
			if(data[x1][y1].state>40){
				tempState=data[x2][y2].state;
				
				stars.add(data[x1][y1]);				//5连特效块,记录
				starsState.add(data[x1][y1].state);
				
				data[x1][y1].state=0;
				mark.add(data[x1][y1]);
			}
			else{
				tempState=data[x1][y1].state;
				
				stars.add(data[x2][y2]);				//5连特效块,记录
				starsState.add(data[x2][y2].state);

				data[x2][y2].state=0;
				mark.add(data[x2][y2]);
			}
			
			//避免棒棒糖被消除
			if(tempState==7||tempState==8||tempState==9){}
			else{
				for(int i=0;i<8;i++){
					for(int j=0;j<10;j++){
						if(data[i][j].state%10==tempState%10){
								mark.add(data[i][j]);
						}					
					}
				}
			}
			
		}//五连+其他，同类全部消除
		else if(data[x1][y1].state>30&&data[x2][y2].state>30){
			
			int state1=data[x1][y1].state%10;
			int state2=data[x2][y2].state%10;
			
			stars.add(data[x1][y1]);				//特效块,记录
			starsState.add(data[x1][y1].state);

			stars.add(data[x2][y2]);				//特效块,记录
			starsState.add(data[x2][y2].state);

			data[x1][y1].state=0;
			data[x2][y2].state=0;
			
			mark.add(data[x1][y1]);
			mark.add(data[x2][y2]);
			
			for(int i=0;i<8;i++){
				for(int j=0;j<10;j++){
					if(data[i][j].state%10==state1||data[i][j].state%10==state2){
							mark.add(data[i][j]);
					}					
				}
			}		
		}//两个九宫格，选中的两个水果都炸
		else if((data[x1][y1].state>30&&data[x2][y2].state>20)||(data[x1][y1].state>20&&data[x2][y2].state>30)){
			
			stars.add(data[x1][y1]);				//特效块,记录
			starsState.add(data[x1][y1].state);

			stars.add(data[x2][y2]);				//特效块,记录
			starsState.add(data[x2][y2].state);

			data[x1][y1].state=0;
			data[x2][y2].state=0;
			
			int bigger = 0;
			int smaller = 0;
			if(x1>x2){
				bigger=x1;
				smaller=x2;
			}
			else{
				bigger=x2;
				smaller=x1;
			}
			
			if(bigger+1<8)
				for(int i=0;i<10;i++){
					mark.add(data[bigger+1][i]);
				}
			
			if(smaller-1>=0)
				for(int i=0;i<10;i++){
					mark.add(data[smaller-1][i]);
				}
			
			for(int i=0;i<10;i++){
				mark.add(data[x1][i]);
			}
			
			for(int i=0;i<10;i++){
				mark.add(data[x2][i]);
			}
			
		}//一个九宫格+竖向全消，4排竖向爆炸
		else if((data[x1][y1].state>30&&data[x2][y2].state>10)||(data[x1][y1].state>10&&data[x2][y2].state>30)){
			
			stars.add(data[x1][y1]);				//特效块,记录
			starsState.add(data[x1][y1].state);

			stars.add(data[x2][y2]);				//特效块,记录
			starsState.add(data[x2][y2].state);

			data[x1][y1].state=0;
			data[x2][y2].state=0;
			
			int bigger = 0;
			int smaller = 0;
			if(y1>y2){
				bigger=y1;
				smaller=y2;
			}
			else{
				bigger=y2;
				smaller=y1;
			}
			
			if(bigger+1<10)
				for(int i=0;i<8;i++){
					mark.add(data[i][bigger+1]);
				}
			
			if(smaller-1>=0){
				for(int i=0;i<8;i++){
					mark.add(data[i][smaller-1]);
				}
			}
			
			for(int i=0;i<8;i++){
				mark.add(data[i][y1]);
			}
			for(int i=0;i<8;i++){
				mark.add(data[i][y2]);
			}		
		}//一个九宫格+横向全消，4排横向爆炸
		else if(data[x1][y1].state>10&&data[x2][y2].state>10){
			
			stars.add(data[x1][y1]);				//特效块,记录
			starsState.add(data[x1][y1].state);

			stars.add(data[x2][y2]);				//特效块,记录
			starsState.add(data[x2][y2].state);

			data[x1][y1].state=0;
			data[x2][y2].state=0;
			
			for(int i=0;i<8;i++){
				mark.add(data[i][y1]);
			}
			
			for(int i=0;i<10;i++){
				if(!mark.contains(data[x1][i]))
					mark.add(data[x1][i]);
			}
			
		}//横竖向4连交换，十字消除
		else{
			mark = checkEach(x1, y1, data);
			if(!mark.contains(data[x2][y2])){			//如果交换的是两个相同的就不用检查下一个
				ArrayList<Map> newMark = checkEach(x2, y2, data);
				for(Map temp : newMark){		//合并两个
					mark.add(temp);
				}
			}
		}//普通交换
		//加入检查双特效的代码
		//加入检查双特效的代码
		//加入检查双特效的代码
		//加入检查双特效的代码
		//加入检查双特效的代码
		return mark;
		
	}//方法checkExchange结束
			
	ArrayList<Integer> isStarRemove(ArrayList<Map> mark, Map[][] data, ArrayList<Map> retMap, ArrayList<Integer> ret){
		
		int originalSize = mark.size();
		ArrayList<Map> trans = new ArrayList<Map>(mark);
		
		for(Map temp : mark){
			
			if (temp.state>40){		//特殊块
						
				int state =(int)(Math.random()*6)+1;
				for(int i=0;i<8;i++){
					for(int j=0;j<10;j++){
						if(!(mark.contains(data[i][j]))&&data[i][j].state==state)
							trans.add(data[i][j]);
					}
				}
				
				ret.add(data[temp.x][temp.y].state);
				retMap.add(data[temp.x][temp.y]);
				data[temp.x][temp.y].state=0;
			}//随机挑一类水果全部消除
			else if(temp.state>30){
				
				if(temp.y-2>=0){
					if(!mark.contains(data[temp.x][temp.y-2]))
						trans.add(data[temp.x][temp.y-2]);
				}
				
				if(temp.y-1>=0){
					if(!mark.contains(data[temp.x][temp.y-1]))
						trans.add(data[temp.x][temp.y-1]);
					if(temp.x-1>=0)
						if(!mark.contains(data[temp.x-1][temp.y-1]))
							trans.add(data[temp.x-1][temp.y-1]);
					if(temp.x+1<8)
						if(!mark.contains(data[temp.x+1][temp.y-1]))
							trans.add(data[temp.x+1][temp.y-1]);
				}
				
				if(temp.y+2<10){
					if(!mark.contains(data[temp.x][temp.y+2]))
						trans.add(data[temp.x][temp.y+2]);
				}
				
				if(temp.y+1<10){
					if(!mark.contains(data[temp.x][temp.y+1]))
						trans.add(data[temp.x][temp.y+1]);
					if(temp.x-1>=0)
						if(!mark.contains(data[temp.x-1][temp.y+1]))
							trans.add(data[temp.x-1][temp.y+1]);
					if(temp.x+1<8)
						if(!mark.contains(data[temp.x+1][temp.y+1]))
							trans.add(data[temp.x+1][temp.y+1]);		
				}
				
				if(temp.x-2>=0){
					if(!mark.contains(data[temp.x-2][temp.y]))
						trans.add(data[temp.x-2][temp.y]);
				}
				
				if(temp.x-1>=0){
					if(!mark.contains(data[temp.x-1][temp.y]))
						trans.add(data[temp.x-1][temp.y]);
				}
				
				if(temp.x+2<8){
					if(!mark.contains(data[temp.x+2][temp.y]))
						trans.add(data[temp.x+2][temp.y]);
				}
				
				if(temp.x+1<8){
					if(!mark.contains(data[temp.x+1][temp.y]))
						trans.add(data[temp.x+1][temp.y]);
				}
				/*for(int i=temp.x-1;i<=temp.x+1;i++){
					for(int j=temp.y-1;j<=temp.y+1;j++){
						if(!(mark.contains(data[i][j])))
							trans.add(data[i][j]);						
					}
				}
				*/
				ret.add(data[temp.x][temp.y].state);
				retMap.add(data[temp.x][temp.y]);
				data[temp.x][temp.y].state=0;
			}//九宫格爆炸
			else if(temp.state>20){
				for(int i =0;i<10;i++){
					if(!(mark.contains(data[temp.x][i])))
						trans.add(data[temp.x][i]);
				}
				ret.add(data[temp.x][temp.y].state);
				retMap.add(data[temp.x][temp.y]);
				data[temp.x][temp.y].state=0;
			}//竖向一条
			else if(temp.state>10){
				for(int i =0;i<8;i++){
					if(!(mark.contains(data[i][temp.y])))
						trans.add(data[i][temp.y]);
				}
				ret.add(data[temp.x][temp.y].state);
				retMap.add(data[temp.x][temp.y]);
				data[temp.x][temp.y].state=0;
			}//横向一条
		}
		
		if(originalSize==trans.size())
			return ret;
		else 
			for(int i=originalSize;i<trans.size();i++){
				mark.add(trans.get(i));
			}
			return isStarRemove(mark,data,retMap,ret);
	}
		
	Map[][] remove(ArrayList<Map> mark, Map[][] data){
		
		//首先去掉棒棒糖
		for(int i=0;i<mark.size();i++){
			if(mark.get(i).state==7||mark.get(i).state==8||mark.get(i).state==9)
				mark.remove(mark.get(i));
		}

		int originalSize = mark.size();
		ArrayList<Map> trans = new ArrayList<Map>(mark);
		
		for(Map temp : mark){
			if(temp.state / 10==0){		//判断是否是普通块
				data[temp.x][temp.y].state = 0;		//是普通块就状态置为0空
			}
			else if (temp.state>40){		//特殊块
						
				int state =(int)(Math.random()*6)+1;
				for(int i=0;i<8;i++){
					for(int j=0;j<10;j++){
						if(!(mark.contains(data[i][j]))&&data[i][j].state==state)
							trans.add(data[i][j]);
					}
				}	
				data[temp.x][temp.y].state = 0;
			}//随机挑一类水果全部消除
			else if(temp.state>30){
				
				if(temp.y-2>=0){
					if(!mark.contains(data[temp.x][temp.y-2]))
						trans.add(data[temp.x][temp.y-2]);
				}
				
				if(temp.y-1>=0){
					if(!mark.contains(data[temp.x][temp.y-1]))
						trans.add(data[temp.x][temp.y-1]);
					if(temp.x-1>=0)
						if(!mark.contains(data[temp.x-1][temp.y-1]))
							trans.add(data[temp.x-1][temp.y-1]);
					if(temp.x+1<8)
						if(!mark.contains(data[temp.x+1][temp.y-1]))
							trans.add(data[temp.x+1][temp.y-1]);
				}
				
				if(temp.y+2<10){
					if(!mark.contains(data[temp.x][temp.y+2]))
						trans.add(data[temp.x][temp.y+2]);
				}
				
				if(temp.y+1<10){
					if(!mark.contains(data[temp.x][temp.y+1]))
						trans.add(data[temp.x][temp.y+1]);
					if(temp.x-1>=0)
						if(!mark.contains(data[temp.x-1][temp.y+1]))
							trans.add(data[temp.x-1][temp.y+1]);
					if(temp.x+1<8)
						if(!mark.contains(data[temp.x+1][temp.y+1]))
							trans.add(data[temp.x+1][temp.y+1]);		
				}
				
				if(temp.x-2>=0){
					if(!mark.contains(data[temp.x-2][temp.y]))
						trans.add(data[temp.x-2][temp.y]);
				}
				
				if(temp.x-1>=0){
					if(!mark.contains(data[temp.x-1][temp.y]))
						trans.add(data[temp.x-1][temp.y]);
				}
				
				if(temp.x+2<8){
					if(!mark.contains(data[temp.x+2][temp.y]))
						trans.add(data[temp.x+2][temp.y]);
				}
				
				if(temp.x+1<8){
					if(!mark.contains(data[temp.x+1][temp.y]))
						trans.add(data[temp.x+1][temp.y]);
				}
				/*for(int i=temp.x-1;i<=temp.x+1;i++){
					for(int j=temp.y-1;j<=temp.y+1;j++){
						if(!(mark.contains(data[i][j])))
							trans.add(data[i][j]);						
					}
				}
				*/
				data[temp.x][temp.y].state = 0;
			}//九宫格爆炸
			else if(temp.state>20){
				for(int i =0;i<10;i++){
					if(!(mark.contains(data[temp.x][i])))
						trans.add(data[temp.x][i]);
				}
				data[temp.x][temp.y].state = 0;
			}//竖向一条
			else if(temp.state>10){
				for(int i =0;i<8;i++){
					if(!(mark.contains(data[i][temp.y])))
						trans.add(data[i][temp.y]);
				}
				data[temp.x][temp.y].state = 0;
			}//横向一条
		}
		
		if(originalSize==trans.size())
			return data;
		else 
			for(int i=originalSize;i<trans.size();i++){
				mark.add(trans.get(i));
			}
			return remove(mark,data);//递归检查新加入的块是否有特效块
	}

	//掉落之后检查是否有需要移动的
	ArrayList<Map> checkAfterMove(boolean[][] hadMoved, Map[][] data){
		ArrayList<Map> mark = new ArrayList<Map>();
		for(int row = 9;row >= 0;row--){		//从下到上检查
			for(int col = 0;col < 8;col++){		//从左到右
				if(hadMoved[col][row]){			//如果这个位置被移动过
					if(mark.contains(data[col][row])){		//如果包含这个位置说明之前的检查已经对该个位置生效
						continue;
					}
					ArrayList<Map> newMark = checkEach(data[col][row].x, data[col][row].y, data);
					for(Map temp2 : newMark){		//加入到mark中
						mark.add(temp2);
					}
				}
			}
		}//完全检查完
		return mark;
	}
}//类BasePlay结束
