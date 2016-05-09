

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLCommandQueue;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLKernel;
import org.lwjgl.opencl.CLMem;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.CLProgram;
import org.lwjgl.opencl.Util;

public class Main {

	public static final String VERSION = "1.0";
	
	// OpenCL Objects
	public static CLPlatform cl_platform;
	public static List<CLDevice> devices;
	public static CLContext context;
	public static CLCommandQueue queue;
	
	// the error buffer
	private static IntBuffer errBuffer = BufferUtils.createIntBuffer(1); 
	
	public static Scanner scan;
	private Program program;
	
	public Main(){	
		 initOpenCL();

		 scan = new Scanner(System.in);
		 System.out.println("Version " + VERSION);
		 System.out.println("Choose the program to run(1 - test program | 2 - PI calculation | 3 - sine | 4 - cosine | 5 - WELL512 | 0 - EXIT):");
		 do{
			 byte command = -1;
			 try{
				 command = Byte.parseByte(scan.nextLine());
			 }catch(NumberFormatException e){}
			 
			 switch(command){
			 case 0:
				 return;
			 case 1:
				 program = new Test();
				 break;
			 case 2:
				 program = new PICalc();
				 break;
			 case 3:
				 program = new Sine();
				 break;
			 case 4:
				 program = new Cosine();
				 break;
				 
			 case 5:
				 program = new RandomWELL512();
				 break;
			 default:
				 System.out.println("Please enter a valid command and retry");
				 continue;
			 }
			 
			 break;
		 }while(true);
		 
		 program.init();
		 program.run();
		 program.dispose();	 
	}
	
	private void initOpenCL(){
		try {
			// initializing OpenCL and creating the required objects 
			CL.create();
			cl_platform = CLPlatform.getPlatforms().get(0);
			devices = cl_platform.getDevices(CL10.CL_DEVICE_TYPE_GPU);
			context = CLContext.create(cl_platform, devices, null);
			queue = CL10.clCreateCommandQueue(context, devices.get(0), CL10.CL_QUEUE_PROFILING_ENABLE, errBuffer);
			Util.checkCLError(errBuffer.get(0));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static CLMem initCLMemoryROM(int length){
		CLMem m = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_ONLY, length * 4, errBuffer);
		Util.checkCLError(errBuffer.get(0));
		return m;
	}
	
	public static CLMem initCLMemoryRW(int length){
		CLMem m = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_WRITE, length * 4, errBuffer);
		Util.checkCLError(errBuffer.get(0));
		return m;
	}
	
	public static CLMem initCLMemoryWOM(FloatBuffer buffer){
		CLMem m = CL10.clCreateBuffer(context, CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR, buffer, errBuffer);
		Util.checkCLError(errBuffer.get(0));
		return m;
	}
	
	public static CLMem initCLMemoryWOM(IntBuffer buffer){
		CLMem m = CL10.clCreateBuffer(context, CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR, buffer, errBuffer);
		Util.checkCLError(errBuffer.get(0));
		return m;
	}
	
	public static CLMem initCLMemoryWOM(LongBuffer buffer){
		CLMem m = CL10.clCreateBuffer(context, CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR, buffer, errBuffer);
		Util.checkCLError(errBuffer.get(0));
		return m;
	}
	
	public static CLMem initCLMemoryRW(FloatBuffer buffer){
		CLMem m = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_WRITE | CL10.CL_MEM_COPY_HOST_PTR, buffer, errBuffer);
		Util.checkCLError(errBuffer.get(0));
		return m;
	}
	
	public static CLMem initCLMemoryRW(IntBuffer buffer){
		CLMem m = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_WRITE | CL10.CL_MEM_COPY_HOST_PTR, buffer, errBuffer);
		Util.checkCLError(errBuffer.get(0));
		return m;
	}
	
	public static CLMem initCLMemoryRW(LongBuffer buffer){
		CLMem m = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_WRITE | CL10.CL_MEM_COPY_HOST_PTR, buffer, errBuffer);
		Util.checkCLError(errBuffer.get(0));
		return m;
	}
	
	public static CLProgram initCLProgram(String path){
		CLProgram cl_program;
		cl_program = CL10.clCreateProgramWithSource(context, loadSource(path), null);
		int errCode = CL10.clBuildProgram(cl_program, devices.get(0), "", null); // building the program
		if(errCode != CL10.CL_SUCCESS){
			Util.checkCLError(errCode);
		}
		
		return cl_program;
	}
	
	public static CLKernel initClKernel(CLProgram cl_program, String kernelName){
		CLKernel cl_kernel;
		cl_kernel = CL10.clCreateKernel(cl_program, kernelName, errBuffer);
		Util.checkCLError(errBuffer.get(0));
		return cl_kernel;
	}
	
	public static String loadSource(String path){
		String data = null;
		
		try{
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line = null;
			StringBuilder strB = new StringBuilder();
			while((line = br.readLine()) != null){
				strB.append(line);
				strB.append("\n");
			}
			
			data = strB.toString();
			br.close();
		}catch(Exception e){}
			
		return data;
	}
	
	
	
	public static void main(String[] args){
		new Main();
	}
}
