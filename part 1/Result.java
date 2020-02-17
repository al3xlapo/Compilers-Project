package compilers1;

public class Result{

    private double val; //value of the result of input until there
    private boolean flag; //flag is 1 if the input is ok up till there, or 0 if it's not
    private char token; //the token that the NT will give to the function it will call so that it knows what to do
    
    //constructor
    public Result(double val, boolean flag, char token) {
    	this.val = val;
    	this.flag = flag;
    	this.token = token;
    }
    
    public double getVal(){
		return val;
	}
    
    public void setVal(double val){
		this.val = val;
	}

	public boolean getFlag(){
		return flag;
	}
	
	public void setFlag(boolean flag){
		this.flag = flag;
	}
	
	public char getToken(){
		return token;
	}
	
	public void setToken(char token){
		this.token = token;
	}
} 