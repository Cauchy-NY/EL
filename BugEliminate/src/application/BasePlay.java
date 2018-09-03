package application;

import java.util.ArrayList;

public class BasePlay {
	
	private ArrayList<Map> checkEach(int x, int y, Map[][] data){
		ArrayList<Map> mark = new ArrayList<Map>();
		int count1 = 1;		//���������
		int count2 = 1;		//���������
		
		for(int i = 1; x-i >= 0;i++){		//��������
			if(BasePlay.isSame(data[x-i][y], data[x][y])){
				count1++;
				mark.add(data[x-i][y]);
			}
			else{		//������������ִ��֮������Ҽ��
				break;
			}
		}
		for(int i = 1;x+i < 8;i++){		//���Ҽ��
			if(BasePlay.isSame(data[x+i][y], data[x][y])){
				count1++;
				mark.add(data[x+i][y]);
			}
			else{
				break;
			}
		}
		
		if(count1 >= 3){			//��������ڵ���3��ʱ
			mark.add(data[x][y]);		//������Ҳ����mark
			
			boolean isMark = false;
			int[] count3 = new int[count1];			//����һ��count1��С��������������
			//�Ըոպ���������ǵ�ÿһ��������������
			
			for(int i = 0;i < count1;i++){
				Map temp = mark.get(i);			//��ʱ����	�����ʾ
				count3[i] = 1;		//��ʼ��������
				for(int j = 1;temp.y - j >= 0 ;j++){		//����
					if(BasePlay.isSame(data[temp.x][temp.y - j].state, temp.state)){
						count3[i]++;
						mark.add(data[temp.x][temp.y - j]);
					}
					else{
						break;
					}
				}//���ϼ���for����
				for(int j = 1;temp.y + j < 10;j++){
					if(BasePlay.isSame(data[temp.x][temp.y + j].state, temp.state)){
						count3[i]++;
						mark.add(data[temp.x][temp.y + j]);
					}
					else{
						break;
					}
				}//���¼���for����
				
				
				//��Ч��ļ��
				
				if(count3[i]<3)
					for(int k = 0;k < count3[i]-1;k++){		
						mark.remove(mark.size()-1);
					}
				//count3[i]С��3ʱ
				//���ղŶ����mark���Ƴ�
				//��Ϊcount3[i]��ֵΪ1	���Զ��ȥ1
				
				if(!isMark){
					if((count1>=5||count3[i]>=5)){
						//ԭλ��������Ч��Ͳ������µ���Ч��
						if(data[x][y].state/10==0){
							data[x][y].state=data[x][y].state+40;
						}
						isMark = true;
					}//����������Ч��
					else if(count3[i] >= 3){
					
						if(data[x][y].state/10==0){
							data[x][y].state=data[x][y].state+30;
						}
						isMark = true;
					}//����ʮ����Ч��		
				}
			}//��ÿһ�������������for����
			
			if(data[x][y].state/10==0&&count1==4){
				data[x][y].state=data[x][y].state+20;
				mark.remove(data[x][y]);
			}
			//����4����Ч��
			if(isMark){
				mark.remove(data[x][y]);
			}
		}
		else{			//���в���3��
			boolean isMark = false;
			mark = new ArrayList<Map>();		//���в���3��	���ղŵļ���ı�ǿ����
			for(int i = 1;y-i >= 0;i++){		//���ϼ��
				if(BasePlay.isSame(data[x][y-i].state, data[x][y].state)){
					count2++;
					mark.add(data[x][y-i]);
				}
				else{
					break;
				}
			}
			for(int i = 1;y+i < 10;i++){			//���¼��
				if(BasePlay.isSame(data[x][y+i].state, data[x][y].state)){
					count2++;
					mark.add(data[x][y+i]);
				}
				else{
					break;
				}
			}
			if(count2 >= 3){			//������ڵ���3ʱ
				mark.add(data[x][y]);		//������Ҳ����mark
				int[] count4 = new int[count2];		//����һ��count2��С���������
				//�Ըո��������ÿһ�����к�����
				for(int i = 0;i < count2;i++){
					Map temp = mark.get(i);		//��ʱ����
					count4[i] = 1;		//��ʼ��������
					for(int j = 1;temp.x -j >= 0;j++){		//������
						if(BasePlay.isSame(data[temp.x-j][temp.y].state, temp.state)){
							count4[i]++;
							mark.add(data[temp.x - j][temp.y]);
						}
						else{
							break;
						}
					}//������for����
					for(int j = 1;temp.x + j < 8;j++){
						if(BasePlay.isSame(data[temp.x+j][temp.y].state, temp.state)){
							count4[i]++;
							mark.add(data[temp.x + j][temp.y]);
						}
						else{
							break;
						}
					}//���Ҽ��for����
					
					if(count4[i]<3){
						for(int k = 0;k < count4[i]-1;k++){		//��Ϊcount4[i]��ֵΪ1	���Զ��ȥ1
							mark.remove(mark.size()-1);
						}
					}
					
					if(!isMark){
						if((count2>=5||count4[i]>=5)){
							//ԭλ��������Ч��Ͳ������µ���Ч��
							if(data[x][y].state/10==0){
								data[x][y].state=data[x][y].state+40;
							}
							isMark=true;
						}//����������Ч��
						else if(count4[i] >= 3){
							
							if(data[x][y].state/10==0){
								data[x][y].state=data[x][y].state+30;
							}
							isMark=true;
						}//����ʮ����Ч��
					}
				}//��ÿһ���������for����
				if(data[x][y].state/10==0&&count2==4){
					data[x][y].state=data[x][y].state+10;
					mark.remove(data[x][y]);
				}//����4����Ч��
				
				if(isMark){
					mark.remove(data[x][y]);
				}
			}
			else{			//����Ҳ����3
				mark = new ArrayList<Map>();		//��ռ���ı�ǿ�
			}
		}//if-else����
		return mark;
	}//����checkEach����
	
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
		//˫��Ч�������Դ��룬ɾ��
		//˫��Ч�������Դ��룬ɾ��
		
