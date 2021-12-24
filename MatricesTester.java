
public class MatricesTester {

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
				testCases[i] = Matrices.randomN(sizes[i]);
			}
		//perform tests, record times
			for (int i=0; i<testSize; i++) {
				//det tests
				startTime = System.nanoTime();
				Matrices.det(testCases[i]);
				endTime = System.nanoTime();
				totalDeterminantTimes[i] = totalDeterminantTimes[i]+endTime-startTime;
				//Inversion tests
				startTime = System.nanoTime();
				Matrices.inverse(testCases[i]);
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
