package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class DFSStrategy extends SearchMethod {
	public DFSStrategy() {
		code = "DFS";
		longName = "DFS Search";
		Frontier = new LinkedList<PuzzleState>();
		Searched = new LinkedList<PuzzleState>();
		iteration = 0;
	}
	
	private int iteration;

	public boolean addToFrontier(PuzzleState aState) {
		if (!Searched.contains(aState) || !Frontier.contains(aState))
			Frontier.add(aState);
		return !Searched.contains(aState) || !Frontier.contains(aState);
	}

	@Override
	protected PuzzleState popFrontier() {
		// remove an item from the fringe to be searched
		PuzzleState thisState = Frontier.pop();
		// Add it to the list of searched states, so that it isn't searched again
		Searched.add(thisState);

		return thisState;
	}

	protected boolean addToSearched(PuzzleState aState) {
		if (!Searched.contains(aState)) {
			Searched.addLast(aState);
		}
		return true;
	}

	protected PuzzleState popFromExplored(PuzzleState node) {
		return Searched.pop();
	}

	@Override
	public direction[] Solve(nPuzzle puzzle) {

		// Set initial state
		// Frontier will be used as a "stack" where we will put the states that will eventually lead to the solution. 
		Frontier.add(puzzle.StartState);
		System.out.println(Frontier.getLast());


		while (Frontier.getLast() != puzzle.GoalState) {
			System.out.println("iteration " + iteration++);
			if (Frontier.getLast().equals(puzzle.GoalState)) {
				// Solution!
				return Frontier.getLast().GetPathToState();
			}

			// Start Exploration
			ArrayList<PuzzleState> statesToExplore = Frontier.getLast().explore();

			// Remove explored states from list of statesToExplore
			statesToExplore = filterExploredStates(statesToExplore);

			if (statesToExplore.size() == 0) {
				// if the newly explored state doesn't have anywhere else to explore, 
				// take a step back, (remove last item from Frontier, which acts as our stack)
				System.out.println("no more nodes to explore, remove last: " + statesToExplore);
				Frontier.removeLast();
			} else {
				
				
				PuzzleState nextStateToExplore = statesToExplore.get(getRandomNumber(0,statesToExplore.size()));
				addToFrontier(nextStateToExplore);
				addToSearched(nextStateToExplore);
				System.out.println(Searched.size());
			}

		}

		return null;
	}

	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	public ArrayList<PuzzleState> filterExploredStates(ArrayList<PuzzleState> newNodes) {
		// Remove explored nodes from list of newNodes
		for (int i = 0; i < newNodes.size(); i++) {
			if (Searched.contains(newNodes.get(i))) {
				newNodes.remove(newNodes.get(i));
			}
		}
		return newNodes;
	}

}
