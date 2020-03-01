package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public class LevelGenerator {

	Random rnd = new Random();

	List<Line> sceneLines = null;
	List<Bounds> roomDimensions = null;
	
	double maxRoomWidth = 200;
	double maxRoomHeight = 200;

	public LevelGenerator() {
		generate();
	}
	
	public List<Line> getLines() {
		return sceneLines;
	}

	public List<Bounds> getRoomDimensions() {
		return roomDimensions;
	}

	public void generate() {

		sceneLines = new ArrayList<>();

		addRandomLines(Settings.get().getLineCount());
		addRooms(Settings.get().getRoomIterations());
		addOuterWalls();

	}

	public void addRandomLines(int lineCount) {
		// other lines
		for (int i = 0; i < lineCount; i++) {

			Line line = new Line(randomBoundedVector(), randomBoundedVector());
			sceneLines.add(line);

		}

	}

	public void addOuterWalls() {

		// outer walls
		double w = Settings.get().getCanvasWidth();
		double h = Settings.get().getCanvasHeight();
		double minX = 0;
		double minY = 0;
		double maxX = minX + w;
		double maxY = minY + h;

		sceneLines.addAll(createWallSegments(minX, minY, maxX, minY, 1)); // north
		sceneLines.addAll(createWallSegments(maxX, minY, maxX, maxY, 1)); // east
		sceneLines.addAll(createWallSegments(maxX, maxY, minX, maxY, 1)); // south
		sceneLines.addAll(createWallSegments(minX, maxY, minX, minY, 1)); // west

	}

	private void addRooms(int roomIterations) {

		// keep list of room dimensions to avoid overlapping rooms
		roomDimensions = new ArrayList<>();

		// iterations in order to create a room; once a random room overlaps
		// another, it gets skipped and another random room is generated
		for (int i = 0; i < roomIterations; i++) {

			double w = rnd.nextDouble() * maxRoomWidth;
			double h = rnd.nextDouble() * maxRoomHeight;
			double minX = rnd.nextDouble() * Settings.get().getCanvasWidth();
			double minY = rnd.nextDouble() * Settings.get().getCanvasHeight();
			double maxX = minX + w;
			double maxY = minY + h;

			// snap to grid
			double cellSize = Settings.get().getCanvasWidth() / Settings.get().getHorizontalCellCount();

			minX = ((int) (minX / cellSize)) * cellSize;
			minY = ((int) (minY / cellSize)) * cellSize;
			maxX = ((int) (maxX / cellSize)) * cellSize;
			maxY = ((int) (maxY / cellSize)) * cellSize;

			// avoid rooms that are dots or lines
			if( minX == maxX || minY == maxY)
				continue;
			
			Bounds roomBounds = new BoundingBox(minX, minY, maxX - minX, maxY - minY);

			// skip room if it overlaps another room
			boolean overlaps = false;
			for (Bounds bounds : roomDimensions) {
				if (roomBounds.intersects(bounds)) {
					overlaps = true;
					break;
				}
			}

			if (overlaps)
				continue;

			roomDimensions.add(roomBounds);

			List<Line> room = createRoom(minX, minY, maxX, maxY);

			for (Line line : room) {
				sceneLines.add(line);
			}

		}

	}

	public List<Line> createRoom(double minX, double minY, double maxX, double maxY) {

		List<Line> walls = new ArrayList<>();

		walls.addAll(createWallSegments(minX, minY, maxX, minY, randomWallCount())); // north
		walls.addAll(createWallSegments(maxX, minY, maxX, maxY, randomWallCount())); // east
		walls.addAll(createWallSegments(maxX, maxY, minX, maxY, randomWallCount())); // south
		walls.addAll(createWallSegments(minX, maxY, minX, minY, randomWallCount())); // west

		return walls;
	}

	public int randomWallCount() {

		// at least 1 wall
		int count = 1;

		// if randomness is satisfied, create a random number of wall segments
		if (rnd.nextDouble() < 0.25) {
			count += rnd.nextInt(3);
		}

		return count;
	}

	/**
	 * Single wall with multiple segments, depending on nr. of walls. A wall
	 * with 2 doors will have 3 walls.
	 * 
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @param walls
	 * @return
	 */
	public List<Line> createWallSegments(double minX, double minY, double maxX, double maxY, int walls) {

		List<Line> wallSegments = new ArrayList<>();

		PVector start = new PVector(minX, minY);
		PVector end = new PVector(maxX, maxY);

		// angle between the 2 vectors
		PVector distance = PVector.sub(end, start);
		double angle = Math.atan2(distance.y, distance.x);

		int numSegments = walls * 2 - 1;

		double dist = distance.mag();
		dist = dist / numSegments;

		for (int i = 0; i < numSegments; i++) {

			// skip every 2nd segment, it's a door
			if (i % 2 == 1)
				continue;

			double startX = start.x + Math.cos(angle) * dist * i;
			double startY = start.y + Math.sin(angle) * dist * i;
			double endX = start.x + Math.cos(angle) * dist * (i + 1);
			double endY = start.y + Math.sin(angle) * dist * (i + 1);

			wallSegments.add(new Line(new PVector(startX, startY), new PVector(endX, endY)));
		}

		return wallSegments;
	}

	private PVector randomBoundedVector() {
		return new PVector(rnd.nextDouble() * Settings.get().getCanvasWidth(), rnd.nextDouble() * Settings.get().getCanvasHeight());
	}

}
