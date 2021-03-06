package juego;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;
import javafx.util.Pair;

/*
 * 
 * En esta clase se localizan diversos m�todos para calcular poblaciones
 * e individuos iterando sobre un grupo de configuraciones.
 * 
 * 
 * No se recomienda utilizarlas m�s que para obtener estos datos.
 */

public class Demo {

	private static final List<Integer> alfabetoNum = 
			Arrays.asList(1,2,3,4,5,6,7,8,9,10,25,50);

	private static final List<Integer> alfabetoOp = 
			Arrays.asList(0,1,2,3);

	private static final double VAR_PROB=0.1;

	private static final int TAM_POBLACION=16;

	private static final int TAM_POBLACION_OPT[] = {4,8,16,32,64,128};

	private static final int NUM_OPERADORES=5;

	private static final Integer MAX_NUMERO=900;

	private static final int TAM_INDIVIDUO = 2*NUM_OPERADORES+1;

	private static final double MUTATION_PROB=0.7;

	private static final double CRUCE_PROB=0.8;

	private static final double MUTATION_PROB_ITERATE=0.0;

	private static final double CRUCE_PROB_ITERATE=0.0;

	private static final Integer objetivo = new Random().nextInt(MAX_NUMERO)+100;

	private static final List<Pair<Boolean, Boolean>> decision = new ArrayList<>();
	private static final long TIME[]= {1000L, 5000L};

	public static void main(String[] args) {
		init();
		algoritmoGenetico();
		//algoritmoGeneticoProbabilidad();
		//algoritmoGeneticoPoblacion();
		//algoritmoGeneticoIterar();
		//algoritmoGeneticoDecision();
		//algoritmoGeneticoMuestra();
		
	}

	private static void algoritmoGeneticoMuestra() {
		AlgoritmoGeneticoExtendido ga = new AlgoritmoGeneticoExtendido
				(TAM_INDIVIDUO, alfabetoNum,alfabetoOp);

		System.out.println(objetivo);
		update(ga,MUTATION_PROB,CRUCE_PROB,true,false);


		for(int ctrl=0;ctrl<5;ctrl++) {
			Set<Individual<Integer>> poblacion = generarPoblacion();
			for(Individual<Integer> i: poblacion) {
				System.out.println(i);
			}
			print(poblacion, ga);
			System.out.println("\n-------------------\n");
			print(poblacion, ga);
			System.out.println("\n-------------------\n");
			//System.out.println();
		}


	}

	private static void algoritmoGeneticoDecision() {
		AlgoritmoGeneticoExtendido ga = new AlgoritmoGeneticoExtendido
				(TAM_INDIVIDUO, alfabetoNum,alfabetoOp);

		System.out.println(objetivo);
		for(int k=0; k<decision.size(); ++k) {
			System.out.println("�nico : "+decision.get(k).getKey()+
					" destructivo: "+decision.get(k).getValue()+
					"\n-------------------\n");
			update(ga,MUTATION_PROB,CRUCE_PROB,decision.get(k).getKey(), decision.get(k).getKey());
			for(int ctrl=0;ctrl<25;ctrl++) {
				//System.out.println("Iteracion: "+ctrl);
				printIterate(generarPoblacion(), ga, 100L);
				//System.out.println();
			}

		}
	}



	private static void algoritmoGeneticoPoblacion() {
		AlgoritmoGeneticoExtendido ga = new AlgoritmoGeneticoExtendido
				(TAM_INDIVIDUO, alfabetoNum,alfabetoOp);
		update(ga,MUTATION_PROB,CRUCE_PROB,true, true);
		System.out.println(objetivo);
		for(int k=0; k<TAM_POBLACION_OPT.length; ++k) {
			System.out.println("Poblacion : "+TAM_POBLACION_OPT[k]+
					"\n-------------------\n");
			for(int ctrl=0;ctrl<10;ctrl++) {
				//System.out.println("Iteracion: "+ctrl);
				printIterate(generarPoblacion(TAM_POBLACION_OPT[k]), ga, 100L);
				//System.out.println();
			}

		}
	}

	private static void algoritmoGeneticoProbabilidad() {

		AlgoritmoGeneticoExtendido ga = new AlgoritmoGeneticoExtendido
				(TAM_INDIVIDUO, alfabetoNum,alfabetoOp);
		System.out.println(objetivo);
		update(ga,0,0,true, true);
		for(int i=0;i< 11; ++i) {
			for(int j=0;j<11;++j) {
				update(ga,i*VAR_PROB,j*VAR_PROB);
				System.out.println("Probabilidad mut: "+ ga.getProbMutacion()+
						" Probabilidad cruce: "+ga.getProbCruce()+
						"\n-------------------\n");
				for(int ctrl=0;ctrl<10;ctrl++) {
					printIterate(generarPoblacion(), ga, 100L);
				}
			}
		}



	}



	private static void init() {

		decision.add(new Pair<Boolean, Boolean>(true, true));
		decision.add(new Pair<Boolean, Boolean>(true, false));
		decision.add(new Pair<Boolean, Boolean>(false, true));
		decision.add(new Pair<Boolean, Boolean>(false, false));
	}

