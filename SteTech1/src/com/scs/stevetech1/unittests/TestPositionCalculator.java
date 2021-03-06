package com.scs.stevetech1.unittests;

import org.junit.Test;

import com.jme3.math.Vector3f;
import com.scs.stevetech1.shared.EntityPositionData;
import com.scs.stevetech1.shared.PositionCalculator;

public class TestPositionCalculator {

	public TestPositionCalculator() {
	}


	@Test
	public void basicPositionCalc1() {
		PositionCalculator posCalc = new PositionCalculator(10000, "");
		for (int i=0 ; i<10 ; i++) {
			posCalc.addPositionData(new Vector3f(i, 0, 0), i*100);
		}
		EntityPositionData after150 = posCalc.calcPosition(150, true);
		Vector3f correctPos = new Vector3f(1.5f, 0, 0);
		float diff = after150.position.distance(correctPos); 
		if (diff != 0) {
			throw new RuntimeException("basicPositionCalc1 Failed: Diff is " + diff);
		}
	}


	@Test
	public void basicPositionCalc2_AddInReverse() {
		PositionCalculator posCalc = new PositionCalculator(10000, "");
		for (int i=10 ; i>0 ; i--) {
			posCalc.addPositionData(new Vector3f(i, 0, 0), i*100);
		}
		EntityPositionData after350 = posCalc.calcPosition(350, true);
		Vector3f correctPos = new Vector3f(3.5f, 0, 0);
		float diff = after350.position.distance(correctPos); 
		if (diff != 0) {
			throw new RuntimeException("basicPositionCalc2_AddInReverse Failed: Diff is " + diff);
		}
	}


	@Test
	public void basicPositionCalc2_EarlyInSegment() {
		PositionCalculator posCalc = new PositionCalculator(10000, "");
		for (int i=10 ; i>0 ; i--) {
			posCalc.addPositionData(new Vector3f(i, 0, 0), i*100);
		}
		EntityPositionData after410 = posCalc.calcPosition(410, true);
		Vector3f correctPos = new Vector3f(4.1f, 0, 0);
		float diff = after410.position.distance(correctPos); 
		if (diff != 0) {
			throw new RuntimeException("basicPositionCalc2_EarlyInSegment Failed: Diff is " + diff);
		}
	}
	

	@Test
	public void basicPositionCalc2_LateInSegment() {
		PositionCalculator posCalc = new PositionCalculator(10000, "");
		for (int i=10 ; i>0 ; i--) {
			posCalc.addPositionData(new Vector3f(i, 0, 0), i*100);
		}
		EntityPositionData after490 = posCalc.calcPosition(490, true);
		Vector3f correctPos = new Vector3f(4.9f, 0, 0);
		float diff = after490.position.distance(correctPos); 
		if (diff != 0) {
			throw new RuntimeException("basicPositionCalc2_LateInSegment Failed: Diff is " + diff);
		}
	}


	@Test
	public void basicPositionCalc_TooEarly() {
		PositionCalculator posCalc = new PositionCalculator(10000, "");
		for (int i=10 ; i>0 ; i--) {
			posCalc.addPositionData(new Vector3f(i, 0, 0), 100+(i*100));
		}
		EntityPositionData after2000 = posCalc.calcPosition(2000, true);
		Vector3f correctPos = new Vector3f(10, 0, 0);
		float diff = after2000.position.distance(correctPos); 
		if (diff != 0) {
			throw new RuntimeException("basicPositionCalc_TooEarly Failed: Diff is " + diff);
		}
		/*if (after2000 != null) {
			throw new RuntimeException("basicPositionCalc_TooEarly Failed: Should be null");
		}*/
	}


	@Test
	public void basicPositionCalc_TooLate() {
		PositionCalculator posCalc = new PositionCalculator(10000, "");
		for (int i=10 ; i>0 ; i--) {
			posCalc.addPositionData(new Vector3f(i, 0, 0), 100+(i*100));
		}
		EntityPositionData after0 = posCalc.calcPosition(0, true);
		Vector3f correctPos = new Vector3f(1, 0, 0);
		float diff = after0.position.distance(correctPos); 
		if (diff != 0) {
			throw new RuntimeException("basicPositionCalc2_LateInSegment Failed: Diff is " + diff);
		}
		/*if (after0 != null) {
			throw new RuntimeException("basicPositionCalc_TooEarly Failed: Should be null");
		}*/
	}


}
