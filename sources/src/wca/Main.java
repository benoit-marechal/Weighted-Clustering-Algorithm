/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wca;

import java.awt.*;
import javax.swing.*;


/**
 *
 * @author benoitmarechal
 */
public class Main {

    Noeud lesNoeuds[][];
    public Constantes c=new Constantes();
        
    // Constructeur
    public Main(){
         lesNoeuds = new Noeud[c.NBTEMPS][c.NBNOEUDS];
 
         
         
        // Génération aléatoire du positionnement de départ des noeuds
        for(int i=0;i<c.NBNOEUDS;i++){
            
            // 
            int x = 150+(int)Math.floor(Math.random()*(c.CANVASWIDTH-300));
            int y = 150+(int)Math.floor(Math.random()*(c.CANVASHEIGHT-300));
            int tr = 100;
            
            // Décommentez la ligne ci dessous si vous voulez des portée (tx range) aléatoire
            // tr = (int)Math.floor(50+Math.random()*50);
            
            // On détermine une velocity entre -10 et 10 pour vx et vy
            int vx=(int)Math.floor(Math.random()*(20));
            vx=vx-10;
            int vy=(int)Math.floor(Math.random()*(20));
            vy=vy-10;
            lesNoeuds[0][i]=new Noeud(i,x,y,tr,vx,vy);
        }
         
         // Initialisation des positions des noeuds pour chaque temp t (a partir de la vélocity)
         for(int t=1;t<c.NBTEMPS;t++){
            for(int i=0;i<c.NBNOEUDS;i++){
                lesNoeuds[t][i]=new Noeud(lesNoeuds[t-1][i].id,
                                           lesNoeuds[t-1][i].x+lesNoeuds[t-1][i].vx,
                                           lesNoeuds[t-1][i].y+lesNoeuds[t-1][i].vy,
                                           lesNoeuds[t-1][i].tr,
                                           lesNoeuds[t-1][i].vx,
                                           lesNoeuds[t-1][i].vy);
            }
         }
    }
    
