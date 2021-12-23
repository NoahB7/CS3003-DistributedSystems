
public class ThreadingLab2 {
	
	/********************************
	Name: Noah Buchanan
	Username: dist103
	Problem Set: PS3
	Due Date: October 6, 2020
	********************************/


	public static class UAThread implements Runnable {

		public int threadId;
		public int j;
		public int[][] A;
		public int[][] B;
		public int[][] C;
		public int buffer;

		public UAThread(int threadId, int j, int[][] A, int[][] B, int[][] C, int buffer) {

			this.threadId = threadId;
			this.j = j;
			this.A = A;
			this.B = B;
			this.C = C;
			this.buffer = buffer;
		}

		@Override
		public void run() {

			int bx = 0;
			int by = 0;
			for (int i = 0; i < A.length; i++) {
				bx = 0;
				for (int x = i - buffer; x <= i + buffer; x++) {
					by = 0;
					for (int y = j - buffer; y <= j + buffer; y++) {

						if (x >= 0 && y >= 0 && x < A.length && y < A[0].length) {
							
							C[i][j] += A[x][y] * B[bx][by];
						}
						by++;
					}
					bx++;
				}
			}
		}

	}
	
	
	

	public static int[][] calculate2(int[][] A, int[][] B) throws Exception {

		int buffer = (int) B.length / 2;
		int[][] C = new int[A.length][A[0].length];
		int threadId = 0;
		Thread[] threads = new Thread[A[0].length];
		// int j = 0;
		for (int j = 0; j < A[0].length; j++) {
			Thread t = new Thread(new UAThread(threadId, j, A, B, C, buffer));
			threads[j] = t;
			threads[j].start();
			threadId++;
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}

		return C;
	}

	
	
	
	
	
	
	public static int[][] calculate(int[][] A, int[][] B) {

		int buffer = (int) B.length / 2;
		int[][] C = new int[A.length][A[0].length];
		int one;
		int two;

		for (int i = 0; i < A.length; i++) {

			for (int j = 0; j < A[0].length; j++) {
				one = 0;
				for (int x = i - buffer; x <= i + buffer; x++) {
					two = 0;
					for (int y = j - buffer; y <= j + buffer; y++) {

						if (x >= 0 && y >= 0 && x < A.length && y < A[0].length) {
							C[i][j] += (A[x][y] * B[one][two]);
						}
						two++;
					}
					one++;
				}
			}
		}

		return C;
	}



}