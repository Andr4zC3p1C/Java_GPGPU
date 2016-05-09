import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLMem;

public class RandomWELL512 extends Program{

	private long[] state = new long[16];
	private int index = 0;
	
	private LongBuffer stateBuffer;
	private IntBuffer indexBuffer;
	
	private int numbers;
	
	@Override
	public void init() {
		clearConsole();
		System.out.println("This program calculates random numbers, but the numbers aren't limited");
		System.out.println("Please enter the amount of random numbers you wish to get: ");
		numbers = Integer.parseInt(Main.scan.nextLine());
		
		cl_program = Main.initCLProgram("res/CL_src/random_WELL512.cl");
		cl_kernel = Main.initClKernel(cl_program, "random_WELL512");
		
		memories = new CLMem[2];
		
		seed((int)System.nanoTime());
		
		// creating the buffer
		stateBuffer = BufferUtils.createLongBuffer(16);
		stateBuffer.put(state);
		stateBuffer.rewind();
		indexBuffer = BufferUtils.createIntBuffer(1);
		indexBuffer.put(0, index);
		indexBuffer.rewind();
		
		memories[0] = Main.initCLMemoryRW(16);
		memories[1] = Main.initCLMemoryRW(1);
		
		cl_kernel.setArg(0, memories[0]);
		cl_kernel.setArg(1, memories[1]);
		
		NUM_OF_ITEMS = 1;
	}

	@Override
	public void run() {
		final int dimensions = 1;
		PointerBuffer itemsBuffer = BufferUtils.createPointerBuffer(dimensions);
		itemsBuffer.put(0, NUM_OF_ITEMS);
		
		boolean running = true;
		int counter = 0;
		for(int i=0; running; i++){
			CL10.clEnqueueNDRangeKernel(Main.queue, cl_kernel, dimensions, null, itemsBuffer, null, null, null);
			CL10.clEnqueueReadBuffer(Main.queue, memories[0], CL10.CL_TRUE, 0, stateBuffer, null, null);
			CL10.clEnqueueReadBuffer(Main.queue, memories[1], CL10.CL_TRUE, 0, indexBuffer, null, null);
			
			// we want to skip the 16th random number to omit the 0
			if((i + 1) % 16 != 0){
				long randomNum = Math.abs(stateBuffer.get(indexBuffer.get(0)));
				System.out.println("The random number " + (counter+1) + ": " + randomNum);
				counter++;
			}
			
			if(counter == numbers){
				running = false;
			}
		}
	}

	private void seed(int seed){
		seed = Math.abs(seed);
		
		// the seed generation
		for(int i=0; i < 16; i++){
			state[i] = (seed + 1) * ((seed + i) << 2) * i;
		}
	}
	
}
