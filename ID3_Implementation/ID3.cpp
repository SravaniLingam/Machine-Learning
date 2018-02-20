#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include<string>
#include<fstream>
#include<sstream>
#include <algorithm>
#include <memory.h>
#include <math.h>
#include <stdlib.h>     
#include <time.h>  
#include <stddef.h>
int no_of_attr=0;//No of attributes

int training_data[10000][10000];
int row,columns; // training  array
float target_entropy=0;
using namespace std;
      //string array to store attributes and class label
   string arr[100];

class node{
	public:
		int class_label;
		float Entrophy;
		int pos_inst,neg_inst;
		int attrval;
		int predictive_class;
		class node * left_ptr;
		class node *right_ptr;
		class node* parent;
		public:
			node();
			~node();
			float compute_entropy(int attr, vector<int> &attr_list, vector<int>& que, vector<int> & path, int& pos,int& neg,int& pos1,int& neg1,int &,int&,float,float & );
			void select_best_attribute(vector<int> &attr_list,vector<int>& que, vector<int> & path,int index,int convert,int& break_cond,float);
			void entropy_rootnode();
			void display(class node * r, vector<char> ss,int vs);
			void pre_process(class node* r,int & ,int &);
			void ID3_alg(class node* nt, vector<int> attr,vector<int> que, vector<int> path,int & index,float);
			void compute_prediction(class node* ,int a[],int &); //computes the prediction of class label an compares with actual class label 
			void prune(class node * , int & ,int );
		//	void class_label_process(class node*);
			
};
class node * root=NULL;
 node::node()
{
    class_label=0;
     Entrophy=0;
  	pos_inst=0;neg_inst=0;
  	predictive_class=-1;
	left_ptr=NULL;
	attrval=-1;
    right_ptr=NULL;parent=NULL; 	
}
 node::~node()
{
  cout<<"node is pruned"<<endl;	
}

float node::compute_entropy(int attr,vector<int> &attr_list, vector<int>& que, vector<int> & path, int& pos,int& neg,int& pos1,int& neg1,int & tot1,int & tot2,float ent,float & ent_tot)
{


int ss;

     	int i,j;
     	float yes=0,no=0,total=0,total1=0;
        int l=0;
     	//target=YES(1) calculation for entropy
    	for(int i=0;i<row;i++)
    	{ l=0;
    		for(j=0;j<que.size();j++)
    		{
    			if(training_data[i][que[j]]!=path[j])
    			  {
    			  	l=1;
    			  	break;
				  }
			}
			if(l==0)
			{ //cout<<"selecte row is "<<i<<endl;
		
              if(training_data[i][attr]==0)
             {
           	    total++;
           	    if(training_data[i][columns-1]==1)
           	       no++;
             }
           else
           {
           	 total1++;
           	 if(training_data[i][columns-1]==1 )
           	     yes++;
		   }    
		   
	      }
           
    	}
    	tot1=total;
    	tot2=total1;
    
    	float cal; // stores Entrophy for H(Y/(X = NO))
    	if(total!=0)
    	{
    	float pr1=no/total;
    //	int nss=pr1;
    	if(pr1==0)
        	pr1=1;
    	float pr2=1-(no/total);
    		if(pr2==0)
        	pr2=1;
    	
    	cal=((-pr1)*log2(pr1)-(pr2)*log2(pr2));

		pos=no;
    	neg=total-pos;
		}
		else
		{
			cal=0;
			pos=no;
         	neg=total-pos;
		}

    	
    	float cal1; //stores Entrophy for H(Y/(X = YES))
    	if(total1!=0)
    	{
	
    	float pr3=yes/total1;
    	  	if(pr3==0)
    	  	{
    	  		pr3=1;
			  }
            
    	float pr4=1-(yes/total1);
    	  	if(pr4==0)
    	  	{
    	  		pr4=1;
			  }
    	cal1=((-pr3)*log2(pr3)-(pr4)*log2(pr4));

		pos1=yes;
    	neg1=total1-pos1;
      }
      else
      {cal1=0;
      	pos1=yes;
    	neg1=total1-pos1;
	  }
    	
    	float sum=total+total1;
    	float no_pr=total/sum;
    	float yes_pr=total1/sum;
    	
     float total_entrophy= (no_pr*(cal)+yes_pr*(cal1));

    	ent_tot=total_entrophy;

    	return (ent-total_entrophy);
	}

