package Test0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class index {

	static String[] people = { "Ahmed", "Salem", "Ayman", "Hani", "Kamal", "Samir", "Hakam", "Fuad", "Ibrahim",
			"Khalid" };

	static double[][] disliked = {
		    { 0.0, 68.0, 55.0, 30.0, 82.0, 48.0, 33.0, 10.0, 76.0, 43.0 },
		    { 68.0, 0.0, 90.0, 11.0, 76.0, 20.0, 55.0, 17.0, 62.0, 99.0 },
		    { 55.0, 90.0, 0.0, 70.0, 63.0, 96.0, 51.0, 90.0, 88.0, 64.0 },
		    { 30.0, 11.0, 70.0, 0.0, 91.0, 86.0, 78.0, 99.0, 53.0, 92.0 },
		    { 82.0, 76.0, 63.0, 91.0, 0.0, 43.0, 88.0, 53.0, 42.0, 75.0 },
		    { 48.0, 20.0, 96.0, 86.0, 43.0, 0.0, 63.0, 97.0, 37.0, 26.0 },
		    { 33.0, 55.0, 51.0, 78.0, 88.0, 63.0, 0.0, 92.0, 87.0, 81.0 },
		    { 10.0, 17.0, 90.0, 99.0, 53.0, 97.0, 92.0, 0.0, 81.0, 78.0 },
		    { 76.0, 62.0, 88.0, 53.0, 42.0, 37.0, 87.0, 81.0, 0.0, 45.0 },
		    { 43.0, 99.0, 64.0, 92.0, 75.0, 26.0, 81.0, 78.0, 45.0, 0.0 }
		};

	public static void main(String[] args) {

		ArrayList<Integer> gen = geneticAlgorithm(100, 1000, 0.1);
		System.out.println("\nGenetic Algorithm:");
		System.out.println("Best seating arrangement: " + finalSeating(gen).toString());
		System.out.println("Total cost: " + calculateCost(gen));
		// _________________________________________________
		ArrayList<Integer> simulate = simulatedAnnealing(1000,.99,10000);
		System.out.println("\nSimulated Annealing:");
		System.out.println("Best seating arrangement: " + finalSeating(simulate).toString());
		System.out.println("Total cost: " + calculateCost(simulate));
		// ________________________________________________
		ArrayList<Integer> hill = hillClimbing(100);
		System.out.println("\nHill Climbing:");
		System.out.println("Best seating arrangement: " + finalSeating(hill).toString());
		System.out.println("Total cost: " + calculateCost(hill));

	}

	private static double calculateCost(ArrayList<Integer> arrangement) {
		double cost = disliked[0][arrangement.size() - 1] ;
		for (int i = 0; i < people.length - 1; i++) {
			cost += disliked[arrangement.get(i)][arrangement.get(i + 1)];
		}
		return cost;
	}

	private static ArrayList finalSeating(ArrayList arrang) {
		ArrayList seating = new ArrayList<>();
		for (int i = 0; i < arrang.size(); i++) {
			seating.add(people[(int) arrang.get(i)]);
		}
		return seating;
	}

	private static ArrayList<Integer> hillClimbing(int restart) {
		ArrayList<Integer> bestArrang = null;
		double bestCost = Double.MAX_VALUE;

		for (int num = 0; num < restart; num++) {
			// Initial solution
			ArrayList<Integer> currentArrang = new ArrayList<>();

			for (int i = 0; i < people.length; i++) {
				currentArrang.add(i);
			}

			Collections.shuffle(currentArrang);
			double currentCost = calculateCost(currentArrang);

			// Neighbors
			ArrayList<Integer> temp = new ArrayList<>(currentArrang);
			for (int i = 0; i < people.length; i++) {
				for (int j = i + 1; j < people.length; j++) {
					if (i == j)
						continue;
					ArrayList<Integer> tempArrang = new ArrayList<>(currentArrang);
					Collections.swap(tempArrang, i, j);
					double tempCost = calculateCost(tempArrang);
					if (tempCost < currentCost) {
						currentCost = tempCost;
						temp = new ArrayList<>(tempArrang);
					}
				}

			}
			if (calculateCost(temp) < bestCost) {
				bestCost = calculateCost(temp);
				bestArrang = new ArrayList<>(temp);
//				System.out.println(bestArrang.toString() + " " + bestCost);
			}
		}

		return bestArrang;
	}

	private static ArrayList<Integer> geneticAlgorithm(int populationSize, int numGenerations, double mutationRate) {
		// Population initialization:
		ArrayList<Genetic> initialPopulation = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			ArrayList<Integer> population = new ArrayList<>();
			for (int j = 0; j < people.length; j++) {
				population.add(j);
			}

			Collections.shuffle(population);
			double cost = calculateCost(population);
			initialPopulation.add(new Genetic(population, cost));
		}
		// Selection:
		Collections.sort(initialPopulation);
		initialPopulation = new ArrayList<>(
				initialPopulation.subList(0, Math.min(populationSize * 60 / 100, initialPopulation.size())));

		// cross over:
		ArrayList<Integer> bestSeating = null;
		double bestCost = Double.MAX_VALUE;
		for (int generation = 0; generation < numGenerations; generation++) {
			int mutate = 0;
			for (int i = 0; i < initialPopulation.size() - 1; i++) {

				for (int j = i + 1; j < initialPopulation.size(); j++) {
					ArrayList child1 = new ArrayList<>(initialPopulation.get(i).getSeating().subList(0, 5));
					ArrayList child2 = new ArrayList<>(initialPopulation.get(j).getSeating().subList(5, 10));
					for (int a = 0; a < child2.size(); a++) {
						if (!child1.contains(child2.get(a))) {
							child1.add(child2.get(a));
						} else {
							for (int b = 0; b < people.length; b++) {
								if (!child1.contains(b) && !child2.contains(b)) {
									child1.add(b);
									break;
								}
							}
						}
					}

					if (mutate < (numGenerations * 0.1)) {
						Random random = new Random();
						int index1 = random.nextInt(child1.size());
						int index2 = random.nextInt(child1.size());
						Collections.swap(child1, index1, index2);
						mutate++;
					}
					double tempCost = calculateCost(child1);
					if (tempCost < bestCost) {
						bestCost = tempCost;
						bestSeating = new ArrayList<>(child1);
//						System.out.println("Generation: " +child1+" "+bestCost);
					}

					////////////////////////////
					ArrayList child22 = new ArrayList<>(initialPopulation.get(i).getSeating().subList(5, 10));
					ArrayList child11 = new ArrayList<>(initialPopulation.get(j).getSeating().subList(0, 5));
					for (int a = 0; a < child11.size(); a++) {
						if (!child22.contains(child11.get(a))) {
							child22.add(child11.get(a));
						} else {
							for (int b = 0; b < people.length; b++) {
								if (!child22.contains(b) && !child11.contains(b)) {
									child22.add(b);
									break;
								}
							}
						}
					}

					if (mutate < (numGenerations * 0.1)) {
						Random random = new Random();
						int index1 = random.nextInt(child22.size());
						int index2 = random.nextInt(child22.size());
						Collections.swap(child22, index1, index2);
						mutate++;
					}
					tempCost = calculateCost(child22);
					if (tempCost < bestCost) {
						bestCost = tempCost;
						bestSeating = new ArrayList<>(child22);
//						System.out.println("Generation: " +child22+"  "+bestCost);
					}
				}

			}
		}
		return bestSeating;
	}

	private static ArrayList<Integer> simulatedAnnealing(int initialTemperature, double coolingRate,
			int numIterations) {
		ArrayList<Integer> bestArrang = null;
		double bestCost = Double.MAX_VALUE;

		// Initial solution:
		ArrayList<Integer> currentArrang = new ArrayList<>();
		for (int i = 0; i < people.length; i++) {
			currentArrang.add(i);
		}
		Collections.shuffle(currentArrang);
		double currentCost = calculateCost(currentArrang);

		double T = initialTemperature;
		for (int num = 0; num < numIterations; num++) {
			if (T <= 0)
				break;

			ArrayList tempArrang = new ArrayList<>(currentArrang);
			Random random = new Random();
			int index1 = random.nextInt(currentArrang.size());
			int index2 = random.nextInt(currentArrang.size());
			Collections.swap(tempArrang, index1, index2);
			double tempCost = calculateCost(tempArrang);

			double E = currentCost - tempCost;
			if (E > 0) {
				currentCost = tempCost;
				currentArrang = new ArrayList<>(tempArrang);
			} else {
				if (Math.exp(E / T) < 1) {
					currentCost = tempCost;
					currentArrang = new ArrayList<>(tempArrang);
				}
			}

			// Update the best solution found
			if (currentCost < bestCost) {
				bestArrang = new ArrayList<>(currentArrang);
				bestCost = currentCost;
//                System.out.println(bestArrang.toString()+" "+bestCost);	
			}

			T *= coolingRate;
		}
		return bestArrang;
	}

}