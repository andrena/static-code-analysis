package de.andrena.tools.staticcodeanalysis.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    static Logger logger = LogManager.getLogger(Main.class);

    private static final List<CodeAnalysis> analyses = List.of(new InvocationAnalysis(), new FieldAccessAnalysis());

    public static void main(String[] args) throws InvalidUsageException {
        ensureCorrectParameters(args);
        var keyword = args[0];
        var remainingArgs = Arrays.copyOfRange(args, 1, args.length);
        var selectedAnalysis = analyses.stream().filter(it -> it.accept(keyword)).findAny();
        if (selectedAnalysis.isEmpty()) {
            showUsage();
            throw new InvalidUsageException("unknown keyword: "+keyword);
        }
        selectedAnalysis.get().run(remainingArgs);
    }

    private static void ensureCorrectParameters(String[] args) throws InvalidUsageException {
        if (args.length < 1) {
            showUsage();
            throw new InvalidUsageException("required parameters: <selected analysis> <further parameters>");
        }
    }

    private static void showUsage() {
        logger.info("required parameters: <selected analysis> <further parameters>");
        logger.info("");
        logger.info("valid analyses: {}", analyses.stream().map(CodeAnalysis::getKeyword).collect(Collectors.joining(", ")));
    }

}