    // Affiche les informations sous forme textuelle : la console
    public void afficherConsole(){
        System.out.println("id\tdv\tDeltav\tDv\tMv\tPv\tWv\tClusterhead\tVoisinDeClusterhead");
         for(int T=0;T<c.NBTEMPS;T++){
            System.out.println("\nT = "+T+"");
            for(int i=0;i<c.NBNOEUDS;i++){
                for(int j=0;j<c.NBNOEUDS;j++)
                    if (lesNoeuds[T][j].id==i)
                        System.out.println(lesNoeuds[T][j]);
            }
        }
    }
            
 
    // Affiche l'interface graphique à l'écran
    public void afficherInterface(int t){
         JCanvas jc = new JCanvas(lesNoeuds,t);


        jc.setBackground(Color.WHITE);
        jc.setPreferredSize(new Dimension(c.CANVASWIDTH,c.CANVASHEIGHT));

        GUIHelper.showOnFrame(jc,"WCA - par Anthony et Benoit MARECHAL");
    }
    
    
    // Implémentation de l'algorithme Weighted Clustering Algorithm (WCA) 
    public void wca(){
        
        // Parcourt de chaque temp t (Fixé a 10 temps, voir Constantes.java)
        for(int T=0;T<c.NBTEMPS;T++){
            
            // Parcourt de chaque noeud
            for(int i=0;i<c.NBNOEUDS;i++)
            {
                Noeud n1=lesNoeuds[T][i];

                // Etape 1 : On détermine tous les voisins du noeud courant
                // Parcourt de chaque noeuds sauf du noeud courant "n1"
                // afin de trouver ses voisins (et donc également son nb de voisin "dv")
                for(int j=0;j<c.NBNOEUDS;j++)
                {
                    if(i!=j){
                        Noeud n2 = lesNoeuds[T][j];
                        //  stock la distance avec le noeud n2, si c'est son voisin
                        n1.creerVoisinDistanceSiVoisinAvec(n2);
                    }
                }
                
                // Etape 2 : Calcule la différence de degré (degree-difference) d'un noeud
                n1.calculerDeltav();
                
                // Etape 3 : Calcule la somme des distances avec ses voisins
                n1.calculerDv();
                
                // Etape 4 : Calcule de Mv la vitesse moyenne
                if (T>0)
                {
                    // On stock toutes les positions précédentes du noeud courant
                    // dans le tableau "positions"
                    int[][] positions= new int[T][2];
                    for(int t=0;t<T;t++)
                    {
                        positions[t][0]=lesNoeuds[t][n1.id].x;
                        positions[t][0]=lesNoeuds[t][n1.id].y;
                    }
                    n1.calculerMv(positions,T);
                }
                
                
                // Etape 5 : Calcule de Pv le temps cumulé durant lequel le noeud est clusterhead
                 int cpt=0;
                 for(int t=0;t<T;t++)
                 {
                    if (lesNoeuds[t][n1.id].estClusterhead == true)
                    {  cpt++; }
                 }
                 n1.Pv=cpt;
                
                // Etape  6 : Calcule de Wv, le poid combiné de tous les paramètres précédent
                // pondéré par les coefficients w1,w2,w3 et w4
                 n1.calculerWv();
            } // Fin des calculs sur chaque noeud
            
           // afficherConsole();
            
              // Etape 7 : Election du Clusterhead (Wv minimum)
            
            // Trie des noeuds au temp T en fonction du Wv
            
           trier(lesNoeuds,T); // trier le tableau de Noeud sur Wv pour le temp T

           for(int m=0;m<c.NBNOEUDS;m++){
               if( !lesNoeuds[T][m].estClusterhead && !lesNoeuds[T][m].estVoisinClusterhead)
                   definirClusterhead(T,m);
           }
               
           
           
            //lesNoeuds[T][0].estClusterhead=true;
            
//            for(int i=0;i<c.NBNOEUDS;i++)
//                System.out.print("  " +lesNoeuds[T][i].Wv);
           // for(int i=0;i<lesNoeuds[T][0])
            
            
//            double min=999999;
//            int idc=-1; // id du clusterhead (init a -1)
//            for(int i=0;i<c.NBNOEUDS;i++) {
//                if(min>lesNoeuds[T][i].Wv){
//                    idc=lesNoeuds[T][i].id;
//                    min=lesNoeuds[T][i].Wv;
//                }
//            }
//            
            
            // Définition du Clusterhead et affichage de tous les noeuds
//            for(int i=0;i<c.NBNOEUDS;i++) {
//                 Noeud n=lesNoeuds[T][i]; 
//                 if (n.id==idc) {
//                    // System.out.println("Clusterhead trouvee ! : Noeud "+n.id);
//                     n.estClusterhead=true;
//                 }
//            
//            }
        }
    }

    
    // Défini le noeud identifié par sa position dans la liste de noeuds "lesNoeuds"
    // et défini à estVoisinClusterhead tous les voisin de ce noeud.
    public void definirClusterhead(int T,int m){
           lesNoeuds[T][m].estClusterhead=true;
           
           // Parcourt de tous les voisins du clusterhead
           for(int k=0;k<lesNoeuds[T][m].dv;k++)
               for(int l=0;l<c.NBNOEUDS;l++)
                  if(lesNoeuds[T][m].listeVoisinDistance[k][0]==lesNoeuds[T][l].id) 
                  {
                      lesNoeuds[T][l].estVoisinClusterhead=true;
                  }
    }
    
    
   // Permet de trier un tableau
    public void trier(Noeud lesNoeuds[][],int T)
    {
        int longueur=c.NBNOEUDS;
        boolean inversion;
        
        do{
            inversion=false;

            for(int i=0;i<longueur-1;i++)
                {
                if(lesNoeuds[T][i].Wv>lesNoeuds[T][i+1].Wv)
                    {
                    Noeud tmp;
                    tmp=lesNoeuds[T][i];
                    lesNoeuds[T][i]=lesNoeuds[T][i+1];
                    lesNoeuds[T][i+1]=tmp;
                    inversion=true;
                    }
                }
             longueur--;
             }
        while(inversion);
    }
    
    
        
    // Fonction principale : Premiere fonction appelé au lancement de l'application
    public static void main(String[] args) {
        
      Main prog =   new Main();
        prog.wca();
        Constantes c = new Constantes();
     prog.afficherConsole();
        for(int t=0;t<c.NBTEMPS;t++)
            prog.afficherInterface(t);
      
    }
    


}
