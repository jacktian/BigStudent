package com.jov.common;
import java.util.HashMap;
import java.util.Map;

public final class ApplicationControl {
	public static Map<String,Integer> calculate(Map<String,Integer> map){
		Map<String,Integer> applicationMap = new HashMap<String,Integer>();
		int heartHealthy = map.get("C")+map.get("F")+(11-map.get("O"))+(11-map.get("Q4"));
		if(heartHealthy>=4&&heartHealthy<=11){
			heartHealthy = 1;
		}else if(heartHealthy>=12&&heartHealthy<=14){
			heartHealthy = 2;
		}else if(heartHealthy>=15&&heartHealthy<=17){
			heartHealthy = 3;
		}else if(heartHealthy>=18&&heartHealthy<=20){
			heartHealthy = 4;
		}else if(heartHealthy>=21&&heartHealthy<=22){
			heartHealthy = 5;
		}else if(heartHealthy>=23&&heartHealthy<=24){
			heartHealthy = 6;
		}else if(heartHealthy>=25&&heartHealthy<=26){
			heartHealthy = 7;
		}else if(heartHealthy>=27&&heartHealthy<=28){
			heartHealthy = 8;
		}else if(heartHealthy>=29&&heartHealthy<=30){
			heartHealthy = 9;
		}else{
			heartHealthy = 10;
		}
		applicationMap.put("心理健康(满分10)", heartHealthy);
		
		int professional = map.get("E")+map.get("H")+map.get("M")+map.get("N")+map.get("Q1");
		if(professional>=5&&professional<=17){
			professional = 1;
		}else if(professional>=18&&professional<=20){
			professional = 2;
		}else if(professional>=21&&professional<=22){
			professional = 3;
		}else if(professional>=23&&professional<=25){
			professional = 4;
		}else if(professional>=26&&professional<=27){
			professional = 5;
		}else if(professional>=28&&professional<=30){
			professional = 6;
		}else if(professional>=31&&professional<=32){
			professional = 7;
		}else if(professional>=33&&professional<=35){
			professional = 8;
		}else if(professional>=36&&professional<=37){
			professional = 9;
		}else{
			professional = 10;
		}
		applicationMap.put("从事专业者素质(满分10)", professional);
		
		int success = 2*map.get("Q3")+2*map.get("G")+2*map.get("C")+map.get("E")+map.get("N")+map.get("Q2")+map.get("Q1");
		if(success>=10&&success<=38){
			success = 1;
		}else if(success>=39&&success<=42){
			success = 2;
		}else if(success>=43&&success<=46){
			success = 3;
		}else if(success>=47&&success<=50){
			success = 4;
		}else if(success>=51&&success<=54){
			success = 5;
		}else if(success>=55&&success<=58){
			success = 6;
		}else if(success>=59&&success<=62){
			success = 7;
		}else if(success>=63&&success<=66){
			success = 8;
		}else if(success>=67&&success<=70){
			success = 9;
		}else{
			success = 10;
		}
		applicationMap.put("专业而有成就者素质(满分10)", success);
		
		
		int create = 2*(11-map.get("A"))+2*map.get("B")+2*(11-map.get("F"))+map.get("E")+map.get("H")+2*map.get("I")+map.get("M")+(11-map.get("N"))+2*map.get("Q2")+map.get("Q1");
		if(create>=15&&create<=62){
			create = 1;
		}else if(create>=63&&create<=67){
			create = 2;
		}else if(create>=68&&create<=72){
			create = 3;
		}else if(create>=73&&create<=77){
			create = 4;
		}else if(create>=78&&create<=82){
			create = 5;
		}else if(create>=83&&create<=87){
			create = 6;
		}else if(create>=88&&create<=92){
			create = 7;
		}else if(create>=93&&create<=97){
			create = 8;
		}else if(create>=98&&create<=102){
			create = 9;
		}else{
			create = 10;
		}
		applicationMap.put("创造能力(满分10)", create);
		int study = map.get("B")+map.get("G")+map.get("Q3")+(11-map.get("F"));
		if(study>=4&&study<=14){
			study = 1;
		}else if(study>=15&&study<=16){
			study = 2;
		}else if(study>=17&&study<=18){
			study = 3;
		}else if(study>=19&&study<=20){
			study = 4;
		}else if(study>=21&&study<=22){
			study = 5;
		}else if(study>=23&&study<=24){
			study = 6;
		}else if(study>=25&&study<=26){
			study = 7;
		}else if(study>=27&&study<=28){
			study = 8;
		}else if(study>=29&&study<=30){
			study = 9;
		}else {
			study = 10;
		}
		applicationMap.put("适应新环境，有学习成长能力者素质(满分10)", study);
		
		int manage = map.get("Q3")+2*map.get("G")+(11-map.get("Q4"))+2*(11-map.get("M"));
		if(manage>=6&&manage<=21){
			manage = 1;
		}else if(manage>=22&&manage<=24){
			manage = 2;
		}else if(manage>=25&&manage<=27){
			manage = 3;
		}else if(manage>=28&&manage<=30){
			manage = 4;
		}else if(manage>=31&&manage<=33){
			manage = 5;
		}else if(manage>=34&&manage<=36){
			manage = 6;
		}else if(manage>=37&&manage<=39){
			manage = 7;
		}else if(manage>=40&&manage<=42){
			manage = 8;
		}else if(manage>=43&&manage<=45){
			manage = 9;
		}else{
			manage = 10;
		}
		applicationMap.put("管理事务正确可靠者素质(满分10)", manage);
		return applicationMap;
	}
}
