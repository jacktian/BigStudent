package com.jov.common;

import java.util.HashMap;
import java.util.Map;

public final class FemaleSwitch {
	 public static Map<String,Integer> switchToStandard(Map<String,Integer> map){
		 Map<String,Integer> factorMap = new HashMap<String,Integer>();
		 if(map.get("A")!=null){
			 int factor = map.get("A");
			 int standard = 1;
			 if(factor>=0 && factor<=1)
				 standard = 1;
	 		 else if(factor>=2 && factor<=3)
	 			standard = 2;
	 		 else if(factor>=4 && factor<=5)
	 			standard = 3;
	 		 else if(factor==6)
	 			standard = 4;
	 		 else if(factor>=7 && factor<=8)
	 			standard = 5;
	 		 else if(factor>=9 && factor<=11)
	 			standard = 6;
	 		 else if(factor>=12 && factor<=13)
	 			standard = 7;
	 		 else if(factor==14)
	 			standard = 8;
	 		 else if(factor>=15 && factor<=16)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("A", standard);
		 }else{
			 factorMap.put("A", 1);
		 }
		 if(map.get("B")!=null){
			 int factor = map.get("B");
			 int standard = 1;
			 if(factor>=0 && factor<=3)
				 standard = 1;
	 		 else if(factor==4)
	 			standard = 2;
	 		 else if(factor==5)
	 			standard = 3;
	 		 else if(factor==6)
	 			standard = 4;
	 		 else if(factor==7)
	 			standard = 5;
	 		 else if(factor==8)
	 			standard = 6;
	 		 else if(factor==9)
	 			standard = 7;
	 		 else if(factor==10)
	 			standard = 8;
	 		 else if(factor==11)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("B", standard);
		 }else{
			 factorMap.put("B", 1);
		 }
		 if(map.get("C")!=null){
			 int factor = map.get("C");
			 int standard = 1;
			 if(factor>=0 && factor<=5)
				 standard = 1;
	 		 else if(factor>=6 && factor<=7)
	 			standard = 2;
	 		 else if(factor>=8&& factor<=9)
	 			standard = 3;
	 		 else if(factor>=10 && factor<=11)
	 			standard = 4;
	 		 else if(factor>=12 && factor<=13)
	 			standard = 5;
	 		 else if(factor>=14 && factor<=16)
	 			standard = 6;
	 		 else if(factor>=17 && factor<=18)
	 			standard = 7;
	 		 else if(factor>=19 && factor<=20)
	 			standard = 8;
	 		 else if(factor>=21 && factor<=22)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("C", standard);
		 }else{
			 factorMap.put("C", 1);
		 }
		 if(map.get("E")!=null){
			 int factor = map.get("E");
			 int standard = 1;
			 if(factor>=0 && factor<=2)
				 standard = 1;
	 		 else if(factor>=3 && factor<=4)
	 			standard = 2;
	 		 else if(factor==5)
	 			standard = 3;
	 		 else if(factor>=6 && factor<=7)
	 			standard = 4;
	 		 else if(factor>=8 && factor<=9)
	 			standard = 5;
	 		 else if(factor>=10 && factor<=12)
	 			standard = 6;
	 		 else if(factor>=13 && factor<=14)
	 			standard = 7;
	 		 else if(factor==15)
	 			standard = 8;
	 		 else if(factor>=16 && factor<=17)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("E", standard);
		 }else{
			 factorMap.put("E", 1);
		 }
		 if(map.get("F")!=null){
			 int factor = map.get("F");
			 int standard = 1;
			 if(factor>=0 && factor<=3)
				 standard = 1;
	 		 else if(factor==4)
	 			standard = 2;
	 		 else if(factor>=5 && factor<=6)
	 			standard = 3;
	 		 else if(factor==7)
	 			standard = 4;
	 		 else if(factor>=8 && factor<=9)
	 			standard = 5;
	 		 else if(factor>=10 && factor<=12)
	 			standard = 6;
	 		 else if(factor>=13 && factor<=14)
	 			standard = 7;
	 		 else if(factor>=15 && factor<=16)
	 			standard = 8;
	 		 else if(factor>=17 && factor<=18)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("F", standard);
		 }else{
			 factorMap.put("F", 1);
		 }
		 if(map.get("G")!=null){
			 int factor = map.get("G");
			 int standard = 1;
			 if(factor>=0 && factor<=5)
				 standard = 1;
	 		 else if(factor>=6 && factor<=7)
	 			standard = 2;
	 		 else if(factor>=8 && factor<=9)
	 			standard = 3;
	 		 else if(factor==10)
	 			standard = 4;
	 		 else if(factor>=11 && factor<=12)
	 			standard = 5;
	 		 else if(factor>=13 && factor<=14)
	 			standard = 6;
	 		 else if(factor>=15 && factor<=16)
	 			standard = 7;
	 		 else if(factor==17)
	 			standard = 8;
	 		 else if(factor==18)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("G", standard);
		 }else{
			 factorMap.put("G", 1);
		 }
		 if(map.get("H")!=null){
			 int factor = map.get("H");
			 int standard = 1;
			 if(factor>=0 && factor<=1)
				 standard = 1;
	 		 else if(factor==2)
	 			standard = 2;
	 		 else if(factor==3)
	 			standard = 3;
	 		 else if(factor>=4 && factor<=6)
	 			standard = 4;
	 		 else if(factor>=7 && factor<=8)
	 			standard = 5;
	 		 else if(factor>=9 && factor<=11)
	 			standard = 6;
	 		 else if(factor>=12 && factor<=14)
	 			standard = 7;
	 		 else if(factor>=15 && factor<=16)
	 			standard = 8;
	 		 else if(factor>=17 && factor<=19)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("H", standard);
		 }else{
			 factorMap.put("H", 1);
		 }
		 if(map.get("I")!=null){
			 int factor = map.get("I");
			 int standard = 1;
			 if(factor>=0 && factor<=5)
				 standard = 1;
	 		 else if(factor==6)
	 			standard = 2;
	 		 else if(factor>=7 && factor<=8)
	 			standard = 3;
	 		 else if(factor==9)
	 			standard = 4;
	 		 else if(factor>=10 && factor<=11)
	 			standard = 5;
	 		 else if(factor>=12 && factor<=13)
	 			standard = 6;
	 		 else if(factor==14)
	 			standard = 7;
	 		 else if(factor>=15 && factor<=16)
	 			standard = 8;
	 		 else if(factor==17)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("I", standard);
		 }else{
			 factorMap.put("I", 1);
		 }
		 if(map.get("L")!=null){
			 int factor = map.get("L");
			 int standard = 1;
			 if(factor>=0 && factor<=3)
				 standard = 1;
	 		 else if(factor>=4 && factor<=5)
	 			standard = 2;
	 		 else if(factor==6)
	 			standard = 3;
	 		 else if(factor>=7 && factor<=8)
	 			standard = 4;
	 		 else if(factor>=9 && factor<=10)
	 			standard = 5;
	 		 else if(factor>=11 && factor<=12)
	 			standard = 6;
	 		 else if(factor==13)
	 			standard = 7;
	 		 else if(factor>=14 && factor<=15)
	 			standard = 8;
	 		 else if(factor==16)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("L", standard);
		 }else{
			 factorMap.put("L", 1);
		 }
		 if(map.get("M")!=null){
			 int factor = map.get("M");
			 int standard = 1;
			 if(factor>=0 && factor<=5)
				 standard = 1;
	 		 else if(factor>=6 && factor<=7)
	 			standard = 2;
	 		 else if(factor>=8 && factor<=9)
	 			standard = 3;
	 		 else if(factor>=10 && factor<=11)
	 			standard = 4;
	 		 else if(factor>=12 && factor<=13)
	 			standard = 5;
	 		 else if(factor>=14 && factor<=15)
	 			standard = 6;
	 		 else if(factor>=16 && factor<=17)
	 			standard = 7;
	 		 else if(factor>=18 && factor<=19)
	 			standard = 8;
	 		 else if(factor==20)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("M", standard);
		 }else{
			 factorMap.put("M", 1);
		 }
		 if(map.get("N")!=null){
			 int factor = map.get("N");
			 int standard = 1;
			 if(factor>=0 && factor<=2)
				 standard = 1;
	 		 else if(factor==3)
	 			standard = 2;
	 		 else if(factor==4)
	 			standard = 3;
	 		 else if(factor>=5 && factor<=6)
	 			standard = 4;
	 		 else if(factor>=7 && factor<=8)
	 			standard = 5;
	 		 else if(factor>=9 && factor<=10)
	 			standard = 6;
	 		 else if(factor==11)
	 			standard = 7;
	 		 else if(factor>=12 && factor<=13)
	 			standard = 8;
	 		 else if(factor==14)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("N", standard);
		 }else{
			 factorMap.put("N", 1);
		 }
		 if(map.get("O")!=null){
			 int factor = map.get("O");
			 int standard = 1;
			 if(factor>=0 && factor<=2)
				 standard = 1;
	 		 else if(factor>=3 && factor<=4)
	 			standard = 2;
	 		 else if(factor>=5 && factor<=6)
	 			standard = 3;
	 		 else if(factor>=7 && factor<=8)
	 			standard = 4;
	 		 else if(factor>=9 && factor<=10)
	 			standard = 5;
	 		 else if(factor>=11 && factor<=12)
	 			standard = 6;
	 		 else if(factor>=13 && factor<=14)
	 			standard = 7;
	 		 else if(factor>=15 && factor<=16)
	 			standard = 8;
	 		 else if(factor>=17 && factor<=18)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("O", standard);
		 }else{
			 factorMap.put("O", 1);
		 }
		 if(map.get("Q1")!=null){
			 int factor = map.get("Q1");
			 int standard = 1;
			 if(factor>=0 && factor<=4)
				 standard = 1;
	 		 else if(factor==5)
	 			standard = 2;
	 		 else if(factor>=6&&factor<=7)
	 			standard = 3;
	 		 else if(factor==8)
	 			standard = 4;
	 		 else if(factor>=9 && factor<=10)
	 			standard = 5;
	 		 else if(factor>=11 && factor<=12)
	 			standard = 6;
	 		 else if(factor==13)
	 			standard = 7;
	 		 else if(factor==14)
	 			standard = 8;
	 		 else if(factor==15)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("Q1", standard);
		 }else{
			 factorMap.put("Q1", 1);
		 }
		 if(map.get("Q2")!=null){
			 int factor = map.get("Q2");
			 int standard = 1;
			 if(factor>=0 && factor<=5)
				 standard = 1;
	 		 else if(factor>=6 && factor<=7)
	 			standard = 2;
	 		 else if(factor==8)
	 			standard = 3;
	 		 else if(factor>=9 && factor<=10)
	 			standard = 4;
	 		 else if(factor>=11 && factor<=12)
	 			standard = 5;
	 		 else if(factor>=13 && factor<=14)
	 			standard = 6;
	 		 else if(factor==15)
	 			standard = 7;
	 		 else if(factor>=16 && factor<=17)
	 			standard = 8;
	 		 else if(factor==18)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("Q2", standard);
		 }else{
			 factorMap.put("Q2", 1);
		 }
		 if(map.get("Q3")!=null){
			 int factor = map.get("Q3");
			 int standard = 1;
			 if(factor>=0 && factor<=4)
				 standard = 1;
	 		 else if(factor>=5&&factor<=6)
	 			standard = 2;
	 		 else if(factor>=7&&factor<=8)
	 			standard = 3;
	 		 else if(factor>=9&&factor<=10)
	 			standard = 4;
	 		 else if(factor>=11 && factor<=12)
	 			standard = 5;
	 		 else if(factor>=13 && factor<=14)
	 			standard = 6;
	 		 else if(factor==15)
	 			standard = 7;
	 		 else if(factor>=16&&factor<=17)
	 			standard = 8;
	 		 else if(factor==18)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("Q3", standard);
		 }else{
			 factorMap.put("Q3", 1);
		 }
		 if(map.get("Q4")!=null){
			 int factor = map.get("Q4");
			 int standard = 1;
			 if(factor>=0 && factor<=2)
				 standard = 1;
	 		 else if(factor>=3 && factor<=4)
	 			standard = 2;
	 		 else if(factor>=5&&factor<=6)
	 			standard = 3;
	 		 else if(factor>=7 && factor<=8)
	 			standard = 4;
	 		 else if(factor>=9 && factor<=11)
	 			standard = 5;
	 		 else if(factor>=12 && factor<=14)
	 			standard = 6;
	 		 else if(factor>=15&&factor<=16)
	 			standard = 7;
	 		 else if(factor>=17 && factor<=19)
	 			standard = 8;
	 		 else if(factor>=20&&factor<=21)
	 			standard = 9;
	 		 else standard = 10;
			 factorMap.put("Q4", standard);
		 }else{
			 factorMap.put("Q4", 1);
		 }
		 return factorMap;
	 }
}
