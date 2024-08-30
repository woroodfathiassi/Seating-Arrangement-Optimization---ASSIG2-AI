package Test0;

import java.util.ArrayList;

public class Genetic implements Comparable<Genetic> {
	private ArrayList<Integer> seating;
	private Double cost;

	public Genetic(ArrayList<Integer> seating, Double cost) {
		this.seating = seating;
		this.cost = cost;
	}

	public ArrayList<Integer> getSeating() {
		return seating;
	}

	public void setSeating(ArrayList<Integer> seating) {
		this.seating = seating;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Genetic [seating=" + seating + ", cost=" + cost + "]\n";
	}

	@Override
	public int compareTo(Genetic o) {
		return this.cost.compareTo(o.getCost());
	}

}
