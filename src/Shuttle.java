class Shuttle extends Thread {
	private Station st; // 账户

	Shuttle(Station st) {
		this.st = st;
	}

	public void run() {
		while (true) {
			st.moveuntilstop();
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
