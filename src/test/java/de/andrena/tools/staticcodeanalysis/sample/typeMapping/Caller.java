package de.andrena.tools.staticcodeanalysis.sample.typeMapping;

import java.util.ArrayList;

public class Caller {
	
	private Service service = new Service();
	
	public void invoke() {
		Value value = new Value();
		service.simple();
		service.simple(3);
		service.simpleReturn();
		service.checkValue(value);
		service.checkValue(value, "param");
		service.checkValue(value, new ArrayList<>(), 2);
		service.mapValues(null, false, (byte) 0, (char) 0, 0, 0, 0, 0,(short)0, new String[0],null);
		service.mapValues(true);
		service.mapValues(new Value[]{value});
	}

}
