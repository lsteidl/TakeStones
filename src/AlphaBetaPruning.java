
import java.util.ArrayList;
import java.util.List;

public class AlphaBetaPruning {


    private int move;
    private double value;
    private double visited;
    private double evaluated;
    private double maxDepthReached;
    private double maxAllowedDepth;
    private double aebf;
    private List<Double> level = new ArrayList<Double>();
    
	public AlphaBetaPruning() {
    	//this.maxAllowedDepth = depth;
    }
	 public void incrementVisited() {
	        this.visited++;
	    }
    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        // TODO Add your code here
    	System.out.println("Move: " + this.move);
    	System.out.println("Value: " + this.value); 
    	System.out.println("Number of Nodes Visited: " + (int)this.visited);
    	System.out.println("Number of Nodes Evaluated: " + (int)this.evaluated);
    	System.out.println("Max Depth Reached: " + (int)maxDepthReached);
    	aebf = 0.0;
    	this.aebf = (this.visited - 1)/(this.visited - this.evaluated);//#totalvisited - 1)/ (#totalvisited - #evaluated
    	System.out.println("Avg Effective Branching Factor: " + aebf);
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
        // TODO Add your code here
    //	System.out.println("Max ALLOWED depth: " + depth);
    	maxAllowedDepth = depth;
    	List<GameState> successors = state.getSuccessors();
    	
    	this.value = alphabeta(state, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, state.getTurn());
    	for (int i = 0; i < level.size(); i++) {
    		if(level.get(i) == this.value){
    //			System.out.println("VALUE MATCH @ " + this.value);
    			this.move = successors.get(i).getLastMove();
    			break;
    		}
    	}

    
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
        // TODO Add your code here
    	
    	incrementVisited();
    	if( depth > maxDepthReached)
    		maxDepthReached = depth;
    	List<GameState> successors = state.getSuccessors();
    //	System.out.println("MAX allowed ===== " + maxAllowedDepth);
    	if(successors.size() == 0 || (depth == maxAllowedDepth)){ // && this.maxAllowedDepth != 0)){//this.maxAllowedDepth && this.maxAllowedDepth != 0)) {
    		this.evaluated++;
    	//	System.out.println("Max allowed DEPTH = " + this.maxAllowedDepth + " =  " + depth);
    	//	System.out.println("BASE CASE returning: " + state.evaluate());
    		if(depth == 1){
				level.add(state.evaluate());
			}
			return state.evaluate();
		}
    	if(maxPlayer){

    		double v = Double.NEGATIVE_INFINITY;
    		//List<GameState> successors = state.getSuccessors();
		//	System.out.println("max call: alpha: " + alpha + " beta: " + beta  + " children: " + successors.size());

    		// loop through all children
			for (int i = 0; i < successors.size(); i++) {
				GameState game = successors.get(i);
				v = Math.max(v, alphabeta(game, depth + 1, alpha, beta, false));
				if(v >= beta){
					return v;
				}
				alpha = Math.max(alpha, v);
			}
		//	System.out.println("Max Returning: " + v);
			if(depth == 1){
				level.add(v);
			}
			return v;
    	}
    	else{ // minPlayer
    		
    		double v = Double.POSITIVE_INFINITY;
    		//List<GameState> successors = state.getSuccessors();
			//System.out.println("min call: alpha: " + alpha + " beta: " + beta  + " children: " + successors.size());
			for (int i = 0; i < successors.size(); i++) {
				GameState game = successors.get(i);
				v = Math.min(v, alphabeta(game, depth + 1, alpha, beta, true));
				if(v <= alpha){
					return v;
				}
				beta = Math.min(beta, v);
			}
			//System.out.println("Min Returning: " + v);
			if(depth == 1){
				level.add(v);
			}
			return v;
						
    	}
    }
}
