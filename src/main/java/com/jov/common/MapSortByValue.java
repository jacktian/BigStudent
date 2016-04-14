package com.jov.common;

import java.util.Comparator;
import java.util.Map;

public class MapSortByValue implements Comparator {

	public int compare(Object o1, Object o2) {
		Map.Entry obj1 = (Map.Entry)o1;  
        Map.Entry obj2 = (Map.Entry)o2;           
        return obj2.getValue().toString().compareTo(obj1.getValue().toString());  
	}

}