	private static void update(AlgoritmoGeneticoExtendido ga, double incr_prob, double incr_cruce,
			boolean unique, boolean destructive) {
		ga.setProbCruce(CRUCE_PROB_ITERATE+incr_cruce);
		ga.setHijoUnico(unique);
		ga.setDestructive(destructive);
		ga.setProbMutacion(MUTATION_PROB_ITERATE+incr_prob);
	}

	private static void update(AlgoritmoGeneticoExtendido ga, 
			double incr_prob, double incr_cruce) {
		update(ga, incr_prob, incr_cruce, ga.isHijoUnico(), ga.isDestructive());
	}


	private static void algoritmoGeneticoIterar() {

		AlgoritmoGeneticoExtendido ga = new AlgoritmoGeneticoExtendido
				(TAM_INDIVIDUO, alfabetoNum,alfabetoOp);

		for(int k = 0; k < decision.size(); ++k) {
			update(ga,0,0,decision.get(k).getKey(), decision.get(k).getValue());
			for(int i=0; i<10; ++i) {
				for(int j=0; j<10;++j) {
					for(int t=0; t < TIME.length; ++t) {
						File file = new File("resultados "+k+"/Resultado (unico ="
								+decision.get(k).getKey()+", destructivo = "
								+decision.get(k).getValue() +", prob ="+i 
								+", cruce ="+j+", mode ="+t+", objetivo ="
								+objetivo+") .out");
						FileOutputStream fos=null;
						try {
							fos = new FileOutputStream(file);
							PrintStream ps = new PrintStream(fos);
							System.setOut(ps);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						update(ga,i*VAR_PROB,j*VAR_PROB);				
						for(int ctrl=0;ctrl<10;ctrl++) {
							printIterate(generarPoblacion(), ga, TIME[t]);
						}

						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}
		}



	}

	private static void print(Set<Individual<Integer>> population, AlgoritmoGeneticoExtendido ga) {
		FitnessFunction<Integer> fitnessFunction = Factoria.getFitnessFunction
				(alfabetoNum, alfabetoOp, objetivo);
		GoalTest<Individual<Integer>> goalTest =
				Factoria.getGoalTest((FitnessExtendida) fitnessFunction);
		// Run for a set amount of time
		IndividuoExtendido bestIndividual = (IndividuoExtendido) 
				ga.geneticAlgorithm(population, fitnessFunction, goalTest, 100L);

		System.out.println("Objective		= "+objetivo);
		System.out.println("Max Time (0.1 second) Best Individual= "
				+bestIndividual+" \n");
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.test(bestIndividual));

		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations      = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");
		System.out.println("\n-------------------------------\n");
		// Run till goal is achieved
		bestIndividual = (IndividuoExtendido) 
				ga.geneticAlgorithm(population, fitnessFunction, goalTest, 5000L);

		System.out.println("");
		System.out.println("Goal Test Best Individual= "+bestIndividual+"\n" );
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations       = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");
	}


	private static void printIterate(Set<Individual<Integer>> population, 
			AlgoritmoGeneticoExtendido ga, long t) {
		FitnessFunction<Integer> fitnessFunction = Factoria.getFitnessFunction
				(alfabetoNum, alfabetoOp, objetivo);
		GoalTest<Individual<Integer>> goalTest =
				Factoria.getGoalTest((FitnessExtendida) fitnessFunction);
		// Run for a set amount of time
		IndividuoExtendido bestIndividual = (IndividuoExtendido) 
				ga.geneticAlgorithm(population, fitnessFunction, goalTest, t);


		System.out.print(fitnessFunction.apply(bestIndividual)+"\t");
		System.out.print(goalTest.test(bestIndividual)+"\t");
		System.out.print(ga.getIterations()+"\t");
		System.out.print(ga.getTimeInMilliseconds() + "\n");
	}

	private static Set<Individual<Integer>> generarPoblacion(){
		return generarPoblacion(TAM_POBLACION);
	}

	private static Set<Individual<Integer>> generarPoblacion(int poblacion){
		Set<Individual<Integer>> population = new HashSet<>();
		for (int i = 0; i < poblacion; i++) {
			population.add(Factoria.generateRandomIndividual
					(alfabetoNum, alfabetoOp, TAM_INDIVIDUO));
		}
		return population;
	}


	private static void algoritmoGenetico() {
		System.out.println("\nPr�ctica 4 - GeneticAlgorithm  -->");
		try {


			// Generate an initial population
			Set<Individual<Integer>> population = generarPoblacion();
			AlgoritmoGeneticoExtendido ga = new AlgoritmoGeneticoExtendido
					(TAM_INDIVIDUO, alfabetoNum,alfabetoOp);
			ga.setProbCruce(CRUCE_PROB);
			ga.setHijoUnico(false);
			ga.setDestructive(false);
			ga.setProbMutacion(MUTATION_PROB);
			print(population, ga);




		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
