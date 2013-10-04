package net.mindview.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import javax.management.RuntimeErrorException;

public class TextFile extends ArrayList<String> {
	// Read a file as a single string
	public static String read(String fileName) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(fileName).getAbsolutePath()));
			try {
				String s;
				while ((s = in.readLine()) != null) {
					sb.append(s);
					sb.append("\n");
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();

	}
//write a single file in one method call	
	public static void write(String filename,String text){
		try{
			PrintWriter out=new PrintWriter(new File(filename).getAbsoluteFile());
			try{
				out.print(text);	
			}finally{
				out.close();
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	public TextFile(String fileName,String splitter){
		super(Arrays.asList(read(fileName).split(splitter)));
		//Regular expression split() often leaves an empty
		//String at the first position
		if(get(0).equals("")) remove(0);
		
	}
	public TextFile(String filename){
		this(filename,"\n");
	}
	public void write(String fileName){
		try{
			PrintWriter out=new PrintWriter(new File(fileName).getAbsoluteFile());
			try{
				for(String item:this)
					out.println(item);
			}finally{
				out.close();
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args){
		String file=read("src/net/mindview/util/TextFile.java");
		write("test.txt",file);
		TextFile text=new TextFile("test.txt");
		text.write("text2.txt");
		TreeSet<String> words=new TreeSet<String>(
			new TextFile("src/net/mindview/util/TextFile.java","\\W+"));
		System.out.println(words.headSet("a"));
	}
}