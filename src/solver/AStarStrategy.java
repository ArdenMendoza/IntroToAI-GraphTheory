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
		Frontier.add(puzzle.StartState);
		System.out.println("(A*) solving...");
		while (!Searched.contains(puzzle.GoalState)) {
			Searched.add(Frontier.pollFirst());
			ArrayList<PuzzleState> newStates = Searched.getFirst().explore();
			// update each newly explored state with new evaluation value
			for (int i = 0; i < newStates.size(); i++) {
				PuzzleState newState = newStates.get(i);
				newState.HeuristicValue = HeuristicValue(newState, puzzle.GoalState);
				newState.setEvaluationFunction(newState.HeuristicValue + newState.Cost);
				addToFrontier(newState);
				addToSearched(newState);
			}
		}
		Collections.sort(Searched, new PuzzleComparator());
		// get 2nd best state from the list. (1st is the solution of course)
		return Searched.get(1).GetPathToState();
	}

	protected boolean addToSearched(PuzzleState aState) {
		if (!Searched.contains(aState)) {
			Searched.addLast(aState);
		}
		return true;
	}

	@Override
	public boolean addToFrontier(PuzzleState aState) {
		// TODO Auto-generated method stub
		if (!Searched.contains(aState) || !Frontier.contains(aState))
			Frontier.add(aState);
		return !Searched.contains(aState) || !Frontier.contains(aState);
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
