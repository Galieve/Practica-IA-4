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

public class AlgoritmoGeneticoExtendido extends GeneticAlgorithm<Integer> {

	protected List<Integer> alfabetoOperadores;

	protected boolean hijoUnico=false;

	protected boolean destructive=false;

	protected double probCruce=0.7;

	public AlgoritmoGeneticoExtendido(int individualLength, 
			Collection<Integer> alfabetoNumeros, Collection<Integer> alfabetoOperadores,
			Random random) {
		super(individualLength, alfabetoNumeros, 0.0, random);

		this.alfabetoOperadores = new ArrayList<>(alfabetoOperadores);
	}

	public AlgoritmoGeneticoExtendido(int individualLength, 
			Collection<Integer> alfabetoNumeros, Collection<Integer> alfabetoOperadores) {
		this(individualLength, alfabetoNumeros,
				alfabetoOperadores, new Random());
	}

	@Override
	protected Individual<Integer> mutate(Individual<Integer> child) {
		int mutateOffset = randomOffset(individualLength);
		int alphaOffset;
		List<Integer> mutatedRepresentation = new ArrayList<Integer>(child.getRepresentation());
		if(mutateOffset%2==0) {
			alphaOffset = randomOffset(this.finiteAlphabet.size());
			mutatedRepresentation.set(mutateOffset, finiteAlphabet.get(alphaOffset));
		}
		else {
			alphaOffset = randomOffset(alfabetoOperadores.size());
			mutatedRepresentation.set(mutateOffset, alfabetoOperadores.get(alphaOffset));
		}
		return new IndividuoExtendido(mutatedRepresentation);
	}


	@SuppressWarnings("unchecked")
	@Override
	protected List<Individual<Integer>> nextGeneration
	(List<Individual<Integer>> population, FitnessFunction<Integer> fitnessFn) {
		if(hijoUnico) {
			if(destructive) {
				return generateComun(population, fitnessFn,
						(l, p)->{
							Individual<Integer> z = hijoUnico(p.getKey(),p.getValue());
							destructivo(l,z);
							return null;
						});
			}
			else {
				return generateComun(population, fitnessFn,
						(l, p)->{
							Individual<Integer> z = hijoUnico(p.getKey(),p.getValue());
							noDestructivo(l,fitnessFn,p.getKey(), p.getValue(), z);
							return null;
						});
			}
		}
		else {
			if(destructive) {
				return generateComun(population, fitnessFn,
						(l, p)->{
							Pair<Individual<Integer>,Individual<Integer>> z = 
									mellizos(p.getKey(),p.getValue());
							destructivo(l,z.getKey());
							destructivo(l,z.getValue());
							return null;
						});
			}
			else {
				return generateComun(population, fitnessFn,
						(l, p)->{
							Pair<Individual<Integer>,Individual<Integer>> z = 
									mellizos(p.getKey(),p.getValue());
							noDestructivo(l,fitnessFn,p.getKey(), p.getValue(),
									z.getKey(),z.getValue());
							return null;
						});
			}
		}
	}




	private List<Individual<Integer>> generateComun
	(List<Individual<Integer>> population, FitnessFunction<Integer> fitnessFn, 
			BiFunction<List<Individual<Integer>>,
			Pair<Individual<Integer>, Individual<Integer>>,Void> funcion){

		List<Individual<Integer>> newPopulation = 
				new ArrayList<Individual<Integer>>(population.size());
		while(newPopulation.size()< population.size()) {

			Individual<Integer> x = randomSelection(population, fitnessFn);
			Individual<Integer> y = randomSelection(population, fitnessFn);

			if (random.nextDouble() <= probCruce) {
				funcion.apply(newPopulation, 
						new Pair<Individual<Integer>, Individual<Integer>>(x,y));
			}
			else if(hijoUnico) {
				if(fitnessFn.apply(x) > fitnessFn.apply(y)) {
					newPopulation.add(x);
				}
				else newPopulation.add(y);
			}
			else {
				newPopulation.add(x);
				newPopulation.add(y);
			}
		}
		return newPopulation;
	}


	protected void destructivo(List<Individual<Integer>> population, Individual<Integer> hijo) {
		population.add(hijo);
	}


	protected void noDestructivo(List<Individual<Integer>> population, FitnessFunction<Integer> f,
			@SuppressWarnings("unchecked") Individual<Integer>...family) {
		//Es 1/ porque sino comparabamos 0 y 0 muchas veces y el comprarador se comportaba mal.
		Collections.sort(Arrays.asList(family),(x,y)-> (int)(1/f.apply(x)-1/f.apply(y)));
		for(int i=0; i<family.length-2; ++i) {
			population.add(family[i]);
		}
	}

	protected Individual<Integer> hijoUnico(Individual<Integer> x, Individual<Integer> y) {
		Individual<Integer> child = new IndividuoExtendido(reproduce(x, y));
		if (random.nextDouble() <= mutationProbability) {
			child = mutate(child);
		}
		return child;
	}

	protected Pair<Individual<Integer>,Individual<Integer>> mellizos
	(Individual<Integer> x, Individual<Integer> y) {
		Pair<Individual<Integer>,Individual<Integer>> children = reproduce2(x, y);
		if (random.nextDouble() <= mutationProbability) {
			children = new Pair<>(mutate(children.getKey()), children.getValue());
		}
		if (random.nextDouble() <= mutationProbability) {
			children = new Pair<>(children.getKey(), mutate(children.getValue()));
		}
		return children;
	}



	protected Pair<Individual<Integer>,Individual<Integer>> reproduce2
	(Individual<Integer> x, Individual<Integer> y) {

		int c = randomOffset(individualLength);

		List<Integer> childRepresentation1 = new ArrayList<Integer>();
		childRepresentation1.addAll(x.getRepresentation().subList(0, c));
		childRepresentation1.addAll(
				y.getRepresentation().subList(c, individualLength));
		List<Integer> childRepresentation2 = new ArrayList<Integer>();
		childRepresentation2.addAll(y.getRepresentation().subList(0, c));
		childRepresentation2.addAll(
				x.getRepresentation().subList(c, individualLength));

		return new Pair<>(new IndividuoExtendido(childRepresentation1), 
				new IndividuoExtendido(childRepresentation2));
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

	public void setProbMutacion(double probMut) {
		this.mutationProbability = probMut;
	}

	public double getProbMutacion() {
		return this.mutationProbability;
	}

}
