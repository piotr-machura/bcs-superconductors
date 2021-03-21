package staticFunctions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

public class FileFunctions 
{
	public static String [] getWordsFromTextFile (String pathDirectory) throws IOException
	{
		try 
		{
			InputStreamReader isr = new InputStreamReader(new FileInputStream(pathDirectory), Charset.forName("UTF-8").newDecoder() );
			int data = isr.read();
			String originalTekst = new String();
			while(data != -1) 
			{
			    char theChar = (char) data;
			    originalTekst = originalTekst +theChar;
			    data = isr.read();
			}
			isr.close();
			StringTokenizer tokenizer = new StringTokenizer(originalTekst);
			String [] arrayOfWords = new String [tokenizer.countTokens()];
			for (int i=0 ; i<arrayOfWords.length ;i++)
			{
				arrayOfWords[i] = tokenizer.nextToken();
			}
			return (arrayOfWords);
		} 
		catch (FileNotFoundException e1) 
		{
			System.out.println("FILE NOT FOUND!!!");
			return (null);
		}
		catch (IOException e1) 
		{
			System.out.println("ENOCING ERROR, UTF-8 REQUIRED!!!");
			return (null);
		}
	}
	
	
	public static void saveArrayOfIntsToFile (int arrayOfInts[], String pathDirectory)
	{
		try 
		{
			OutputStreamWriter osw = new OutputStreamWriter( new FileOutputStream(pathDirectory),  Charset.forName("UTF-8").newEncoder() );
			for (int j=0; j<arrayOfInts.length; j++)
			{
				osw.write(  new String(arrayOfInts[j] +" ") );
			}
			osw.close();
		} 
		catch (FileNotFoundException e1) 
		{
			System.out.println("FILE CANNOT BE CREATED!!!");
		}
		catch (IOException e1) 
		{
			System.out.println("ENOCING ERROR, UTF-8 REQUIRED");
		}
	}
}
