package application;

import java.util.*;
import java.io.*;

public class Player implements Serializable{
	
	String name = "";  //玩家姓名
	int lastGame = 11;  //最后通关关卡，第几章第几节,默认在第一章第一关
	int[] topGrades;  //玩家的几个最高分
	final int NUM_OF_GREAT_GRADES = 10;  //玩家的最高分数量
	ArrayList<Integer> grades; //玩家分数
	
	public Player(String name) {
		this.name = name;
		this.grades = new ArrayList<Integer>();
		this.topGrades = new int[NUM_OF_GREAT_GRADES];
	}
	
	//设置分数
	public void setGrade(int grade) {
		grades.add(grade);
	}
	
	//获取玩家名称
	public String getName() {
		return this.name;
	}
	
	//最大数在数组最低位
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
	
	//设置最后解锁的关卡
	public void setLastGame(int num) {
		this.lastGame = num;
	}

}
