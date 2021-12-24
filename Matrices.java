public class Matrices {
	private double[][] matrix;
	private int rows;
	private int columns;
	
//Constructors
	public Matrices(double[][] matrix) {
		this.matrix = matrix;
		this.rows = matrix.length;
		this.columns = matrix[0].length;
	}
	public Matrices(int rows, int columns) {
		this.matrix = new double[rows][columns];
		this.rows = rows;
		this.columns = columns;
	}

	public Matrices(String nums, int rows, int columns) {
		this.matrix = stringConverter(nums, rows, columns);
		this.rows = rows;
		this.columns = columns;
	}
	
//Converts string of numbers separated by commas into 2d array of numbers
	
	public double[][] stringConverter(String nums, int rows, int columns) {
		String[] numsArray = nums.split(",");
		double[][] doublesArray = new double[rows][columns];
		for (int i=0;i<numsArray.length;i++) {
			doublesArray[(int) i/columns][i%columns] = Double.parseDouble(numsArray[i]);
		}
		return doublesArray;
	}
		
//Convert array into string
	public static String arrayToString(double[] arr) {
		String result = "";
		for (double element : arr) {
			result+=element + " ";
		}
		return result;
	}
	
//Rounding long decimals for readability (Found on StackExchange)
   public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
  
//toString override (Found on StackExchange)
	public String toString() {
		String result = "";
		for(int i = 0; i<rows; i++)
		{
		    for(int j = 0; j<columns; j++)
		    {
		        result+= round(this.matrix[i][j], 3) + " ";
		    }
		    result+= "\n";
		}
		return result;
	}
	
//Set particular value of matrix
	public void set(double value, int i, int j) {
			this.matrix[i][j] = value;
	}
	
//addition
	public static Matrices add(Matrices A, Matrices B) {
		Matrices result = new Matrices(new double[A.rows][A.columns]);
	

	for(int i=0; i<A.rows; i++) {
		for(int j=0; j<A.columns; j++) {
			result.matrix[i][j] = A.matrix[i][j] + B.matrix[i][j];
		}
	}
	return result;
	}
	
//Scalar multiplication
	public static Matrices multiply(double x, Matrices A) {
		Matrices result = new Matrices(new double[A.rows][A.columns]);
				
		for (int i=0; i<A.rows; i++) {
			for (int j=0; j<A.columns; j++) {
				result.matrix[i][j] = A.matrix[i][j] * x;
			}
		}
		return result;
	}

//Matrix multiplication
	public static Matrices multiply(Matrices A, Matrices B) {
		
			Matrices result = new Matrices(new double[A.rows][B.columns]);
			//iterates through result matrix ROWS
			for (int i=0; i<A.rows; i++) {
				//iterates through result matrix COLUMNS
				for (int j=0; j<B.columns; j++) {
					result.matrix[i][j] = 0;
					//Calculates the i,j entry in the result matrix
					//iterates through the entries of the dot product of row i and column j
					for (int k=0; k<A.columns;k++) {
						result.matrix[i][j] += A.matrix[i][k]*B.matrix[k][j];
						}
					}
				}
			
			return result;
		
	}
	
//Row operations
//Swap row "rOne" and row "rTwo"
	public void swap(int rOne, int rTwo) {
		double[] temporary = this.matrix[rOne];
		this.matrix[rOne] = this.matrix[rTwo];
		this.matrix[rTwo] = temporary;
	}

//Multiply row "row" by a scalar
	public void scaleRow(double scalar, int row) {
		for (int j=0;j<this.columns;j++) {
			this.matrix[row][j] *= scalar;
		}
	}

//Add multiple of one row to another
	public void addRowMult(double scalar, int rOne, int rTwo) {
		for (int j=0;j<this.columns;j++) {
			this.matrix[rOne][j] += scalar*this.matrix[rTwo][j];
		}
	}

//ref helper function, only used internally. Keeps track of "where" the rrefing process currently
//is in the matrix (the coords of the current pivot)
	private void refh(int counter1, int counter2) {
		if (counter2==this.columns || counter1==this.rows) {
			
		} else {
			//
			//find the first non-zero element
			int i = counter1;
			while (i<this.rows&&this.matrix[i][counter2]==0) {
				i++;

			} if(i==this.rows) {
			//if no nonzero elements found, move to next column
				refh(counter1, counter2+1);
			} else {
			//If nonzero element found
			//swap with first row
			swap(counter1, i);
			//change first row pivot to 1
			scaleRow(1/this.matrix[counter1][counter2],counter1);
			//Subtract multiples of first row from all others
			for (int iterate=counter1+1; iterate<this.rows;iterate++) {
				addRowMult(-this.matrix[iterate][counter2], iterate, counter1);
			}
			//Move on to next pivot
			refh(counter1+1, counter2+1);
			}
		}
	}
	
//Initiates refh at row 0, column 0
	public void ref() {
		this.refh(0, 0);
	}
	
//Goes from ref to rref form. Only used internally when input will be a ref matrix
	private void refToRref(int counter1) {
		if (counter1==-1) {
		//we are finished here
		} else {
			
			//find the first non-zero element in row "counter1"
			int j = 0;
			while (j<this.columns&&this.matrix[counter1][j]==0) {
				j++;
			}
			//if none, move to next row above
			if (j==this.columns) {
				refToRref(counter1-1);
			} else {
			//otherwise, zero out the column then move to the next one. Pivot should already be a 1.
				for (int i=counter1-1; i>-1;i--) {
					this.addRowMult(-this.matrix[i][j], i, counter1);
				}
				refToRref(counter1-1);
			}
		}
	}

//Full rref	
	public void rref() {
		this.ref();
		this.refToRref(this.rows-1);
	}

//Prints out Identity Matrix (Diagonal of ones for non-square matrices) for dimensions mn
	public static Matrices identity(int rows, int columns) {
		Matrices result = new Matrices(rows, columns);
		for (int i=0; i<Math.min(rows, columns); i++) {
			result.matrix[i][i] = 1;
		}
		return result;
	}
	
//Remove a column
	public static Matrices removeColumn(Matrices A, int column) {

		Matrices result = new Matrices(A.rows, A.columns-1);
		for (int i=0;i<A.rows;i++) {
			for (int j=0;j<A.columns-1;j++) {
				if (j<column-1) {
					result.matrix[i][j] = A.matrix[i][j];
				} else {
					result.matrix[i][j] = A.matrix[i][j+1];
				}
			}
		}
		return result;
	}
//Remove a row
	public static Matrices removeRow(Matrices A, int row) {
		Matrices result = new Matrices(A.rows-1,A.columns);
		for(int i=0;i<A.rows-1;i++) {
			for (int j=0;j<A.columns;j++) {
				if (i<row-1) {
					result.matrix[i][j] = A.matrix[i][j];
				} else {
					result.matrix[i][j] = A.matrix[i+1][j];
				}
			}
		}
		return result;
	}
//Augment
	public static Matrices augment(Matrices A, Matrices B) {
			Matrices result = new Matrices(A.rows, A.columns+B.columns);
			//Reaches all positions in new matrix
			for (int i=0;i<A.rows;i++) {
				for (int j=0;j<A.columns+B.columns;j++) {
					//Determine whether to fill in a value from A or B
					if (j<A.columns) {
						result.matrix[i][j]=A.matrix[i][j];
					} else {
						result.matrix[i][j]=B.matrix[i][j-A.columns];
					}
				}
			}
			return result;
	}
//Inverse
	public static Matrices inverse(Matrices A) {

			Matrices result = new Matrices(A.rows, A.columns);
			//Scratch matrix for row operation method of finding inverses
			Matrices scratchMatrix = augment(A, identity(A.columns, A.columns));
			scratchMatrix.rref();
			//Copies values from right side of scratch matrix
			for (int i=0; i<A.rows; i++) {
				for (int j=0; j<A.columns; j++) {
					result.matrix[i][j] = scratchMatrix.matrix[i][j+A.columns];
				}
			}
			return result;
	}
	
//Determinant helper function (modified from refh)
	public double detH(int counter1, int counter2, double detCounter) {
		if (counter2==this.columns || counter1==this.rows) {
			return detCounter;
		} else {
			//
			//find the first non-zero element
			int i = counter1;
			while (i<this.rows&&this.matrix[i][counter2]==0) {
				i++;

			} if(i==this.rows) {
				//if no nonzero elements found, return 0
				return 0;
			} else {
				//If nonzero element found
				//swap with first row
				this.swap(counter1, i);
				//Record row-swap if done
				if (i!=counter1) {
					detCounter*= -1;
				}
			
				//change first row pivot to 1
				//Record in detCounter
				detCounter*=this.matrix[counter1][counter2];
				scaleRow(1/this.matrix[counter1][counter2],counter1);

				//Subtract multiples of first row from all others
				for (int iterate=counter1+1; iterate<this.rows;iterate++) {
					addRowMult(-this.matrix[iterate][counter2], iterate, counter1);
				}
				//Move on to next pivot
				return this.detH(counter1+1, counter2+1, detCounter);
			}
		}
	}

//Determinant
	public static double det(Matrices A) {
		Matrices ACopy = new Matrices(A.matrix);
		return ACopy.detH(0, 0, 1.0);
		
	}
	
//Transpose
	public static Matrices transpose(Matrices A) {
		Matrices result = new Matrices(A.columns, A.rows);
		for (int i=0; i<result.rows; i++) {
			for (int j=0; j<result.columns; j++) {
				result.matrix[i][j] = A.matrix[j][i];
			}
		}
		return result;
	}
	
//Minors
//	public static double minor(Matrices A, int row, int column) {
	//	return det(removeColumn(removeRow(A, row), column));
	//}

//Makes a random nxn matrix
	public static Matrices randomN(int n) {
		Matrices result = new Matrices(n, n);
		for (int i=0; i<result.rows; i++) {
			for (int j=0; j<result.columns; j++) {
				result.matrix[i][j] = (int) (20*Math.random()-10);
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		//Set up test
		int testCount = 1;
		int[] sizes = {1, 2, 3, 4, 5, 10, 15, 20, 25, 50, 100};
		int testSize = sizes.length;
		Matrices[] testCases = new Matrices[testSize];
		double[] totalInversionTimes = new double[testSize];
		double[] totalDeterminantTimes = new double[testSize];
		double[] totalRREFTimes = new double[testSize];
		long startTime;
		long endTime;
		//Test for testCount sets of random matrices
		for (int testNumber=0;testNumber<testCount;testNumber++) {
		//Initialize matrices
			for (int i=0; i<testSize; i++) {
				testCases[i] = randomN(sizes[i]);
			}
		//perform tests, record times
			for (int i=0; i<testSize; i++) {
				//det tests
				startTime = System.nanoTime();
				det(testCases[i]);
				endTime = System.nanoTime();
				totalDeterminantTimes[i] = totalDeterminantTimes[i]+endTime-startTime;
				//Inversion tests
				startTime = System.nanoTime();
				inverse(testCases[i]);
				endTime = System.nanoTime();
				totalInversionTimes[i] = totalInversionTimes[i]+endTime-startTime;
				//RREF tests
				startTime = System.nanoTime();
				testCases[i].rref();
				endTime = System.nanoTime();
				totalRREFTimes[i] = totalRREFTimes[i]+endTime-startTime;
			}

		}
		//Convert total time to average times
		for(int index = 0; index<testSize;index++) {
			totalInversionTimes[index] = totalInversionTimes[index]/testCount;
			totalDeterminantTimes[index] = totalDeterminantTimes[index]/testCount;
			totalRREFTimes[index] = totalRREFTimes[index]/testCount;
		}
		
		System.out.println("Inversion Average Time: ");
		for (int i=0;i<testSize;i++) {
			System.out.print(sizes[i]+"by"+sizes[i]+": " + totalInversionTimes[i]);
			System.out.println();

		}
		System.out.println();
		System.out.println("Determinant Average Time: ");
		for (int i=0;i<testSize;i++) {
			System.out.print(sizes[i]+"by"+sizes[i]+": " + totalDeterminantTimes[i]);
			System.out.println();
		}
		System.out.println();
		
		System.out.println("RREF Average Time: ");
		for (int i=0;i<testSize;i++) {
			System.out.print(sizes[i]+"by"+sizes[i]+": " + totalRREFTimes[i]);
			System.out.println();

		}
		System.out.println();
	}
	
	

}
