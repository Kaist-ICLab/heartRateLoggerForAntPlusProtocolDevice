package kr.ac.kaist.kse.cc.heartratemonitor.domain.service;

public interface HeartRateMeasuringService {
	boolean isRunning();
	
	void stop();
	
	void start();
}
