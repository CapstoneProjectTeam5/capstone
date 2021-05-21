package edu.skku.map.capstone;

import java.util.ArrayList;
import java.util.List;


public class TouchAction {
	public TouchActionType Type;
	
	public enum TouchActionType {
		None, Tap, DoubleTap, SingleTouchDragAndDrop	//, MultiTouchDragAndDrop
	}
	
	//  action type, e.g., tap
	public List<Stroke> Strokes;

	//  strokes, may be 1 or 2 strokes for our experiment
	public Point TargetPoint;

	//  target point for tap and double tap
	public List<Point> StartPoints;

	//  start point(s) for single-touch and multi-touch drag and drop
	public List<Point> StopPoints;

	//  stop point(s) for single-touch and multi-touch drag and drop
	public TouchAction() {
		this.Strokes = new ArrayList<>();
		this.StartPoints = new ArrayList<>();
		this.StopPoints = new ArrayList<>();
	}

	public final Stroke FirstStroke() {
		return this.Strokes.get(0);
	}
	public final Stroke SecondStroke() {
		return this.Strokes.get(1);
	}

	public final double SingleTouchDragAndDropTime() {
		return FirstStroke().get(FirstStroke().size() - 1).T;
	}

	///  Computes the accuracy of the single-touch drag and drop task (the starting point perspective).
	public final double SingleTouchDragAndDropAccuracy1() {
		return this.EuclideanDistance(FirstStroke().get(0), this.StartPoints.get(0));
	}

	public final double SingleTouchDragAndDropAccuracy2 (){
		return this.EuclideanDistance(FirstStroke().get(FirstStroke().size() - 1), this.StopPoints.get(0));
	}

	///  Computes the overall accuracy of the single-touch drag and drop task.
	public final double SingleTouchDragAndDropAccuracy() {
		return 0.5 * (this.SingleTouchDragAndDropAccuracy1() + this.SingleTouchDragAndDropAccuracy2());
	}

	///  Computes the overall path accuracy of the single-touch drag and drop task.
	public final double SingleTouchDragAndDropPathAccuracy() {
		double pathLength = 0;
		for (int i = 0; i < (FirstStroke().size() - 1); i++) {
			pathLength += this.EuclideanDistance(FirstStroke().get(i), FirstStroke().get(i+1));
		}
		return (this.EuclideanDistance(FirstStroke().get(0), FirstStroke().get(FirstStroke().size() - 1)) / pathLength);
	}

	public final double DoubleTapTime_FirstTap (){
		return FirstStroke().get(FirstStroke().size() - 1).T;
	}

	///  Computes the time of the second tap.
	public final double DoubleTapTime_SecondTap (){
		return (SecondStroke().get(SecondStroke().size() - 1).T - SecondStroke().get(0).T);
	}

	///  Computes the time between taps.
	public final double DoubleTapTime_InBetweenTaps() {
		return (SecondStroke().get(0).T - FirstStroke().get(FirstStroke().size() - 1).T);
	}

	///  Computes the overall time of the double tap task.
	public final double DoubleTapTime() {
		return SecondStroke().get(SecondStroke().size() - 1).T;
	}

	///  Computes the accuracy of the first tap.
	public final double DoubleTapAccuracy_FirstTap() {
		return this.EuclideanDistance(FirstStroke().get(FirstStroke().size() - 1), this.TargetPoint);
	}

	///  Computes the accuracy of the second tap.
	public final double DoubleTapAccuracy_SecondTap() {
		return this.EuclideanDistance(SecondStroke().get(SecondStroke().size() - 1), this.TargetPoint);
	}

	///  Computes the overall accuracy of the double tap task.
	public final double DoubleTapAccuracy() {
		return (0.5 * (this.DoubleTapAccuracy_FirstTap() + this.DoubleTapAccuracy_SecondTap()));
	}

	public final double TapTime (){
		return FirstStroke().get(FirstStroke().size() - 1).T;
	}

	///  Computes the accuracy of the tap as the distance from the center of the target.
	public final double TapAccuracy() {
		return this.EuclideanDistance(FirstStroke().get(FirstStroke().size() - 1), this.TargetPoint);
	}

	private final double EuclideanDistance(Point a, Point b) {
		return Math.sqrt((((a.X - b.X) * (a.X - b.X)) + ((a.Y - b.Y) * (a.Y - b.Y))));
	}
}
