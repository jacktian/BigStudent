package com.jov.common;

import java.util.HashMap;
import java.util.Map;

public final class FactorControl {
	public static Map<String,Integer> calculate(Map<String,Integer> map,int sex){
		Map<String,Integer> factorMap = new HashMap<String,Integer>();
		if(sex==1){
			factorMap = MaleSwitch.switchToStandard(map);
		}else{
			factorMap = FemaleSwitch.switchToStandard(map);
		}
		return factorMap;
	}
}
