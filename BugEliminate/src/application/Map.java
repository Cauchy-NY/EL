package application;

public class Map {
	int x;
	int y;
	int state = 0;			//保存状态，默认为空
	//4x随机消,3x菱形,2x竖向,1x横向
	boolean isShow = true;			//是否显示
	boolean isSelected = false;		//保存是或否被选中，默认未被选中
	boolean willBeMoved = false;		//保存是否需要被移动的状态，默认为不需
	
	public Map(int x,int y){
		this.x = x;
		this.y = y;
	}
}
