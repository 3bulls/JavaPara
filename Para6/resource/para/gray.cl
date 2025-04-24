#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable

// OpenCL Kernel Function
__kernel void Gray(const int width, const int height,
                        __global uchar* input, __global uchar* output,
                        float parameter){
/**
 * int width :  image width
 * int height : image height
 * __global uchar* input :  input image from camera
 * __global uchar* output :  output from this program
 * float parameter : this is not used in this program
 */

  int x = get_global_id(0);
  int y = get_global_id(1);
  int addr = (y*width+x)*3;

  

  // the following is do nothing sample
  int r = input[addr  ]*0.212671;
  int g = input[addr+1]*0.715160;
  int b = input[addr+2]*0.072169;
  output[(y*width+x)*4  ] = r+g+b;
  output[(y*width+x)*4+1] = r+g+b;
  output[(y*width+x)*4+2] = r+g+b;
  output[(y*width+x)*4+3] = 0xff;
}
