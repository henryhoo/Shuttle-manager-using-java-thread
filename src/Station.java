import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Station {
	private static int N = 50;// passenger number. default is 50.
	private static int M = 1;// shuffle number. default is 50.

	private Lock lock = new ReentrantLock(); // lock
	private Condition _move = lock.newCondition(); // move the train
	private Condition _stop0 = lock.newCondition(); // station 0
	private Condition _stop1 = lock.newCondition(); // station 1
	private Condition _stop2 = lock.newCondition(); // station 2
	private Condition _stop3 = lock.newCondition(); // station 3
	private Condition _stop4 = lock.newCondition(); // station 4
	private Condition _stop5 = lock.newCondition(); // station 5
	private Condition _shuttlewait = lock.newCondition(); // wait for shuttle to
															// leave the station

	private int[] start = new int[N];
	// each passenager's start station, default is 0(A),-2 for late passenger at
	// current station
	private int[] dest = new int[N];
	// each passenager's destination, default is 0(A)
	private int now = 0;// where the station the shuffle is in
	private int[] on = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
	// which passenger is on the train(-1 for no)

	/*
	 * follow is the new variables in order to support N shuttle which manage
	 * "now array" and "on array" to represent every shuttle's movement and its
	 * passengers. In movement method, add conflict check to avioid two shuttle
	 * appear in the same station. relpace the "now" and "on" variables when a
	 * shuttle finish its movement
	 * 
	 */
	private int[] nows;// where the station the shuffles is in
	private int ons[][];// which passenger is on which train(-1 for no)

	Station(int passengernumber, int shufflenumber) {

		Station.N = passengernumber;
		Station.M = shufflenumber;
		nows = new int[M];
		ons = new int[M][10];
		for (int i = 0; i < ons.length; i++) {
			for (int j = 0; j < ons[i].length; j++) {
				ons[i][j] = -1;
			}
		}
		// need to fill the init //////////////
	}

	private boolean isstationempty(int stat) {
		for (int i = 0; i < start.length; i++) {
			if (start[i] == stat) {
				return false;
			}
		}
		System.out.println("station is empty");
		return true;
	}

	private boolean tryonshuffle(int id) {
		boolean flag = false;
		for (int i = 0; i < on.length; i++) {
			if (on[i] == -1) {
				on[i] = id;
				start[id] = -1;
				dest[id] = (int) (Math.random() * (5 - 0 + 1));
				System.out.println("passenger:" + id + " go on the train" + ",destination is stop" + dest[id]);
				flag = true;
				return true;
			}
		}
		// System.out.println(flag == false ? "train is full" : "");
		return flag;
	}

	public void passengeron(int id) {
		lock.lock();
		try {
			switch (start[id]) {
			case 0:
				try {
					// System.out.println("pass"+id+" wait at stop 0");
					if (isstationempty(0)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop0.await();
					}
					if (!tryonshuffle(id)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop0.await();
					} else {
						passengeroff(id, 0);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					// System.out.println("pass"+id+" wait at stop 1");
					if (isstationempty(1)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop1.await();
					}
					if (!tryonshuffle(id)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop1.await();
					} else {
						passengeroff(id, 1);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					// System.out.println("pass"+id+" wait at stop 2");
					if (isstationempty(2)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop2.await();
					}
					if (!tryonshuffle(id)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop2.await();
					} else {
						passengeroff(id, 2);
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				break;
			case 3:
				try {
					// System.out.println("pass"+id+" wait at stop 3");
					if (isstationempty(3)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop3.await();
					}
					if (!tryonshuffle(id)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop3.await();
					} else {
						passengeroff(id, 3);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				try {
					// System.out.println("pass"+id+" wait at stop 4");
					if (isstationempty(4)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop4.await();
					}
					if (!tryonshuffle(id)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop4.await();
					} else {
						passengeroff(id, 4);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					// System.out.println("pass"+id+" wait at stop 5");
					if (isstationempty(5)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop5.await();
					}
					if (!tryonshuffle(id)) {
						// System.out.println("tell train to move");
						_move.signalAll();
						_stop5.await();
					} else {
						passengeroff(id, 5);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}

		} finally {
			lock.unlock();
		}
	}

	private void passengeroff(int id, int stat) {
		switch (dest[id]) {
		case 0:
			try {
				// System.out.println("passenger:" + id + " waiting to stop 0");
				if (isstationempty(stat)) {
					_move.signalAll();
				}
				_stop0.await();
				for (int i = 0; i < on.length; i++) {
					if (on[i] == id) {
						on[i] = -1;
						start[id] = (int) (Math.random() * (5 - 0 + 1));
						dest[id] = -1;
						System.out.println("passenger:" + id + " go off the train and wait at stop" + start[id]);
					}
				}
				_move.signalAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			try {
				// System.out.println("passenger:" + id + " waiting to stop 1");
				if (isstationempty(stat)) {
					_move.signalAll();
				}
				_stop1.await();
				for (int i = 0; i < on.length; i++) {
					if (on[i] == id) {
						on[i] = -1;
						start[id] = (int) (Math.random() * (5 - 0 + 1));
						dest[id] = -1;
						System.out.println("passenger:" + id + " go off the train and wait at stop" + start[id]);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				// System.out.println("passenger:" + id + " waiting to stop 2");
				if (isstationempty(stat)) {
					_move.signalAll();
				}
				_stop2.await();
				for (int i = 0; i < on.length; i++) {
					if (on[i] == id) {
						on[i] = -1;
						start[id] = (int) (Math.random() * (5 - 0 + 1));
						dest[id] = -1;
						System.out.println("passenger:" + id + " go off the train and wait at stop" + start[id]);
					}
				}
				_move.signalAll();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			break;
		case 3:
			try {
				// System.out.println("passenger:" + id + " waiting to stop 3");
				if (isstationempty(stat)) {
					_move.signalAll();
				}
				_stop3.await();
				for (int i = 0; i < on.length; i++) {
					if (on[i] == id) {
						on[i] = -1;
						start[id] = (int) (Math.random() * (5 - 0 + 1));
						dest[id] = -1;
						System.out.println("passenger:" + id + " go off the train and wait at stop" + start[id]);
					}
				}
				_move.signalAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 4:
			try {
				// System.out.println("passenger:" + id + " waiting to stop 4");
				if (isstationempty(stat)) {
					_move.signalAll();
				}
				_stop4.await();
				for (int i = 0; i < on.length; i++) {
					if (on[i] == id) {
						on[i] = -1;
						start[id] = (int) (Math.random() * (5 - 0 + 1));
						dest[id] = -1;
						System.out.println("passenger:" + id + " go off the train and wait at stop" + start[id]);
					}
				}
				_move.signalAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 5:
			try {
				// System.out.println("passenger:" + id + " waiting to stop 5");
				if (isstationempty(stat)) {
					_move.signalAll();
				}
				_stop5.await();
				for (int i = 0; i < on.length; i++) {
					if (on[i] == id) {
						on[i] = -1;
						start[id] = (int) (Math.random() * (5 - 0 + 1));
						dest[id] = -1;
						System.out.println("passenger:" + id + " go off the train and wait at stop" + start[id]);
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public void moveuntilstop(int sid) {
		// move the the shuffle to next station
		lock.lock();
		try {
			// System.out.println("in move until");

			boolean flag = false;
			while (!flag) {

				int next = (nows[sid] + 1 >= 6) ? 0 : (nows[sid] + 1);
				for (int i = 0; i < nows.length; i++) {
					if (nows[i] == next) {
						try {
							System.out.println("Shuttle" + sid + " is wait for Shuttle" + i
									+ "//////////////////Shuttle conflict here");
							_shuttlewait.await();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				for (int i = 0; i < start.length; i++) {
					if (start[i] == next) {
						flag = true;
					}
				}
				for (int i = 0; i < ons[sid].length; i++) {
					int t = ons[sid][i];
					if (t != -1) {
						if (dest[t] == next) {
							flag = true;
						}
					}
				}
				if (!flag) {
					nows[sid] = next;
					_shuttlewait.signalAll();
					System.out
							.println("skip stop " + (nows[sid] - 1) + ",Shuttle" + sid + " move to stop " + nows[sid]);
				}
				if (flag) {
					nows[sid] = next;
					_shuttlewait.signalAll();
					System.out.println("Shuttle" + sid + " move to stop " + nows[sid]);
					try {
						switch (nows[sid]) {
						case 0:
							// System.out.println("signal stop 0");
							_stop0.signalAll();
							break;

						case 1:
							// System.out.println("signal stop 1");
							_stop1.signalAll();
							break;

						case 2:
							// System.out.println("signal stop 2");
							_stop2.signalAll();
							break;

						case 3:
							// System.out.println("signal stop 3");
							_stop3.signalAll();
							break;

						case 4:
							// System.out.println("signal stop 4");
							_stop4.signalAll();
							break;

						default:
							// System.out.println("signal stop 5");
							_stop5.signalAll();
							break;
						}
						System.out.println("Shuttle" + sid + " is waiting");
						now = nows[sid];
						on = ons[sid];
						_move.await();
						flag = false;

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
	/*
	 * @deprecated method for shuttle movement, replace by moveuntilstop(int
	 * sid) in order to support N shuttles public void moveuntilstop() { // move
	 * the the shuffle to next station lock.lock(); try { // System.out.println(
	 * "in move until"); boolean flag = false; while (!flag) { int next = (now +
	 * 1 >= 6) ? 0 : (now + 1); for (int i = 0; i < start.length; i++) { if
	 * (start[i] == next) { flag = true; } } for (int i = 0; i < on.length; i++)
	 * { int t = on[i]; if (t != -1) { if (dest[t] == next) { flag = true; } } }
	 * if (!flag) { now = next; System.out.println("skip stop " + (now - 1) +
	 * ",shuffle move to stop " + now); } if (flag) { now = next;
	 * System.out.println("shuffle move to stop " + now); try { switch (now) {
	 * case 0: // System.out.println("signal stop 0"); _stop0.signalAll();
	 * break;
	 * 
	 * case 1: // System.out.println("signal stop 1"); _stop1.signalAll();
	 * break;
	 * 
	 * case 2: // System.out.println("signal stop 2"); _stop2.signalAll();
	 * break;
	 * 
	 * case 3: // System.out.println("signal stop 3"); _stop3.signalAll();
	 * break;
	 * 
	 * case 4: // System.out.println("signal stop 4"); _stop4.signalAll();
	 * break;
	 * 
	 * default: // System.out.println("signal stop 5"); _stop5.signalAll();
	 * break; } System.out.println("train waiting"); _move.await(); flag=false;
	 * 
	 * } catch (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * } } finally { lock.unlock(); } }
	 */

}