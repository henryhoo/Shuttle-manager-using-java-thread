import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
	private static int N = 50;
	private static int M = 1;

	public static void main(String[] args) {
		// 创建并发访问的账户
		
		Station st = new Station(50,1);
		// 创建一个线程池
		ExecutorService pool = Executors.newFixedThreadPool(100);

		for (int i = 0; i < N; i++) {
			Thread p = new Passager(st, i);
			pool.execute(p);
		}
		for (int i = 0; i < M; i++) {
			Thread p = new Shuttle(st, i);
			pool.execute(p);
		}

		pool.shutdown();
	}
}