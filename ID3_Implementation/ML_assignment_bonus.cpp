#include <iostream>
#include <cmath>
#include <fstream>
#include <cstdio>
#include <sstream>
#include <vector>
#include <memory.h>
#include <string>
#include <stdlib.h>
#include <algorithm>
#include <time.h>
#include <math.h>
#include <stddef.h>
int training_data[900][900],no_of_attributes=0,row,cols;
using namespace std;


float target_entropy=0,train_accuracy=0,tem_var;
string atrbNames[200];
int countCorrectInst=0,trainLeafNodes=0,trainInternalNodes=0;

class Node
{
public:
    int class_label,atribute_val,prdctv_cls,negative_instance,positive_instance,height;
    float entropy;
    class Node * parent,* rt_pntr,* lt_pntr;
    
public:
    Node();
    ~Node();
    void calculate_entropy_rootnode();
    void find_best_attribute(vector<int> &atribute_list,vector<int>& cuee, int index,vector<int> & path,int convrt,int& break_cond,float);
    void print(class Node * a, vector<char> b,int c);
    float estimate_entropy(int attributes, vector<int> &atribute_list, vector<int>& cuee, vector<int> & path, int& positive,int& negative,int& positive1,int& negative1,int &,float,int&,float & );
    void preProcessing(class Node* a,int & ,int &,int &);
    void Id3_Algo(class Node* nt, vector<int> attributes,vector<int> cuee, vector<int> path,int & index,float,int heigh);
    void evaluate_prediction(class Node* ,int a[],int &); 
   // void prune(class Node * , int & ,int );
    void random_attribute_selection(class Node* nt, vector<int> attr,vector<int> que, vector<int> path,int & index,int heigh);		
	void random_attr(vector<int> &attr,vector<int> &que, vector<int>& path,int &,int );
};

class Node * root=NULL;
Node::Node()
{
    class_label=0;
    entropy=0;
    positive_instance=0;
    negative_instance=0;
    prdctv_cls=-1;
    atribute_val=-1;
    rt_pntr=NULL;
    lt_pntr=NULL;
    parent=NULL;
    
}
Node::~Node()
{
    cout<<"Node is pruned now!!"<<"\n";
}

float Node::estimate_entropy(int attributes,vector<int> &atribute_list, vector<int>& cuee, vector<int> & path, int& positive,int& negative,int& positive1,int& negative1,int & tot1,float ent,int & tot2,float & ent_tot)
{
    int j,k=0;
    float yes=0,no=0,sumTotal=0,totalSum1=0,cal,cal1, total_entrophy,yes_ptr,no_ptr,sum,ptr4,ptr3,ptr2,ptr1;
    for(int i=0;i<row;i++)
    { k=0;
        j=0;
        while(j<cuee.size())
        {
        	if(training_data[i][cuee[j]]!=path[j])
            {
                k=1;
                break;
            }
        	j++;
		}
        if(k==0)
        { 
            if(training_data[i][attributes]!=0)
            {
           	    totalSum1++;
           	    if(training_data[i][cols-1]==1)
                    yes++;
            }
            else
            {
                sumTotal++;
                if(training_data[i][cols-1]==1 )
                    no++;
            }
        }
    }
    tot1=sumTotal; tot2=totalSum1;
   
    if(sumTotal==0)
    {
    	 cal=0;
        positive=no;
        negative=sumTotal-positive;
    }
    else
    {
           ptr1=no/sumTotal;
        if(ptr1==0)
            ptr1=1;
         ptr2=1-(no/sumTotal);
        if(ptr2==0)
            ptr2=1;
        cal=((-ptr1)*log2(ptr1)-(ptr2)*log2(ptr2));
        positive=no;
        negative=sumTotal-positive;
    }
    
    if(totalSum1!=0)
    {
         ptr3=yes/totalSum1;
    	  	if(ptr3==0)
            ptr3=1;
         ptr4=1-(yes/totalSum1);
    	  	if(ptr4==0)
            ptr4=1;   
        cal1=((-ptr3)*log2(ptr3)-(ptr4)*log2(ptr4));
        positive1=yes;
        negative1=totalSum1-positive1;
    }
    if(totalSum1==0)
    {
	    cal1=0;
        positive1=yes;
        negative1=totalSum1-positive1;
    }
    
     sum=sumTotal+totalSum1;
     no_ptr=sumTotal/sum;
     yes_ptr=totalSum1/sum;
     total_entrophy= (no_ptr*(cal)+yes_ptr*(cal1));    
    ent_tot=total_entrophy;
    return (ent-total_entrophy);
}

