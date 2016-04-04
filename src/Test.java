import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test { 
        public static void main(String[] args) { 
                //创建并发访问的账户 
                Station st = new Station(50); 
                //创建一个线程池 
                ExecutorService pool = Executors.newFixedThreadPool(2); 
                Thread t1 = new Shuttle(st); 
                Thread t2 = new Passager(st); 

                //执行各个线程 
                pool.execute(t2); 
                pool.execute(t1); 

                //关闭线程池 
                pool.shutdown(); 
        } 
} 