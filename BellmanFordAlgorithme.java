import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.Scanner;
import java.io.FileInputStream;

public class BellmanFordAlgorithme
{   
    public static void main(String[] args) 
    {
        System.setProperty("org.graphstream.ui", "swing");

        SingleGraph graph = new SingleGraph("Bellman-Ford Algorithm");

        ArrayList<Node> nodes = new ArrayList<Node>();

        try
		{
            // Création des noeuds via la première ligne du fichier
            Scanner scNodes = new Scanner ( new FileInputStream ( "entree.txt" ), "UTF8" );


            scNodes.useDelimiter ( ";" );
            
            while ( scNodes.hasNextLine() )
            {
                String t = scNodes.next() ;
                  
                if(scNodes.hasNextLine())
                {
                    nodes.add( graph.addNode(t ) );
                    graph.getNode(t).addAttribute("ui.label", t);           
                }
            }
            scNodes.close(); // On ferme le scanner des noeuds

                   
            // Scanner pour le reste du fichier ( les arcs )
            Scanner scFic = new Scanner ( new FileInputStream ( "entree.txt" ), "UTF8" );
            scFic.nextLine(); // On passe la première ligne ( la création des noeuds)
    

			while ( scFic.hasNextLine() )
            {
                Scanner scLig = new Scanner ( scFic.nextLine() );
                scLig.useDelimiter ( "," );
                
                String node1  = scLig.next();       // Node 1
                String node2  = scLig.next();       // Node 2
                int    length = scLig.nextInt();    // Coût de l'arc

                //System.out.println( node1 + " " + node2 + " " + length);
                
                graph.addEdge( node1 + node2, node1, node2, true).setAttribute("ui.label", length);
                graph.getEdge( node1 + node2).setAttribute("length", length);
                       
                scLig.close();
            }

			scFic.close();
		}
		catch (Exception e){ e.printStackTrace(); }
    
        graph.display();
    }

    public static ArrayList<String> BellManFord(Graph graph, Node source ) 
    {
        
       ArrayList<String> path = new ArrayList<String>();
       
        // Initialisation
        for (Node node : graph) 
        {
    
            node.setAttribute("distance", Double.POSITIVE_INFINITY);
            node.setAttribute("parent", "null");
        }
        source.setAttribute("distance", 0);
    
        // Algorithme
        for (int i = 0; i < graph.getNodeCount() - 1; i++) 
        {
            for (Node node : graph) 
            {
                Iterator<Node> it = node.getNeighborNodeIterator();
                while (it.hasNext()) 
                { 
                    Node neighbor = it.next();
    
                    if (node.getEdgeBetween(neighbor) == null) continue;
                    
                    double distance = node.getNumber("distance") + node.getEdgeBetween(neighbor).getNumber("length");
    
                    if (distance < neighbor.getNumber("distance")) 
                    {
                        neighbor.setAttribute("distance", distance);
                        neighbor.setAttribute("parent", node);
                    }
                }
            }
        }
    
        // Affichage
        for (Node node : graph) 
        {
            if (node == source) continue;
            
            Node prev = (Node) node.getAttribute("parent");
    
            String str = node.getId() + "";
    
            while (prev != source && prev != null) {
    
                str = prev.getId() + " -> " + str;
                
                prev = (Node) prev.getAttribute("parent");
            }

    
            path.add(node.getId() + " : " + source.getId() + " -> " + str);
            //System.out.println(node.getId() + " : " + source.getId() + " -> " + str);
        }

        return path;
    }
}