/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airport;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.RED;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Rotate;

/**
 *
 * @author MY
 */
public class FXMLDocumentController implements Initializable {
  
     @FXML
    private Circle ref, enternode,prev;
    @FXML
    private AnchorPane ap;
    @FXML
    private VBox edge;
    @FXML
    private Button connectedge,delete,go,reset;
    @FXML
    private TextArea id;
    @FXML
    private TextField node1,node2;
    @FXML
    private Label label_path;
  
    
    private TextField weight;
    private boolean except = true,state = false;
    private int id1 , id2 ;
    private Node node,n1,n2,final_node,initial_node;
    private LinkedList<Node> nodes = new LinkedList<Node>();
    private Line l;
    private Path p,p1;
    private double radius,x1,x2,y1,y2,m,D,m1,m2,r=10,arrow_x,arrow_y;
    private Label label,refff;
    private Circle j;
    
    public Node FindNode(Circle c){
        
       for(Node i : nodes){
           if(i.n == c){
               return i;
           }
       }
        
       return null; 
    }
    public Node FindNodeByLabel(Label l){
          for(Node i : nodes){
           if(i.label == l){
               return i;
           }
       }
        
       return null; 
    }
    public Node FindNodeByID(int id){
        for(Node i : nodes){
           if(i.ID == id){
               return i;
           }
       }
        return null;
    }
    public void Reset(){
         initial_node.n.setFill(Color.DODGERBLUE);         
     for(Edge er : final_node.pathedges){
         er.l.setStroke(Color.BLACK);
         er.p.setStroke(Color.BLACK);
         er.node2.n.setFill(Color.DODGERBLUE);
         er.l.setStrokeWidth(1);
         er.p.setStrokeWidth(1);
         
         label_path.setText("NA");
     }
    }
    
