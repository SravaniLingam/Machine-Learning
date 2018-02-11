
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.math.NumberUtils;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class preprocessing_final {
	//static String FilePath;
	static FileInputStream fs;
	static Workbook wb;
	static Sheet sh;
	static int totalNoOfRows,totalNoOfCols,count=0,deleteRowNumber=-1,i=1;
	static List<String> ExpectedColumns;
	static LinkedHashMap<String, List<String>> columnDataValues;
	static List<String> column1,l,temp,l1,l2,l3;
	static List<Integer> nullRows =  new ArrayList<Integer>();
	static TreeMap<String, Integer> MapSetValue=new TreeMap<String, Integer>();
	 
	public static void readExcel(String FilePath) throws BiffException, IOException {
		//FilePath = "/Users/shivapodugu/Desktop/ML/Assignment3/boston_raw_data_set.xls";
		//FilePath="/Users/shivapodugu/Desktop/ML/Assignment3/iris_raw_dataset.xls";
		//FilePath="/Users/shivapodugu/Desktop/ML/a.xls";
		//FilePath="/Users/shivapodugu/Desktop/ML/Assignment3/census_income_raw_data.xls";
		 fs = new FileInputStream(FilePath);
		 wb = Workbook.getWorkbook(fs);

		// To get the access to the sheet
		sh = wb.getSheet("Sheet1");

		// To get the number of rows present in sheet
		 totalNoOfRows = sh.getRows();

		// To get the number of columns present in sheet
		 totalNoOfCols = sh.getColumns();

		/*for (int row = 0; row < totalNoOfRows; row++) {

			for (int col = 0; col < totalNoOfCols; col++) {
				System.out.print(sh.getCell(col, row).getContents() + "\t");
			}
			System.out.println();
			}
			*/
		
		//storing column names 
		 ExpectedColumns = new ArrayList<String>();
	    for (int x = 0; x < totalNoOfCols; x++) {
	        String d = sh.getCell(x, 0).getContents();
	        ExpectedColumns.add(d);
	    }
	    //printing column names
	  /*  for(int i = 0; i < ExpectedColumns.size(); i++) {
            System.out.println(ExpectedColumns.get(i));
        }
        */
	    //System.out.println("\n\n\n\n");
	}
	
	
	public static void Display_LinkedHashMap()
	{
		  // Get a set of the entries
	      Set set = columnDataValues.entrySet();
	      
	      // Get an iterator
	      Iterator i = set.iterator();  
	      // Display elements
	      while(i.hasNext()) 
	      {
	         Map.Entry me = (Map.Entry)i.next();
	         System.out.print(me.getKey() + ": ");
	         System.out.println(me.getValue());
	      }
	     // System.out.println("columnDataValues "+columnDataValues.size());  
	}
	
	
	public static void find_add_incomplete_features()
	{
		//System.out.println("find_incomplete_features()");
		
		    for (int jj = 0; jj < totalNoOfCols; jj++) 
		    {
		        column1 = new ArrayList<String>();
		        l =new ArrayList<String>();
		       
		        for (int ii = i; ii < sh.getRows(); ii++) 
		        {    
		        	String c=sh.getCell(jj, ii).getContents().toString().trim();
		            if((sh.getCell(jj, ii).getContents().isEmpty()) )
		       	    {
		       		 
		       		    System.out.println("sh.getCell(jj, ii).getContents().isEmpty() : "+ii);
		       		    
		       		 //nullRows[count]=String.valueOf(ii);
		       				 //Integer.toString(ii);
		       		 //nullRows[count]=ii;
		       		 //System.out.println("nullRows.contains(ii)"+nullRows.contains(ii));
		       		    if(!nullRows.contains(ii))
		       		nullRows.add(ii);
		       		 //count++;
		       		column1.add(sh.getCell(jj, ii).getContents());
		       	    }
		            //System.out.println("read values from driver sheet for each column :");
		            //System.out.println(sh.getCell(j, i).getContents().toString());
		            else if (c.equalsIgnoreCase("?") ) 
			       	   {
		            	//System.out.println("sh.getCell(jj, ii).getContents().toString() ? : "+ii);
		            	if(!nullRows.contains(Integer.toString(ii)))
				       		nullRows.add(ii);
		          
		            	//System.out.println("nullRows size: "+nullRows.size());
		            	column1.add(sh.getCell(jj, ii).getContents());			        	
		            	}
		            
		            else if (sh.getCell(jj,ii).getType() == CellType.NUMBER) 
			       	   {
			        	    NumberCell nc = (NumberCell) sh.getCell(jj, ii);
			        	    double doubleA = nc.getValue();
			        	    // this is a double containing the exact numeric value that was stored 
			        	    // in the spreadsheet
			        	    column1.add(Double.toString(doubleA));
			        	}
			       	
			        	else
			        	{
			        		
			            column1.add(sh.getCell(jj, ii).getContents());/////////////
			          //  System.out.println("sh.getCell(jj, ii).getType()"+sh.getCell(jj, ii).getType());
			           // System.out.println("sh.getCell(jj, ii).getContents()"+sh.getCell(jj, ii).getContents());
			           // String c=sh.getCell(jj, ii).getContents().toString().trim();
			            //if(c.equalsIgnoreCase("?"))
			            //{
			            //	System.out.println("found u ?");
			            //}
			        	}
		            	
		            }
		        l =new ArrayList<String>();
		        if(columnDataValues.containsKey(ExpectedColumns.get(jj))){
		    	    // if the key has already been used,
		    	    // we'll just grab the array list and add the value to it
		    	    l = columnDataValues.get(ExpectedColumns.get(jj));
		    	    l.addAll(column1);
		    	    if(l.size()<sh.getRows()-count)
		    	    columnDataValues.put(ExpectedColumns.get(jj), l);
		    	} 
		       else {
		    	    // if the key hasn't been used yet,
		    	    // we'll create a new ArrayList<String> object, add the value
		    	    // and put it in the array list with the new key
		    	  //  l = new ArrayList<String>();
		    	    
		    	   // l.addAll(column1);
		    	   if(column1.size()<sh.getRows()-count)
		    	    columnDataValues.put(ExpectedColumns.get(jj), column1);
		    	}
		        
		        }
		    }
	      
		//System.out.println(sh.getCell(13, 469).getContents());
	
	public static void print_nullrows()
	{
		//System.out.println("print_nullrows start");
		//System.out.println("nullRows.size()"+nullRows.size());
		for(int i = 0; i < nullRows.size(); i++) 
		{
            System.out.println(nullRows.get(i));
		}
		//System.out.println("print_nullrows end");
	}
	public static void sort_nullrows_desc()
	{
		/* Sorting in reversing order*/
		//System.out.println("sort_nullrows_desc() called");
		Collections.sort(nullRows, Collections.reverseOrder());
		//Removing duplicates
		int totalSize = nullRows.size();
	    for (int i = 0; i < totalSize; i++) 
	    {
	        for (int j = i + 1; j < totalSize; j++) 
	        {
	            if (nullRows.get(i).equals(nullRows.get(j)))
	            {
	            	nullRows.remove(j--);
	            	totalSize--;
	            }
	        }
	    }
	}
	
	public static void delete_nullrows()
	{
		sort_nullrows_desc();
		//print_nullrows();
		//System.out.println("delete_nullrows");
		column1 = new ArrayList<String>();
        l =new ArrayList<String>();
		for (int j = 0; j < totalNoOfCols; j++) 
	    {
			if(columnDataValues.containsKey(ExpectedColumns.get(j)))
			{ 
	    	    l = columnDataValues.get(ExpectedColumns.get(j));
	    	    //System.out.println("l.size() "+l.size());
	    	    for (int m = 0; m < nullRows.size(); m++) 
	    	    {
	    	    	//System.out.println("nullRows.get(m) "+nullRows.get(m));
	    	    	//System.out.println("l.size() "+l.size());
	    	    	//System.out.println("l.get(nullRows.get(m)) "+l.get(Integer.parseInt(nullRows.get(m))));
	    	    	//if(l.size()>Integer.parseInt(nullRows.get(m)))
	    	        l.remove(nullRows.get(m)-1);
	    	    }
	    	    columnDataValues.put(ExpectedColumns.get(j), l);
			}      	    
	    	}  
		totalNoOfRows=l.size();
		//System.out.println("totalNoOfRows=l.size();"+totalNoOfRows);
	    }
	
	public static double getMean(double[] data,int size)
	    {
	        double sum = 0.0;
	        for(double a : data)
	            sum += a;
	        return sum/size;
	    }

	 public static double getVariance(double mean,int size,double[] data)
	    {
	        
	        double temp = 0;
	        for(double a :data)
	            temp += (a-mean)*(a-mean);
	        return temp/size;
	    }

	    public static  double getStdDev(double var)
	    {
	        return Math.sqrt(var);
	    }
	    public static void setValueToCategory(Set set1)
		{
	    	Iterator itr = set1.iterator();
	        int setValue=1;
	   	    //TreeMap<String, Integer> MapSetValue = new TreeMap<String, Integer>();
	        while(itr.hasNext())
	        {
	        	String CatData=itr.next().toString();
	        	
	        	if (MapSetValue.containsKey(CatData)!= true)
	        	{
		        	MapSetValue.put(CatData, setValue);
		        	setValue++;
	        	}
	            //System.out.println(CatData);
	        }
	        System.out.println("The values set to various categories: " + MapSetValue);
	        //System.out.println(MapSetValue);
	        
	        //System.out.println("setValueToCategory end");
		}
	    public static void convertCategoricalToNumeric()
		{
	    	LinkedHashSet<String> set = new LinkedHashSet<String>();
	    	//Set set = new HashSet();
		    //TreeMap<String, Integer> MapSetValue = new TreeMap<String, Integer>();
		    for (int j = 0; j < totalNoOfCols; j++) 
		    { 
				if(columnDataValues.containsKey(ExpectedColumns.get(j)))
				{ 
					
		    	    l = columnDataValues.get(ExpectedColumns.get(j));
		    	    for (int m = 0; m < totalNoOfRows; m++) 
		    	    {
		    	    	if(!NumberUtils.isNumber(l.get(m))  )
		    	    	{
		    	    		 set.add(l.get(m));	
		    	    	}
		    	    }
		    	    }
		    }
		    setValueToCategory(set);
		    l1 =new ArrayList<String>();
		    for (int j = 0; j < totalNoOfCols; j++) 
		    {
				if(columnDataValues.containsKey(ExpectedColumns.get(j)))
				{ 
					
		    	    l1 = columnDataValues.get(ExpectedColumns.get(j));
		    	    for (int m = 0; m < totalNoOfRows; m++) 
		    	    {
		    	    	if(!NumberUtils.isNumber(l1.get(m))  )
		    	    	{
		    	    		if (MapSetValue.containsKey(l1.get(m)))
		    	        	{
		    	    			int t=MapSetValue.get(l1.get(m));
		    	    			 l1.set(m,Integer.toString(t));
		    	        	}
		    	    	}
		    	    }
		    	    }
				
				columnDataValues.put(ExpectedColumns.get(j), l1);
		    }
		    
	    	//System.out.println("convertCategoricalToNumeric() end");
		}
		
	
	
	public static void standardization()
	{
		double[] data = new double[totalNoOfRows];
	    int size;  
	   double standardDeviation,mean,variance;
	   size=totalNoOfRows;
	   convertCategoricalToNumeric();
	   //Display_LinkedHashMap();
	   System.out.println("standardizing under process..");
	   l2 =new ArrayList<String>();
		for (int j = 0; j < totalNoOfCols; j++) 
	    {
			if(columnDataValues.containsKey(ExpectedColumns.get(j)))
			{ 
				
	    	    l2 = columnDataValues.get(ExpectedColumns.get(j));
	    	    for (int m = 0; m < l2.size(); m++) 
	    	    {    data[m]=0;
	    	    	if(NumberUtils.isNumber(l2.get(m))  )
	    	    	{
	    	    		//System.out.println("NumberUtils.isNumber(l1.get(m)");
	    	    		data[m]=Double.parseDouble(l2.get(m));	
	    	    	}
	    	    }
	    	    mean=getMean(data,size);
	    	    //System.out.println("mean: "+mean);
	    	    variance=getVariance(mean,size,data);
	    	    //System.out.println("variance: "+variance);
	    	    standardDeviation=getStdDev(variance);
	    	    //System.out.println("standardDeviation: "+standardDeviation);
	    	   // l.clear();
	    	    l3 =new ArrayList<String>();
	    	    for (int k = 0; k < totalNoOfRows;k++) 
	    	    {
	    	    	if(standardDeviation==0)
	    	    	{
	    	    		data[k]=standardDeviation;
	    	    	}
	    	    	if(standardDeviation!=0)
	    	    	{
	    	    	data[k]=(data[k]-mean)/standardDeviation;
	    	    	}
	    	    	DecimalFormat df = new DecimalFormat(".####"); 
	    	    	//data[m]=Double.parseDouble(df.format(data[k]));
	    	    	l3.add(Double.toString(Double.parseDouble(df.format(data[k]))));
	    	    }
	    	    
			}      	   
	    	    columnDataValues.put(ExpectedColumns.get(j), l3);
	    }  
	    }
	
	
	private static void writeExcel(String fp) throws WriteException, IOException {
		
		//String filePath = "/Users/shivapodugu/Desktop/ML/postprocessedData__.xls";
		//String filepath2=fp;
		WritableWorkbook workBook = null;
		try {
			//initialize workbook
			
			workBook = Workbook.createWorkbook(new File(fp));
			//create sheet with name as Employee and index 0
			WritableSheet sheet = workBook.createSheet("Sheet1", 0);
			// create font style for header cells
			WritableFont headerCellFont = new WritableFont(WritableFont.ARIAL, 14,
					WritableFont.BOLD, true);
			//create format for header cells
			WritableCellFormat headerCellFormat = new WritableCellFormat(headerCellFont);
			// create header cells
			for(int i = 0; i < ExpectedColumns.size(); i++) 
			{
	           // System.out.println(ExpectedColumns.get(i));
				Label headerCell = new Label(i, 0, ExpectedColumns.get(i), headerCellFormat);
				sheet.addCell(headerCell);
	        }
			
 			// create cell contents for header cells
			temp= new ArrayList<String>();
			//System.out.println("sixz of columnDataValues"+columnDataValues.size());
			//System.out.println("sixz of totalNoOfRows"+totalNoOfRows);
			for (int j = 0; j < columnDataValues.size(); j++) 
		    {
								//System.out.println("columnDataValues.containsKey(ExpectedColumns.get(j))"+columnDataValues.containsKey(ExpectedColumns.get(j)));
				if(columnDataValues.containsKey(ExpectedColumns.get(j)))
				{ 
					
		    	    temp = columnDataValues.get(ExpectedColumns.get(j));
		    	   
		    	    for (int m = 0; m < temp.size(); m++) 
		    	    {
		    	    	Label contentCell = new Label(j, m+1, temp.get(m));
						sheet.addCell(contentCell);
						
		    	    }
		    	    
				}   
				
		    }
			
			//write workbook
			workBook.write();
			System.out.println("Post Processed File is created and stored in "+fp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} finally {
			//close workbook
			workBook.close();
		}
	}
	
	public static void main(String[] args) throws BiffException, IOException
	{
		readExcel(args[0]);
		columnDataValues = new LinkedHashMap<String, List<String>>();
		find_add_incomplete_features();
		//Display_LinkedHashMap();
		//print_nullrows();
		delete_nullrows();
		//Display_LinkedHashMap();
		standardization();
		//Display_LinkedHashMap();
		try {
			writeExcel(args[1]);
		} 
		catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
