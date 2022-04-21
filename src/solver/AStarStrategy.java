package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class AStarStrategy extends SearchMethod {
	public AStarStrategy() {
		code = "AStar";
		longName = "AStar Search";
		Frontier = new LinkedList<PuzzleState>();
		Searched = new LinkedList<PuzzleState>();
	}
	
	@Override
	public direction[] Solve(nPuzzle puzzle) {
		// The formula for this is:
		// f(n) = g(n) + h(n)
		addToFrontier(puzzle.StartState);
		System.out.println("(A*) solving...");
		while (!Frontier.contains(puzzle.GoalState)) {
			// Searched.add(PrioQueue.pollFirst());
			ArrayList<PuzzleState> newStates = Frontier.getLast().explore();
			PuzzleState currentState = Frontier.getFirst();
			currentState.HeuristicValue = HeuristicValue(currentState, puzzle.GoalState);
			currentState.setEvaluationFunction(currentState.Cost + currentState.HeuristicValue);
			// update each newly explored state with new evaluation value(g(n) + h(n))
			for (int i = 0; i < newStates.size(); i++) {
				PuzzleState newState = newStates.get(i);
				newState.HeuristicValue = HeuristicValue(newState, puzzle.GoalState);
				newState.setEvaluationFunction(newState.Cost + newState.HeuristicValue);
				addToSearched(newState);

				Collections.sort(Searched, new PuzzleComparator());
				addToFrontier(Searched.get(0));
			}
		}
		Collections.sort(Frontier, new PuzzleComparator());
		// get 2nd best state from the list. (1st is the solution of course)
		return Frontier.getFirst().GetPathToState();
	}

	protected boolean addToSearched(PuzzleState aState) {
		if (!Frontier.contains(aState) || !Searched.contains(aState)) {
			Searched.addLast(aState);
		}
		return true;
	}

	public boolean addToFrontier(PuzzleState aState) {
		if (!Frontier.contains(aState)) {
			Frontier.addLast(aState);
			return true;
		}
		return false;
	}

	@Override
	protected PuzzleState popFrontier() {
		// TODO Auto-generated method stub
		return null;
	}

	private int HeuristicValue(PuzzleState aState, PuzzleState goalState) {
		// find out how many elements in aState match the goalState
		// return the number of elements that don't match
		int heuristic = 0;
		for (int i = 0; i < aState.Puzzle.length; i++) {
			for (int j = 0; j < aState.Puzzle[i].length; j++) {
				if (aState.Puzzle[i][j] != goalState.Puzzle[i][j])
					heuristic++;
			}
		}

		return heuristic;
	}
}
