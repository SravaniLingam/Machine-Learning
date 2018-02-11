
Programming Language used: Java 

1. Training a Neural Net

Libraries used:
We have used JXL(Jxl.jar) library for fetching and writing data to and from excel sheet(.xls)

Assumptions and Input:
Preprocessed output file is passed as a first input argument 
second argument is the percentage of file division i.e if we give 80 then 80% of the data will be training dataset and 20% will be test set
third argument is minErrorCondition
fourth argument is number of hidden layers
remaining arguments will be based on the number of neurons in each hidden layers

Sample Input:
"C:\Users\sravani\Documents\Assignments\MI\ML_Neuralnet_assignment\preprocessing_output\preprocessing_output\postprocessedData_IRIS.xls" 80.0 0.01 3 2 2 3




The main class in the Network,Neuron and Connection is Network.java

Please add the above mention libraries from library jar files(folder).
In Windows/Mac Environment open program in Eclipse IDE and compile it.

To run, right click on network.java select runas then select runconfigurations select arguments and give the sample arguments as below
"C:\Users\sravani\Documents\Assignments\MI\ML_Neuralnet_assignment\preprocessing_output\preprocessing_output\postprocessedData_IRIS.xls" 80.0 0.01 3 2 2 3





















  