void node::select_best_attribute(vector<int> &attr_list,vector<int>& que, vector<int> & path,int index,int convert,int& break_cond,float ent)
{
	int i,store=0;
	float max=-9999;
	
	int str_pos=0,str_pos1=0,str_neg=0,str_neg1=0,str_tota1=0,str_tota2=0;
	float str_ent_tot=0;

	for(i=0;i<attr_list.size();i++)
	{
		int pos=-1,pos1=-1,neg=-1,neg1=-1,tota1=-1,tota2=-1;
				float ent_tot1=0;
	    float computed=compute_entropy(attr_list[i],attr_list,que,path,pos,neg,pos1,neg1,tota1,tota2,ent,ent_tot1);
		if(max < computed)
		{
			max=computed;
			store=attr_list[i];
			str_pos=pos;
			str_neg=neg;
			str_pos1=pos1;
			str_neg1=neg1;
			str_tota1=tota1;
			str_tota2=tota2;
			str_ent_tot=ent_tot1;
			
		}
		
	}
	if(str_tota1==0 || str_tota2==0)
	{
		this->pos_inst=str_pos;
		this->neg_inst=str_neg;
		 break_cond=1;
	}
	
	 
if(convert==0)
{

	this->pos_inst=str_pos;
    this->neg_inst=str_neg;

	this->class_label=index;
	this->Entrophy=str_ent_tot;
	this->attrval=store;
	for(i=0;i<attr_list.size();i++)
	{
		if(store==attr_list[i])
		{
			break;
		}
	}
	attr_list.erase(attr_list.begin()+i);

	  que.push_back(store);
}

}

void node::entropy_rootnode()
{
     	int i,j;
     	float yes=0,no=0,total=0 ,total1=0;
     	
    	for(int i=0;i<row;i++)
    	{
           	 if(training_data[i][columns-1]==1)
           	     yes++;
        }	
	   total=row;
	   no=total-yes;
	   float pr=yes/total;
	   if(pr==0)
	     pr=1;
	   float pr1=no/total;
	    if(pr1==0)
	      pr1=0;
	   target_entropy=(-pr*(log2(pr))-(pr1*(log2(pr1))));
	   this->class_label=1;
	    this->Entrophy=target_entropy;
	    this->pos_inst=yes;
	    this->neg_inst=no;
	    
	    	  
	
}



void node::compute_prediction(class node * r, int a[], int & c)
{
 if(r)
   {
   	int i;

        
        if(r->attrval==-1)
        {

           if(a[r->left_ptr->attrval]==0)
        {

        	compute_prediction(r->left_ptr,a,c);
		}

         else
         {
        	compute_prediction(r->right_ptr,a,c);
		 }
		}
		else
		{
				
	     if(r->left_ptr==NULL && r->right_ptr==NULL )
        {

        	if(r->predictive_class==a[columns-1])
        	{
        		c=c+1;
			}
		 }
		 else
		 {
		 	if(r->left_ptr!=NULL)
		 	{
			 }
		 	if(r->left_ptr!=NULL && r->right_ptr==NULL)	 
		 	{	    if(a[r->left_ptr->attrval]==0)
                  {
                	compute_prediction(r->left_ptr,a,c);
		          }
		          else
		          {
        	     if(r->predictive_class==a[columns-1])
              	 {
        		   c=c+1;
			      }
			     }	
			 }
			 else if(r->left_ptr==NULL && r->right_ptr!=NULL)
			 {
			 	 if(a[r->right_ptr->attrval]==1)
                  {
                	compute_prediction(r->right_ptr,a,c);
		          }
		          else
		          {
        	     if(r->predictive_class==a[columns-1])
              	{
        		c=c+1;
			    }
			   }
			 }
				else
				{
	           if(a[r->left_ptr->attrval]==0)
               {
         
             	compute_prediction(r->left_ptr,a,c);
		       }
                else
               {
        	       compute_prediction(r->right_ptr,a,c);
		       }
		      }  
		  }

		}
	}

}

