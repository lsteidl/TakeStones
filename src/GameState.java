import java.util.ArrayList; // I added
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private int size;            // The number of stones
    private boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size) {

        this.size = size;

        //  For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
        	//System.out.println("All stones set : ");
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
        // TODO Add your code here
    	List<Integer> moves = new ArrayList<Integer>();
    	// First move: odd number stone, must be less than n/2 (size/2)
    	if(lastMove == -1){
    		//System.out.println("first move");
    		int next = 1; // start with first odd number
    		while(next <= (size/2)){ // 
    			if(stones[next] == true)
    				moves.add(next); // 
    			next = next + 2; // increase to next odd number
    		}
    	}
    	else {
    	//	System.out.println("last move");
    	//Non-First Move: must be multiple or factor of last move
    	int check;
    	// find factors
    	
    	check = lastMove - 1; // check first potential factor
    	// iterate through each stone below last
    	while(check > 0){
    		if(lastMove % check == 0){ // check if factor
    			// add factors to list
    			// but only if not already taken
    			if(stones[check] == true){
    				
    				moves.add(check);
    			//	System.out.println("Adding factor to children : " + check);
    			}
    			
    		}
    		check--; // decrement to next lowest integer
    	}
    	// find multiples
    	check = lastMove * 2; // check first multiple
    	int i = 2;
    	// iterate through each stone above last
    	while(check <= size){
    		// add multiple to list
    		// but only if not already taken
    		//System.out.println("CHeck ==" + check);
    		if(stones[check] == true){
    			moves.add(check);
    			//System.out.println("Adding multiple to children : " + check);
    		}
    		else {
    			//System.out.println("Already multi taken: " + check);
    		}
    		i++; 
    		check = lastMove * i; // calculate next potential multiple
    	}
    	for(int j = 0; j < moves.size(); j++){
    		//System.out.println(moves.get(j));
    	}
    		
    	}
		//System.out.println("DONE, returning " + moves.size() + " moves");
		Collections.sort(moves);
    	return moves; 
    }

    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
            GameState state = new GameState(this); // changed from "var state" to "GameState state"
            state.removeStone(move);
            return state;
        }).collect(Collectors.toList());
    }


    /**
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     */
    public double evaluate() {
        // TODO Add your code here
    	
    //	System.out.println("Evaluating: ");
    	List <Integer> successors = getMoves();
    //	System.out.println("# successors = " + successors.size());
    // End-Game State
    	if(successors.size() == 0){

    		if(getTurn()){
    			// Max player's turn, Min wins
    		//	System.out.println("EVal Returning -1.0");
    			return -1.0;
    		}
    		else {
    		//	System.out.println("Eval Returning 1.0");
    			return 1.0;
    		}
    	}
    	//Non-Final States..
    	// If stone 1 is not yet taken
    	if(stones[1] == true){
    			return 0.0;	
    	}
    	// If last move was 1, count number of possible successors
    	//List <Integer> successors = getMoves();
    	if(lastMove == 1){
    		//List <Integer> successors = getMoves();
    		if(successors.size() % 2 == 0){
    			// even successors
    			if(getTurn())
    				return -0.5;
    			else
    				return 0.5;
    		}
    		else{
    			if(getTurn())
    				return 0.5;
    			else
    				return -0.5;
    		}
    	} 
    	// If last move was prime, count multiples of that prime in all possible successors
    	int count = 0;
    	if(Helper.isPrime(lastMove)){
    		count = 0;
    		for(int i = 0; i < successors.size(); i++){
    			if( successors.get(i) % lastMove == 0){
    				count++;
    			}
    		}
    		if(count % 2 == 0){
    			// even
    			if(getTurn())
    				return -0.7;
    			else
    				return 0.7;
    		}
    		else{
    			//odd
    			if(getTurn())
    				return 0.7;
    			else 
    				return -0.7;
    		}
    	}
    	else{
    		// last move was not prime (composite)
    		// find largest prime that can divide lastMove
    		int largestPrime = Helper.getLargestPrimeFactor(lastMove);
    		//System.out.println("LAST MOVE: " + lastMove);
    		//System.out.println("LARGEST PRIME: " + largestPrime);
    		// count multiples of that prime
    		count = 0;
    		for(int i=0; i < successors.size(); i++){
    			//System.out.println("Successor: " + i + " = " + successors.get(i));
    			if( (successors.get(i) % largestPrime) == 0){
    				count++;
    			}
    			if(successors.get(i) == largestPrime){
    				//System.out.println("SELF COUNTED++++++++++++");
    			}
    		}
    		//System.out.println("Count: " + count);
    		if(count % 2 == 0){
    			// even
    			if(getTurn())
    				return -0.6;
    			else
    				return 0.6;
    		}
    		else{
    			//odd
    			if(getTurn())
    				return 0.6;
    			else
    				return -0.6;
    		}
    		
    	}
       // return 0.0;
    }

    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
    	//System.out.println("REMOVING STONE: " + idx);
        this.stones[idx] = false;
        this.lastMove = idx;
      
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
    	//System.out.println("SET STONE: " + idx);
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }
    // returns true is MAX
    // false if MIN
    public boolean getTurn(){
    	int count = 0;
    	for( int i = 1; i < stones.length; i++){
			if(stones[i] == false){
				count++;
			}
		}
    	if(count % 2 == 0) {
    		// even, MAX turn
    		return true;
    	}
    	else return false;
    	
    }
    // next move
/*    public int getNextMove(double val){
    	int move = 0;
    	List<GameState> successors = getSuccessors();
    	for (int i = 0; i < successors.size(); i++) {
			GameState game = successors.get(i);
			if(game.evaluate() == val){
				move = game.getLastMove();
				break;
			}
    	}
    	return move;
    }
*/
    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }

}	
