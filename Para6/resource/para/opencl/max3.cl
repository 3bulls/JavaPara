#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable

__kernel void Max3(__constant float* a, __constant float* b, __constant float* c,
                    __global float* d, int sizeOfArr, int numOfArrs){
  // get index into global data array
    int i = get_global_id(0);
    if (numOfArrs<i) return;
    //**this will find the max in each array */
    // int result;
    // switch(i){
    //     case 0:
    //         result = fmax(fmax(a[0],a[1]),a[2]);
    //         break;
    //     case 1:
    //         result = fmax(fmax(b[0],b[1]),b[2]);
    //         break;
    //     case 2:
    //         result = fmax(fmax(c[0],c[1]),c[2]);
    //         break;
    // }
    // d[i] = result;
    d[i] = fmax(fmax(a[i],b[i]),c[i]);

}