void Node::calculate_entropy_rootnode()
{
    float yes=0,no=0,sumTotal=0;
    int i=0;
    while(i<row)
    {
    	 if(training_data[i][cols-1]==1)
            yes++;
            i++;
	}
	   sumTotal=row;
	   no=sumTotal-yes;
	   float pr=yes/sumTotal;
	   if(pr==0)
           pr=1;
	   float p1=no/sumTotal;
    if(p1==0)
        p1=0;
	   target_entropy=(-pr*(log2(pr))-(p1*(log2(p1))));
	   this->class_label=1;
    this->entropy=target_entropy;
    this->positive_instance=yes;
    this->negative_instance=no;   
}

void Node::find_best_attribute(vector<int> &atribute_list,vector<int>& cuee, int index,vector<int> & path,int convrt,int& break_cond,float ent)
{
    int i,store=0;
    float max=-9999;
    
    int str_positive=0,str_negative=0,str_positive1=0,str_negative1=0,str_tota1=0,str_tota2=0;
    float str_ent_tot=0;
   i=0;
    while(i<atribute_list.size())
    {
    	int positive=-1,negative=-1,positive1=-1,negative1=-1,tota1=-1,tota2=-1;
        float ent_tot1=0;
        float computed=estimate_entropy(atribute_list[i],atribute_list,cuee,path,positive,negative,positive1,negative1,tota1,ent,tota2,ent_tot1);
        if(max < computed)
        {
            max=computed;
            store=atribute_list[i];
            str_positive=positive;
            str_negative=negative;
            str_positive1=positive1;
            str_negative1=negative1;
            str_tota1=tota1;
            str_tota2=tota2;
            str_ent_tot=ent_tot1;
        } 
    	i++;
    	
	}
    if(str_tota1==0 || str_tota2==0)
    {
        this->positive_instance=str_positive;
        this->negative_instance=str_negative;
        break_cond=1;
    }
    if(convrt==0)
    { 
        this->positive_instance=str_positive;
        this->negative_instance=str_negative;   
        this->class_label=index;
        this->entropy=str_ent_tot;
        this->atribute_val=store;
        i=0;
        while(i<atribute_list.size())
        {
            if(store==atribute_list[i])
            {
                break;
            } 
            i++;
        }
        atribute_list.erase(atribute_list.begin()+i);
        cuee.push_back(store);
    } 
}





void Node::evaluate_prediction(class Node * n, int a[], int & c)
{
    if(n)
    { 
        if(n->atribute_val==-1)
        {
            if(a[n->lt_pntr->atribute_val]==0)
                evaluate_prediction(n->lt_pntr,a,c);
            if(a[n->lt_pntr->atribute_val]!=0)
                evaluate_prediction(n->rt_pntr,a,c);
        }
        else
        {
            if(n->lt_pntr==NULL && n->rt_pntr==NULL )
            {
                if(n->prdctv_cls==a[cols-1])
				c=c+1; 
            }
            else
            {
                if(n->lt_pntr!=NULL && n->rt_pntr==NULL)
                {	    
				if(a[n->lt_pntr->atribute_val]==0)
                evaluate_prediction(n->lt_pntr,a,c);
                else
                {
                    if(n->prdctv_cls==a[cols-1])
                        c=c+1;
                }
            }
                else if(n->lt_pntr==NULL && n->rt_pntr!=NULL)
                {
                    if(a[n->rt_pntr->atribute_val]!=1)
                    {
                    	if(n->prdctv_cls==a[cols-1]) 
						c=c+1; 
                    }
                    else
                         evaluate_prediction(n->rt_pntr,a,c); 
                }
                else
                {
                    if(a[n->lt_pntr->atribute_val]!=0)
                        evaluate_prediction(n->rt_pntr,a,c);
                    else
                        evaluate_prediction(n->lt_pntr,a,c);
                }
            }
        }
    }  
}

