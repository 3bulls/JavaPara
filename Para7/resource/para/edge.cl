int addr(const int width, const int height, int x, int y){
  if(y<0){y=0;}
  if(height-1<y){y=height-1;}
  if(x<0){x=0;}
  if(width-1<x){x=width-1;}
  return (y*width*3+x*3);
}

float bound(const float in){
  if(in<0) return 0; 
  if(in>255) return 255.0f;
  return in;
}

__kernel void EdgeFilter(const int width, const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
		     const float cx) {
  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);

float A[3];
float B[3];
  for (int shift =0; shift<3; shift++) {
       A[shift] = in[addr(width,height,lx-1,ly-1)+shift]*(-1)+
                  in[addr(width,height,lx,ly-1)+shift]*(0)+
                  in[addr(width,height,lx+1,ly-1+shift)]*(1)+
                  in[addr(width,height,lx-1,ly)+shift]*(-2)+
                  in[addr(width,height,lx,ly)+shift]*(0)+
                  in[addr(width,height,lx+1,ly)+shift]*(2)+
                  in[addr(width,height,lx-1,ly+1)+shift]*(-1)+
                  in[addr(width,height,lx,ly+1)+shift]*(0)+
                  in[addr(width,height,lx+1,ly+1)+shift]*(1);
       B[shift] = in[addr(width,height,lx-1,ly-1)+shift]*(-1)+
                  in[addr(width,height,lx,ly-1)+shift]*(-2)+
                  in[addr(width,height,lx+1,ly-1)+shift]*(-1)+
                  in[addr(width,height,lx-1,ly)+shift]*(0)+
                  in[addr(width,height,lx,ly)+shift]*(0)+
                  in[addr(width,height,lx+1,ly)+shift]*(0)+
                  in[addr(width,height,lx-1,ly+1)+shift]*(1)+
                  in[addr(width,height,lx,ly+1)+shift]*(2)+
                  in[addr(width,height,lx+1,ly+1)+shift]*(1);
  }

  int oadd = (ly*width+lx)*4;
  for (int shift =0 ;shift<3;shift++) {
    outb[oadd+shift] = bound((A[shift]*A[shift]+B[shift]*B[shift])*cx/1600);
  }
  outb[oadd+3] = 0xff;
}

