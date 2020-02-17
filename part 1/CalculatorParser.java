package compilers1;

import java.io.InputStream;
import java.io.IOException;

public class CalculatorParser {

	private int lookaheadToken;

	private InputStream in;
	
	
	public CalculatorParser(InputStream in) throws IOException {
			this.in = in;
			lookaheadToken = in.read();
	}
	
	private void consume(int symbol) throws IOException, ParseError {
		if (lookaheadToken != symbol)
		    throw new ParseError();
		lookaheadToken = in.read();
	}
	
	public Result exp() throws IOException, ParseError  {
		Result res, tail; //local variable for the result of each NT
		if( (lookaheadToken < '0' || lookaheadToken > '9') && lookaheadToken != '(') { 
			res = new Result(0, false, (char)lookaheadToken);
			throw new ParseError();	
		}
		res = term();
		
		if(res.getToken() == '+' || res.getToken() == '-') {
			res = new Result(res.getVal(), true, res.getToken());
		}
		tail = exp2(res);
		return res = new Result(tail.getVal(), true, (char)lookaheadToken); 
		
	}
	
	
	public Result exp2(Result prev) throws IOException, ParseError  {
		Result res, tail;
		char token;
		if(lookaheadToken == '+' || lookaheadToken == '-') {
			token = (char)lookaheadToken;
			consume(lookaheadToken);
			res = term();
			if(token == '+') { //do a + with the rest of the factors in tail (left-associative)
				res = new Result(prev.getVal() + res.getVal(), true, token);
			}
			else { // do a -
				res = new Result(prev.getVal() - res.getVal(), true, token);
			}
			tail = exp2(res);
			return res = new Result(tail.getVal(), true, (char)lookaheadToken);
		}
		else { //return previous sum if there is nothing more to be done
			return res = new Result(prev.getVal(), true, (char)lookaheadToken); 
		}
	}

	
	public Result term() throws IOException, ParseError  {
		Result res, tail;
		if((lookaheadToken < '0' || lookaheadToken > '9') && lookaheadToken != '(') {
			res = new Result(0, false, (char)lookaheadToken);
			throw new ParseError();	
		}
		res = factor();
		char token = res.getToken();
		
		if(res.getToken() == '*' || res.getToken() == '/') { 
			//do a * or / with the rest of the factors in tail (left-associative)
			res = new Result(res.getVal(), true, token);
		}
		tail=term2(res);
		return res = new Result(tail.getVal(), true, (char)lookaheadToken);
	}
	
	
	
		public Result term2(Result prev) throws IOException, ParseError  {
			Result res, tail;
			char token;
			if(lookaheadToken == '*' || lookaheadToken == '/') {
				token = (char)lookaheadToken;
				consume(lookaheadToken);
				res = factor();
				if(token == '*') { //do a * with the rest of the factors in tail (left-associative)
					res = new Result(prev.getVal() * res.getVal(), true, token);
				}
				else { // do a /
					res = new Result(prev.getVal() / res.getVal(), true, token);
				}
				tail = term2(res);
				return res = new Result(tail.getVal(), true, (char)lookaheadToken);
			}
			else { //return previous product if there is nothing to be done
				return res = new Result(prev.getVal(), true, (char)lookaheadToken); 
			}
		}

	
	public Result factor() throws IOException, ParseError  {
		Result res;
		if((lookaheadToken < '0' || lookaheadToken > '9') && lookaheadToken != '(') {
			res = new Result(0, false, (char)lookaheadToken);
			throw new ParseError();
		}	
		if(lookaheadToken == '(') {
			consume('(');
			res = exp();
			consume(')');
			return res = new Result(res.getVal(), true, (char)lookaheadToken);
		}
		else { //the factor is a number
			//get the number from the parsed character and then cast it to double
			res = new Result((double)Character.getNumericValue((char)lookaheadToken), true, (char)lookaheadToken);
			consume(lookaheadToken);
			res.setToken((char) lookaheadToken);
			return res;
		}
	}
	
	
    public void parse() throws IOException, ParseError {
    	Result res;
    	res = exp();
    	consume(lookaheadToken);
    	if (lookaheadToken != '\n' && lookaheadToken != -1)
    		throw new ParseError();
    	System.out.printf("%f\n", res.getVal());
    }
	
	public static void main(String[] args) {
		try {
		    CalculatorParser parser = new CalculatorParser(System.in);
		    parser.parse();
		}
		catch (IOException e) {
		    System.err.println(e.getMessage());
		}
		catch(ParseError err){
		    System.err.println(err.getMessage());
		}
	}
}
