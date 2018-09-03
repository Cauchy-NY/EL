package application;

import java.util.*;
import java.io.*;

public class Player implements Serializable{
	
	String name = "";  //�������
	int lastGame = 11;  //���ͨ�عؿ����ڼ��µڼ���,Ĭ���ڵ�һ�µ�һ��
	int[] topGrades;  //��ҵļ�����߷�
	final int NUM_OF_GREAT_GRADES = 10;  //��ҵ���߷�����
	ArrayList<Integer> grades; //��ҷ���
	
	public Player(String name) {
		this.name = name;
		this.grades = new ArrayList<Integer>();
		this.topGrades = new int[NUM_OF_GREAT_GRADES];
	}
	
	//���÷���
	public void setGrade(int grade) {
		grades.add(grade);
	}
	
	//��ȡ�������
	public String getName() {
		return this.name;
	}
	
	//��������������λ
	public int[] getTopGrades() {
		Collections.sort(grades);
		if(grades.size() >= 10) {
			for(int i = 0; i < NUM_OF_GREAT_GRADES; i++) {
				topGrades[i] = grades.get(grades.size() - i - 1);
			}
		} else {
			for(int i = 0; i < grades.size(); i++) {
				topGrades[i] = grades.get(grades.size() - i - 1);
			}
		}
		return topGrades;
	}
	
	//�����������Ĺؿ�
	public void setLastGame(int num) {
		this.lastGame = num;
	}

}