void Node::print(class Node * a,vector<char> b,int c)
{
    if(a)
    {
        int i=0;
         while(i<b.size())
		 {cout<<b[i]<<" ";
		 i++;
			}   
            
        if((a->atribute_val)!=-1)
        {
            cout<<atrbNames[a->atribute_val]<<" = "<<c<<" : ";
            if(a->lt_pntr==NULL && a->rt_pntr==NULL)
            cout<<a->prdctv_cls;
            cout<<"\n";
            b.push_back('|');
        }
        print(a->rt_pntr,b,1);
        print(a->lt_pntr,b,0);
        
    }
}

void Node::preProcessing(class Node * a,int& leaves,int& internal,int& total_height)
{
    if(a)
    {
        if(a->lt_pntr==NULL && a->rt_pntr==NULL)
        {
        	leaves=leaves+1;
        	total_height=total_height+ (a->height-1);
		}
            
        else
            internal=internal+1;
       
        if(a->positive_instance>=(a->negative_instance))  
            a->prdctv_cls=1;
        else
            a->prdctv_cls=0;
        preProcessing(a->lt_pntr,leaves,internal,total_height);
        preProcessing(a->rt_pntr,leaves,internal,total_height);
    }
}

void Node::random_attr( vector<int> &attr_list,vector<int> &que, vector<int>& path,int & break_cond,int convert )		
{		
			
	srand (time(NULL));		
	int random_val=0;		
	if(attr_list.size()!=0)		
    random_val	= rand() % (attr_list.size());		
     else		
     return ;		
		int i,j;		
     	float yes=0,no=0,total=0,total1=0;		
        int l=0;		
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
			{ 		
				
              if(training_data[i][attr_list[random_val]]==0)		
             {		
             	//cout<<"selecte row is "<<i<<endl;		
           	    total++;		
           	    if(training_data[i][cols-1]==1)		
           	       no++;		
             }		
           else		
           {		
           	 total1++;		
           	 if(training_data[i][cols-1]==1 )		
           	     yes++;		
		   }    		
		   		
	      }		
           		
    	}		
    			
   if(total==0 || total1==0)		
	{		
        this->positive_instance=no;		
        this->negative_instance=total-no;		
		 break_cond=1;		
		//    cout<<"the postive instances are"<<this->positive_instance<<endl;		
      //cout<<"the negitive instances are"<<this->negative_instance<<endl;		
    this->atribute_val=attr_list[random_val];		
		 return;		
	}		
				
	if(convert==0)		
	{		
	this->positive_instance=no;		
    this->negative_instance=total-no;		
   // cout<<"the postive instances are"<<this->positive_instance<<endl;		
    //cout<<"the negitive instances are"<<this->negative_instance<<endl;		
//	this->class_label=index;		
	this->atribute_val=attr_list[random_val];		
	for(i=0;i<attr_list.size();i++)		
	{		
		if(attr_list[random_val]==attr_list[i])		
		{		
			break;		
		}		
	}		
	  int store=attr_list[random_val];		
	attr_list.erase(attr_list.begin()+i);		
	que.push_back(store);		
  }		
  			
}		
//random attribute selection based tree		
void Node::random_attribute_selection(class Node* nt, vector<int> attr,vector<int> que, vector<int> path,int & index,int heigh)		
{		
	if(nt)		
	{		
int i,j;		
vector<int> attr_temp;		
vector<int> que_temp;		
vector<int> path_temp;		
//cout<<"attribute is   "<<endl;		
for(i=0;i<attr.size();i++)		
{		
	attr_temp.push_back(attr[i]);			
	//cout<<attr_temp[i]<<" ";			
}		
//cout<<endl;		
//cout<<"que temp is "<<endl;		
for(j=0;j<que.size();j++)		
{		
	que_temp.push_back(que[j]);		
//	cout<<que_temp[j]<<" ";		
}		
//cout<<endl;		
//cout<<"path is"; 		
for(j=0;j<path.size();j++)		
{			
	path_temp.push_back(path[j]);		
//	cout <<path_temp[j]<<" ";		
}		
//cout<<endl;		
//cout <<endl;		
	if(attr.size()==0)		
	{		
		int ma;		
		ma=nt->positive_instance;		
		//nt->predictive_class=1;		
		if(ma<nt->negative_instance)		
		 {		
	    	//nt->predictive_class=0;		
	    //	cout<<"exit 1"<<endl;		
		 } 		
		 return ;		
		  		
	}		
	if(nt->positive_instance==0)		
	{		
		//nt->predictive_class=0;		
		//cout<<"exit 2"<<endl;		
		return;		
				
	}		
	if(nt->negative_instance==0)		
	{		
	//	nt->predictive_class=1;		
	//cout<<"exit 3"<<endl;		
		return;		
				
	}		
			
    if(que.size()==cols-1)		
   {		
   	 	int ma;		
		ma=nt->positive_instance;		
		if(ma< nt->negative_instance)		
		 {		
	    	//nt->predictive_class=0;		
		 } 		
		//cout<<"exiting here4"<<endl;		
		 return;		
   }		
   else		
   {		
   	 class Node* tem1=new class Node;		
     class Node* tem2=new class Node;  		
     int convert=0;		
     int break_cond=0;		
        index++;		
     //	ent=nt->Entrophy;		
    // tem1->select_best_attribute(attr,que,path,index,convert,break_cond,ent);		
    		
      path.push_back(0);		
      int conver=0;		
    tem1->random_attr(attr, que, path, break_cond,conver);		
    heigh=heigh+1;		
     nt->lt_pntr=tem1;		
     tem1->class_label=index;		
     tem1->height=heigh;		
     tem1->parent=nt;		
     //display(tem1);		
        if(break_cond!=1)		
      random_attribute_selection(nt->lt_pntr,attr,que,path,index,heigh);		
      else		
      {		
      		tem1->positive_instance=(nt->positive_instance)-(nt->lt_pntr->positive_instance);		
         	tem1->negative_instance=(nt->negative_instance)-(nt->lt_pntr->negative_instance);		
	  }		
    path_temp.push_back(1);		
   	tem2->atribute_val=nt->lt_pntr->atribute_val;		
   		
 //cout<<"the attribute pushed is "<<tem2->atribute_val<<endl;		
	for(i=0;i<attr_temp.size();i++)		
	{		
		if(tem2->atribute_val==attr_temp[i])		
		{		
			break;		
		}		
	}		
	index++;		
	int bbc=attr_temp[i];		
	//cout<<"erased element is "<<bbc<<endl;			
	attr_temp.erase(attr_temp.begin()+i);		
	que_temp.push_back(bbc);		
	tem2->class_label=index;		
	tem2->height=heigh;		
	tem2->positive_instance=(nt->positive_instance)-(nt->lt_pntr->positive_instance);		
	tem2->negative_instance=(nt->negative_instance)-(nt->lt_pntr->negative_instance);		
    break_cond=0;		
    conver=1;		
    		
   tem2->random_attr(attr_temp,que_temp,path_temp, break_cond,conver);		
    //tem2->select_best_attribute(attr_temp,que_temp,path_temp,index,convert,break_cond,ent);		
    		
    nt->rt_pntr=tem2;		
    tem2->parent=nt;		
   if(break_cond!=1)		
      random_attribute_selection(nt->rt_pntr,attr_temp,que_temp,path_temp,index,heigh);		
      else		
      {		
      		tem2->positive_instance=(nt->positive_instance)-(nt->lt_pntr->positive_instance);		
         	tem2->negative_instance=(nt->negative_instance)-(nt->lt_pntr->negative_instance);		
	  }		
   }		
}		
			
}
//Tree-Pruning method
/*void Node::prune(class Node * s,int& pf,int depth)
{
    if(s)
    {
        if(pf==0)
            return ;
        else
        {
            if(s->lt_pntr==NULL && s->rt_pntr==NULL)
            {
                int a= (s->positive_instance+(s->negative_instance));
                if(a<0)
                    a=-a;
                if(a<=depth && pf>0)
                {
                    pf=pf-1;
                    class Node * tem=s->parent->lt_pntr;
                   // class Node * tem1=s->parent->rt_pntr;
                    if(tem!=NULL)
                    {
                        int b=(tem->positive_instance+tem->negative_instance);
                        if(b<0)
                            b=-b;
                        if(a==b)
                           s->parent->lt_pntr=NULL;
                        else
                            s->parent->rt_pntr=NULL;
                    }
                    else
                        s->parent->rt_pntr=NULL;
                }   
            }
            else
            {
                prune(s->lt_pntr,pf,depth);
                prune(s->rt_pntr,pf,depth);
            }
        }
    }
}
*/
//Implementation of ID3 Algorithm  begins here.

