package juego;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;

public class AlgoritmoGeneticoExtendido<A> extends GeneticAlgorithm<A> {
	
	protected List<A> alfabetoOperadores;
	
	

	public AlgoritmoGeneticoExtendido(int individualLength, 
			Collection<A> alfabetoNumeros, Collection<A> alfabetoOperadores,
			double mutationProbability, Random random) {
		super(individualLength, alfabetoNumeros, mutationProbability, random);
		
		this.alfabetoOperadores = new ArrayList<>(alfabetoOperadores);
	}

	@Override
	protected Individual<A> mutate(Individual<A> child) {
		int mutateOffset = randomOffset(individualLength);
		int alphaOffset;
		List<A> mutatedRepresentation = new ArrayList<A>(child.getRepresentation());
		if(individualLength%2==0) {
			alphaOffset = randomOffset(this.finiteAlphabet.size());
			mutatedRepresentation.set(mutateOffset, finiteAlphabet.get(alphaOffset));
		}
		else {
			alphaOffset = randomOffset(alfabetoOperadores.size());
			mutatedRepresentation.set(mutateOffset, alfabetoOperadores.get(alphaOffset));
		}
		return new Individual<A>(mutatedRepresentation);
	}

	@Override
	protected List<Individual<A>> nextGeneration(List<Individual<A>> arg0, FitnessFunction<A> arg1) {
		// TODO Auto-generated method stub
		return super.nextGeneration(arg0, arg1);
	}

	@Override
	protected int randomOffset(int length) {
		// TODO Auto-generated method stub
		return super.randomOffset(length);
	}

	@Override
	protected Individual<A> randomSelection(List<Individual<A>> arg0, FitnessFunction<A> arg1) {
		// TODO Auto-generated method stub
		return super.randomSelection(arg0, arg1);
	}

	@Override
	protected Individual<A> reproduce(Individual<A> x, Individual<A> y) {
		// TODO Auto-generated method stub
		return super.reproduce(x, y);
	}

	@Override
	protected void updateMetrics(Collection<Individual<A>> population, int itCount, long time) {
		// TODO Auto-generated method stub
		super.updateMetrics(population, itCount, time);
	}

	@Override
	protected void validatePopulation(Collection<Individual<A>> arg0) {
		// TODO Auto-generated method stub
		super.validatePopulation(arg0);
	}

	public static 

	
	

}
