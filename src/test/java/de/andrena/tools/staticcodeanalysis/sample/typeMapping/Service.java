package de.andrena.tools.staticcodeanalysis.sample.typeMapping;

import java.util.List;

public class Service {
	
	public void simple() {
	}
	public int simpleReturn() {
		return 0;
	}
	public void simple(int arg) {
	}
	public boolean checkValue(Value argument) {
		return true;
	}
	public boolean checkValue(Value argument, String parameter) {
		return true;
	}
	public boolean checkValue(Value argument, List<Value> parameters, int param) {
		return true;
	}
	public List<Value> mapValues(List<Value> parameters, boolean bool, byte b, char c, int parameter, long l, double d, float f, short s, String[] rest, Boolean otherParameter) {
		return parameters;
	}
	public long mapValues(boolean bool, String... rest) {
		return 0;
	}
	public Value[] mapValues(Value[] input) {
		return input;
	}

}
