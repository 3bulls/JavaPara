#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable


__kernel void Sum3(__constant float* a, __constant float* b, __constant float* c,
                    __global float* d, int sizeOfArr, int numOfArrs){
    int i = get_global_id(0);
    if (sizeOfArr<i) return;
    d[i] = a[i]+b[i]+c[i];
}