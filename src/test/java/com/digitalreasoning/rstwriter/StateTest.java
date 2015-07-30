package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.BulletList;
import com.digitalreasoning.rstwriter.directive.Image;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by creynolds on 7/30/15.
 */
public class StateTest
{
	public static final String INDENT = "    ";
	@Test
	public void headingTest(){
		Heading.Builder builder = Heading.builder("Heading")
		                                 .addParagraph("Text Text Text")
		                                 .addDirective(new Image("img.jpeg"));
		Heading h = builder.build();
		builder.openSubHeading("SubHeading").addTransition().addLinkTarget("target").closeSubHeading();
		Heading h1 = builder.build();
		assertFalse("Headings are the same", h.write().equals(h1.write()));

		String result0 = "#######\nHeading\n#######\n\nText Text Text\n\n.. image:: img.jpeg\n\n";
		assertEquals("First heading write fail", result0, h.write());
		String result1 = result0 + ".. _target: \n\n**********\nSubHeading\n**********\n\n------\n\n";
		assertEquals("Second heading write fail", result1, h1.write());
	}

	@Test
	public void fileTest(){
		RstFile.Builder builder = RstFile.builder("file").addParagraph("text text text")
		                                 .addHeading(Heading.builder("heading").build())
				                         .addBodyElement(RstBodyElement.alphabeticList("item\nitem2\nitem3"));
		RstFile file0 = builder.build();
		builder.addHeading(Heading.builder("Second heading").build()).addParagraph("Another paragraph");
		RstFile file1 = builder.build();
		assertFalse("Files are the same", file0.write().equals(file1.write()));

		String result0 = "text text text\n\n#######\nheading\n#######\n\nA. item\n#. item2\n#. item3\n\n";
		assertEquals("First file fail", result0, file0.write());
		String result1 = result0 + "##############\nSecond heading\n##############\n\nAnother paragraph\n\n";
		assertEquals("Second file fail", result1, file1.write());
	}

	@Test
	public void containedListTest(){
		BulletList bullet = new BulletList("item\nitem2\nitem3");
		Heading.Builder heading = Heading.builder("heading");
		heading.addBodyElement(bullet);
		bullet.addItem("item4").addItem("item5");
		String result = "#######\nheading\n#######\n\n* item\n* item2\n* item3\n\n";
		assertEquals("Failed container test", result, heading.build().write());
	}
}
