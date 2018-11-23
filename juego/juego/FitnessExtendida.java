package juego;

import java.util.List;
import java.util.function.BiFunction;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

public class FitnessExtendida implements FitnessFunction<Integer> {

	protected List<Integer> alfabetoOperadores;

	protected List<Integer> alfabetoNumeros;

	protected Integer objetivo;





	public FitnessExtendida(List<Integer> alfabetoOperadores, List<Integer> alfabetoNumeros, Integer objetivo) {
		super();
		this.alfabetoOperadores = alfabetoOperadores;
		this.alfabetoNumeros = alfabetoNumeros;
		this.objetivo = objetivo;
	}

	private BiFunction<Integer, Integer,  Integer> parser(Integer op){
		switch(op) {
		case 0: return (x,y)->x+y;
		case 1: return (x,y)->x-y;
		case 2: return (x,y)->x*y;
		case 3: return (x,y)->x/y;
		default: return null;
		}

	}

	@Override
	public double apply(Individual<Integer> arg) {
		List<Integer> datos = arg.getRepresentation();
		Integer provisional=datos.get(0);
		try {
			for(int i=1;i<datos.size(); i = i+2) {
				provisional = parser(datos.get(i)).apply(provisional, datos.get(i+1));
			}
		}catch(Exception e) {
			provisional = 0;
		}
		if(provisional == objetivo) return Integer.MAX_VALUE;
		return 1 / (double) Math.abs(provisional-objetivo);

	}



}
