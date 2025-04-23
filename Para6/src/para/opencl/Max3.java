package para.opencl;

import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLCommandQueue;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLProgram;

import easycl.*;
import java.nio.*;
import java.io.IOException;
import java.io.*;
import java.util.*;

import static java.lang.System.*;
import static com.jogamp.opencl.CLMemory.Mem.*;

/** OpenCLを使用例を知るための簡単なデモ。
 * BufferAとBufferBとBufferCのそれぞれのi番目の要素の最大値をBufferDのi番目に格納する。
 * @see <a href=../../easycl/index.html>easyclパッケージ</a>
 */
public class Max3{
  /** OpenCLの初期化、データ初期化、演算、結果出力、OpenCL資源の開放をする
   */
  public Max3(){
    //OpenCL演算環境の準備
    CLSetup cl = CLSetupCreator.createCLSetup();
    cl.initContext();

    FloatBuffer tmpfb;
    tmpfb = loadData("data/dataa.txt");
    CLBuffer<FloatBuffer> BufferA = cl.createBuffer(tmpfb,READ_ONLY);
    tmpfb.rewind();
    tmpfb = loadData("data/datab.txt");
    CLBuffer<FloatBuffer> BufferB = cl.createBuffer(tmpfb,READ_ONLY);
    tmpfb.rewind();
    tmpfb = loadData("data/datac.txt");
    CLBuffer<FloatBuffer> BufferC = cl.createBuffer(tmpfb,READ_ONLY);
    int datasize = tmpfb.limit();
    CLCommandQueue queue = cl.createQueue();

    CLProgram program = cl.createProgramFromResource(this,"max3.cl");
    CLKernel kernel = program.createCLKernel("Max");
    kernel.putArgs(BufferA, BufferB, BufferC);
    kernel.setArg(3,datasize);

    BufferC.getBuffer().rewind();
    //to device
    //デバイスへ転送、並列演算、演算結果の取得
    queue.putWriteBuffer(BufferA, false)//BufferAのデータをカーネル側へ転送指令
      .putWriteBuffer(BufferB, false)//BufferBのデータをカーネル側へ転送指令
      .putBarrier() //今までの指令がすべて完了するまで待つ
      .put1DRangeKernel(kernel, 0, datasize, 1)//演算指令
      .putBarrier() //今までの指令がすべて完了するまで待つ
      .putReadBuffer(BufferC, true);//BufferCのデータをホスト側へ転送指令
                                    //転送完了まで待つ
    //演算結果の出力
    FloatBuffer fb = BufferC.getBuffer();
    fb.rewind();
    for(int i=0;i<fb.limit();i++){
      System.out.print(fb.get()+" ");
    }
    System.out.println();

    cl.release();
  }

  public static void main(String[] argv){
    new Max3();
  }
}
