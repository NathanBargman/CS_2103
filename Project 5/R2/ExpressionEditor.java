import javafx.application.Application;
import javafx.collections.ObservableList;

import java.util.*;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ExpressionEditor extends Application {
	public static void main (String[] args) {
		launch(args);
	}

	/**
	 * Mouse event handler for the entire pane that constitutes the ExpressionEditor
	 */
	private static class MouseEventHandler implements EventHandler<MouseEvent> {
		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
		}

		public void handle (MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			}
		}
	}

	/**
	 * Size of the GUI
	 */
	private static final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 250;

	/**
	 * Initial expression shown in the textbox
	 */
	private static final String EXAMPLE_EXPRESSION = "2*x+3*y+4*z+(7+6*z)";

	/**
	 * Parser used for parsing expressions.
	 */
	private final ExpressionParser expressionParser = new SimpleExpressionParser();

	//Instance varibles
	private Expression _focus;
	private Expression _copy;
	private Pane _copyNode;
	private Node _expressionNode;
	
	/** 
	 * @param node an object of a javafx scene.
	 * @return the Bounds of the the node.
	 */
	private Bounds getBoundsOf(Node node) {
	 return node.localToScene(node.getBoundsInLocal());
	}
	
	/**
	 * To decide whether a HBox should be dragged.
	 * @return true if _focus is not null and if _focus.parent is not a Parenthesis
	 */
	private boolean checkFocusNode() {
		if(_focus.getParent() != null && !(_focus.getParent() instanceof ParenthesisExpression)) {	
			return true;
		}
		return false;
	}
	
	/**
	 * Translates the _copy.getNode
	 * @param x a double of the desired X coordinate location in relation to the scene.
	 * @param y a double of the desired Y coordinate location in relation to the scene.
	 */
	private void copyNodeTranslate(double x, double y) {									
		double newX = x;
		double newY = y;
		Bounds bounds = _copy.getNode().localToScene( _copy.getNode().getBoundsInLocal());
		
		if (newX < bounds.getWidth()/2) {
			newX = bounds.getWidth()/2;
		} else if (newX > WINDOW_WIDTH - bounds.getWidth()/2) {
			newX = WINDOW_WIDTH - bounds.getWidth()/2;
		}

		if (newY < 0) {
			newY = 0;
		} else if (newY > WINDOW_HEIGHT) {
			newY = WINDOW_HEIGHT;
		}

		_copy.getNode().setTranslateX(newX - (_copy.getNode().getLayoutX()  + bounds.getWidth()/2));
		_copy.getNode().setTranslateY(newY - (_copy.getNode().getLayoutY() + bounds.getHeight()));
	}
	
	@Override
	public void start (Stage primaryStage) {
		primaryStage.setTitle("Expression Editor");

		// Add the textbox and Parser button
		final Pane queryPane = new HBox();
		final TextField textField = new TextField(EXAMPLE_EXPRESSION);
		final Button button = new Button("Parse");
		queryPane.getChildren().add(textField);

		final Pane expressionPane = new Pane();
		
		// Add the callback to handle when the Parse button is pressed	
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle (MouseEvent e) {
				// Try to parse the expression
				try {
					// Success! Add the expression's Node to the expressionPane
					final Expression expression = expressionParser.parse(textField.getText(), true);
					System.out.println(expression.convertToString(0));
					expressionPane.getChildren().clear();
					_expressionNode = expression.getNode();
					expressionPane.getChildren().add(_expressionNode);
					((Pane)expression.getNode()).setLayoutX(WINDOW_WIDTH/4);
					((Pane)expression.getNode()).setLayoutY(WINDOW_HEIGHT/4);

					// If the parsed expression is a CompoundExpression, then register some callbacks
					if (expression instanceof CompoundExpression) {
						//Was having trouble with the line below so I created a event handler for each.
						//final MouseEventHandler eventHandler = new MouseEventHandler(expressionPane, (CompoundExpression) expression);
						((Pane) expression.getNode()).setBorder(Expression.NO_BORDER);
						
						//On Mouse Down
						expressionPane.setOnMousePressed(new MouseEventHandler(expressionPane, (CompoundExpression)expression) {
							@Override
							public void handle (MouseEvent e) {
								
								//Check to see if the expression was even clicked
								if(!expression.getNode().localToScene(expression.getNode().getBoundsInLocal()).contains(e.getSceneX(), e.getSceneY())) {
									if(_focus != null) {
										((Pane)_focus.getNode()).setOpacity(100);
										((Pane)_focus.getNode()).setBorder(Expression.NO_BORDER);
									}
									_focus = null;
									return;
								}
								
								//Check to see if we need to look inside the focus
								if(_focus != null) {
									((Pane)_focus.getNode()).setOpacity(100);
									((Pane)_focus.getNode()).setBorder(Expression.NO_BORDER);
									//Did we click inside the focus
									if(_focus.getNode().localToScene(_focus.getNode().getBoundsInLocal()).contains(e.getSceneX(), e.getSceneY())) {
										//Did we click any of the children
										for (Node focusBox : ((HBox)_focus.getNode()).getChildren()) {
											if(focusBox instanceof HBox) {
												if(focusBox.localToScene(focusBox.getBoundsInLocal()).contains(e.getSceneX(), e.getSceneY())) {
													//Make the HBox we clicked the focus
													_focus = ((CompoundExpression) _focus).getOwnerOfBox((HBox) focusBox);
													((Pane)_focus.getNode()).setBorder(Expression.RED_BORDER);
													//Check to see if we should be able to drag the focus
													if(checkFocusNode()) {
														//Make a copy
														_copy = _focus.deepCopy();
													}
													return;
												}	
											}
										}
										_focus = null;
										return;
									}
									_focus = null;
								}
								
								//If there is no focus or if the focus is not clicked
								for (Node box : ((HBox)expression.getNode()).getChildren()) {
									if(box instanceof HBox) {
										if(box.localToScene(box.getBoundsInLocal()).contains(e.getSceneX(), e.getSceneY())) {
											//Make the HBox we clicked the focus
											_focus = ((CompoundExpression) expression).getOwnerOfBox((HBox) box);
											((Pane)_focus.getNode()).setBorder(Expression.RED_BORDER);
											//Check to see if we should be able to drag the focus
											if(checkFocusNode()) {
												//Make a copy
												_copy = _focus.deepCopy();
											}
										}
									}
								}

							}
						});
						
						//On Mouse Dragged
						expressionPane.setOnMouseDragged(new MouseEventHandler(expressionPane, (CompoundExpression)expression) {
							@Override
							public void handle (MouseEvent e) {
								
								//Make sure the focus is valid
								if(_focus == null || !checkFocusNode()) {return;}
								
								//Add the copy node to be dragged
								if(_copyNode == null || !_copyNode.equals(_copy.getNode())) {
									_copyNode = (Pane) _copy.getNode();
									expressionPane.getChildren().add(_copyNode);
									_copy.getNode().setLayoutX(getBoundsOf(_focus.getNode()).getMinX());
									_copy.getNode().setLayoutY(getBoundsOf(_focus.getNode()).getMinY());
								}
								
								//Ghost the focus the has been left behind
								((Pane)_focus.getNode()).setOpacity(0.5);
								
								//Drag around the copy
								copyNodeTranslate(e.getSceneX(), e.getSceneY());

								//Variables to find the closest location
								ObservableList<Node> temp = ((HBox)_focus.getParent().getNode()).getChildren();
								int crntIndex = temp.indexOf(_focus.getNode());					
								int indexOfClosest = crntIndex;
								double crntPos = Math.abs(e.getSceneX());
								Bounds copyBounds = _copy.getNode().localToScene(_copy.getNode().getBoundsInLocal());
								double valueOfClosest = WINDOW_WIDTH;
								
								//double crntPos = Math.abs(copyBounds.getMinX() + copyBounds.getWidth()/2);								
								for(int i = 0; i < temp.size(); i+=2) {
									double leftSide = ((HBox)_focus.getParent().getNode()).localToScene(((HBox)_focus.getParent().getNode()).getBoundsInLocal()).getMinX();
									double crntx = leftSide + copyBounds.getWidth()/2;
									for(int z = 0; z < i + 1; z++) {
										if(z != crntIndex) {
											crntx += temp.get(z).localToScene(temp.get(z).getBoundsInLocal()).getWidth();
										}
									}
									
									if(Math.abs(crntPos - crntx) < valueOfClosest) {		
										valueOfClosest = Math.abs(crntPos - crntx);		
										indexOfClosest = i;
									}
								}
		
								//See if the closest index is not the one the focus inhabits
								if(crntIndex != indexOfClosest) {									
									//Clear the scene
									expressionPane.getChildren().clear();
									expressionPane.getChildren().add(_copyNode);
									
									//Re-organize the tree and make new HBoxes
									_focus.getParent().reorgChildren(_focus, indexOfClosest/2);
									((CompoundExpression) expression).resetHBox();
									((Pane)_focus.getNode()).setBorder(Expression.RED_BORDER);
									((Pane)_focus.getNode()).setOpacity(0.5);

									//Re-full the scene
									_expressionNode = expression.getNode();
									expressionPane.getChildren().add(_expressionNode);
									((Pane)expression.getNode()).setLayoutX(WINDOW_WIDTH/4);
									((Pane)expression.getNode()).setLayoutY(WINDOW_HEIGHT/4);
									
									//Print the changed tree
									System.out.println(expression.convertToString(0));
								}

							}
						});
						
						//On Mouse Up
						expressionPane.setOnMouseReleased(new MouseEventHandler(expressionPane, (CompoundExpression)expression) {
							@Override
							public void handle (MouseEvent e) {
								
								//Take the copy node out of the scene
								expressionPane.getChildren().remove(_copyNode);
								_copyNode = null;
								
								//Un Ghost the focus
								if(_focus != null) {((Pane)_focus.getNode()).setOpacity(100);}
							}
						});
					}
				} catch (ExpressionParseException epe) {
					// If we can't parse the expression, then mark it in red
					textField.setStyle("-fx-text-fill: red");
				}
			}
		});
		queryPane.getChildren().add(button);

		// Reset the color to black whenever the user presses a key
		textField.setOnKeyPressed(e -> textField.setStyle("-fx-text-fill: black"));
		
		final BorderPane root = new BorderPane();
		root.setTop(queryPane);
		root.setCenter(expressionPane);

		primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
		primaryStage.show();
	}
}
