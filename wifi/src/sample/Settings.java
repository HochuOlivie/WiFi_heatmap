package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Application settings
 */
public class Settings {

	// ================================================================================================
	// gridpane
	// ================================================================================================
	GridPane gp;
	int rowIndex = 0;
	
	// toolbar properties
	private DoubleProperty toolbarWidth = new SimpleDoubleProperty(300);

	private Color backgroundColor = Color.WHITE;
	private Color gridColor = Color.LIGHTGRAY;

	// ================================================================================================
	// settings properties
	// ================================================================================================
	
	// scene settings
	// -------------------------------
	private DoubleProperty sceneWidth = new SimpleDoubleProperty(1280);
	private DoubleProperty sceneHeight = new SimpleDoubleProperty(720);
	private ObjectProperty<Color> sceneColor = new SimpleObjectProperty<>( Color.BLACK);

	private DoubleProperty canvasWidth = new SimpleDoubleProperty(sceneWidth.doubleValue()-toolbarWidth.doubleValue());
	private DoubleProperty canvasHeight = new SimpleDoubleProperty(sceneHeight.doubleValue());

	// example properties
	// -------------------------------
	private IntegerProperty gridHorizontalCellCount = new SimpleIntegerProperty(60);
	private BooleanProperty highlightGridCell = new SimpleBooleanProperty(false);
	private IntegerProperty lineCount = new SimpleIntegerProperty( 0);
	private IntegerProperty roomIterations = new SimpleIntegerProperty( 100);
	private BooleanProperty environmentVisible = new SimpleBooleanProperty(true);
	private BooleanProperty userVisible = new SimpleBooleanProperty(true);
	private DoubleProperty scanLineLength = new SimpleDoubleProperty(200);
	private BooleanProperty drawPoints = new SimpleBooleanProperty( true);
	private BooleanProperty drawShape = new SimpleBooleanProperty( true);
	private BooleanProperty gradientShapeFill = new SimpleBooleanProperty(false);
	private BooleanProperty shapeBorderVisible = new SimpleBooleanProperty(true);
	private BooleanProperty drawScanLines = new SimpleBooleanProperty( false);
	private BooleanProperty limitToScanLineLength = new SimpleBooleanProperty( true);
	private IntegerProperty scanLineCount = new SimpleIntegerProperty( 200);
	private BooleanProperty rooms = new SimpleBooleanProperty(false);

	public boolean isRooms() {
		return rooms.get();
	}

	public BooleanProperty roomsProperty() {
		return rooms;
	}

	// ================================================================================================
	// methods
	// ================================================================================================
	
	// instance handling
	// ----------------------------------------
	private static Settings settings = new Settings();
	
	private Settings() {
	}
	
	/**
	 * Return the one instance of this class
	 */
	public static Settings get() {
		return settings;
	}
	

	
	// ------------------------------------------------------------------------------------------------
	// user interface: nodes for property modification
	// ------------------------------------------------------------------------------------------------
	
	public Node createToolbar() {

		gp = new GridPane();
		
		// gridpane layout
		gp.setPrefWidth( Settings.get().getToolbarWidth());

	    gp.setHgap(1);
	    gp.setVgap(1);
	    gp.setPadding(new Insets(8));
	    
	    // set column size in percent
	    ColumnConstraints column = new ColumnConstraints();
	    column.setPercentWidth(50);
	    gp.getColumnConstraints().add(column);

	    column = new ColumnConstraints();
	    column.setPercentWidth(70);
	    gp.getColumnConstraints().add(column);
		
	    // add components for settings to gridpane

		// grid
		// -------------------------------------
	    addSeparator( "Grid");
		
		addNumberSlider( "Horiz. Cells", 0, gridHorizontalCellCount, 1, 60);
		addCheckBox( "Highlight", highlightGridCell);
		
		// Scene
		// -------------------------------------
		addSeparator( "Scene");

		addNumberSlider( "Lines", 0, lineCount, 0, 150);
		addNumberSlider( "Room Iterations", 0, roomIterations, 0, 4000);
		addCheckBox( "Environment Visible", environmentVisible);
		addCheckBox( "User Visible", userVisible);

		// Intersections
		// -------------------------------------
		addSeparator( "Intersections");
		addCheckBox( "Points", drawPoints);
		addCheckBox( "Shape", drawShape);
		addCheckBox( "Shape Border", shapeBorderVisible);
		addCheckBox( "Gradient Fill", gradientShapeFill);
		addCheckBox( "Limit", limitToScanLineLength);
		
		// group 2
		// -------------------------------------
		addSeparator( "Scan Lines");

		addCheckBox( "Scanlines Visible", drawScanLines);
		addNumberSlider( "Count", 0, scanLineCount, 1, 2000);
		
		double maxLength = Math.sqrt( getCanvasWidth() * getCanvasWidth() + getCanvasHeight() * getCanvasHeight());
		addNumberSlider( "Length", 0, scanLineLength, 1, maxLength);

		return gp;
	}

