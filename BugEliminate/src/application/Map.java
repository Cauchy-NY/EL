package application;

public class Map {
	int x;
	int y;
	int state = 0;			//����״̬��Ĭ��Ϊ��
	//4x�����,3x����,2x����,1x����
	boolean isShow = true;			//�Ƿ���ʾ
	boolean isSelected = false;		//�����ǻ��ѡ�У�Ĭ��δ��ѡ��
	boolean willBeMoved = false;		//�����Ƿ���Ҫ���ƶ���״̬��Ĭ��Ϊ����
	
	public Map(int x,int y){
		this.x = x;
		this.y = y;
	}
}