		if(data[x1][y1].state>40&&data[x2][y2].state>40){
			for(int i=0;i<8;i++){
				for(int j=0;j<10;j++){
					mark.add(data[i][j]);
				}
			}
		}//����������Ч����,ȫ������
		
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
		}//һ������+9���񽻻���ͬ��ˮ��ȫ����Ϊ9����ը
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
		}//һ������+��������Ч������ͬ��ˮ��ȫ����Ϊ������ը
		*/
		else if(data[x1][y1].state>40||data[x2][y2].state>40){
			
			int tempState = 0;
			if(data[x1][y1].state>40){
				tempState=data[x2][y2].state;
				
				stars.add(data[x1][y1]);				//5����Ч��,��¼
				starsState.add(data[x1][y1].state);
				
				data[x1][y1].state=0;
				mark.add(data[x1][y1]);
			}
			else{
				tempState=data[x1][y1].state;
				
				stars.add(data[x2][y2]);				//5����Ч��,��¼
				starsState.add(data[x2][y2].state);

				data[x2][y2].state=0;
				mark.add(data[x2][y2]);
			}
			
			//��������Ǳ�����
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
			
		}//����+������ͬ��ȫ������
		else if(data[x1][y1].state>30&&data[x2][y2].state>30){
			
			int state1=data[x1][y1].state%10;
			int state2=data[x2][y2].state%10;
			
			stars.add(data[x1][y1]);				//��Ч��,��¼
			starsState.add(data[x1][y1].state);

			stars.add(data[x2][y2]);				//��Ч��,��¼
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
		}//�����Ź���ѡ�е�����ˮ����ը
		else if((data[x1][y1].state>30&&data[x2][y2].state>20)||(data[x1][y1].state>20&&data[x2][y2].state>30)){
			
			stars.add(data[x1][y1]);				//��Ч��,��¼
			starsState.add(data[x1][y1].state);

			stars.add(data[x2][y2]);				//��Ч��,��¼
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
			
		}//һ���Ź���+����ȫ����4������ը
		else if((data[x1][y1].state>30&&data[x2][y2].state>10)||(data[x1][y1].state>10&&data[x2][y2].state>30)){
			
			stars.add(data[x1][y1]);				//��Ч��,��¼
			starsState.add(data[x1][y1].state);

			stars.add(data[x2][y2]);				//��Ч��,��¼
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
		}//һ���Ź���+����ȫ����4�ź���ը
		else if(data[x1][y1].state>10&&data[x2][y2].state>10){
			
			stars.add(data[x1][y1]);				//��Ч��,��¼
			starsState.add(data[x1][y1].state);

			stars.add(data[x2][y2]);				//��Ч��,��¼
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
			
		}//������4��������ʮ������
		else{
			mark = checkEach(x1, y1, data);
			if(!mark.contains(data[x2][y2])){			//�����������������ͬ�ľͲ��ü����һ��
				ArrayList<Map> newMark = checkEach(x2, y2, data);
				for(Map temp : newMark){		//�ϲ�����
					mark.add(temp);
				}
			}
		}//��ͨ����
		//������˫��Ч�Ĵ���
		//������˫��Ч�Ĵ���
		//������˫��Ч�Ĵ���
		//������˫��Ч�Ĵ���
		//������˫��Ч�Ĵ���
		return mark;
		
	}//����checkExchange����
			
	ArrayList<Integer> isStarRemove(ArrayList<Map> mark, Map[][] data, ArrayList<Map> retMap, ArrayList<Integer> ret){
		
		int originalSize = mark.size();
		ArrayList<Map> trans = new ArrayList<Map>(mark);
		
		for(Map temp : mark){
			
			if (temp.state>40){		//�����
						
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
			}//�����һ��ˮ��ȫ������
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
			}//�Ź���ը
			else if(temp.state>20){
				for(int i =0;i<10;i++){
					if(!(mark.contains(data[temp.x][i])))
						trans.add(data[temp.x][i]);
				}
				ret.add(data[temp.x][temp.y].state);
				retMap.add(data[temp.x][temp.y]);
				data[temp.x][temp.y].state=0;
			}//����һ��
			else if(temp.state>10){
				for(int i =0;i<8;i++){
					if(!(mark.contains(data[i][temp.y])))
						trans.add(data[i][temp.y]);
				}
				ret.add(data[temp.x][temp.y].state);
				retMap.add(data[temp.x][temp.y]);
				data[temp.x][temp.y].state=0;
			}//����һ��
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
		
		//����ȥ��������
		for(int i=0;i<mark.size();i++){
			if(mark.get(i).state==7||mark.get(i).state==8||mark.get(i).state==9)
				mark.remove(mark.get(i));
		}

		int originalSize = mark.size();
		ArrayList<Map> trans = new ArrayList<Map>(mark);
		
		for(Map temp : mark){
			if(temp.state / 10==0){		//�ж��Ƿ�����ͨ��
				data[temp.x][temp.y].state = 0;		//����ͨ���״̬��Ϊ0��
			}
			else if (temp.state>40){		//�����
						
				int state =(int)(Math.random()*6)+1;
				for(int i=0;i<8;i++){
					for(int j=0;j<10;j++){
						if(!(mark.contains(data[i][j]))&&data[i][j].state==state)
							trans.add(data[i][j]);
					}
				}	
				data[temp.x][temp.y].state = 0;
			}//�����һ��ˮ��ȫ������
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
			}//�Ź���ը
			else if(temp.state>20){
				for(int i =0;i<10;i++){
					if(!(mark.contains(data[temp.x][i])))
						trans.add(data[temp.x][i]);
				}
				data[temp.x][temp.y].state = 0;
			}//����һ��
			else if(temp.state>10){
				for(int i =0;i<8;i++){
					if(!(mark.contains(data[i][temp.y])))
						trans.add(data[i][temp.y]);
				}
				data[temp.x][temp.y].state = 0;
			}//����һ��
		}
		
		if(originalSize==trans.size())
			return data;
		else 
			for(int i=originalSize;i<trans.size();i++){
				mark.add(trans.get(i));
			}
			return remove(mark,data);//�ݹ����¼���Ŀ��Ƿ�����Ч��
	}

	//����֮�����Ƿ�����Ҫ�ƶ���
	ArrayList<Map> checkAfterMove(boolean[][] hadMoved, Map[][] data){
		ArrayList<Map> mark = new ArrayList<Map>();
		for(int row = 9;row >= 0;row--){		//���µ��ϼ��
			for(int col = 0;col < 8;col++){		//������
				if(hadMoved[col][row]){			//������λ�ñ��ƶ���
					if(mark.contains(data[col][row])){		//����������λ��˵��֮ǰ�ļ���Ѿ��Ըø�λ����Ч
						continue;
					}
					ArrayList<Map> newMark = checkEach(data[col][row].x, data[col][row].y, data);
					for(Map temp2 : newMark){		//���뵽mark��
						mark.add(temp2);
					}
				}
			}
		}//��ȫ�����
		return mark;
	}
}//��BasePlay����
