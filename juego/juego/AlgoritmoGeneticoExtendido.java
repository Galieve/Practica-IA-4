package juego;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;
import javafx.util.Pair;

public class AlgoritmoGeneticoExtendido<A> extends GeneticAlgorithm<A> {

	protected List<A> alfabetoOperadores;

	protected boolean hijoUnico=true;

	protected boolean destructive=false;

	protected double probCruce=0.7;
	
	public AlgoritmoGeneticoExtendido(int individualLength, 
			Collection<A> alfabetoNumeros, Collection<A> alfabetoOperadores,
			double mutationProbability, Random random) {
		super(individualLength, alfabetoNumeros, mutationProbability, random);

		this.alfabetoOperadores = new ArrayList<>(alfabetoOperadores);
	}

	public AlgoritmoGeneticoExtendido(int individualLength, 
			Collection<A> alfabetoNumeros, Collection<A> alfabetoOperadores,
			double mutationProbability) {
		this(individualLength, alfabetoNumeros,
				alfabetoOperadores, mutationProbability, new Random());
	}

	@Override
	protected Individual<A> mutate(Individual<A> child) {
		int mutateOffset = randomOffset(individualLength);
		int alphaOffset;
		List<A> mutatedRepresentation = new ArrayList<A>(child.getRepresentation());
		if(mutateOffset%2==0) {
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
	protected List<Individual<A>> nextGeneration
	(List<Individual<A>> population, FitnessFunction<A> fitnessFn) {
		if(hijoUnico) {
			if(destructive) {
				return generateComun(population, fitnessFn,(l, p)->{
					Individual<A> z = hijoUnico(p.getKey(),p.getValue());
					destructivo(l,z);
					return null;
				});
			}
			else {
				return generateComun(population, fitnessFn,(l, p)->{
					Individual<A> z = hijoUnico(p.getKey(),p.getValue());
					noDestructivo(l,fitnessFn,p.getKey(), p.getValue(), z);
					return null;
				});
			}
		}
		else {
			if(destructive) {
				return generateComun(population, fitnessFn,(l, p)->{
					Pair<Individual<A>,Individual<A>> z = 
							mellizos(p.getKey(),p.getValue());
					destructivo(l,z.getKey());
					destructivo(l,z.getValue());
					return null;
				});
			}
			else {
				return generateComun(population, fitnessFn,(l, p)->{
					Pair<Individual<A>,Individual<A>> z = 
							mellizos(p.getKey(),p.getValue());
					noDestructivo(l,fitnessFn,p.getKey(), p.getValue(),
							z.getKey(),z.getValue());
					return null;
				});
			}
		}
		//return generateComun(population, fitnessFn);
	}


	private List<Individual<A>> generateComun
	(List<Individual<A>> population, FitnessFunction<A> fitnessFn, 
			BiFunction<List<Individual<A>>,
			Pair<Individual<A>, Individual<A>>,Void> funcion){
		List<Individual<A>> newPopulation = new ArrayList<Individual<A>>(population.size());
		// for i = 1 to SIZE(population) do
		while(newPopulation.size()< population.size()) {
			// x <- RANDOM-SELECTION(population, FITNESS-FN)
			Individual<A> x = randomSelection(population, fitnessFn);
			// y <- RANDOM-SELECTION(population, FITNESS-FN)
			Individual<A> y = randomSelection(population, fitnessFn);
			if (random.nextDouble() <= probCruce) {
				funcion.apply(newPopulation, new Pair<Individual<A>, Individual<A>>(x,y));
			}
			else if(hijoUnico) {
				//TODO
				newPopulation.add(x);
			}
			else {
				newPopulation.add(x);
				newPopulation.add(y);
			}
			

		}
		//while(newPopulation.size()< population.size());
		return newPopulation;
	}

	private void destructivo(List<Individual<A>> population, Individual<A> hijo) {
		population.add(hijo);
	}
	@SafeVarargs
	private void noDestructivo(List<Individual<A>> population, FitnessFunction<A> f,
			Individual<A>...family) {
		Collections.sort(Arrays.asList(family),(x,y)-> (int)(1/f.apply(x)-1/f.apply(y)));
		//System.out.println(family[0]+" "+family[1]);
		for(int i=0; i<family.length-2; ++i) {
			population.add(family[i]);
		}
	}

	private Individual<A> hijoUnico(Individual<A> x, Individual<A> y) {
		// child <- REPRODUCE(x, y)
		Individual<A> child = reproduce(x, y);
		// if (small random probability) then child <- MUTATE(child)
		if (random.nextDouble() <= mutationProbability) {
			child = mutate(child);
		}
		return child;
	}

	private Pair<Individual<A>,Individual<A>> mellizos(Individual<A> x, Individual<A> y) {
		Pair<Individual<A>,Individual<A>> children = reproduce2(x, y);
		// if (small random probability) then child <- MUTATE(child)
		if (random.nextDouble() <= mutationProbability) {
			children = new Pair<>(mutate(children.getKey()), children.getValue());
		}
		if (random.nextDouble() <= mutationProbability) {
			children = new Pair<>(children.getKey(), mutate(children.getValue()));
		}
		return children;
	}



	protected Pair<Individual<A>,Individual<A>> reproduce2(Individual<A> x, Individual<A> y) {
		// n <- LENGTH(x);
		// Note: this is = this.individualLength
		// c <- random number from 1 to n
		int c = randomOffset(individualLength);
		// return APPEND(SUBSTRING(x, 1, c), SUBSTRING(y, c+1, n))
		List<A> childRepresentation1 = new ArrayList<A>();
		childRepresentation1.addAll(x.getRepresentation().subList(0, c));
		childRepresentation1.addAll(
				y.getRepresentation().subList(c, individualLength));
		List<A> childRepresentation2 = new ArrayList<A>();
		childRepresentation2.addAll(y.getRepresentation().subList(0, c));
		childRepresentation2.addAll(
				x.getRepresentation().subList(c, individualLength));

		return new Pair<>(new Individual<A>(childRepresentation1), 
				new Individual<>(childRepresentation2));
	}



	public boolean isHijoUnico() {
		return hijoUnico;
	}

	public void setHijoUnico(boolean hijoUnico) {
		this.hijoUnico = hijoUnico;
	}

	public boolean isDestructive() {
		return destructive;
	}

	public void setDestructive(boolean destructive) {
		this.destructive = destructive;
	}

	public double getProbCruce() {
		return probCruce;
	}

	public void setProbCruce(double probCruce) {
		this.probCruce = probCruce;
	}
	
	


}
