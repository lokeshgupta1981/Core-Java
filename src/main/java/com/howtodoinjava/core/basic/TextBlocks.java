package com.howtodoinjava.core.basic;

public class TextBlocks 
{
	public static void main(String[] args) 
	{
		String dbSchema = 	"""
					CREATE TABLE 'TEST'.'EMPLOYEE'          \s
					(                                       \s
					  'ID' INT NOT NULL DEFAULT 0 ,         \s
					   'FIRST_NAME' VARCHAR(100) NOT NULL , \s
					  'LAST_NAME' VARCHAR(100) NULL ,       \s
					  'STAT_CD' TINYINT NOT NULL DEFAULT 0  \s
					);                                      \s
					""";
		
		System.out.println(dbSchema.replace("\s", "."));
		
		String string = "Hello";
		String textBlock = """
							World
							 !! """;
		
		String joinedString =  string + textBlock;
		
		joinedString = joinedString.replace("\n", System.lineSeparator());
		
		System.out.println(joinedString);
	}
}
