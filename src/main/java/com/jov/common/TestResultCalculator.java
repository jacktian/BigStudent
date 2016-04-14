package com.jov.common;

import java.util.HashMap;
import java.util.Map;

public class TestResultCalculator {

	public static Map<String, Integer> calculate(Map<String, Integer> map) {
		Map<String, Integer> adaptionMap = new HashMap<String, Integer>();
		int skill = map.get("B") + map.get("C") + map.get("G") + map.get("Q3")
				+ (11 - map.get("A")) + (11 - map.get("F"));
		if (skill >= 6 && skill <= 21) {
			skill = 1;
		} else if (skill >= 22 && skill <= 24) {
			skill = 2;
		} else if (skill >= 25 && skill <= 27) {
			skill = 3;
		} else if (skill >= 28 && skill <= 30) {
			skill = 4;
		} else if (skill >= 31 && skill <= 33) {
			skill = 5;
		} else if (skill >= 34 && skill <= 36) {
			skill = 6;
		} else if (skill >= 37 && skill <= 39) {
			skill = 7;
		} else if (skill >= 40 && skill <= 42) {
			skill = 8;
		} else if (skill >= 43 && skill <= 45) {
			skill = 9;
		} else {
			skill = 10;
		}
		adaptionMap.put("技术型", skill);
		int art = 2 * map.get("M") + 2 * map.get("I") + map.get("B")
				+ map.get("E") + map.get("F") + map.get("Q1") + map.get("Q2")
				+ (11 - map.get("G"));
		if (art >= 10 && art <= 35) {
			art = 1;
		} else if (art >= 36 && art <= 40) {
			art = 2;
		} else if (art >= 41 && art <= 45) {
			art = 3;
		} else if (art >= 46 && art <= 50) {
			art = 4;
		} else if (art >= 51 && art <= 55) {
			art = 5;
		} else if (art >= 56 && art <= 60) {
			art = 6;
		} else if (art >= 61 && art <= 65) {
			art = 7;
		} else if (art >= 66 && art <= 70) {
			art = 8;
		} else if (art >= 71 && art <= 75) {
			art = 9;
		} else {
			art = 10;
		}
		adaptionMap.put("艺术型", art);
		int tech = map.get("B") + map.get("G") + map.get("Q3") + map.get("C")
				+ map.get("H") + (11 - map.get("Q4"));
		if (tech >= 6 && tech <= 21) {
			tech = 1;
		} else if (tech >= 22 && tech <= 24) {
			tech = 2;
		} else if (tech >= 25 && tech <= 27) {
			tech = 3;
		} else if (tech >= 28 && tech <= 30) {
			tech = 4;
		} else if (tech >= 31 && tech <= 33) {
			tech = 5;
		} else if (tech >= 34 && tech <= 36) {
			tech = 6;
		} else if (tech >= 37 && tech <= 39) {
			tech = 7;
		} else if (tech >= 40 && tech <= 42) {
			tech = 8;
		} else if (tech >= 43 && tech <= 45) {
			tech = 9;
		} else {
			tech = 10;
		}
		adaptionMap.put("教育型", tech);
		int science = 2 * map.get("B") + 2 * (11 - map.get("A")) + 2
				* (11 - map.get("F")) + map.get("C") + map.get("E")
				+ map.get("H") + map.get("Q1") + map.get("Q2")
				+ (11 - map.get("O")) + (11 - map.get("N"));
		if (science >= 13 && science <= 52) {
			science = 1;
		} else if (science >= 53 && science <= 57) {
			science = 2;
		} else if (science >= 58 && science <= 62) {
			science = 3;
		} else if (science >= 63 && science <= 67) {
			science = 4;
		} else if (science >= 68 && science <= 72) {
			science = 5;
		} else if (science >= 73 && science <= 77) {
			science = 6;
		} else if (science >= 78 && science <= 82) {
			science = 7;
		} else if (science >= 83 && science <= 87) {
			science = 8;
		} else if (science >= 88 && science <= 92) {
			science = 9;
		} else {
			science = 10;
		}
		adaptionMap.put("科研型", science);

		int service = 2 * map.get("A") + 2 * map.get("F") + map.get("G")
				+ map.get("N") + map.get("Q3") + (11 - map.get("E"))
				+ (11 - map.get("Q4"));
		if (service >= 9 && service <= 33) {
			service = 1;
		} else if (service >= 34 && service <= 37) {
			service = 2;
		} else if (service >= 38 && service <= 41) {
			service = 3;
		} else if (service >= 42 && service <= 45) {
			service = 4;
		} else if (service >= 46 && service <= 49) {
			service = 5;
		} else if (service >= 50 && service <= 53) {
			service = 6;
		} else if (service >= 54 && service <= 57) {
			service = 7;
		} else if (service >= 58 && service <= 61) {
			service = 8;
		} else if (service >= 62 && service <= 65) {
			service = 9;
		} else {
			service = 10;
		}
		adaptionMap.put("服务型", service);

		int manage = 2 * map.get("C") + 2 * map.get("G") + 2 * map.get("Q3")
				+ map.get("B") + map.get("E") + map.get("H") + map.get("N")
				+ map.get("Q1") + map.get("Q2");
		if (manage >= 12 && manage <= 47) {
			manage = 1;
		} else if (manage >= 48 && manage <= 52) {
			manage = 2;
		} else if (manage >= 53 && manage <= 57) {
			manage = 3;
		} else if (manage >= 58 && manage <= 62) {
			manage = 4;
		} else if (manage >= 63 && manage <= 67) {
			manage = 5;
		} else if (manage >= 68 && manage <= 72) {
			manage = 6;
		} else if (manage >= 73 && manage <= 77) {
			manage = 7;
		} else if (manage >= 78 && manage <= 82) {
			manage = 8;
		} else if (manage >= 83 && manage <= 87) {
			manage = 9;
		} else {
			manage = 10;
		}
		adaptionMap.put("管理型", manage);
		int scoiety = 2 * map.get("F") + 2 * map.get("N") + 2 * map.get("H")
				+ 2 * map.get("A") + map.get("E") + (11 - map.get("O"));
		if (scoiety >= 10 && scoiety <= 35) {
			scoiety = 1;
		} else if (scoiety >= 36 && scoiety <= 40) {
			scoiety = 2;
		} else if (scoiety >= 41 && scoiety <= 45) {
			scoiety = 3;
		} else if (scoiety >= 46 && scoiety <= 50) {
			scoiety = 4;
		} else if (scoiety >= 51 && scoiety <= 55) {
			scoiety = 5;
		} else if (scoiety >= 56 && scoiety <= 60) {
			scoiety = 6;
		} else if (scoiety >= 61 && scoiety <= 65) {
			scoiety = 7;
		} else if (scoiety >= 66 && scoiety <= 70) {
			scoiety = 8;
		} else if (scoiety >= 71 && scoiety <= 75) {
			scoiety = 9;
		} else {
			scoiety = 10;
		}
		adaptionMap.put("社交型", scoiety);
		return adaptionMap;
	}
}