	private void addSeparator( String text) {
		gp.addRow(rowIndex++, createSeparator( text));
	}

	private void addNumberSlider( String text, int digits, Property<Number> observable, double min, double max) {

		// number format, eg "%.3f"
		String format = "%." + digits + "f";
		
		addNumberSlider( text, observable, min, max, format);

	}

	private void addNumberSlider( String text, Property<Number> observable, double min, double max, String labelFormat) {
		
		Slider slider = createNumberSlider( observable, min, max);
		
		Label valueLabel = new Label();
		valueLabel.setPrefWidth(70);
		valueLabel.textProperty().bind(slider.valueProperty().asString( labelFormat));
		
		HBox box = new HBox();
		box.setSpacing(10);
		box.getChildren().addAll( slider, valueLabel);
		
		gp.addRow(rowIndex++, new Label( text), box);
	}
	
	private void addCheckBox( String text, Property<Boolean> observable) {
		CheckBox checkBox = createCheckBox( observable);
		gp.addRow(rowIndex++, new Label( text), checkBox);
	}
	
	// ------------------------------------------------------------------------------------------------
	// gui helper methods
	// ------------------------------------------------------------------------------------------------
	
	private Node createSeparator( String text) {

		VBox box = new VBox();
		
		Label label = new Label( text);
		label.setFont(Font.font(null, FontWeight.BOLD, 14));	

		Separator separator = new Separator();

		box.getChildren().addAll(separator, label);
		
		box.setFillWidth(true);
		
		GridPane.setColumnSpan(box, 2);

		GridPane.setFillWidth(box, true);
		GridPane.setHgrow(box, Priority.ALWAYS);
	    
		return box;
	}
	
	private Slider createNumberSlider( Property<Number> observable, double min, double max) {
		
		Slider slider = new Slider( min, max, observable.getValue().doubleValue());
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.valueProperty().bindBidirectional(observable);
		
		return slider;
		
	}

	private CheckBox createCheckBox( Property<Boolean> observable) {
		
		CheckBox cb = new CheckBox();
		cb.selectedProperty().bindBidirectional(observable);
		
		return cb;
		
	}
	
	// ================================================================================================
	// auto-generated begin
	// ================================================================================================
	
	public final DoubleProperty toolbarWidthProperty() {
		return this.toolbarWidth;
	}

	public final double getToolbarWidth() {
		return this.toolbarWidthProperty().get();
	}

	public final void setToolbarWidth(final double toolbarWidth) {
		this.toolbarWidthProperty().set(toolbarWidth);
	}

	public final DoubleProperty sceneWidthProperty() {
		return this.sceneWidth;
	}

	public final double getSceneWidth() {
		return this.sceneWidthProperty().get();
	}

	public final void setSceneWidth(final double sceneWidth) {
		this.sceneWidthProperty().set(sceneWidth);
	}

	public final DoubleProperty sceneHeightProperty() {
		return this.sceneHeight;
	}

	public final double getSceneHeight() {
		return this.sceneHeightProperty().get();
	}

	public final void setSceneHeight(final double sceneHeight) {
		this.sceneHeightProperty().set(sceneHeight);
	}