    public void ShortestPath(Node a, Node b){
       
        int x = nodes.size()-1;
        LinkedList<Node> unvisited = new LinkedList<Node>();
        for(Node nx : nodes){
            nx.TL = Integer.MAX_VALUE;
            nx.isPL = false;
            nx.pathedges.clear();
            if(nx != a){                
              unvisited.add(nx);              
            }             
        }
        
        a.isPL = true;
        Node PLnode = a;
        int PL = 0;
        boolean reached = false;
        while(unvisited.size() >0 && PLnode != b){
            int min = Integer.MAX_VALUE;
            reached = false;
            for(Edge ep : PLnode.edge){
                if(!ep.node2.isPL){
                    int ax = ep.node2.TL;
                    int bx = PL + Integer.parseInt(ep.weight.getText());
                    if(ax > bx){
                        ep.node2.TL = bx ;
                        ep.node2.pathedges.clear();
                       
                        for(Edge et : PLnode.pathedges){
                            ep.node2.pathedges.add(et);                            
                        }
                        ep.node2.pathedges.add(ep); 
                    }
                }
            }
            for(Node nx : unvisited){               
                if(nx.TL < min){
                    min = nx.TL;
                    PLnode = nx;
                    reached = true;
                }                
            }
            if(reached){
                PL = min;      
               // PLnode.PL = PL;
                unvisited.remove(PLnode);
                //visited.add(PLnode);                
            }else{
                //System.out.print("No Path exists");
                label_path.setText("No Path");
                break;
            }
        }
        if(reached){
        label_path.setText(Integer.toString(PL));
        //System.out.print("Path : " +a.ID);
        a.n.setFill(RED);
        for(Edge fg : PLnode.pathedges){
            fg.node2.n.setFill(RED);
            fg.p.setStroke(Color.RED);
            fg.l.setStroke(Color.RED);
            fg.l.setStrokeWidth(3);
            fg.p.setStrokeWidth(3);
            //System.out.print(fg.node2.ID);
            //fg.n.setFill(Color.RED);
        }
       // System.out.print(b.ID);
        //System.out.println();
        
            
        }
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radius = enternode.getRadius();
        
        
      enternode.setOnMousePressed(new EventHandler<MouseEvent>(){
          
          @Override
          public void handle(MouseEvent event) {
              
             if(event.getButton() == MouseButton.PRIMARY){
                // System.out.println("here1");
                
                 ref = new Circle();
                 label = new Label();
                 label.setPrefSize(28, 16);
                 Insets i = new Insets(0,0,0,9);
                 label.setPadding(i);
                 label.setLayoutX(radius);
                 label.setLayoutX(enternode.getLayoutX() - 14);
                 label.setLayoutY(enternode.getLayoutY() - 8);
                 
                 ref.setCenterX(enternode.getLayoutX());
                 ref.setCenterY(enternode.getLayoutY());
                 ref.setRadius(enternode.getRadius());
                 ref.setFill(enternode.getFill());
                 
                 label.setOnMousePressed(new EventHandler<MouseEvent>(){
                  
                     @Override
                     public void handle(MouseEvent event) {
                         //System.out.print("1");
                         except = false;
                         connectedge.setVisible(except);
                         id.setVisible(except);
                         delete.setVisible(except);
                         refff = (Label)event.getSource();
                         j = (Circle) FindNodeByLabel(refff).n;
                       
                         if(event.getButton() == MouseButton.SECONDARY){
                             
                         edge.setLayoutX(j.getCenterX() + j.getRadius());
                         edge.setLayoutY(j.getCenterY() + j.getRadius());
                         prev = j;
                         id.setText(j.getId());
                         edge.toFront();
                         connectedge.setVisible(true);
                         id.setVisible(true);
                         delete.setVisible(true);
                         
                         }
                         else if(event.getButton() == MouseButton.PRIMARY){
                             node = FindNode(j);
                         }
                     }
                     
             });
                 label.setOnMouseDragged(new EventHandler<MouseEvent>(){
                     
                     double x,y;
                     @Override
                     public void handle(MouseEvent event) {
                        
                         if(event.getButton() == MouseButton.PRIMARY){
                         x = event.getSceneX();
                         y = event.getSceneY();
                          if(x>=185 )
                          {j.setCenterX(x);
                         j.setCenterY(y);
                         
                        refff.setLayoutX(j.getCenterX() - 14);
                         refff.setLayoutY(j.getCenterY() - 8);
                          }                         
                         
             Rotate rotate;  
             
             for(Node n: nodes){      
                   if(n == node){                       
                       for(Edge e: n.edge){
                           
                           x1 = n.n.getCenterX();
                           y1 = n.n.getCenterY();
                           x2 = e.node2.n.getCenterX();
                           y2 = e.node2.n.getCenterY();
                           e.weight.setLayoutX((x1+x2)/2);
                           e.weight.setLayoutY((y1+y2)/2);
                           D = Math.sqrt( (x2-x1)*(x2-x1)
                     + (y2-y1)*(y2-y1) );
             m = D-radius;
             double a = (m*x2 + radius*x1)/(D);
             double b = (m*y2 + radius*y1)/D;
             
             double x;
             ap.getChildren().remove(e.p);
             p = new Path();
             rotate = new Rotate(0,a,b);
             p.getElements().clear();
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a+5 ) ,(b-8.66)));
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a-5 ) ,(b-8.66)));
             
             if(x2 !=x1){                 
                m = (y2-y1)/(x2-x1);                                   
                
                if(y1 < y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(-x*180/Math.PI);
                    p.getTransforms().add(rotate);
                }
                else if(y1 > y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(180 - x*180/Math.PI);
                    p.getTransforms().add(rotate);                    
                }
                else{
                    if(x2 > x1){
                        rotate.setAngle(90);
                        p.getTransforms().add(rotate);
                    }
                    else{
                        rotate.setAngle(-90);
                        p.getTransforms().add(rotate);
                    }
                }
             }
             else{
                 if(y1 > y2){
                     rotate.setAngle(180);
                        p.getTransforms().add(rotate);
                 }
             }
             ap.getChildren().add(p);
             e.l.startXProperty().set(j.getCenterX());
             e.l.startYProperty().set(j.getCenterY());
             e.l.endXProperty().set(a);
             e.l.endYProperty().set(b);
             e.p = p;                          
                                                    
                       }
                       
                   }else{
                       for(Edge e: n.edge){
                     //System.out.print("here");                
                     
                  if(e.node2 == node){
             x1 = n.n.getCenterX();
             y1 = n.n.getCenterY();
             x2 = j.getCenterX();
             y2 = j.getCenterY();
             
             e.weight.setLayoutX((x1+x2)/2);
             e.weight.setLayoutY((y1+y2)/2);
             
             D = Math.sqrt( (x2-x1)*(x2-x1)
                     + (y2-y1)*(y2-y1) );
             m = D-radius;
             double a = (m*x2 + radius*x1)/(D);
             double b = (m*y2 + radius*y1)/D;
             
             double x;
             ap.getChildren().remove(e.p);
             p = new Path();
             rotate = new Rotate(0,a,b);
             p.getElements().clear();
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a+5 ) ,(b-8.66)));
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a-5 ) ,(b-8.66)));
             
             
            // System.out.print((a - p.getLayoutX() ) + " " + (b - p.getLayoutY()));
             if(x2 !=x1){                 
                m = (y2-y1)/(x2-x1);                                   
                
                if(y1 < y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(-x*180/Math.PI);
                    p.getTransforms().add(rotate);
                }
                else if(y1 > y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(180 - x*180/Math.PI);
                    p.getTransforms().add(rotate);                    
                }
                else{
                    if(x2 > x1){
                        rotate.setAngle(90);
                        p.getTransforms().add(rotate);
                    }
                    else{
                        rotate.setAngle(-90);
                        p.getTransforms().add(rotate);
                    }
                }
             }
             else{
                 if(y1 > y2){
                     rotate.setAngle(180);
                        p.getTransforms().add(rotate);
                 }
             }
             ap.getChildren().add(p);
             e.l.endXProperty().set(a);
             e.l.endYProperty().set(b);
             e.p = p;
                         
                 }
                   
             }                       
                   }
                 
             }            
                         }
                     }
                 
                 });
                 
                  ref.setOnMousePressed(new EventHandler<MouseEvent>(){
                      Circle j;
                     @Override
                     public void handle(MouseEvent event) {
                         //System.out.print("2");
                         except = false;
                         connectedge.setVisible(except);
                         id.setVisible(except);
                         delete.setVisible(except);
                         
                         j = (Circle) event.getSource();
                       
                         if(event.getButton() == MouseButton.SECONDARY){
                             
                         edge.setLayoutX(j.getCenterX() + j.getRadius());
                         edge.setLayoutY(j.getCenterY() + j.getRadius());
                         prev = j;
                         id.setText(j.getId());
                         edge.toFront();
                         connectedge.setVisible(true);
                         id.setVisible(true);
                         delete.setVisible(true);
                       
                         
                         }
                         else if(event.getButton() == MouseButton.PRIMARY){
                             node = FindNode(j);
                         }
                     }
                 
                 });
                 ref.setOnMouseDragged(new EventHandler<MouseEvent>(){
                     Circle j;
                     double x,y;
                     @Override
                     public void handle(MouseEvent event) {
                         j = (Circle) event.getSource();
                         if(event.getButton() == MouseButton.PRIMARY){
                         x = event.getSceneX();
                         y = event.getSceneY();
                         if(x>=185 ){                             
                         j.setCenterX(x);
                         j.setCenterY(y);
                         
                         node.label.setLayoutX(x - 14);
                         node.label.setLayoutY(y-8);
                             
                         }
             Rotate rotate;  
             
             for(Node n: nodes){      
                   if(n == node){                       
                       for(Edge e: n.edge){
                           
                           x1 = n.n.getCenterX();
                           y1 = n.n.getCenterY();
                           x2 = e.node2.n.getCenterX();
                           y2 = e.node2.n.getCenterY();
                           
                           D = Math.sqrt( (x2-x1)*(x2-x1)
                     + (y2-y1)*(y2-y1) );
             m = D-radius;
             double a = (m*x2 + radius*x1)/(D);
             double b = (m*y2 + radius*y1)/D;
             
             double x;
             ap.getChildren().remove(e.p);
             p = new Path();
             rotate = new Rotate(0,a,b);
             p.getElements().clear();
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a+5 ) ,(b-8.66)));
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a-5 ) ,(b-8.66)));
             
             if(x2 !=x1){                 
                m = (y2-y1)/(x2-x1);                                   
                
                if(y1 < y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(-x*180/Math.PI);
                    p.getTransforms().add(rotate);
                }
                else if(y1 > y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(180 - x*180/Math.PI);
                    p.getTransforms().add(rotate);                    
                }
                else{
                    if(x2 > x1){
                        rotate.setAngle(90);
                        p.getTransforms().add(rotate);
                    }
                    else{
                        rotate.setAngle(-90);
                        p.getTransforms().add(rotate);
                    }
                }
             }
             else{
                 if(y1 > y2){
                     rotate.setAngle(180);
                        p.getTransforms().add(rotate);
                 }
             }
             ap.getChildren().add(p);
             e.l.startXProperty().set(j.getCenterX());
             e.l.startYProperty().set(j.getCenterY());
             e.l.endXProperty().set(a);
             e.l.endYProperty().set(b);
             e.p = p;                          
                                                    
                       }
                       
                   }else{
                       for(Edge e: n.edge){
                     //System.out.print("here");                
                     
                  if(e.node2 == node){
             x1 = n.n.getCenterX();
             y1 = n.n.getCenterY();
             x2 = j.getCenterX();
             y2 = j.getCenterY();
             
             D = Math.sqrt( (x2-x1)*(x2-x1)
                     + (y2-y1)*(y2-y1) );
             m = D-radius;
             double a = (m*x2 + radius*x1)/(D);
             double b = (m*y2 + radius*y1)/D;
             
             double x;
             ap.getChildren().remove(e.p);
             p = new Path();
             rotate = new Rotate(0,a,b);
             p.getElements().clear();
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a+5 ) ,(b-8.66)));
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a-5 ) ,(b-8.66)));
             
             
            // System.out.print((a - p.getLayoutX() ) + " " + (b - p.getLayoutY()));
             if(x2 !=x1){                 
                m = (y2-y1)/(x2-x1);                                   
                
                if(y1 < y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(-x*180/Math.PI);
                    p.getTransforms().add(rotate);
                }
                else if(y1 > y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(180 - x*180/Math.PI);
                    p.getTransforms().add(rotate);                    
                }
                else{
                    if(x2 > x1){
                        rotate.setAngle(90);
                        p.getTransforms().add(rotate);
                    }
                    else{
                        rotate.setAngle(-90);
                        p.getTransforms().add(rotate);
                    }
                }
             }
             else{
                 if(y1 > y2){
                     rotate.setAngle(180);
                        p.getTransforms().add(rotate);
                 }
             }
             ap.getChildren().add(p);
             e.l.endXProperty().set(a);
             e.l.endYProperty().set(b);
             e.p = p;
                         
                 }
                   
             }                       
                   }
                 
             }            
                         }
                     }
                 
                 });
                 
                 node = new Node();
                 node.n = ref;
                 node.label = label;
                 nodes.add(node);
                 
                 ap.getChildren().add(ref);
                 ap.getChildren().add(label);
                 label.toFront();
             }
          }
          
      });
      enternode.setOnMouseDragged(new EventHandler<MouseEvent>(){
          double x,y;
          @Override
          public void handle(MouseEvent event) {
               
              if(event.getButton() == MouseButton.PRIMARY){
       
              x = event.getSceneX();
              y = event.getSceneY();
              if(x>=185  )
              {
              ref.setCenterX(x);
              ref.setCenterY(y);
               
               label.setLayoutX(ref.getCenterX() - 14);
               label.setLayoutY(ref.getCenterY() - 8);
              }
              }
          }
      });
      
      id.textProperty().addListener((observable, oldvalue, newvalue)->{
          if(newvalue != null){ 
              Node v = FindNode(prev);
            v.label.setText(newvalue);
            v.ID = Integer.parseInt(newvalue);
            prev.setId(newvalue);         
          }
      }); 
    

      delete.setOnMouseClicked(e->{
          
          if(e.getButton() == MouseButton.PRIMARY){
              
              for(Node i : nodes){
                  for(int ix = 0 ; ix<i.edge.size(); ix++){
                      if(i.edge.get(ix).node2.n == prev){     
                          //System.out.println("here");
                          ap.getChildren().remove(i.edge.get(ix).l);
                          ap.getChildren().remove(i.edge.get(ix).p);
                          ap.getChildren().remove(i.edge.get(ix).weight);
                          i.edge.remove(i.edge.get(ix));
                      }
                  }                                  
              }
              
              Node n = FindNode(prev);
              for(Edge ex : n.edge){
                  ap.getChildren().remove(ex.l);
                  ap.getChildren().remove(ex.p);
                  ap.getChildren().remove(ex.weight);
              }
              ap.getChildren().remove(n.n);
              ap.getChildren().remove(n.label);
              nodes.remove(n); 
              connectedge.setVisible(false);
              id.setVisible(false);
              delete.setVisible(false);
              
         }
          
      });
      
      connectedge.setOnMouseClicked(e->{
      if(e.getButton() == MouseButton.PRIMARY){
      l = new Line(prev.getCenterX(),prev.getCenterY(),e.getSceneX(),e.getSceneY());
      state = true;
      connectedge.setVisible(false);
      id.setVisible(false);
      delete.setVisible(false);
     
      ap.getChildren().add(l);
      }
      });
      
      ap.setOnMouseMoved(e->{
      if(state){
          for(Node n : nodes){
            if(n.n.getLayoutBounds().intersects(e.getSceneX(),e.getSceneY(),0,0)) {
                n2 = n;
                break;
            };              
          }
          if(e.getSceneX()>=185 ){  
          l.endXProperty().set(e.getSceneX());
          l.endYProperty().set(e.getSceneY());
          l.toBack();              
          }
      }
      });
    
      
     ap.setOnMousePressed(e->{
         //System.out.print("3");
         if(except){
      connectedge.setVisible(false); 
      id.setVisible(false);
      delete.setVisible(false);
     // hbox.setVisible(false);
         }
         if(state  && n2!=null){
             x1 = prev.getCenterX();
             y1 = prev.getCenterY();
             x2 = n2.n.getCenterX();
             y2 = n2.n.getCenterY();
            
             D = Math.sqrt( (x2-x1)*(x2-x1)
                     + (y2-y1)*(y2-y1) );
             m = D-radius;
             double a = (m*x2 + radius*x1)/(D);
             double b = (m*y2 + radius*y1)/D;
             double x;
             p = new Path();
             Rotate rotate = new Rotate(0,a,b);
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a+5 ) ,(b-8.66)));
             p.getElements().add(new MoveTo(a,b));
             p.getElements().add(new LineTo((a-5 ) ,(b-8.66)));
            
             if(x2 !=x1){                 
                m = (y2-y1)/(x2-x1);                                   
                
                if(y1 < y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(-x*180/Math.PI);
                    p.getTransforms().add(rotate);
                }
                else if(y1 > y2){
                    x = Math.atan(1/m);
                    rotate.setAngle(180 - x*180/Math.PI);
                    p.getTransforms().add(rotate);                    
                }
                else{
                    if(x2 > x1){
                        rotate.setAngle(90);
                        p.getTransforms().add(rotate);
                    }
                    else{
                        rotate.setAngle(-90);
                        p.getTransforms().add(rotate);
                    }
                }
             }
             else{
                 if(y1 > y2){
                     rotate.setAngle(180);
                        p.getTransforms().add(rotate);
                 }
             }
             ap.getChildren().add(p);
             l.endXProperty().set(a);
             l.endYProperty().set(b);
             Edge edgee = new Edge();
             edgee.node2 = n2;
             edgee.p = p;
             edgee.l = l;
             if(e.getButton() == MouseButton.PRIMARY)
             //if(true)
             {  weight = new TextField();             
                weight.setMaxWidth(54);
                weight.setMaxHeight(25);
                weight.setLayoutX((x1+x2)/2);
                weight.setLayoutY((y1+y2)/2);
                ap.getChildren().add(weight);
                weight.toFront();
                edgee.weight = weight;
                FindNode(prev).edge.add(edgee);
                 
             }
                       
             state = false;
             n2 = null;
         }
         except = true;
         
      });
     
     go.setOnMouseClicked(e->{
         if(final_node != null && initial_node !=null)          Reset();
         
         final_node = FindNodeByID(Integer.parseInt(node2.getText()));
         initial_node = FindNodeByID(Integer.parseInt(node1.getText()));
         ShortestPath(initial_node , final_node);
     });
     reset.setOnMouseClicked(e->{
        Reset();
     
     });
     
    }    
    
}
