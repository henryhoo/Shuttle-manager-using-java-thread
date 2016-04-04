import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Station {
	private static int N = 50;// passenger number. default is 50.

	private Lock lock = new ReentrantLock(); // lock
	private Condition _move = lock.newCondition(); // 存款条件
	private Condition _stop = lock.newCondition(); // 取款条件

	private int[] start = new int[N];// each passenager's start station, default
										// is 0(A),-2 for late passenger at current station
	private int[] dest = new int[N];// each passenager's destination, default is
									// 0(A)
	private int now = 0;// the station the shuffle is in
	private int[] on = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };// which
																	// passenger
																	// is on the
																	// train,-1
																	// for no
																	// passenger

	Station(int passengernumber) {
		Station.N = passengernumber;
	}

	public void passengeron() {
		// move the passenger to shuffle
		lock.lock();
		try {
//			System.out.println("try on pass");
				for (int i = 0; i < start.length; i++) {
					if (start[i] == now) {
						for (int j = 0; j < on.length; j++) {
							if (on[j] == -1) {
								on[j] = i;
								start[i] = -1;
								System.out.println("passenger:" + i + " go on the train");
								break;
							}
						}
					}
					if(start[i]==-2){
						start[i] = now;//deal with the late passenger, set their start to now
					}
				}
		} finally {
			lock.unlock();
		}
	}

	public void randomizedest() {
		// change passenager's destination randomly
		lock.lock();
		try {
//			System.out.println("try random");
			for (int i = 0; i < on.length; i++) {
				int t = on[i];
				if (t != -1) {
					dest[t] = (int) (1 + Math.random() * (5 - 0 + 1));
				}
			}
//			System.out.println("signal shuffle");
			_move.signal();
			try {
//				System.out.println("passenger stop");
				_stop.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			lock.unlock();
		}
	}
	public void passengeroff() {
		// move the passenger off the shuffle
		lock.lock();
		try {
//			System.out.println("try off pass");
			for (int i = 0; i < on.length; i++) {
				int t = on[i];
				if (t != -1) {
					if (dest[t] == now) {
						on[i] = -1;
						start[t] = (int) (1 + Math.random() * (5 - 0 + 1));
						System.out.println("passenger:" + t + " go off the train");
						if (start[t]==now) {
							start[t]=-2;//for the late passenger, set start to -2 as a flag
							System.out.println("passenger "+t+" arrive too late, wait for next turn wo get on");
						}
					}
				}
			}
			
		} finally {
			lock.unlock();
		}
	}

	public void moveshuffle() {
		// move the the shuffle to next station

		lock.lock();
		try {
//			System.out.println("move shuffle");
			now++;
			if (now >= 6) {
				now = 0;
			}
			System.out.println("shuffle move to stop " + now); 
		} finally {
			lock.unlock();
		}
	}

	public void moveuntilstop() {
		// move the the shuffle to next station
		lock.lock();
		try {
//			System.out.println("in move until");
			boolean flag = false;
			while (!flag) {
				int next = (now + 1 >= 6) ? 0 : (now + 1);
				for (int i = 0; i < start.length; i++) {
					if (start[i] == next) {
						flag = true;
					}
				}
				for (int i = 0; i < on.length; i++) {
					int t = on[i];
					if (t != -1) {
						if (dest[t] == next) {
							flag = true;
						}
					}
				}
				if (!flag) {
					now = next;
					System.out.println("skip stop "+(now-1)+",shuffle move to stop " + now);
				}
				if (flag) {
					now = next;
					System.out.println("shuffle move to stop " + now);
					try {
//						System.out.println("signal passenger");
						_stop.signal();
//						System.out.println("movement stop");
						_move.await();
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		} finally {
			lock.unlock();
		}

	}

}