/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package wca;


import java.awt.*;
import javax.swing.*;



public class JCanvas extends JPanel {
    public Noeud[][] lesNoeuds;
    public int t;
    public Constantes c= new Constantes();
    
        public JCanvas(Noeud[][] lesNoeuds,int t){
           this.lesNoeuds= lesNoeuds; 
           this.t = t;
        }
    
	public void paint(Graphics g) {
		Color color = g.getColor();
		JPanel p = new JPanel();
                
                g.setColor(Color.RED);
                for(int i=0;i<c.NBNOEUDS;i++) {
                    Noeud n=lesNoeuds[t][i];
                    
                    g.drawString(String.valueOf(n.id), n.x, n.y);
                    if (n.estClusterhead){
                        g.setColor(Color.BLACK);
                        g.fillOval(n.x-3,n.y-3,6,6);
                          g.drawOval(n.x-n.tr,n.y-n.tr,n.tr*2, n.tr*2);
                        g.setColor(Color.RED);
                    }
                    else{
                    g.fillOval(n.x-3,n.y-3,6,6);
                    g.drawOval(n.x-n.tr,n.y-n.tr,n.tr*2, n.tr*2);
                    }
                    
                }
                
                // Voisins
                g.setColor(Color.BLUE);
                
                  for(int i=0;i<c.NBNOEUDS;i++)
                    {
            
                        Noeud n1=lesNoeuds[t][i];


                        for(int j=0;j<c.NBNOEUDS;j++)
                        {
                            if(i!=j){
                                Noeud n2 = lesNoeuds[t][j];

                                if(n1.estVoisinDe(n2)){
                                     g.drawLine(n1.x, n1.y, n2.x, n2.y);
                                }
                            }
                       }
                    }
		
                g.setColor(color);
	}
        
        public void ajouterNoeux(int x,int y){
//            g.setColor(Color.BLUE);
//            g.fillOval(150,50,80,80);
		
        }

}