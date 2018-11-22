package juego;

import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.Individual;

public class TestObjetivo implements GoalTest<Individual<Integer>> {
	
	private FitnessExtendida f;

	public TestObjetivo(FitnessExtendida f) {
		super();
		this.f = f;
	}

	@Override
	public boolean test(Individual<Integer> t) {
		return f.apply(t) == Double.POSITIVE_INFINITY;
	}

}
