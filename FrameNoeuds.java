import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.Scanner;


public class FrameNoeuds extends JFrame 
{
    private JComboBox<String> node;
    private JLabel            chemin;
    private JButton           btn;

    private SingleGraph       graph;
    private ArrayList<Node>   nodes;


    public FrameNoeuds() 
    {
        super("Sélection de nœuds");
          
        // Obtention des infos du graph
        this.chargerGraph();
   
        this.node   = new JComboBox<String>();
        this.btn    = new JButton("Valider");
        this.chemin = new JLabel("Chemin le plus court :");

        // Création des panels
        JPanel panelPrincipal = new JPanel(new BorderLayout() );    
        JPanel panelPlacement = new JPanel(new BorderLayout() );
        JPanel panelGrid      = new JPanel(new GridLayout(1, 2) );

        // Placement des éléments pour le choix des noeuds
        panelGrid.add(new JLabel("Noeud :"));
        panelGrid.add(this.node);

        // Ajout des noeuds dans la liste déroulante
        for(Node n : nodes)
            this.node.addItem(n.getId());
        

        JLabel label = new JLabel("Sélectionnez un noeud pour afficher ses chemins les plus courts :");
        label.setHorizontalAlignment(JLabel.CENTER);


        panelPlacement.add( label    , BorderLayout.NORTH  );
        panelPlacement.add( panelGrid, BorderLayout.CENTER );

        panelPrincipal.add( panelPlacement, BorderLayout.NORTH  );
        panelPrincipal.add( this.chemin   , BorderLayout.CENTER );  

        // Gestion du bouton
        this.btn.addActionListener( new GereBtn() );
        
        panelPrincipal.add(btn, BorderLayout.SOUTH);

        this.add(panelPrincipal);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 250);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }
    

    public void chargerGraph()
    {
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

        this.graph = graph;
        this.nodes = nodes;
    }

     
    public void setPath(String s)
    {
        this.chemin.setText(s);
    }
    

    class GereBtn implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {     
            // Récupération du nom du Node
            String nodeS = (String) node.getSelectedItem();
            
            // Récupération de l'objet Node
            Node nodeN = graph.getNode(nodeS);
            
            //System.out.println("Node : " + nodeN );
            
            // Récupération du chemin le plus court
            ArrayList<String> path = BellmanFordAlgorithme.BellManFord( graph, nodeN );

            String chemin = "Chemin le plus court :\n ";

            for (String s : path)
                chemin += s + "\n";

            setPath(chemin);                 
        }
    }
}