	public final ObjectProperty<Color> sceneColorProperty() {
		return this.sceneColor;
	}

	public final javafx.scene.paint.Color getSceneColor() {
		return this.sceneColorProperty().get();
	}

	public final void setSceneColor(final javafx.scene.paint.Color sceneColor) {
		this.sceneColorProperty().set(sceneColor);
	}

	public final DoubleProperty canvasWidthProperty() {
		return this.canvasWidth;
	}

	public final double getCanvasWidth() {
		return this.canvasWidthProperty().get();
	}

	public final void setCanvasWidth(final double canvasWidth) {
		this.canvasWidthProperty().set(canvasWidth);
	}

	public final DoubleProperty canvasHeightProperty() {
		return this.canvasHeight;
	}

	public final double getCanvasHeight() {
		return this.canvasHeightProperty().get();
	}

	public final void setCanvasHeight(final double canvasHeight) {
		this.canvasHeightProperty().set(canvasHeight);
	}



	public final IntegerProperty lineCountProperty() {
		return this.lineCount;
	}

	public final int getLineCount() {
		return this.lineCount.get();
	}


	public final IntegerProperty horizontalCellCountProperty() {
		return this.gridHorizontalCellCount;
	}

	public final int getHorizontalCellCount() {
		return this.horizontalCellCountProperty().get();
	}

	public final void setHorizontalCellCount(final int horizontalCellCount) {
		this.horizontalCellCountProperty().set(horizontalCellCount);
	}

	public final boolean isDrawScanLines() {
		return drawScanLines.get();
	}

	public double getScanLineLength() {
		return scanLineLength.get();
	}
	public boolean isDrawShape() {
		return drawShape.get();
	}
	public boolean isDrawPoints() {
		return drawPoints.get();
	}

	public boolean isLimitToScanLineLength() {
		return limitToScanLineLength.get();
	}
	
	public int getScanLineCount() {
		return scanLineCount.get();
	}

	public final BooleanProperty highlightGridCellProperty() {
		return this.highlightGridCell;
	}

	public final boolean isHighlightGridCell() {
		return this.highlightGridCellProperty().get();
	}

	public final void setHighlightGridCell(final boolean highlightGridCell) {
		this.highlightGridCellProperty().set(highlightGridCell);
	}

	public final BooleanProperty environmentVisibleProperty() {
		return this.environmentVisible;
	}

	public final boolean isEnvironmentVisible() {
		return this.environmentVisibleProperty().get();
	}

	public final void setEnvironmentVisible(final boolean environmentVisible) {
		this.environmentVisibleProperty().set(environmentVisible);
	}

	public final BooleanProperty gradientShapeFillProperty() {
		return this.gradientShapeFill;
	}

	public final boolean isGradientShapeFill() {
		return this.gradientShapeFillProperty().get();
	}

	public final void setGradientShapeFill(final boolean gradientShapeFill) {
		this.gradientShapeFillProperty().set(gradientShapeFill);
	}

	public final BooleanProperty shapeBorderVisibleProperty() {
		return this.shapeBorderVisible;
	}

	public final boolean isShapeBorderVisible() {
		return this.shapeBorderVisibleProperty().get();
	}

	public final void setShapeBorderVisible(final boolean shapeBorderVisible) {
		this.shapeBorderVisibleProperty().set(shapeBorderVisible);
	}

	public final BooleanProperty userVisibleProperty() {
		return this.userVisible;
	}

	public final boolean isUserVisible() {
		return this.userVisibleProperty().get();
	}

	public final void setUserVisible(final boolean userVisible) {
		this.userVisibleProperty().set(userVisible);
	}

	public final IntegerProperty roomIterationsProperty() {
		return this.roomIterations;
	}

	public final int getRoomIterations() {
		return this.roomIterationsProperty().get();
	}

	public final void setRoomIterations(final int roomIterations) {
		this.roomIterationsProperty().set(roomIterations);
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Color getGridColor() {
		return gridColor;
	}

	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}


	// ================================================================================================
	// auto-generated end
	// ================================================================================================
	

	
	
}
