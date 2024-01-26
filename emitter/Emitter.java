package emitter;
import java.io.*;
import ast.*;
import java.util.*;

/**
 * Class Emitter includes methods to print MIPS code to the output asm file. Methods include
 * emitting code, printing a newline, pushing to and popping from the stack, and getting the
 * next label number for conditionals.
 *
 * @author Juliana Li
 * @version December 2, 2023
 */
public class Emitter
{
	// instance variables
	private PrintWriter out;
	private int labelNum;
	private ProcedureDeclaration proc;
	private int excessStackHeight;

	/**
	 * This method creates an emitter for writing to a new file with given name.
	 *
	 * @param outputFileName the name of the
	 */
	public Emitter(String outputFileName)
	{
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		labelNum = 0;
		proc = null;
	}

	/**
	 * This method prints one line of code to file with non-labels indented.
	 *
	 * @param code the string representing the code to be emitted
	 */
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}

	/**
	 * This method emits the MIPS code to print a newline.
	 */
	public void printLine()
	{
		emit("la $a0 newLine\nli $v0 4\nsyscall");
	}

	/**
	 * This method pushes the value in the given register onto the stack.
	 *
	 * @param reg the register with the value to be pushd
	 */
	public void emitPush(String reg)
	{
		excessStackHeight += 4;
		emit("subu $sp $sp 4\t# push to stack\n" +
				"sw " + reg + " ($sp)");
	}

	/**
	 * This method pops the value at the top of the stack onto the given register.
	 *
	 * @param reg the register to pop onto
	 */
	public void emitPop(String reg)
	{
		excessStackHeight -= 4;
		emit("lw " + reg + " ($sp)\t# pop from stack\n" +
				"addu $sp $sp 4 ");
	}

	/**
	 * This method adds 1 to the current label count and returns the label ID.
	 *
	 * @return the number of the next label
	 */
	public int nextLabelID()
	{
		labelNum++;
		return labelNum;
	}

	/**
	 * This method remembers the given procedure as the current procedure context.
	 *
	 * @param proc procedure to set as procedure context
	 */
	public void setProcedureContext(ProcedureDeclaration proc)
	{
		this.proc = proc;
		excessStackHeight = 0;
	}

	/**
	 * This method clears the current procedure context by setting it to null.
	 */
	public void clearProcedureContext()
	{
		proc = null;
	}

	/**
	 * This method checks if a given variable name is the name of a local variable,
	 * meaning either the name of the current procedure, the name of a locally declared
	 * variable, or a parameter.
	 *
	 * @param varName the name of the variable to check
	 * @return true if the variable is local, false otherwise
	 */
	public boolean isLocalVariable(String varName)
	{
		if (proc != null)
		{
			if (varName.equals(proc.getId())) return true;
			List<String> localVars = proc.getLocalVars();
			if (localVars.contains(varName)) return true;
			List<String> params = proc.getParams();
			return params.contains(varName);
		}
		return false;
	}

	/**
	 * This method gets the offset (multiple of 4) for the stack pointer pointing
	 * to a given local variable. Top of stack is local vars, then params, then
	 * the return value.
	 *
	 * @param localVarName the local variable to find the stack offset of
	 * @precondition localVarName is the name of a local variable for the procedure
	 * currently being compiled
	 * @return the offset of the stack pointer for a given variable
	 */
	public int getOffset(String localVarName)
	{
		int offset = excessStackHeight;
		List<String> localVars = proc.getLocalVars();
		for (int i=localVars.size()-1; i>=0; i--)
		{
			if (localVars.get(i).equals(localVarName))
				return offset + (localVars.size() - i - 1) * 4;
		}
		offset += 4 * localVars.size();
		List<String> params = proc.getParams();
		for (int i=params.size()-1; i>=0; i--)
		{
			if (params.get(i).equals(localVarName))
				return offset + (params.size() - i - 1) * 4;
		}
		offset += 4 * params.size();
		if (localVarName.equals(proc.getId()))
			return offset;
		return 0;
	}

	/**
	 * This method closes the file and should be called after all calls to emit.
	 */
	public void close()
	{
		out.close();
	}
}