void node::pre_process(class node * r,int & leafs,int& internal)
{
	if(r)
	{
		
		//	cout<<"r-postive instances"<<r->pos_inst<<"negitve"<<r->neg_inst<<endl;
		if(r->left_ptr==NULL && r->right_ptr==NULL)
		{
			leafs=leafs+1;
	    }
	   	else
		{
			internal=internal+1;
		}
		//	cout<<"leaf_ postive instances"<<r->pos_inst<<"leaf_ negitve"<<r->neg_inst<<endl;
		   if(r->pos_inst>=(r->neg_inst))
		   {
		   	
		      r->predictive_class=1;
		   }
		   else
		   {
		     r->predictive_class=0; 	
		   }
		   	
	


	 //  int p=r->class_label;
		pre_process(r->left_ptr,leafs,internal);
		pre_process(r->right_ptr,leafs,internal);

		
	}
}
void node::display(class node * r,vector<char> ss,int vs)
{
  if(r)
   {
   	int i;
   	for(i=0;i<ss.size();i++)
   	{
   		cout<<ss[i]<<" ";
    }
   	if((r->attrval)!=-1)
   	{
   		
        cout<<arr[r->attrval]<<" = "<<vs<<" : ";
        if(r->left_ptr==NULL && r->right_ptr==NULL)
        {
        	cout<<r->predictive_class;
		}
		//cout<<"index is   "<<r->class_label;
		cout<<endl;
        ss.push_back('|');
	}
   display(r->left_ptr,ss,0);
   display(r->right_ptr, ss,1);
  }
}

//Pruning the Tree
void node::prune(class node * r,int& pf,int steps)
{
if(r)
{
  if(pf==0)
{
	return ;
 }
 else
 {
   if(r->left_ptr==NULL && r->right_ptr==NULL) 
	{
	     int a= (r->pos_inst+(r->neg_inst));
	     if(a<0)
	     a=-a;
	     if(a<=steps && pf>0)
	     {
	     	//cout<<"nodes that are pruned is "<<r->class_label<<endl;
	     	pf=pf-1;
	      class node * tem=r->parent->left_ptr;
	      class node * tem1=r->parent->right_ptr;
	      if(tem!=NULL)
	      {
	      int b=(tem->pos_inst+tem->neg_inst);
	      if(b<0)
	       b=-b;
	       if(a==b)
	       {
	       	r->parent->left_ptr=NULL;
		   }
		   else
		   {
		   	r->parent->right_ptr=NULL;
		   }
		  }
		  else
		  {
		  	r->parent->right_ptr=NULL;
		  }
		 }
	         	
   }
   else
   {
   	prune(r->left_ptr,pf,steps);
   	prune(r->right_ptr,pf,steps);

   }      
 }
	
 }
		
}

//ID3 Algorithm Implementation Starts here.

