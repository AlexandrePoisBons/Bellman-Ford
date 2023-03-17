public class Lanceur 
{
    public static void main(String[] args) 
    {   
        System.setProperty("org.graphstream.ui", "swing");
        
        new Thread()
        {
            public void run()
            {
                new FrameNoeuds();
            }
        }.start();
        
        new Thread()
        {
            public void run()
            {
                BellmanFordAlgorithme.main(args);
            }
        }.start();
    }
}