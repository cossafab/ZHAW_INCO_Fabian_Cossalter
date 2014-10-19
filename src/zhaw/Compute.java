package zhaw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Formatter;
import java.util.HashMap;




public class Compute {
	public static final boolean LDEBUG = true;
	
	public class CharProp {
		public int occurence = 0;
		public BigDecimal probability = new BigDecimal(0);
		public double information = 0;
		@Override
		public String toString() {
			return new Formatter().format("o=%1$7s p=%2$10s i=%3$10s", occurence, probability, Double.toString(information)).toString();
		}
	}
	
	private HashMap< Integer /*character*/, CharProp> chars = null;
	private double fileCharactersCount = 0;
	
	// log2:  Logarithm base 2
    public static double log2(double d) {
    	return Math.log(d)/Math.log(2.0);
    }
	
    
	public void ReadInputTextFileCharacters( String relativeFilePath) throws UserErrorException 
	{
	    if ( LDEBUG ) {
	    	System.out.println("Current directory is: " + System.getProperty("user.dir"));
		}

		chars = new HashMap<>();
		fileCharactersCount = 0;
		try ( BufferedReader in = new BufferedReader( new FileReader( relativeFilePath)))
		{
			System.out.println( "Reading the input text file...");
			int c;
			while ((c = in.read()) != -1) {
				/* 
				 * [1.1] computing of the relative frequency of the current character. 
				 * */
				CharProp thisone;
				if(chars.containsKey(c)){
					thisone = chars.get(c);
					thisone.occurence++;
				}else{
					thisone = new CharProp();
					thisone.occurence=1;
					chars.put(c, thisone);
				}
				/* 
				 * [1.2] count the characters in the file. 
				 * */
				fileCharactersCount++;

			}
		} 
		catch (FileNotFoundException ex)
		{
			throw new UserErrorException( "input file " + relativeFilePath + " does not exists.");
		} catch (IOException e) {
			throw new UserErrorException( "input file " + relativeFilePath + " reading failed.");
		}

	}
	
	public void ComputeProbabilities( String relativeFilePath) throws UserErrorException 
	{
		// you have to read the file before computing the probabilities
		if ( chars == null )
			ReadInputTextFileCharacters( relativeFilePath);
		System.out.println( "Computing probabilities...");
		/* 
		 * [2] implement computing of the probabilities of the existing characters. 
		 * 		Use the precision 10 after the comma and the constant RoundingMode.HALF_UP
		 * */
		for(CharProp propability: chars.values()){
			propability.probability = new BigDecimal(propability.occurence / fileCharactersCount).setScale(10, RoundingMode.HALF_UP);
		}
	}

	public void ComputeInformation( String relativeFilePath) throws UserErrorException 
	{
		// you have to read the file before computing the information
		if ( chars == null )
			ComputeProbabilities( relativeFilePath);
		System.out.println( "Computing information...");
		/* 
		 * [3] implement computing of the information of the existing characters. 
		 * 			 Use the precision 10 after the comma and the constant RoundingMode.HALF_UP
		 * */
		double information = 0.0d;
			for(CharProp propability: chars.values()){
				information = 1 / propability.probability.doubleValue();
				if(information != 0){
					information = log2(information);
					propability.information = new BigDecimal(information).setScale(10, RoundingMode.HALF_UP).doubleValue();
				}
			}
	}

	
	public BigDecimal ComputeEntropy( String relativeFilePath) throws UserErrorException 
	{
		// you have to read the file before computing the entropy
		if ( chars == null )
			ComputeInformation( relativeFilePath);
		System.out.println( "Computing entropy...");
		BigDecimal sum = new BigDecimal( "0.0000000");
		/* 
		 * [5] implement computing of the entropy of the existing characters. 
		 * 			 Send the entropy value back as a result.
		 * */
		for(CharProp charProp : chars.values()){
			sum = sum.add(charProp.probability.multiply(new BigDecimal(charProp.information)));
		}
		
		return sum;
	}
	
	public void PrintOutCharProps() 
	{
		System.out.println("Character types in file: " + chars.size());
		System.out.println("Number of character in file: " + fileCharactersCount);
		for ( int c : chars.keySet() ) {
			String chr = "" + (char) c;
			if ( Character.isWhitespace(c) )
				chr = "(" + c + ")";
			System.out.println( new Formatter().format("%1$5s : %2$s", chr,chars.get( c) ).toString());
		}
	}
}
