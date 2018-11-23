package juego;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

public class Factoria {

	public static FitnessFunction<Integer> 
	getFitnessFunction(List<Integer> alfabetoNumeros, 
			List<Integer> alfabetoOperadores, Integer objetivo) {
		return new FitnessExtendida(alfabetoOperadores,alfabetoNumeros,objetivo);
	}

	public static GoalTest<Individual<Integer>> getGoalTest(FitnessExtendida f) {
		return new TestObjetivo(f);
	}

	public static GoalTest<Individual<Integer>> getGoalTest(List<Integer> alfabetoOperadores, 
			List<Integer> alfabetoNumeros, Integer objetivo) {
		return new TestObjetivo(new FitnessExtendida(alfabetoOperadores,alfabetoNumeros,objetivo));
	}


	public static Individual<Integer> generateRandomIndividual(
			List<Integer> numeros, List<Integer> operadores, int tam) {
		List<Integer> individualRepresentation = new ArrayList<>();
		for (int i = 0; i < tam; i++) {
			if(i%2==0)
				individualRepresentation.
				add(numeros.get(new Random().nextInt(numeros.size())));
			else {
				individualRepresentation.
				add(operadores.get(new Random().nextInt(operadores.size())));
			}
		}
		return new Individual<Integer>(individualRepresentation);
	}

}
