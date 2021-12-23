import java.io.BufferedReader;
import java.io.FileReader;

public class ThreadingLab1 {
	
	/********************************
	Name: Noah Buchanan
	Username: dist103
	Problem Set: PS2
	Due Date: September 18, 2020
	********************************/

	public static void main(String[] args) throws Exception{

		if ( args.length < 2 ) {
			System.out.println("Invalid Arguments:  java ThreadingLab1 inputfilepath  numThreads");
			System.exit(100);
		}

		int numThreads = Integer.parseInt(args[1]);

		int[] A = getDataFromFile(args[0]);


		Thread[] threads = new Thread[numThreads];
		int[] result = new int[numThreads];


		//************************************************
		for(int i = 0; i < threads.length; i++) {
			int start = (int)((double)i/numThreads*A.length);
			int end = (int)((double)(i+1)/numThreads*A.length);
			Thread t = new Thread(new UAThread(i,start,end,A,result));
			threads[i] = t;
			threads[i].start();
			threads[i].join();
		}
		
		
		//************************************************



		int sum = 0;
		for ( int i = 0; i < result.length; i++ ) {
			sum += result[i];
		}

		int asum = 0;
		for ( int i = 0; i < A.length; i++ ) {
			asum += A[i];
		}

		System.out.println("Threaded Sum:     " + sum);
		System.out.println("Actual Sum:       " + asum);

	}

	public static int[] getDataFromFile(String path) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			String[] line = br.readLine().split(" ");
			int[] array = new int[line.length];
			for(int i = 0; i < line.length; i++) {
				array[i] = Integer.parseInt(line[i]);
			}
			br.close();
			return array;
		} catch(Exception e) {
			System.out.println("Failed to get data");
		}
		return null;

	}






	//************************************************
	public static class UAThread implements Runnable {

		public int threadId;
		public int startIndex;
		public int endIndex;
		public int[] A;
		public int[] result;

		public UAThread(int threadId, int startIndex, int endIndex, int[] A, int[] result) {
			
			this.threadId = threadId;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.A = A;
			this.result = result;
		}


		@Override
		public void run() {
			System.out.printf("Starting thread %d for indexes [%d,%d) %n", threadId, startIndex, endIndex);
			int sum = 0;

			//************************************************

			
			for(int i = startIndex; i < endIndex; i++) {
				sum+= A[i];
			}
			result[threadId] = sum;



			//************************************************
			// Do not modify anything below this line for this method only
			//************************************************

			try {
				Thread.sleep(2000);			//just going to wait for 2 seconds to watch your code
			} catch(Exception ex) {

			}

			System.out.printf("The sum for thread %d for indexes [%d,%d) is:     %d %n", threadId, startIndex, endIndex, sum);
		}

	}


}