void Node::Id3_Algo(class Node* nt, vector<int> attributes,vector<int> cuee, vector<int> path,int & index,float ent,int heigh)
{
    if(nt)
    {
        int i=0,j=0;
        vector<int> atribute_temp;
        vector<int> cue_temp;
        vector<int> path_temp;
        while(i<attributes.size())
            {
			atribute_temp.push_back(attributes[i]);
			i++;
            }
         while(j<cuee.size())
		 {
		 	cue_temp.push_back(cuee[j]);
		 	j++;
			}   
        j=0;
           

        while(j<path.size())
        {   path_temp.push_back(path[j]);
            j++;
		}           
        
        if(attributes.size()==0)
        {
            int ma;
            ma=nt->positive_instance;
            return ;
        }
        if(nt->positive_instance==0)
          return; 
        
        if(nt->negative_instance==0)
         return; 
        
        if(cuee.size()==cols)
        {
            int ma;
            ma=nt->positive_instance;
            return;
        }
        else
        {
            class Node* tem1=new class Node;
            class Node* tem2=new class Node;
            int convrt=0;
            int break_cond=0;
            index++;
            ent=nt->entropy;
            tem1->find_best_attribute(attributes,cuee,index,path,convrt,break_cond,ent);
            path.push_back(0);
            nt->lt_pntr=tem1;
            heigh=heigh+1;		
            tem1->height=heigh;
            tem1->parent=nt;
            if(break_cond!=1)
                Id3_Algo(nt->lt_pntr,attributes,cuee,path,index,ent,heigh);
            else
            {
                tem1->positive_instance=(nt->positive_instance)-(nt->lt_pntr->positive_instance);
                tem1->negative_instance=(nt->negative_instance)-(nt->lt_pntr->negative_instance);
            }
            path_temp.push_back(1);
            convrt=1;
            tem2->atribute_val=nt->lt_pntr->atribute_val;
            i=0;
            while(i<atribute_temp.size())
            {
                if(tem2->atribute_val==atribute_temp[i])
                break; 
                i++;
            }
            index++;
            int bbc=atribute_temp[i];
            atribute_temp.erase(atribute_temp.begin()+i);
            cue_temp.push_back(bbc);
            tem2->class_label=index;
            tem2->height=heigh;
            tem2->entropy=nt->lt_pntr->entropy;
            ent=tem2->entropy;
            tem2->positive_instance=(nt->positive_instance)-(nt->lt_pntr->positive_instance);
            tem2->negative_instance=(nt->negative_instance)-(nt->lt_pntr->negative_instance);
            
            break_cond=0;
            tem2->find_best_attribute(atribute_temp,cue_temp,index,path_temp,convrt,break_cond,ent);
            
            nt->rt_pntr=tem2;
            tem2->parent=nt;
            
            if(break_cond!=1)
                Id3_Algo(nt->rt_pntr,atribute_temp,cue_temp,path_temp,index,ent,heigh);
            else
            {
                tem2->positive_instance=(nt->positive_instance)-(nt->lt_pntr->positive_instance);
                tem2->negative_instance=(nt->negative_instance)-(nt->lt_pntr->negative_instance);
            } 
        }
    }
}

