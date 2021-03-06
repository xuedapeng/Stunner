package com.hal.stun.cli;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class ArgumentParser {

    public static final int DEFAULT_TCP_PORT = 8000;
    public static final int DEFAULT_UDP_PORT = 8001;
    public static final int DEFAULT_THREAD_COUNT = 2;

    public static final Map<String, ArgumentDefinition> definitions = buildDefinitions();
    public static final String TCP_KEY = "--tcp";
    public static final String UDP_KEY = "--udp";
    public static final String TCP_PORT_KEY = "--tcpport";
    public static final String UDP_PORT_KEY = "--udpport";
    public static final String THREADS_KEY = "--threads";

    public static Map<String, Argument> parse(String[] args) throws ArgumentParseException {
        Map<String, Argument> values = new HashMap<>();
        List<String> argList = new ArrayList<>(Arrays.asList(args));

        while (!argList.isEmpty()) {
            String argument = argList.remove(0);
            ArgumentDefinition definition = definitions.get(argument);
            if (definition == null) {
                throw new ArgumentParseException("Unrecognized argument \"" + argument + "\"");
            }
            String value = getArgumentValue(argList, definition);
            values.put(definition.getKey(), definition.parse(value));
        }
        addDefaults(values);

        return values;
    }

    public static Set<ArgumentDefinition> getDefinitionSet() {
        return new HashSet<>(definitions.values());
    }

    private static Map<String, ArgumentDefinition> buildDefinitions() {
        Map<String, ArgumentDefinition> newDefinitions = new HashMap<>();
        addDefinition(newDefinitions, new FlagArgumentDefinition(
                "--help",
                "-h",
                false,
                "Show help"));
        addDefinition(newDefinitions, new FlagArgumentDefinition(
                TCP_KEY,
                "-tcp",
                new TCPDefaultConditionalValue(),
                "Run TCP STUN Server (can be used with --udp)"));
        addDefinition(newDefinitions, new FlagArgumentDefinition(
                UDP_KEY,
                "-udp",
                false,
                "Run UDP STUN Server (can be used with --tcp)"));
        addDefinition(newDefinitions, new PairArgumentDefinition<>(Integer.class,
                TCP_PORT_KEY,
                "-tport",
                DEFAULT_TCP_PORT,
                "Port on which to bind the TCP server"));
        addDefinition(newDefinitions, new PairArgumentDefinition<>(Integer.class,
                UDP_PORT_KEY,
                "-uport",
                DEFAULT_UDP_PORT,
                "Port on which to bind the UDP server"));
        addDefinition(newDefinitions, new PairArgumentDefinition<>(Integer.class,
                THREADS_KEY,
                "-t",
                DEFAULT_THREAD_COUNT,
                "Number of threads to use in handler threadpool"));

        return newDefinitions;
    }

    private static void addDefinition(Map<String, ArgumentDefinition> definitions, ArgumentDefinition definition) {
        definitions.put(definition.getKey(), definition);
        definitions.put(definition.getShortKey(), definition);
    }

    private static void addDefaults(Map<String, Argument> arguments) {
        for (ArgumentDefinition definition : getDefinitionSet()) {
            if (!arguments.containsKey(definition.getKey())) {
                Argument defaultValue = definition.getDefaultArgument(arguments);
                arguments.put(definition.getKey(), defaultValue);
            }
        }
    }

    private static String getArgumentValue(List<String> argList, ArgumentDefinition definition) throws ArgumentParseException {
        if (definition instanceof FlagArgumentDefinition) {
            return null;
        } else if (argList.isEmpty()) {
            throw new ArgumentParseException("Argument " + definition.getKey() + "  requires a value");
        } else {
            return argList.remove(0);
        }
    }

    private static class TCPDefaultConditionalValue implements ConditionalValue<Boolean> {
        public Boolean getValue(Map<String, Argument> otherArgs) {
            return !(otherArgs.containsKey("--udp") &&
                    otherArgs.get("--udp").getBoolean().booleanValue());
        }
    }
}
