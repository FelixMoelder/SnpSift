package org.snpsift.testCases;

import java.io.File;

import org.snpeff.util.Gpr;
import org.snpsift.SnpSift;
import org.snpsift.SnpSiftCmdSplit;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * SnpSift 'split' test cases
 * 
 * @author pcingola
 */
public class TestCasesSplit extends TestCase {

	public static boolean debug = false;
	public static boolean verbose = false;

	/**
	 * Split by chromosome
	 */
	public void test_01() {
		Gpr.debug("Test");
		String file = "test/test_split_01.vcf";

		// Delete old files
		(new File("test/test_split_01.Y.vcf")).delete();
		(new File("test/test_split_01.19.vcf")).delete();

		// Run command
		String args[] = { "split", file };
		SnpSift snpSift = new SnpSift(args);
		SnpSiftCmdSplit cmd = (SnpSiftCmdSplit) snpSift.cmd();
		cmd.setVerbose(verbose);
		cmd.setDebug(debug);
		cmd.run();

		// Check output
		String expected = Gpr.readFile("test/test_split_01.Y.expected.vcf");
		String actual = Gpr.readFile("test/test_split_01.Y.vcf");
		Assert.assertEquals(expected, actual);

		expected = Gpr.readFile("test/test_split_01.19.expected.vcf");
		actual = Gpr.readFile("test/test_split_01.19.vcf");
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Split by number
	 */
	public void test_02() {
		Gpr.debug("Test");

		int numLines = 2;
		String file = "test/test_split_01.vcf";

		// Run command
		String args[] = { "split", "-l", numLines + "", file };
		SnpSift snpSift = new SnpSift(args);
		SnpSiftCmdSplit cmd = (SnpSiftCmdSplit) snpSift.cmd();
		cmd.setVerbose(verbose);
		cmd.setDebug(debug);
		cmd.run();

		// Check output
		for (String splitFile : cmd.getFileNames()) {
			int n = Gpr.countLines(splitFile);
			if (debug) Gpr.debug("Checking file '" + splitFile + "'\tnumber of lines: " + n);
			Assert.assertEquals(numLines + 1, n); // We expect 'numLines' + 1 header line
		}
	}

	/**
	 * Split and join
	 */
	public void test_03() {
		Gpr.debug("Test");

		//---
		// Split
		//---
		String file = "test/test_split_01.vcf";
		String originalFile = Gpr.readFile(file);

		// Delete old files
		(new File("test/test_split_01.Y.vcf")).delete();
		(new File("test/test_split_01.19.vcf")).delete();

		// Run command
		String args[] = { "split", file };
		SnpSift snpSift = new SnpSift(args);
		SnpSiftCmdSplit cmd = (SnpSiftCmdSplit) snpSift.cmd();
		cmd.setVerbose(verbose);
		cmd.setDebug(debug);
		cmd.run();

		//---
		// Join
		//---
		String argsJoin[] = { "split", "-j", "test/test_split_01.Y.vcf", "test/test_split_01.19.vcf" };
		snpSift = new SnpSift(argsJoin);
		cmd = (SnpSiftCmdSplit) snpSift.cmd();
		cmd.setVerbose(true);
		cmd.setDebug(debug);
		String joinedFile = cmd.join(true);

		Assert.assertEquals(originalFile, joinedFile);

	}

}
