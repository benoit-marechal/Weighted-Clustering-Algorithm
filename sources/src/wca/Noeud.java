/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wca;

/**
 *
 * @author benoitmarechal
 */
public class Noeud {

    public Constantes c=new Constantes();
    
    public int id; // identifiant du noeud 
    
    public int x; // position en x
    public int y; // position en y
    public int tr ; // transmission Range
    
    public int vx; // velocity en x
    public int vy; // velocity en y (= déplacement en y pour chaque temps t)
    
    public double[][] listeVoisinDistance; // Liste des voisins et de leur distances
    
    public int dv; // Nombre de voisins
    public double deltav; // Somme des distances avec les voisins 
    public double Dv; // Somme des distances avec les voisins 
    public double Mv; // Vitesse moyenne 
    public double Pv; // Temps cumulé à etre clusterhead
    public double Wv; // Poid combiné de toutes les variables précédente
    
    public boolean estClusterhead=false;
    public boolean estVoisinClusterhead=false; // Vrai si le noeud est voisin d'un clusterhead
   
 
    // Constructeurs
    public Noeud(){
        listeVoisinDistance = new double[c.NBNOEUDS][2];
        estClusterhead=false;
    }
    public Noeud(int id,int x,int y,int tr){ 
        
        // Init des paramètres du Noeud
        this.id=id; this.x=x;this.y=y;this.tr=tr; 
        
        // Initialisation de la liste de voisins
        listeVoisinDistance= new double[c.NBNOEUDS][2];
        
        // init
        dv=0; deltav=0; Dv=0; Mv=0; Pv=0;estClusterhead=false;
    }
    
    public Noeud(int id,int x,int y,int tr,int vx,int vy){
        this( id, x, y, tr);
        this.vx=vx;
        this.vy=vy;
    }
    
    // 
    public double calculDistanceVoisin(Noeud n){
         int dx=n.x-this.x;
         int dy=n.y-this.y;
         
         double dst = Math.sqrt(dx*dx + dy*dy);
         
         return dst;
    }
    
    // 
    public boolean estVoisinDe(Noeud n){
        double dst=calculDistanceVoisin(n);
        
        if (dst<=this.tr) return true;
        
        return false;
    }
    
    // Si le noeud donnée en parametre est un voisin, il est stocké dans le
    // tableau des voisins
    public void creerVoisinDistanceSiVoisinAvec(Noeud n){
        if (estVoisinDe(n))
            {
            listeVoisinDistance[dv][0] = n.id;
            listeVoisinDistance[dv][1] = calculDistanceVoisin(n);
            dv++;
        }
       
    }
    
    // 
    public void calculerDeltav(){
        deltav=Math.abs(dv-c.gamma);
    }
    
    // Calcule la somme des distances avec les voisins
    public void calculerDv(){
        for(int i=0;i<dv;i++)
            Dv+=listeVoisinDistance[i][1];
    }
    
    // Calcule la vitesse moyenne du noeud en fonction de ses positions précédentes
    public void calculerMv(int[][] positions,int k){
      
        int dx=0;
        int dy=0;
        double somme=0;
        for(int t=1;t<k;t++)
        {
            dx=positions[t][0] - positions[t-1][0] ;
            dy=positions[t][1] - positions[t-1][1] ;
            somme += Math.sqrt( dx*dx + dy*dy );
        }
        Mv = somme / k;
    }
    
    // Calcul le poid combiné (combinedWeight)
    public void calculerWv(){
        Wv= c.w1*deltav + c.w2*Dv + c.w3*Mv + c.w4*Pv ;
    }
    
    public double arrondir(double nb,int n){
        return (Math.round(nb*Math.pow(10,n)) )/ (Math.pow(10,n));
    }
            
    
    // affiche sous forme de chaine de caractere le Noeud
    public String toString(){
        
        String r = id + "\t"+ dv+"\t"+ arrondir(deltav,2)+ "\t"+arrondir(Dv,2)+
                "\t"+arrondir(Mv,2)+"\t"+Pv+"\t"+arrondir(Wv,2)+"\t"+
                "\t"+estClusterhead+
                "\t"+estVoisinClusterhead;
                
                
//        String r="Noeud "+id + " : \n";
//        r+="\tx="+x +"  y="+y+" portée="+tr;
//        r+="\n\tvx="+vx +"  vy="+vy+" \t\t(velocity)\n";
//        
//        r+="\tdv = "+dv+"\t\t(Nombre de voisins)\n";
//        r+="\tDv = "+arrondir(Dv,2)+"\t\t(Somme des distances des voisins)\n";
//        r+="\tMv = "+arrondir(Mv,2)+"\t\t(Vitesse moyenne)\n";
//        r+="\tPv = "+Pv+"\t\t(Temps cumulé a etre clusterhead)\n";
//        r+="\tWv = "+arrondir(Wv,2)+"\t\t(Poid combiné, combined weight)\n";
//        r+="\tClusterhead? = "+estClusterhead+"\t\t(true si le noeud est clusterhead)\n";
//        r+="\tVoisins : ";
//        for(int i=0;i<dv;i++)
//        {    
//            r+=" [ noeud ";
//            r+=((int)(listeVoisinDistance[i][0]));
//            r+=" distance : "+listeVoisinDistance[i][1];
//            r+=" ] ";
//        }
//        r+="\n-----------------------------------------\n";
        return r;
    }
    
}
