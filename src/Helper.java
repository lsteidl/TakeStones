public class Helper {
	
	/** 
    * Class constructor.
    */
	private Helper () {}

	/**
	* This method is used to check if a number is prime or not
	* @param x A positive integer number
	* @return boolean True if x is prime; Otherwise, false
	*/
	public static boolean isPrime(int x) {
		int check = x - 1;
		boolean prime = true; // assume x is prime
		while( check > 1){
			if((x % check) == 0){ 
				prime = false; // x is not prime when factor is found
			}
			check--;
		}
		// TODO Add your code here
		return prime;
	}

	/**
	* This method is used to get the largest prime factor 
	* @param x A positive integer number
	* @return int The largest prime factor of x
	*/
	public static int getLargestPrimeFactor(int x) {

    	// TODO Add your code here
		int check = x - 1; // begin here
		while( check > 1) {
			if( (x % check) == 0){
				if(isPrime(check)){
					// factor found
					return check;	
				}
			}
			// factor not found, iterate
			check--;
		}
		return -1;

    }
}