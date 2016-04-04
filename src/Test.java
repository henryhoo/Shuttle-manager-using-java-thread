import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
	private static int N = 50;// passenger number
	private static int M = 3;//shuttle number,should be less than 5, or will cause deadlock(only 6 station)

	public static void main(String[] args) {
		// new station for data exchange
		Station st = new Station(N,M);
		// new thread pool
		ExecutorService pool = Executors.newFixedThreadPool(100);
		//create passenger threads
		for (int i = 0; i < N; i++) {
			Thread p = new Passenger(st, i);
			pool.execute(p);
		}
		//create Shuttle threads
		for (int i = 0; i < M; i++) {
			Thread p = new Shuttle(st, i);
			pool.execute(p);
		}

		pool.shutdown();
	}
}