void node::ID3_alg(class node* nt, vector<int> attr,vector<int> que, vector<int> path,int & index,float ent)
{
	if(nt)
	{
int i,j;
vector<int> attr_temp;
vector<int> que_temp;
vector<int> path_temp;
for(i=0;i<attr.size();i++)
{
	attr_temp.push_back(attr[i]);		
}

for(j=0;j<que.size();j++)
{

	que_temp.push_back(que[j]);

}

for(j=0;j<path.size();j++)
{
	
	path_temp.push_back(path[j]);
}
//cout <<endl;
	if(attr.size()==0)
	{
		int ma;
		ma=nt->pos_inst;
		//nt->predictive_class=1;
		if(ma<nt->neg_inst)
		 {
	    	//nt->predictive_class=0;
		 } 

		 return ;
		  
	}
	if(nt->pos_inst==0)
	{
		//nt->predictive_class=0;
		return;
		
	}
	if(nt->neg_inst==0)
	{
	//	nt->predictive_class=1;
		return;
		
	}
	
    if(que.size()==columns)
   {
   	 	int ma;
		ma=nt->pos_inst;
		if(ma<nt->neg_inst)
		 {
	    	//nt->predictive_class=0;
		 } 
		 return;
   }
   else
   {
   	 class node* tem1=new class node;
     class node* tem2=new class node;  
     int convert=0;
    int break_cond=0;
         index++;
   	ent=nt->Entrophy;
     tem1->select_best_attribute(attr,que,path,index,convert,break_cond,ent);
     path.push_back(0);
     nt->left_ptr=tem1;
     tem1->parent=nt;
     //display(tem1);
     if(break_cond!=1)
      ID3_alg(nt->left_ptr,attr,que,path,index,ent);
      else
      {
      		tem1->pos_inst=(nt->pos_inst)-(nt->left_ptr->pos_inst);
         	tem1->neg_inst=(nt->neg_inst)-(nt->left_ptr->neg_inst);
	  }
      
    path_temp.push_back(1);

       convert=1;
       	tem2->attrval=nt->left_ptr->attrval;
	for(i=0;i<attr_temp.size();i++)
	{
		if(tem2->attrval==attr_temp[i])
		{
			break;
		}
	}
	index++;
	int bbc=attr_temp[i];
	attr_temp.erase(attr_temp.begin()+i);
	que_temp.push_back(bbc);
	tem2->class_label=index;
	tem2->Entrophy=nt->left_ptr->Entrophy;
	ent=tem2->Entrophy;
	tem2->pos_inst=(nt->pos_inst)-(nt->left_ptr->pos_inst);
	tem2->neg_inst=(nt->neg_inst)-(nt->left_ptr->neg_inst);
	
    break_cond=0;
    tem2->select_best_attribute(attr_temp,que_temp,path_temp,index,convert,break_cond,ent);

    nt->right_ptr=tem2;
     tem2->parent=nt;

   if(break_cond!=1)
    ID3_alg(nt->right_ptr,attr_temp,que_temp,path_temp,index,ent);
     else
     {
      		tem2->pos_inst=(nt->pos_inst)-(nt->left_ptr->pos_inst);
         	tem2->neg_inst=(nt->neg_inst)-(nt->left_ptr->neg_inst);   
	 }
 
   }
}
}

