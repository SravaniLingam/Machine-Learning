import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Network 
{
	 final boolean isTrained = false;
	    final DecimalFormat df;
	    final Random rand = new Random();
	    final ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
	   // final ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
	    //final ArrayList<Neuron> hiddenLayermulti; 
	       ArrayList<ArrayList<Neuron>> all_hiddenLayers = new ArrayList<ArrayList<Neuron>>();
	      ArrayList<Neuron> hiddenLayer; 
	    final ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
	    final Neuron bias = new Neuron();
	    final int[] layers;
	   static int z=0;
	   // final int[] layers1;
	    final int randomWeightMultiplier = 1;
	    
	    final double epsilon = 0.1;
	   // static int k=0;
	    final double learningRate = 0.9f;
	    final double momentum = 0.7f;
	    double[] output;
	    final HashMap<String, Double> weightUpdate = new HashMap<String, Double>();
	   static int num_of_hiddenLayer;
    private String inputFile;
    static int total_columns;
    static int[] neurons_in_hidden;
    String[][] data = null;
	private int[] layers1;
	//private int[] layer1;
	//private int[] layer1;
   public static double[][] Full_set = null;
   public static double[][] validation_set1= null;
   public static double[][] test_set1 = null;
   //int m=validation_set1[0].length-1;
   public  static double[][] expectedOutputs;
   public  static double[][] expectedtestOutputs;//=new double[validation_set1.length];
   
  
   public static double resultOutputs[][] ; 
  
   
    //@SuppressWarnings("null")
	private static void actualoutput() {
		expectedOutputs=new double[validation_set1.length][1];
		expectedtestOutputs=new double[test_set1.length][1];
    	//double[] actualoutput_set = null;
		//System.out.println(" start total_columns :"+total_columns);
		int a=validation_set1[0].length-1;
		int b=test_set1[0].length-1;
		//System.out.println(expectedOutputs.length);
    	//System.out.println("validation actual method\t"+validation_set1.length);
    	for(int i=0;i<validation_set1.length;i++)
    	{
    		expectedOutputs[i][0]=validation_set1[i][a];
    		//System.out.println(validation_set1[i][0]);
    	}
    	for(int i=0;i<test_set1.length;i++)
    	{
    		expectedtestOutputs[i][0]=test_set1[i][b];
    		//System.out.println(validation_set1[i][0]);
    	}
    	//System.out.println("end");
    	//return actualoutput_set;
    	
	
}

	public void setInputFile(String inputFile) 
    {
        this.inputFile = inputFile;
    }
    public static void main(String[] args) throws IOException 
    {
        Network test = new Network();
        test.setInputFile(args[0]);
        //test.read();
        double percentage=Double.parseDouble(args[1]);
        validation_set1= test.read1(percentage);
        
        System.out.println("Training Dataset Length:"+validation_set1.length);
        System.out.println(("Test Dataset Length:"+test_set1.length));
        //System.out.println((test_set1[0].length));
        //public Network(int input,int hidden,int output) 
       // Network nn = new Network(validation_set1[0].length-1, 3, 1);
        
         num_of_hiddenLayer=Integer.parseInt(args[3]);
         neurons_in_hidden=new int[20];
          int neurons_in_hidden1=0;
         int z=4;
        
         for(int hidden_neurons=0;hidden_neurons<num_of_hiddenLayer;hidden_neurons++)
         {
		neurons_in_hidden[hidden_neurons]=Integer.parseInt(args[z]);
		neurons_in_hidden1+=neurons_in_hidden[hidden_neurons];
		//System.out.println("In main"+neurons_in_hidden[hidden_neurons]);
        z=z+1;
         }
        //public Network(int inputLen,int num_hiddenLayer,int neurons_in_hidden,int output)
        // System.out.println("neurons_in_hidden1"+neurons_in_hidden1);
        Network nn = new Network(validation_set1[0].length-1,num_of_hiddenLayer,neurons_in_hidden1, 1);
        
        actualoutput();
        int maxRuns = 5000;
        
        double minErrorCondition = Double.parseDouble(args[2]);
        nn.run(maxRuns, minErrorCondition);
        Network nn1 = new Network(test_set1[0].length-1,num_of_hiddenLayer,neurons_in_hidden1, 1);
        nn.run1(maxRuns, minErrorCondition);
    }
    

    double getRandom() {
        return randomWeightMultiplier * (rand.nextDouble() * 2 - 1); // [-1;1[
    }
  
    
    //changed
  //public Network(int inputLen,int num_hiddenLayer,int neurons_in_hidden,int output)
    
     public Network(int inputLen,int num_hiddenLayer,int neurons_in_hidden1,int output)
    {    int count=0;
    
    	 this.layers = new int[] { inputLen, neurons_in_hidden1, output };
         df = new DecimalFormat("#.0#");
         
         
  
         /**
          * Create all neurons and connections Connections are created in the
          * neuron class
          */
         for (int i = 0; i < layers.length; i++) 
         {
        	
        	 
             if (i == 0) 
                 { // input layer
                     for (int j = 0; j < layers[i]; j++) 
                     {
                        Neuron neuron = new Neuron();
                        inputLayer.add(neuron);
                     }
                 } 
             else if (i == 1) 
               { // hidden layer
            	 hiddenLayer = new ArrayList<Neuron>();
            	
            	 for(int k=0;k<num_hiddenLayer;k++)
          	   {
            		 
            		 
            		    if(k==0)
            		    {
            		    	int neurons=neurons_in_hidden[z];
            		    	//System.out.println("No of nodes in hidden"+neurons_in_hidden[k]);
            			    for (int j = 0; j < neurons; j++) 
                            {
                                Neuron neuron = new Neuron();
                                neuron.addConnectionsS(inputLayer);
                                neuron.BiasConnection(bias);
                                hiddenLayer.add(neuron);
                             }
            			    all_hiddenLayers.add(hiddenLayer);
            			    
            			    z++;
            			   // hiddenLayer = new ArrayList<Neuron>();
            		    }
            		   else
            		   {
            			   hiddenLayer = new ArrayList<Neuron>();
            			   int neurons=neurons_in_hidden[z];
           		    	//System.out.println("No of nodes in hidden"+neurons_in_hidden[z]);
                         for (int j = 0; j < neurons; j++) 
                         {
                             Neuron neuron = new Neuron();
                             neuron.addConnectionsS(all_hiddenLayers.get(count));
                             neuron.BiasConnection(bias);
                             hiddenLayer.add(neuron);
                          }
                         all_hiddenLayers.add(hiddenLayer);
                         count++;
                         z++;
            		 }
            		
            		    
          	   }
                 
            }
             
  
             
             else if (i == 2) 
             { // output layer
            	 //System.out.println("outputLayer");
                 for (int j = 0; j < layers[i]; j++) 
                 {
                     Neuron neuron = new Neuron();
                    //System.out.println( "output "+(num_hiddenLayer-1));
                    //System.out.println( "output "+(count));
                     neuron.addConnectionsS(all_hiddenLayers.get(num_hiddenLayer-1));
                     neuron.BiasConnection(bias);
                     outputLayer.add(neuron);
                    // System.out.println("outputLayer"+outputLayer);
                 }
             } 
             
             else {
                 System.out.println("!Error NeuralNetwork init");
             }
             
         }
         //System.out.println(all_hiddenLayers);
         // initialize random weights
         for(int k=0;k<num_hiddenLayer;k++)
         {
            for (Neuron neuron : all_hiddenLayers.get(k)) 
            {
             ArrayList<Connection> connections = neuron.getAllInConnections();
             for (Connection conn : connections) 
             {
                 double newWeight = getRandom();
                 conn.setWeight(newWeight);
             }
            }
        }
         
         for (Neuron neuron : outputLayer) {
             ArrayList<Connection> connections = neuron.getAllInConnections();
             for (Connection conn : connections) {
                 double newWeight = getRandom();
                 conn.setWeight(newWeight);
             }
         }
  
         // reset id counters
         Neuron.counter = 0;
         Connection.counter = 0;
  
         if (isTrained) {
            trainedWeights();
             updateAllWeights();
         }
         
    }
    
    
    public void setInput(double inputs[]) {
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.get(i).setOutput(inputs[i]);
        }
    }
 
    public double[] getOutput() {
        double[] outputs = new double[outputLayer.size()];
        for (int i = 0; i < outputLayer.size(); i++)
            outputs[i] = outputLayer.get(i).getOutput();
        return outputs;
    }
    public void activate() 
    {   for(int k=0;k<num_of_hiddenLayer;k++)
       {   
        for (Neuron n : all_hiddenLayers.get(k))
            n.cal_Output();
       }
    
        for (Neuron n : outputLayer)
            n.cal_Output();
        
    }
    public void applyBackpropagation(double expectedOutput[]) {
    	 
        // error check, normalize value ]0;1[
        for (int i = 0; i < expectedOutput.length; i++) {
            double d = expectedOutput[i];
            if (d < 0 || d > 1) {
                if (d < 0)
                    expectedOutput[i] = 0 + epsilon;
                else
                    expectedOutput[i] = 1 - epsilon;
            }
        }
 
        int i = 0;
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double ak = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double desiredOutput = expectedOutput[i];
 
                double partialDerivative = -ak * (1 - ak) * ai
                        * (desiredOutput - ak);
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
            i++;
        }
 
        // update weights for the hidden layer
      //  for(int k=0;k<num_of_hiddenLayer;k++)
       // {
        for (Neuron n : all_hiddenLayers.get(num_of_hiddenLayer-1)) 
        {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double aj = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double sumKoutputs = 0;
                int j = 0;
                for (Neuron out_neu : outputLayer) {
                	//System.out.println("out_neu.getConnection(n.id).getWeight()"+out_neu.getConnection(n.id).getWeight());
                    double wjk = out_neu.getConnection(n.id).getWeight();
                    double desiredOutput = (double) expectedOutput[j];
                    double ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
 
                double partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
        }
   // }
 
    void run(int maxSteps, double minError) {
        int i;
        resultOutputs=new double[validation_set1.length][1];
        // Train neural network until minError reached or maxSteps exceeded
        double error = 1;
        for (i = 0; i < maxSteps && error > minError; i++) {
            error = 0;
            for (int p = 0; p < validation_set1.length; p++) {
                setInput(validation_set1[p]);
 
                activate();
                //output=new double[validation_set1.length]
                output = getOutput();
                 resultOutputs[p] = output;
 
                for (int j = 0; j < expectedOutputs[p].length; j++) {
                    double err = Math.pow(output[j] - expectedOutputs[p][j], 2);
                    error += err;
                }
 
                applyBackpropagation(expectedOutputs[p]);
            }
        }
 
        //printResult();
         
        System.out.println("Sum of training squared errors = " + (error/expectedOutputs.length));
        System.out.println("##### EPOCH " + i+"\n");
        if (i == maxSteps) {
            System.out.println("Reached maximum iterations");
            printAllWeights();
           // printWeightUpdate();
        } 
        else {
            printAllWeights();
            //printWeightUpdate();
        }
    }
    void run1(int maxSteps, double minError) {
        int i;
        resultOutputs=new double[test_set1.length][1];
        // Train neural network until minError reached or maxSteps exceeded
        double error = 1;
        for (i = 0; i < maxSteps && error > minError; i++) {
            error = 0;
            for (int p = 0; p < test_set1.length; p++) {
                setInput(test_set1[p]);
 
                activate();
                //output=new double[validation_set1.length]
                output = getOutput();
                 resultOutputs[p] = output;
 
                for (int j = 0; j < expectedtestOutputs[p].length; j++) {
                    double err = Math.pow(output[j] - expectedtestOutputs[p][j], 2);
                    error += err;
                }
 
                applyBackpropagation(expectedtestOutputs[p]);
            }
        }
 
       // printResult1();
         
        System.out.println("Sum of squared test errors = " + (error/expectedOutputs.length));
        System.out.println("##### EPOCH " + i+"\n");
        if (i == maxSteps) {
            System.out.println("Reached maximum iterations ");
           // printAllWeights();
           // printWeightUpdate();
        } 
        else {
            //printAllWeights();
            //printWeightUpdate();
        }
    }
    void printResult()
    {
    	 System.out.println("Neural net Training set ");
         for (int p = 0; p < validation_set1.length; p++) {
             System.out.print("INPUTS: ");
             for (int x = 0; x < layers[0]; x++) {
                 System.out.print(validation_set1[p][x] + " ");
             }
  
             System.out.print("Actualfrom excel: ");
             for (int x = 0; x < layers[2]; x++) {
                 System.out.print(expectedOutputs[p][x] + " ");
             }
  
             System.out.print("Calculated: ");
             for (int x = 0; x < layers[2]; x++) {
                 System.out.print(resultOutputs[p][x] + " ");
             }
             System.out.println();
         }
         System.out.println();
    }
    void trainedWeights() {
        weightUpdate.clear();
         
        weightUpdate.put(weightKey(3, 0), 0.93);
        weightUpdate.put(weightKey(3, 1), 0.73);
       
    }
    void printResult1()
    {
    	 System.out.println("Neural net Test set ");
         for (int p = 0; p < test_set1.length; p++) {
             System.out.print("INPUTS: ");
             for (int x = 0; x < layers[0]; x++) {
                 System.out.print(test_set1[p][x] + " ");
             }
  
             System.out.print("Actualfrom excel: ");
             for (int x = 0; x < layers[2]; x++) {
                 System.out.print(expectedtestOutputs[p][x] + " ");
             }
  
             System.out.print("Calculated: ");
             for (int x = 0; x < layers[2]; x++) {
                 System.out.print(resultOutputs[p][x] + " ");
             }
             System.out.println();
         }
         System.out.println();
    }
 
 
    String weightKey(int neuronId, int conId) {
        return "N" + neuronId + "_C" + conId;
    }
 
    /**
     * Take from hash table and put into all weights
     */
    public void updateAllWeights() {
        // update weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                double newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
        // update weights for the hidden layer
        for(int k=0;k<num_of_hiddenLayer;k++)
        {
           for (Neuron n : all_hiddenLayers.get(k)) 
           {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) 
            {
                String key = weightKey(n.id, con.id);
                double newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
           }
        }
        
    }
 
    // trained data
  
 
    public void printWeightUpdate() {
        System.out.println("printWeightUpdate, put this i trainedWeights() and set isTrained to true");
        // weights for the hidden layer
        for(int k=0;k<num_of_hiddenLayer;k++)
        {
        for (Neuron n : all_hiddenLayers.get(k)) 
        {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) 
            {
                String w = df.format(con.getWeight());
                System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                        + con.id + "), " + w + ");");
            }
        }
        }
        // weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String w = df.format(con.getWeight());
                System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                        + con.id + "), " + w + ");");
            }
        }
        System.out.println();
    }
 
    public void printAllWeights() {
        System.out.println("printAllWeights");
        // weights for the hidden layer
        for(int k=0;k<num_of_hiddenLayer;k++)
        { 
        	System.out.println("");
        	 System.out.println("Hiddenlayer:"+(k+1)+"\n");
        	 
        for (Neuron n : all_hiddenLayers.get(k)) 
        {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) 
            {
                double w = con.getWeight();
               System.out.println("neuron=" + (n.id-1) + " c=" + con.id + " w=" + w);
            }
        }
    }
        
        // weights for the output layer
        for (Neuron n : outputLayer) {
        	System.out.println("");
        	System.out.println("Outputlayer weights\n");
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("neuron=" + (n.id-1) + " c=" + con.id + " w=" + w);
            }
        }
        System.out.println();
    }
    
    public Network() {
		super();
		this.layers=null;
		this.df=null;
		
		
	}
	public String[][] read() throws IOException  
    {
        File inputWorkbook = new File(inputFile);
        Workbook w;

        try 
        {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet


            Sheet sheet = w.getSheet(0);
            data = new String[sheet.getRows()][sheet.getColumns()];
            
            total_columns=sheet.getColumns();
            // Loop over first 10 column and lines
       //     System.out.println(sheet.getColumns() +  " " +sheet.getRows());
            for (int j = 0; j <sheet.getRows(); j++) 
            {
                for (int i = 0; i < sheet.getColumns(); i++) 
                {
                    Cell cell = sheet.getCell(i, j);
                    data[j][i] = cell.getContents();
                  //  System.out.println(cell.getContents());
                }
            }
 
            System.out.println("Entire File Length"+data.length);
         /* for (int j = 0; j < data.length; j++) 
            {
                for (int i = 0; i <data[j].length; i++) 
                {

                    System.out.print(data[j][i]+"\t");
                }
                System.out.println("\n");
            } */

        } 
        catch (BiffException e) 
        {
            e.printStackTrace();
        }
    return data;
    }
    

	private double[][] read1(double percentage) throws IOException {
		
		File inputWorkbook = new File(inputFile);
        Workbook w;

        try 
        {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
    

            Sheet sheet = w.getSheet(0);
            Full_set = new double[sheet.getRows()][sheet.getColumns()];
            int length= (int)((Full_set.length*percentage)/100);
            validation_set1=new double[length][sheet.getColumns()];
            int testlength=sheet.getRows()-length;
            test_set1 = new double[testlength][sheet.getColumns()];
            
            // Loop over first 10 column and lines
       //     System.out.println(sheet.getColumns() +  " " +sheet.getRows());
            for (int j = 0; j <length; j++) 
            {
                for (int i = 0; i < sheet.getColumns(); i++) 
                {
                	
                    Cell cell = sheet.getCell(i, j+1);
                    validation_set1[j][i] = Double.parseDouble(cell.getContents());
                	
                  //  System.out.println(cell.getContents());
                }
            }
            int k=0;
            for (int j = length; j <sheet.getRows(); j++) 
            {
                for (int i = 0; i < sheet.getColumns(); i++) 
                {
                	
                    Cell cell = sheet.getCell(i, j);
                    test_set1[k][i] = Double.parseDouble(cell.getContents());
                	}
                k++;
                
            }

         /* for (int j = 0; j < length; j++) 
            {
                for (int i = 0; i <validation_set1[j].length; i++) 
                {

                	//test_set[j][i]=Full_set[j][i];
                    System.out.print(validation_set1[j][i]+"\t");
                }
                System.out.println("\n");
            }
         
          System.out.println("Test data");

        for (int j = 0; j < testlength; j++) 
          {
              for (int i = 0; i <test_set1[j].length; i++) 
              {

              	//test_set[j][i]=Full_set[j][i];
                  System.out.print(test_set1[j][i]+"\t");
              }
              System.out.println("\n");
          }*/
        

        } 
        catch (BiffException e) 
        {
            e.printStackTrace();
        }
    return validation_set1;
	}

}