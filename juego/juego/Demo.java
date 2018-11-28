package juego;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;

public class Demo {

	private static final List<Integer> alfabetoNum = 
			Arrays.asList(1,2,3,4,5,6,7,8,9,10,25,50);

	private static final List<Integer> alfabetoOp = 
			Arrays.asList(0,1,2,3);
	
	private static final int TAM_POBLACION=12;
	
	private static final int NUM_OPERADORES=5;
	
	private static final Integer MAX_NUMERO=1000;
	
	private static final int TAM_INDIVIDUO = 2*NUM_OPERADORES+1;
	
	private static final double MUTATION_PROB=0.7;
	
	private static final double CRUCE_PROB=0.8;
	
	private static final Integer objetivo = new Random().nextInt(MAX_NUMERO);
	
	public static void main(String[] args) {

		algoritmoGeneticoDemo();
	}

	private static void algoritmoGeneticoDemo() {

	
		algoritmoGenetico();
	}
	
	private static void algoritmoGenetico() {
		System.out.println("\nNQueensDemo GeneticAlgorithm  -->");
		try {
			FitnessFunction<Integer> fitnessFunction = Factoria.getFitnessFunction
					(alfabetoNum, alfabetoOp, objetivo);
			GoalTest<Individual<Integer>> goalTest =
					Factoria.getGoalTest((FitnessExtendida) fitnessFunction);
			// Generate an initial population
			Set<Individual<Integer>> population = new HashSet<>();
			for (int i = 0; i < TAM_POBLACION; i++) {
				population.add(Factoria.generateRandomIndividual
						(alfabetoNum, alfabetoOp, TAM_INDIVIDUO));
			}

			AlgoritmoGeneticoExtendido<Integer> ga = new AlgoritmoGeneticoExtendido
					<>(TAM_INDIVIDUO, alfabetoNum,alfabetoOp,
							 MUTATION_PROB);
			ga.setProbCruce(CRUCE_PROB);
			ga.setHijoUnico(false);
			ga.setDestructive(true);
			// Run for a set amount of time
			IndividuoExtendido bestIndividual = new IndividuoExtendido
					( ga.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L));
			
			System.out.println("Objective		= "+objetivo);
			System.out.println("Max Time (1 second) Best Individual= "
			+bestIndividual+" \n");
			System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
			System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
		
			System.out.println("Population Size = " + ga.getPopulationSize());
			System.out.println("Iterations      = " + ga.getIterations());
			System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

			// Run till goal is achieved
			bestIndividual = new IndividuoExtendido
					( ga.geneticAlgorithm(population, fitnessFunction, goalTest, 15000L));

			System.out.println("");
			System.out.println("Goal Test Best Individual= "+bestIndividual+"\n" );
			System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
			System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
			System.out.println("Population Size = " + ga.getPopulationSize());
			System.out.println("Itertions       = " + ga.getIterations());
			System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printInstrumentation(Properties properties) {
		for (Object o : properties.keySet()) {
			String key = (String) o;
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}

	}

	private static void printActions(List<Action> actions) {
		for (Action action : actions) {
			System.out.println(action.toString());
		}
	}
	
	
}