int main(int argc,char *argv[])
{
     ifstream CSVfile(argv[1]);
     if (CSVfile == NULL) {
    cout << "ERROR: Cannot Open CSV file" << endl;
    exit(1);
}

    string line;
    getline(CSVfile,line,'\n');
    stringstream str(line);
   int counter=0;
   string temp;
 
   while (str>>temp !='\0')
   {
   	arr[counter]=temp;
   	counter++;
   }
   no_of_attr=counter;
   columns=counter;
   memset(training_data,-1,sizeof(training_data));
   
  int i=0,j;
   while(getline(CSVfile,line,'\n'))
   {  
     stringstream str1(line);
     int t1;
     j=0;
     while(str1>>t1 !='\0')
     {
     	training_data[i][j]=t1;
     	j++;
     
	 }
	 i++;
   }
   row=i;  
   
   vector<int> que;
    vector<int> path;
   class node * tree = new class node;
   tree->entropy_rootnode();
   root=tree;
   root->Entrophy=target_entropy;
     vector<int> attrs;
     for(i=0;i<columns-1;i++)
     {
     	attrs.push_back(i);
	 }
 int indic=0;
   tree->ID3_alg(root,attrs,que,path,indic,target_entropy);
   vector<char> ss;
   int training_leafnodes=0,training_internalnodes=0,Accuracy;

   tree->pre_process(root,training_leafnodes,training_internalnodes);
   cout<<endl<<endl;
   tree->display(root,ss,0);
   cout<<endl;
   
   cout<<"Pre-Pruned Accuracy" <<endl;
   cout <<"-------------------------------------"<<endl;
   cout<<"Number of training instances =  "<<row<<endl;
   cout<<"Number of training attributes =  "<<columns-1<<endl;  
   cout <<"Total number of nodes in the tree =  "<<training_internalnodes+training_leafnodes-1<<endl;
   cout <<"Total number of leaf nodes in the tree =  "<<training_leafnodes<<endl;
   
   // training Accuracy
   int training_inst=row;
   int training_attr=columns;
      
	  int count_correct_inst=0;	

   for(i=0;i<row;i++)
    {
	 tree->compute_prediction(root,training_data[i],count_correct_inst);
   }
   float tem_var=count_correct_inst;
 float train_accuracy=(((tem_var)/row)*100);
  

    cout <<"Accuracy of the model on the training dataset = "<<train_accuracy<<endl;
    
    cout <<endl;
    cout<<endl;
    cout <<endl;
   
    ifstream CSVfile1(argv[2]);
    if (CSVfile1== NULL) {
    cout << "ERROR: Cannot Open CSV file" << endl;
    exit(1);
}
   getline(CSVfile1,line,'\n');
   //stringstream str(line);
   int test_columns=columns;
   int test_rows=0;
   int temp_arr[10000];
   int test_correct_instances=0;
   while(getline(CSVfile1,line,'\n'))
   {  
     stringstream str1(line);
     int t1;
     j=0;
     while(str1>>t1 !='\0')
     {
     	temp_arr[j]=t1;
     	j++;
     
	 }
	 tree->compute_prediction(root,temp_arr,test_correct_instances);
	 test_rows++;
   }
   cout <<"Number of testing instances = "<<test_rows<<endl;
   cout <<"Number of testing attributes = "<<columns-1<<endl;
   tem_var=test_correct_instances;
   float test_accuracy=((tem_var/test_rows)*100);
   cout <<"Accuracy of the model on the testing dataset ="<<test_accuracy<<endl; 

    cout <<endl;
    cout <<endl;
    cout <<endl;
    //prune Tree based on the pruning factor given
    float prun_factor=0;
    int total_nodes=(training_internalnodes+training_leafnodes-1);
    prun_factor=atof(argv[3]);
    int prun=(prun_factor)*(total_nodes);
    
    int steps=1;
   training_leafnodes=0;
   training_internalnodes=0;
     tree->prune(root,prun,steps);
	 while(prun!=0)
	 {
	   steps=steps+4; 
	   tree->prune(root,prun,steps); 
	 }
   tree->pre_process(root,training_leafnodes,training_internalnodes);
   tree->display(root,ss,0);

   
    count_correct_inst=0;	

   for(i=0;i<row;i++)
    {
	 tree->compute_prediction(root,training_data[i],count_correct_inst);
   }
    cout <<endl<<endl<<endl;

   cout<<"Post-Pruned Accuracy" <<endl;
   cout <<"-------------------------------------"<<endl;
   cout<<"Number of training instances =  "<<row<<endl;
   cout<<"Number of training attributes =  "<<columns-1<<endl;  
   cout <<"Total number of nodes in the tree =  "<<training_internalnodes+training_leafnodes-1<<endl;
   cout <<"Total number of leaf nodes in the tree =  "<<training_leafnodes<<endl;

    tem_var=(count_correct_inst);
    train_accuracy=(tem_var/row)*100;
    cout <<"Accuracy of the model on the training dataset ="<<train_accuracy<<endl;
    cout <<endl;
    cout<<endl;
   
    ifstream CSVfile2(argv[2]);
    if (CSVfile2 == NULL) {
    cout << "ERROR: Cannot Open CSV file" << endl;
    exit(1);
}
   getline(CSVfile2,line,'\n');
   //stringstream str(line);
    test_columns=columns;
   test_rows=0;
   test_correct_instances=0;
   while(getline(CSVfile2,line,'\n'))
   {  
     stringstream str1(line);
     int t1;
     j=0;
     while(str1>>t1 !='\0')
     {
     	temp_arr[j]=t1;
     	j++;
     
	 }
	 tree->compute_prediction(root,temp_arr,test_correct_instances);
	 test_rows++;
   }

    tem_var=test_correct_instances;
    test_accuracy=(tem_var/test_rows)*100;
    cout <<"Number of testing instances = "<<test_rows<<endl;
    cout <<"Number of testing attributes = "<<columns-1<<endl;
    cout <<"Accuracy of the model on the testing dataset ="<<test_accuracy<<endl; 
	return 0;
}





