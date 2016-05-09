import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLMem;

public class Test extends Program{
	private static final int testSize = 2000;
	
	@Override
	public void init(){
		clearConsole();
		System.out.println("This is a little test program for OpenCL which calculates something... " + testSize + " * 2^576 times...");
		
		// program initialization
		cl_program = Main.initCLProgram("res/CL_src/test.cl");
		cl_kernel = Main.initClKernel(cl_program, "test");
		
		// the data
		memories = new CLMem[3];
		
		int[] testArray = new int[testSize];
		for(int i=0; i < testSize; i++){
			testArray[i] = i;
		}
		//System.out.println(Arrays.toString(testArray));
		
		IntBuffer bufferA = BufferUtils.createIntBuffer(testSize);
		IntBuffer bufferB = BufferUtils.createIntBuffer(testSize);
		bufferA.put(testArray);
		bufferB.put(testArray);
		bufferA.rewind();
		bufferB.rewind();
		
		memories[0] = Main.initCLMemoryWOM(bufferA);
		memories[1] = Main.initCLMemoryWOM(bufferB);
		memories[2] = Main.initCLMemoryROM(testSize);
		
		// setting the parameters
		cl_kernel.setArg(0, memories[0]);
		cl_kernel.setArg(1, memories[1]);
		cl_kernel.setArg(2, memories[2]);
		cl_kernel.setArg(3, testSize);
		
		NUM_OF_ITEMS = testSize;
	}
	
	@Override
	public void run(){
		final int dimensions = 1;
		PointerBuffer itemsBuffer = BufferUtils.createPointerBuffer(dimensions);
		itemsBuffer.put(0, NUM_OF_ITEMS);
		
		IntBuffer resultData = BufferUtils.createIntBuffer(testSize);
		startTimer();
		CL10.clEnqueueNDRangeKernel(Main.queue, cl_kernel, dimensions, null, itemsBuffer, null, null, null);
		CL10.clEnqueueReadBuffer(Main.queue, memories[2], CL10.CL_TRUE, 0, resultData, null, null);
		double time = stopTimer();

		//printBuffer(resultData, ", ");
		
		logTime("res/logs/test.log", 27367.0);
	}
}
