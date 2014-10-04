package assignment;

import java.util.Random;

public class Buffer {

	private Random ran;
	public static boolean RUN = true;

	public Buffer() {
		this.ran = new Random();
	}

	public static void main(String[] args) {
		Buffer buffer = new Buffer();
		System.out.println("Start");

		Thread t1 = new Thread() {
			int count = 0;

			public void run() {
				while (Buffer.RUN) {
					System.out.println("T1" + ++count);
					try {
						this.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		Thread t2 = new Thread() {
			int count = 0;

			public void run() {
				while (Buffer.RUN) {
					System.out.println("T2" + ++count);
					try {
						this.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		Thread t3 = new Thread() {
			int count = 0;

			public void run() {
				while (Buffer.RUN) {
					System.out.println("T3" + ++count);
					try {
						this.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t1.start();
		t2.start();
		t3.start();
		try {
			Thread.sleep(10000);
			Buffer.RUN = false;
			System.out.println("stop");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Producer implements Runnable {
		Buffer buffer;
		Random ran;

		public void run() {
			while (true) {

			}
		}
	}

	class Consumer implements Runnable {
		Buffer buffer;
		Random ran;

		public void run() {
			while (true) {

			}
		}
	}
}