void TrainingAccuracy(int row, int cols, Node * tree)
{
    cout<<"Number of training instances :  "<<row<<"\n";
    cout<<"Number of training attributes :  "<<cols-1<<"\n";
    cout <<"Total number of nodes in the tree :  "<<trainInternalNodes+trainLeafNodes-1<<"\n";
   // cout <<"Total number of leaf nodes in the tree :  "<<trainLeafNodes<<"\n";
    int i=0,countCorrectInst=0;
    while(i<row)
    {
        tree->evaluate_prediction(root,training_data[i],countCorrectInst);
        i++;
    }
    tem_var=countCorrectInst;
     cout<<"train correct insances  =  "<<tem_var<<endl;
    train_accuracy=(((tem_var)/row)*100);
    cout <<"Accuracy of the model on the training dataset : "<<train_accuracy<<"\n";
    cout <<"\n\n\n";
}

void ValidationTestAccuracy(int cols,char arg[],string type, Node * tree)
{
    string line;
    ifstream DataInFile1(arg);
    getline(DataInFile1,line,'\n');
   
    int test_cols=cols;
    int test_rows=0;
    int temp_arr[2000];
    int test_correct_instances=0,j;
    while(getline(DataInFile1,line,'\n'))
    {
        replace( line.begin(), line.end(), ',', ' ');
        stringstream str_strm1(line);
        int t1;
        j=0;
        //str_strm1>>t1;
        while(str_strm1>>t1 !='\0')
        {
            temp_arr[j]=t1;
            j++;
        }
        tree->evaluate_prediction(root,temp_arr,test_correct_instances);
        test_rows++;
    }
  //  cout <<"Number of "<<type<<" instances = "<<test_rows<<"\n";
   // cout <<"Number of "<<type<<" attributes = "<<cols-1<<"\n";
    tem_var=test_correct_instances;
    cout<<"correct instances = "<<test_correct_instances<<endl;
    float test_accuracy=((tem_var/test_rows)*100);
    cout <<"Accuracy of the model on the "<<type<<" dataset ="<<test_accuracy<<"\n"; 
    cout <<"\n\n\n";
}

