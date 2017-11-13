import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RSA {

	public static double pgcd(double p, double q)
	{
		double pgcd = 1;
		for(int i = 1; i <= p && i <= q; ++i)
        {
            if(p % i==0 && q % i==0)
                pgcd = i;
        }
		return pgcd;
	}
	
	public static int getE(double cardN)
	{
		ArrayList<Integer> possibleE = new ArrayList<Integer>();
		
		for(int i = 2; i < cardN; i++)
		{
			if(pgcd(i,cardN) == 1)
				possibleE.add((int) i);
		}
		
		Random rand = new Random();
		return possibleE.get(rand.nextInt(possibleE.size()));
	}
	
	public static int euclide(int a, int b) {
		// r = PGCD(a, b) et r = au + bv
		int r = a; int u = 1; int v = 0;
		int rp = b; int up = 0; int vp = 1;
		int q, rs, us, vs;
		while (rp != 0) {
			q = r / rp;
			rs = r; us = u; vs = v;
			r = rp; u = up; v = vp;
			rp = rs - q * rp; up = us - q * up; vp = vs - q * vp;
		}
		return u > 2 ? u : u+b;
	}
	
	public static int cryptLetter(char letter, int e, int n)
	{
		//System.out.println("test: "+ (int)letter + " " + e + " " +n);
		int res = 1;
		for (int i = 0; i < e; i++) {
			res = ((res * letter) % n);
		}
		return res;
	}
	
	public static char decryptLetter(int letter, int d, int n)
	{
		int res = 1;
		for (int i = 0; i < d; i++) {
			res = ((res * letter) % n);
		}
		return (char)res;
	}
	
	public static String cryptString(String str, int e, int n)
	{
		String res = new String();
		
		for(char c : str.toCharArray())
			res += cryptLetter(c, e, n)+" ";
		
		return res;
	}
	
	public static String decryptString(String str, int d, int n)
	{
		String res = new String();
		String numbers[] = str.split(" ");
		
		for(String c : numbers)
		{
			//System.out.print(Integer.parseInt(c)+" ");
			res += decryptLetter(Integer.parseInt(c), d, n);
		}
		
		return res;
	}
	
	public static String readFile(String filename) throws FileNotFoundException
	{
		return new Scanner(new File(filename)).useDelimiter("\\Z").next();
	}
	
	public static void writeFile(String filename, String str) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		writer.print(str);
		writer.close();
	}
	
	public static void debug()
	{
		int p = 11;
		int q = 17;
		int n = p*q;
		
		int cardinalN = (p - 1)*(q - 1);
		
		int e = getE(cardinalN);
		int d = euclide(e, cardinalN);
		
		System.out.println("p = "+p+"\ng = "+q);
		System.out.println("n = "+n+"\ncard(n) = "+cardinalN);
		System.out.println("e = "+e);
		System.out.println("d = "+d);
		
		int cryptedLetter = cryptLetter('c', e, n);
		char decryptedLetter = decryptLetter(cryptedLetter, d, n);
		
		System.out.println("c("+(int)'c'+") = "+cryptedLetter);
		System.out.println(cryptedLetter+" = "+decryptedLetter+"("+(int)decryptedLetter+")");
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		if(args.length != 6)
		{
			System.err.println("java RSA [n] [e] [d] [-c/-d] [clearTextPath] [cryptedTextPath]");
			System.exit(0);
		}
		
		debug();
		
		String sentence = new String();
		
		String cryptFile = new String(args[5]);
		String clearFile = new String(args[4]);
		
		int n = Integer.parseInt(args[0]);
		int d = Integer.parseInt(args[2]);
		int e = Integer.parseInt(args[1]);
		
		switch (args[3]) {
		case "-c":
			sentence = readFile(clearFile);
			String cryptedSentence = cryptString(sentence, e, n);
			writeFile(cryptFile, cryptedSentence);
			break;
		case "-d":
			sentence = readFile(cryptFile);
			String decryptedSentence = decryptString(sentence, d, n);
			writeFile(clearFile, decryptedSentence);
			break;
		default:
			System.err.println(args[3]+" invalide.");
			System.exit(0);
			break;
		}
	}
}
