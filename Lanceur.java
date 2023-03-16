public class Lanceur 
{
    public static void main(String[] args) 
    {   
        System.setProperty("org.graphstream.ui", "swing");
        
        new BellmanFordAlgorithme();
        new FrameNoeuds();
    }
}