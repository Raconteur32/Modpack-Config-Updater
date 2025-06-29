package fr.raconteur.sbcou;

import fr.raconteur.sbcou.exceptions.SbcouException;
import fr.raconteur.sbcou.platform.CmdPlatformHelper;
import fr.raconteur.sbcou.platform.Services;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.TreeMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.Scanner;

@Command(name = "sbcou", mixinStandardHelpOptions = true, version = "${version}",
        description = "SBCOU command-line tool",
        subcommands = {
                Main.HelloCommand.class,
                Main.InitCommand.class,
                Main.StatesCommand.class,
                Main.NewVersionCommand.class
        })
public class Main implements Callable<Integer> {

  @Option(names = {"-d", "--dev-instance"}, description = "Path to the dev instance")
  private String devInstancePath;

  private static final CmdPlatformHelper platformHelper = (CmdPlatformHelper) Services.PLATFORM;

  public static void main(String[] args) {
    Main main = new Main();
    int exitCode = new CommandLine(main)
            .setExecutionStrategy(main::executionStrategy)
            .execute(args);
    System.exit(exitCode);
  }

  private int executionStrategy(CommandLine.ParseResult parseResult) {
    if (devInstancePath != null) {
      platformHelper.setDevInstancePath(Path.of(devInstancePath));
    }
    return new CommandLine.RunLast().execute(parseResult);
  }

  @Override
  public Integer call() {
    throw new CommandLine.ParameterException(new CommandLine(this), "Missing required subcommand");
  }

  @Command(name = "hello", description = "Say hello")
  static class HelloCommand implements Callable<Integer> {
    @Parameters(index = "0", description = "The name to greet", defaultValue = "World")
    private String name;

    @Override
    public Integer call() {
      System.out.println("Hello, " + name + "!");
      return 0;
    }
  }

  @Command(name = "init", description = "Initialize SBCOU")
  static class InitCommand implements Callable<Integer> {
    @Override
    public Integer call() {
      try {
        OptionsActionsContext.verifySbcouConfigDir();
        Version.verifyVersionsSpecsFiles();
        System.out.println("SBCOU directory initialized");
        if (!OptionsActionsUtils.isDevInstance()) {
          OptionsActionsUtils.initDevDirectory();
          System.out.println("Dev directory initialized");
        }
      } catch (SbcouException e) {
        System.err.println("Failed to initialize SBCOU: " + e.getMessage());
        return 1;
      }
      return 0;
    }
  }

  @Command(name = "states", description = "Manage option states",
          subcommands = {StatesCommand.GenCommand.class, StatesCommand.SubmitCommand.class})
  static class StatesCommand implements Callable<Integer> {
    @Override
    public Integer call() {
      System.out.println("Use 'gen' or 'submit' subcommands");
      return 0;
    }

    @Command(name = "gen", description = "Generate editStates.json file")
    static class GenCommand implements Callable<Integer> {
      @Override
      public Integer call() {
        try {
          OptionsActionsContext.fullRefresh();
          Map<String, OptionChange> optionChangeMap = OptionsActionsContext.getOptionChangeNatureMap();
          Map<String, String> stateMap = OptionsActionsContext.getStateMap();
          
          Map<String, String> jsonMap = new TreeMap<>();
          for (String key : optionChangeMap.keySet()) {
            jsonMap.put(key, stateMap.getOrDefault(key, "unknown"));
          }
          
          Gson gson = new GsonBuilder().setPrettyPrinting().create();
          String json = gson.toJson(jsonMap);
          
          try (FileWriter file = new FileWriter("./editStates.json")) {
            file.write(json);
            System.out.println("Successfully generated ./editStates.json");
          } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return 1;
          }
        } catch (SbcouException e) {
          System.err.println("Failed to generate states file: " + e.getMessage());
          return 1;
        }
        return 0;
      }
    }

    @Command(name = "submit", description = "Submit edited states")
    static class SubmitCommand implements Callable<Integer> {
      @Override
      public Integer call() {
        try {
          String content = new String(Files.readAllBytes(Path.of("./editStates.json")));
          Gson gson = new Gson();
          Map<String, String> jsonMap = gson.fromJson(content, new TypeToken<Map<String, String>>(){}.getType());
          
          TreeMap<String, String> sortedStates = new TreeMap<>(
                  Comparator.comparingInt(String::length).thenComparing(a -> a));
          
          sortedStates.putAll(jsonMap);
          
          OptionsActionsContext.fullRefresh();
          for (Map.Entry<String, String> entry : sortedStates.entrySet()) {
            if (!entry.getValue().equalsIgnoreCase("unknown")) {
              OptionsActionsContext.setState(entry.getKey(), OptionHandlingState.valueOf(entry.getValue().toUpperCase()));
            }
          }
          
          System.out.println("Successfully submitted states");

          // Verification with editor memory
          Map<String, String> editorMemory = OptionsActionsContext.getStateMap();
          boolean differencesFound = false;
          for (Map.Entry<String, String> entry : editorMemory.entrySet()) {
            if (entry.getValue().equalsIgnoreCase("nested_case") && !jsonMap.getOrDefault(entry.getKey(), "").equalsIgnoreCase("nested_case")) {
              System.out.println("The option " + entry.getKey() + " has been automatically interpreted as nested_case because nested options have been modified.");
              differencesFound = true;
            }
          }

          if (differencesFound) {
            System.out.print("Do you want to visualize these differences in ./editStates.json? (Y/N): ");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equalsIgnoreCase("y")) {
              new GenCommand().call();
            }
          }

        } catch (IOException e) {
          System.err.println("Error reading ./editStates.json: " + e.getMessage());
          return 1;
        } catch (SbcouException e) {
          System.err.println("Failed to submit states: " + e.getMessage());
          return 1;
        }
        return 0;
      }
    }
  }

  @Command(name = "newversion", description = "Create a new version")
  static class NewVersionCommand implements Callable<Integer> {
    @Parameters(index = "0", description = "Version ID")
    private String versionId;

    @Override
    public Integer call() {
      try {
        OptionsActionsContext.createVersion(versionId);
      } catch (SbcouException e) {
        System.out.println(e.getMessage());
        return 1;
      }
      return 0;
    }
  }
}