int main(int argcount,char *CmdLineArgs[])
{
    string line,data;
    //Reading first file
    ifstream DataInFile(CmdLineArgs[1]);
    //Checking if file is readable
    if(!DataInFile)
    {
        cout<<"file"<<"\n";
        return 0;
    } 
    getline(DataInFile,line,'\n');
    //Replacing commas with space from CSV file.
    replace( line.begin(), line.end(), ',', ' ');
 
    stringstream str(line);
    int Col_counter=0;
    
    while (str>>data !='\0')
    {
        atrbNames[Col_counter]=data;
        Col_counter++;
    }
    
    no_of_attributes=Col_counter;
    cols=Col_counter;
    memset(training_data,-1,sizeof(training_data));
    
    int i=0,j;
    while(getline(DataInFile,line,'\n'))
    {  
        replace( line.begin(), line.end(), ',', ' ');
        stringstream str_strm1(line);
        int temp_var;
        j=0;
        while(str_strm1>>temp_var !='\0')
        {
            training_data[i][j]=temp_var;
            j++;
        }
        i++;
    }
    row=i;  
    vector<int> path;
    vector<int> cuee;
    class Node * tree = new class Node;
    tree->calculate_entropy_rootnode();
    root=tree;
    root->entropy=target_entropy;
    vector<int> atrs;
    for(i=0;i<cols-1;i++)
    {
        atrs.push_back(i);
    }
    int indic=0;
    //tree->Id3_Algo(root,atrs,cuee,path,indic,target_entropy);
    vector<char> ss;
     int opt=0;		
   cout <<endl;		
   cout<<"Enter the option:"<<endl;		
   cout<<"1. construct tree using ID3 algorithm"<<endl;		
   cout<<"2. construct tree using Random Attribute selection algorithm"<<endl;		
  // cin>>opt;		
   opt=1;
    int Accuracy;
    trainLeafNodes=0;
    trainInternalNodes=0;
    if(opt==2)
   {
   	 tree->random_attribute_selection(root,atrs,cuee,path,indic,0);
   }
  else if(opt==1)
  {
  	   tree->Id3_Algo(root,atrs,cuee,path,indic,target_entropy,0);
  }
  else
  {
  	cout<<" please select the option among the specified."<<endl;
  }
  //cout<<"iam failing here itself"<<endl;
  int t_heights=0;
    tree->preProcessing(root,trainLeafNodes,trainInternalNodes,t_heights);
    //cout<<"temp var is "<<t_heights<<endl;
    float tem_height=t_heights;
    float avg_height=tem_height/(trainLeafNodes);
    tree->print(root,ss,0);
     cout <<endl;
    cout <<endl;
    cout <<endl;
    cout <<"Avg Depth of tree = "<<avg_height<<endl;
  // 	cout <<"Total number of leaf nodes in the tree =  "<<training_leafnodes<<endl; 
   	cout <<endl;
    cout<<"Number of training attributes =  "<<cols-1<<endl; 
   cout <<"Total number of nodes in the tree =  "<<trainInternalNodes+trainLeafNodes-1<<endl;
   
    //tree->preProcessing(root,trainLeafNodes,trainInternalNodes);
   // cout<<"\n"<<"\n";
   // tree->print(root,ss,0);
  //  cout<<"\n";
    
    //cout<<"Pre-Pruned Accuracy" <<"\n";
  //  cout <<"-------------------------------------"<<"\n";
    TrainingAccuracy(row,cols,tree);
  
    ValidationTestAccuracy(cols,CmdLineArgs[2],"validation",tree);
    ValidationTestAccuracy(cols,CmdLineArgs[3],"test",tree);
    
   /* //Now, lets prune the tree based on d given pruning factor 0.2
    float prunning_factor=0;
    int totalNumOfNodes=(trainInternalNodes+trainLeafNodes-1);
    prunning_factor=atof(CmdLineArgs[3]);
    int prunning_nodes=(prunning_factor)*(totalNumOfNodes);
    int depth=1;
    trainInternalNodes=0;
    trainLeafNodes=0;
    tree->prune(root,prunning_nodes,depth);
    while(prunning_nodes!=0)
    {
        depth=depth+4; 
        tree->prune(root,prunning_nodes,depth); 
    }
    tree->preProcessing(root,trainLeafNodes,trainInternalNodes);
    tree->print(root,ss,0);
    
    
    cout<<"\n Post-Pruned Accuracy" <<"\n";
    cout <<"------------------------------------------"<<"\n";
    countCorrectInst=0;	
    TrainingAccuracy(row,cols,tree);
    ValidationTestAccuracy(cols,CmdLineArgs[2],"validation",tree);
    ValidationTestAccuracy(cols,CmdLineArgs[4],"test",tree); */
    return 0;
}


