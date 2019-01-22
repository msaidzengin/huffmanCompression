import java.util.*;
import java.io.*;

public class HuffCode {
	public static void main(String[] args) {
		if(args.length == 2 && args[0].equals("encode"))
		encode(args[1]);
		if(args.length == 2 && args[0].equals("decode"))
		decode(args[1]);
	}
	public static void encode(String file) {
		String text = textOku(file);
		int[][] chars = findFrequency(text);
		String[][] huffCode = makeHuff(chars);
		String[] hash = hash(huffCode);
		String binaryS = makeString(text, hash);
		binaryYaz(file,chars,binaryS);
		deleteFile(file);
	}
	public static String textOku(String fileName) {
		Scanner input = null;
		String s = "";
		try{
			input = new Scanner(new File(fileName));
			boolean first = true;
			while(input.hasNextLine()){
				if(!first)
				s += "\n";
				s += input.nextLine();
				first = false;
			}
		}catch(Exception e){
			System.out.println("Dosya okunurken hata.");
		}
		input.close();
		return s;
	}
	public static int[][] findFrequency(String s) {
		ArrayList<Character> chars = new ArrayList<>();
		ArrayList<Integer> freq = new ArrayList<>();
		while(s.length() != 0) {
			char c = s.charAt(0);
			if(chars.contains(c)) {
				int konum = 0;
				for(int i=0; i<chars.size(); i++)
				if(chars.get(i) == c)
				konum = i;
				freq.set(konum, freq.get(konum)+1);
			}
			else {
				chars.add(c);
				freq.add(1);
			}
			s = s.substring(1);
		}
		int[][] arr = new int[chars.size()][2];
		for(int i=0; i<chars.size(); i++) {
			arr[i][0] = chars.get(i);
			arr[i][1] = freq.get(i);
		}
		return arr;
	}
	public static String[][] makeHuff(int[][] arr) {
		TreeNode[] nodes= new TreeNode[arr.length];
		for(int i=0; i<arr.length; i++)
		nodes[i] = new TreeNode((char)arr[i][0], arr[i][1]);
		HeapPQ heap = new HeapPQ(nodes);
		TreeNode root = null;
		while (heap.size > 1) {
			TreeNode x = heap.removeMin();
			TreeNode y = heap.removeMin();
			TreeNode f = new TreeNode('-', x.frekans + y.frekans);
			f.left = x;
			f.right = y;
			root = f;
			heap.insert(f);
		}
		ArrayList<Character> ch = new ArrayList<>();
		ArrayList<String> st = new ArrayList<>();

		makeCode(root, "", ch, st);
		String[][] codes = new String[ch.size()][2];
		for(int i=0; i<ch.size(); i++) {
			codes[i][0] = ""+ch.get(i);
			codes[i][1] = st.get(i);
		}
		return codes;
	}
	public static void makeCode(TreeNode root, String s, ArrayList<Character> ch, ArrayList<String> st) {
		if (root.left	== null	&& root.right == null) {
			ch.add(root.c);
			st.add(s);
			return;
		}
		makeCode(root.left, s + "0", ch, st);
		makeCode(root.right, s + "1", ch, st);
	}
	public static String[] hash(String[][] arr) {
		String[] hash = new String[4096];
		for(int i=0; i<arr.length; i++) {
			int key = arr[i][0].charAt(0);
			hash[key%hash.length] = arr[i][1];
		}
		return hash;
	}
	public static String makeString(String s, String[] hash) {
		StringBuilder st = new StringBuilder();
		for(int i=0; i<s.length(); i++) {
			int key = s.charAt(i);
			st.append(hash[key%hash.length]);
		}
		String finalBinary = st.toString();
		return finalBinary;
	}
	public static void binaryYaz(String file, int[][] arr, String binary) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file + ".huff"));
			byte numOfChar = (byte)arr.length;
			out.writeByte(numOfChar);
			for(int i=0; i<numOfChar; i++){
			out.writeChar((char)arr[i][0]);
			out.writeInt(arr[i][1]);
			}
			int binaryLength = binary.length();
			int ekle = 8 - (binary.length() % 8);
			for(int i=0; i<ekle; i++)
			binary += "0";
			binaryLength = binary.length();
			out.writeInt(binaryLength/8);
			out.writeByte((byte)ekle);
			String[] yazilacakByte = new String[binary.length()/8];
			for(int i=0; i<yazilacakByte.length; i++) {
				yazilacakByte[i] = binary.substring(0,8);
				binary = binary.substring(8);
			}
			int[] cevir = new int[yazilacakByte.length];
			for(int i=0; i<cevir.length; i++)
			cevir[i] = Integer.parseInt(yazilacakByte[i],2);
			byte[] son = new byte[cevir.length];

			for(int i=0; i<son.length;i++)
			son[i] = (byte)cevir[i];

			for(int i=0; i<son.length; i++)
			out.writeByte(son[i]);
			out.close();
			System.out.println("("+file + ".huff) dosyasi olusturuldu.");
		} catch(Exception e) {
			System.out.println("Dosya yazarken hata.");
		}
	}
	public static void decode(String file) {
		ArrayList<Character> ch = new ArrayList<>();
		ArrayList<Integer> in = new ArrayList<>();
		String s = binaryOku(file,ch,in);
		int[][] chars = new int[ch.size()][2];
		for(int i=0; i<ch.size(); i++) {
			chars[i][0] = ch.get(i);
			chars[i][1] = in.get(i);
		}
		String[][] huffCode = makeHuff(chars);
		String metin = convert(s,huffCode);
		metinYaz(metin,file);
		deleteFile(file);
	}
	public static String binaryOku(String file, ArrayList<Character> ch, ArrayList<Integer> in) {
		StringBuilder s = new StringBuilder();
		int fazlalik = 0;
		ObjectInputStream inpStream = null;
		try{
			inpStream = new ObjectInputStream(new FileInputStream(file));
			byte karakterSayisi;
			karakterSayisi = inpStream.readByte();
			for(int i=0; i<karakterSayisi; i++) {
				char c = inpStream.readChar();
				int freq = inpStream.readInt();
				ch.add(c);
				in.add(freq);
			}
			int toplamOkunacakBit;
			toplamOkunacakBit = inpStream.readInt();
			fazlalik = inpStream.readByte();
			byte b = 0;
			for(int i=0; i<toplamOkunacakBit; i++) {
				b = inpStream.readByte();
				s.append( String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
			}
			inpStream.close();
		}catch(Exception e){
			System.out.println("Binary dosya okunurken hata");
		}
		String ss = s.toString();
		ss = ss.substring(0,ss.length()-fazlalik);
		return ss;
	}
	public static String convert(String s, String[][] huff) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(int i=1; i<s.length()+1; i++) {
			String yeni = s.substring(index,i);
			for(int j=0; j<huff.length; j++)
			if(huff[j][1].equals(yeni)){
			sb.append(huff[j][0]);
			index = i;
			}
		}
		String son = sb.toString();
		return son;
	}
	public static void metinYaz(String s, String file) {
		String fileName = file.substring(0,file.length()-5);
		PrintWriter yazici = null;
		try {
			yazici = new PrintWriter(fileName);
			yazici.println(s);
			System.out.println("("+fileName + ") dosyasi olusturuldu.");
		}
		catch(Exception e) {
			System.out.println("Metin dosyasi olusturulurken hata.");
		}
		yazici.close();
	}
	public static void deleteFile(String s) {
		try{
    		File file = new File(s);
        file.delete();
    	}catch(Exception e){
	  		System.out.println("Dosya silinirken hata.");
    	}
	}
}
