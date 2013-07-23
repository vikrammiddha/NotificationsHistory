package com.bun.notificationshistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Utils {

	public static LinkedHashMap<String,Integer> sortHashMapByValuesD(LinkedHashMap<String,Integer> passedMap) {
		   List mapKeys = new ArrayList(passedMap.keySet());
		   List mapValues = new ArrayList(passedMap.values());
		   Collections.sort(mapValues, Collections.reverseOrder());
		   Collections.sort(mapKeys,Collections.reverseOrder());

		   LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();

		   Iterator valueIt = mapValues.iterator();
		   while (valueIt.hasNext()) {
		       Integer val = Integer.valueOf(valueIt.next().toString());
		       Iterator keyIt = mapKeys.iterator();

			    while (keyIt.hasNext()) {
			        Object key = keyIt.next();
			        Integer comp1 = passedMap.get(key);
			        Integer comp2 = val;
	
			        if (comp1 == comp2){
			            passedMap.remove(key);
			            mapKeys.remove(key);
			            sortedMap.put((String)key, Integer.valueOf(val));
			            break;
			        }
	
			    }

		}
		//passedMap.putAll(sortedMap);
		return sortedMap; 
	}


}
