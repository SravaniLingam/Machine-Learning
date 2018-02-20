Open this README.txt using notepad ++

Programming_project: Team Members: shiva podugu     Net_id:sxp170130
                                   sravani lingam   Net_id:sxl170330

Language used: c++.

CODE COMPLIATION:

In linux or ubuntu Environment 
use command g++ ML_assignment1.cpp 

In windows Environment open program in IDE and compile it.

CODE EXECUTION:

In ubuntu or linux Environment run the following commands to execute :
Command line inputs:
1. Training data file Path(like : "C:\Users\shiva podugu\Downloads\data_sets1\training_set.csv")
2. validation Data file Path (Like : "C:\Users\shiva podugu\Downloads\data_sets1\validation_set.csv")
3. Testing Data file Path (Like : "C:\Users\shiva podugu\Downloads\data_sets1\test_set.csv")

./a.out train_data_file_path test_data_path 
 
To redirect output to a file, use the following command.
./a.out training_data_path validatation_data_path testing_data_path  > output_filename.txt

In windows Environment after compling the program an new excutable will be generated.

Run the Executable using the following command:
ML_assignment_bonus.exe training_data_path validatation_data_path testing_data_path.

To redirect output to a file, use the following command.
ML_assignment1.exe training_data_path validatation_data_path testing_data_path > output_filename.txt

OUTPUT:
 After the above command is executed from the command prompt user is asked to enter a value that is either 1 or 2.
 
 If user enters 1 then the tree is constructed using ID3 algorithm.
 If user enters 2 then the tree is constructed using random attribute selection algorithm.
 
Program displays tree constructed using the user preferred option and displays the results and accuracy of that constructed tree.
   

ASSUMPTIONS:

1. Assuming training data and test data given are comapatible(meaning they both have same number of attributes).

2. code runs properly if user gives the command line arguments like file paths should proper as metioned above.






