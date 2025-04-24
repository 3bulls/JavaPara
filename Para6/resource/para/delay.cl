#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable

__kernel void Delay(const int width, const int height,
         __global uchar* latest, __global uchar* previous,
         __global uchar* output, const float p){
    int idx = get_global_id(0);
    int idy = get_global_id(1);
    for (int c=0;c<3;c++) {
        output[(idy*width + idx)*4+c] = p*latest[(idy*width + idx)*3+c] + (1-p)*previous[(idy*width + idx)*3+c];
        previous[(idy*width + idx)*3+c] = output[(idy*width + idx)*4+c];
    }
    output[(idy*width + idx)*4+3] = 0xff;
}