package br.ufrj.cos.bri.citeseer;

public class MultiThreadDispatcher{

	private int InputFileLength = 72;
	private int threadNumber;
	
	private void start() {

//		int blockFileLength = InputFileLength/threadNumber;
//		
//		for (int i = 0; i < threadNumber; i++) {
//			int fileRangeStart = i*blockFileLength +1;
//			int fileRangeEnd = fileRangeStart + blockFileLength -1;
//
//			CiteSeerLoader citeSeerLoader = new CiteSeerLoader(fileRangeStart, fileRangeEnd);
//			
//			citeSeerLoader.start();
//		}

		CiteSeerLoader citeSeerLoader = new CiteSeerLoader(72, 72);
		citeSeerLoader.start();
				
	}

	private void setThreadNumber(int i) {
		threadNumber = i;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MultiThreadDispatcher multiThreadDispatcher = new MultiThreadDispatcher();
		multiThreadDispatcher.setThreadNumber(16);
		multiThreadDispatcher.start();

	}

}
