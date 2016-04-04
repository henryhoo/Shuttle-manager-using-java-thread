class Passager extends Thread { 
        private Station st;        //账户 

        Passager(Station st) { 
                this.st = st; 
        } 

        public void run() { 
                while (true) {
                	st.passengeroff();
//                	st.randomizestart();
                	st.passengeron();
                    st.randomizedest();
                    
                    try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

        } 
} 