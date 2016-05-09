import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLKernel;
import org.lwjgl.opencl.CLMem;
import org.lwjgl.opencl.CLProgram;

public abstract class Program {
	protected CLProgram cl_program;
	protected CLKernel cl_kernel;
	protected CLMem[] memories;
	
	protected int NUM_OF_ITEMS;
	
	public abstract void init();
	public abstract void run();
	
	// a timer for counting
	private long timer;
	
	protected void startTimer(){
		timer = System.nanoTime();
	}
	
	protected double stopTimer(){
		double deltaTime = (double)(System.nanoTime() - timer) / 1000000.0; // in milliseconds
		System.out.println("The time it took to compute the algorithm: " + deltaTime + " ms");
		return deltaTime;
	}
	
	protected void printBuffer(IntBuffer buffer, String append){
		System.out.print("[ ");
		for(int i=0; i < buffer.capacity()-1; i++){
			System.out.print(buffer.get(i) + append);
		}
		
		System.out.println(buffer.get(buffer.capacity() - 1) + " ]");
	}
	
	protected void printBuffer(FloatBuffer buffer, String append){
		System.out.print("[ ");
		for(int i=0; i < buffer.capacity()-1; i++){
			System.out.print(buffer.get(i) + append);
			if(i % 20 == 0) System.out.print("\n");
		}
		
		System.out.println(buffer.get(buffer.capacity() - 1) + " ]");
	}
	
	protected void clearConsole(){
		for(int i=0; i < 100; i++){
			System.out.print("\n");
		}
	}
	
	protected void logTime(String path, double deltaTime){
		try {	
			String file_content = Main.loadSource(path);
			PrintWriter pw = new PrintWriter(path);
			StringBuilder bl = new StringBuilder();
			double a=0;
			boolean isUseless = false;
			
			if(file_content != null){
				file_content.trim();
				String[] data = file_content.split(" ms\n");
				
				int counter = 0;
				for(int i=0; i < data.length-1; i++){
					data[i].trim();
					if(!(data[i].isEmpty() || data[i].startsWith("-"))){
						a += Double.parseDouble(data[i]);
						bl.append(data[i] + " ms\n");
						counter++;
					}
				}
				
				if(deltaTime > a / (data.length-1) + 2 ||  deltaTime < a / (data.length-1) - 2){
					isUseless = true;
					a /= counter;
				}else{
					a += deltaTime;
					a /= counter+1;
				}
			}else{
				a += deltaTime;
			}
			
			String average = "------------------------------------\n" + "Average time: " + a + " ms\n";
			
			if(isUseless)
				bl.append("-" + String.valueOf(deltaTime) + " ms\n");
			else
				bl.append(String.valueOf(deltaTime) + " ms\n");
			
			bl.append(average);
			pw.write(bl.toString());
			
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void dispose(){
		CL10.clReleaseKernel(cl_kernel);
		CL10.clReleaseProgram(cl_program);
		
		// releasing the memory
		for(int i=0; i < memories.length; i++){
			CL10.clReleaseMemObject(memories[i]);
		}
		
		CL10.clReleaseCommandQueue(Main.queue);
		CL10.clReleaseContext(Main.context);
		CL.destroy();
	}
}
