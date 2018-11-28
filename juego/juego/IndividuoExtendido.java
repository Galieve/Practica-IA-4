package juego;

import java.util.List;

import aima.core.search.local.Individual;

public class IndividuoExtendido extends Individual<Integer> {

	public IndividuoExtendido(List<Integer> representation) {
		super(representation);
	}
	
	public IndividuoExtendido(Individual<Integer> ind) {
		super(ind.getRepresentation());
	}

	private String conversorOp(Integer i) {
		switch(i) {
		case 0:return "+";
		case 1:return "-";
		case 2:return "*";
		case 3:return "/";
		default: return null;
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		int resultadoActual=this.getRepresentation().get(0);
		sb.append(this.getRepresentation().get(0));
		for(int i=1;i<this.getRepresentation().size();i=i+2) {
			sb.append(" ");
			sb.append(conversorOp(this.getRepresentation().get(i)));
			sb.append(" ");
			sb.append(this.getRepresentation().get(i+1));
			resultadoActual=FitnessExtendida.parser(this.getRepresentation().get(i)).
					apply(resultadoActual, this.getRepresentation().get(i+1));
		}
		sb.append(" = "+resultadoActual);
		return sb.toString();
		
	}
}
