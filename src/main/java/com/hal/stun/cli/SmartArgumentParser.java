package com.hal.stun.cli;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class SmartArgumentParser {

  public static final int DEFAULT_TCP_PORT = 8000;
  public static final int DEFAULT_UDP_PORT = 8001;
  public static final int DEFAULT_THREAD_COUNT = 2;

  public static Map<String, Argument> parse(String[] args) throws ArgumentParseException {
    Map<String, ArgumentDefinition> definitions = buildDefinitions();
    Map<String, Argument> values = new HashMap<String, Argument>();
    List<String> argList = new ArrayList<String>(Arrays.asList(args));

    while (!argList.isEmpty()) {
      String argument = argList.remove(0);
      ArgumentDefinition definition = definitions.get(argument);
      if (definition == null) {
        throw new ArgumentParseException("Unrecognized argument \"" + argument);
      }
      String value = getArgumentValue(argList, definition);
      values.put(definition.getKey(), definition.parse(value));
    }
    addDefaults(values, definitions);

    return values;
  }

  private static Map<String, ArgumentDefinition> buildDefinitions() {
    Map<String, ArgumentDefinition> definitions = new HashMap<String, ArgumentDefinition>();
    addDefinition(definitions, new FlagArgumentDefinition("--help",
                                                          "-h",
                                                          false,
                                                          "Show help"));
    addDefinition(definitions, new FlagArgumentDefinition("--tcp",
                                                          "-tcp",
                                                          new TCPDefaultConditionalValue(),
                                                          "Run TCP STUN Server (can be used with --udp)"));
    addDefinition(definitions, new FlagArgumentDefinition("--udp",
                                                          "-udp",
                                                          false,
                                                          "Run UDP STUN Server (can be used with --tcp)"));
    addDefinition(definitions, new PairArgumentDefinition<Integer>(Integer.class,
                                                                   "--tcpport",
                                                                   "-tport",
                                                                   DEFAULT_TCP_PORT,
                                                                   "Port on which to bind the TCP server"));
    addDefinition(definitions, new PairArgumentDefinition<Integer>(Integer.class,
                                                                   "--udpport",
                                                                   "-uport",
                                                                   DEFAULT_UDP_PORT,
                                                                   "Port on which to bind the UDP server"));
    addDefinition(definitions, new PairArgumentDefinition<Integer>(Integer.class,
                                                                   "--threads",
                                                                   "-t",
                                                                   DEFAULT_THREAD_COUNT,
                                                                   "Number of threads to use in handler threadpool"));

    return definitions;
  }

  private static void addDefinition(Map<String, ArgumentDefinition> definitions, ArgumentDefinition definition) {
    definitions.put(definition.getKey(), definition);
    definitions.put(definition.getShortKey(), definition);
  }

  private static void addDefaults(Map<String, Argument> arguments, Map<String, ArgumentDefinition> definitions) {
    Set<ArgumentDefinition> dedupedDefinitions = new HashSet<ArgumentDefinition>(definitions.values());
    for (ArgumentDefinition definition : dedupedDefinitions) {
      if (!arguments.containsKey(definition.getKey())) {
        Argument defaultValue = definition.getDefaultArgument(arguments);
        arguments.put(definition.getKey(), defaultValue);
      }
    }
  }

  private static String getArgumentValue(List<String> argList, ArgumentDefinition definition) {
    if (definition instanceof FlagArgumentDefinition) {
      return null;
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
