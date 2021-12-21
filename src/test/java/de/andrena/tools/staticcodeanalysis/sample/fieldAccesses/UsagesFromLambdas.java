package de.andrena.tools.staticcodeanalysis.sample.fieldAccesses;

import java.util.function.Supplier;
import java.util.stream.LongStream;

@SuppressWarnings("all")
public class UsagesFromLambdas {

	private int a;
	private long b;

	public long usesAB() {
		return LongStream.range(0, a).map(i -> i + b).sum();
	}

	public int usesA() {
		Supplier<Integer> func = () -> a;
		return 12;
	}
}
