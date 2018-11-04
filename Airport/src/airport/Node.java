/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airport;

import java.util.LinkedList;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

/**
 *
 * @author gundeep
 */
public class Node {
    public LinkedList<Edge> edge = new LinkedList<Edge>();
    public LinkedList<Edge> pathedges = new LinkedList<Edge>();
    public Circle n;
    public Label label;
    public int TL;
    public int ID; 
    boolean isPL;
}
