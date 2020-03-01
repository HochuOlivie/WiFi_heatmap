package sample;


public class Line {

	PVector start;
	PVector end;

	public Line(PVector start, PVector end) {
		super();
		this.start = start;
		this.end = end;
	}

	public PVector getStart() {
		return start;
	}

	public void setStart(PVector start) {
		this.start = start;
	}

	public PVector getEnd() {
		return end;
	}

	public void setEnd(PVector end) {
		this.end = end;
	}

	public String toString() {
		return String.format( "[%f,%f]-[%f,%f]", start.x, start.y, end.x, end.y);
	}
}
