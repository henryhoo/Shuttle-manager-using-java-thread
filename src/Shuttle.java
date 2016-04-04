class Shuttle extends Thread {
	private Station st; // 账户
	private int id;
	Shuttle(Station st,int id) {
		this.st = st;
		this.id = id;
	}

	public void run() {
		while (true) {
//			st.moveuntilstop();
			st.moveuntilstop(id);
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
