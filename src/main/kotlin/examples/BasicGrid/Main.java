package examples.BasicGrid;

import agent.Agent;
import agent.AgentConfiguration;
import agent.blocks.ExplorationProvider;
import agent.blocks.LearningProvider;
import agent.blocks.SelectionProvider;
import environment.Action;
import environment.Objective;
import environment.Point;
import environment.discrete.GridEnvironment;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private final static double LEARNING_RATE = 0.1;
    private final static double FUTURE_DISCOUNT = 0.9;
    private final static double EXPLORATION_PROBABILITY = 0.1;
    private final static double EXPLORATION_DECREASE = 0.0001;
    private final static double STOPPING_CRITERIA = 100000;

    private final static Map<Integer, String> map;
    static {
        Map<Integer, String> temp = new HashMap<>();
        temp.put(0, "Right");
        temp.put(1, "Left");
        temp.put(2, "Up");
        temp.put(3, "Down");
        map = Collections.unmodifiableMap(temp);
    }

    public static void main(final String args[]) {

        Objective single = new Objective(1);
        GridEnvironment env = new BasicGridEnvironment(false, single, -0.5, 4, 4);
        printArray(env.getRewards());
        AgentConfiguration configuration = new AgentConfiguration(
                new ExplorationProvider().getMonotonicallyDecreasingStochasticExploration(EXPLORATION_PROBABILITY,
                EXPLORATION_DECREASE),
                new SelectionProvider().getGreedySelection(),
                new LearningProvider().getQLearning(LEARNING_RATE, FUTURE_DISCOUNT),
                new Point(0, 0),
                new Action(4),
                single);

        Agent agent = new BasicGridAgent(env, configuration);

        for (int i=0; i<STOPPING_CRITERIA; i+=1) {
            agent.behave();
        }

        System.out.println(agent.getTotalReward());
        for (int i=0; i<4; i+=1) {
            for (int j = 0; j < 4; j += 1) {
                final int x = i;
                final int y = j;
                System.out.println(x + " , " + y);
                Map<Action.ActionType, Double> am = agent.getPolicy().actionValues(new BasicGridEnvironmentModel(() -> new Point(x, y)))
                        .get(single.getType(0)).getActionMap();
                am.keySet().stream()
                        .map(key -> map.get(key.getType()) + " : " + am.get(key))
                        .reduce((s1, s2) -> s1+", " +s2)
                        .ifPresent(System.out::println);

            }
        }

    }

    private static void printArray(Object[] toPrint) {
        Arrays.stream(toPrint).forEachOrdered(x -> System.out.println(Arrays.toString((Double[]) x)));
    }
}
