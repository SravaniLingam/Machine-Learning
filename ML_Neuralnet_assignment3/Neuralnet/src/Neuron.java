import java.util.*;
 
public class Neuron {   
    public static int counter = 0;
    final public int id;  // auto increment, starts at 0
    Connection biasConection;
    final double bias = 1;
    double output;
    HashMap<Integer,Connection> connectionLookup = new HashMap<Integer,Connection>();
    ArrayList<Connection> connections = new ArrayList<Connection>();
    
     
    public Neuron(){        
        id = counter;
        counter++;
    }
     
    /**
     * Compute Sj = Wij*Aij + w0j*bias
     */
    public void cal_Output(){
        double i = 0;
        for(Connection con : connections){
            Neuron ltNeuron = con.getFromNeuron();
            double weight = con.getWeight();
            double a = ltNeuron.getOutput(); //output from previous layer
             
            i = i + (weight*a);
        }
        i = i + (biasConection.getWeight()*bias);
         
        output = g(i);
    }			
     
     
    double g(double x) {
        return sigmoid(x);
    }
 
    double sigmoid(double x) {
        return 1.0 / (1.0 +  (Math.exp(-x)));
    }
     
    public void addConnectionsS(ArrayList<Neuron> inNeurons){
        for(Neuron n: inNeurons){
            Connection con = new Connection(n,this);
            connections.add(con);
            connectionLookup.put(n.id, con);
        }
    }
     
    public Connection getConnection(int neuronIndex){
        return connectionLookup.get(neuronIndex);
    }
 
    public void addConnection(Connection con){
        connections.add(con);
    }
    public void BiasConnection(Neuron n){
        Connection con = new Connection(n,this);
        biasConection = con;
        connections.add(con);
    }
    public ArrayList<Connection> getAllInConnections(){
        return connections;
    }
     
    public double getBias() {
        return bias;
    }
    public double getOutput() {
        return output;
    }
    public void setOutput(double o){
        output = o;
    }
}
