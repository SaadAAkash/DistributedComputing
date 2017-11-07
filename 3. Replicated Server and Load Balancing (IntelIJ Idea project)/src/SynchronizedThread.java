/**
 * Created by akash on 8/11/17.
 */
public class SynchronizedThread extends Thread {

//    ThreadA aa = new ThreadA();

}

/*
 an object, b, is synchronized. b completes the calculation before Main thread outputs its total value.

 wait() tells the calling thread to give up the monitor and go to sleep until some other thread enters the same monitor and calls notify( ).
notify() wakes up the first thread that called wait() on the same object.
*/

class ThreadA
{
    public static void main(String[] args)
    {
        ThreadB b = new ThreadB();
        b.start();  // b starts to run parallelly

        synchronized(b){
            try{
                System.out.println("Waiting for b to complete...");
                b.wait();

                //b.wait means WAIT FOR B
                //so ThreadA will  go to sleep until b notifies it to wake up & print the total

            }catch(InterruptedException e){
                e.printStackTrace();
            }

            System.out.println("Total is: " + b.total);
        }
    }
}

class ThreadB extends Thread{
    int total;
    @Override
    public void run(){
        synchronized(this){
            for(int i=0; i<100 ; i++){
                total += i;
            }
            notify(); // call the wait() calling thread, means threadA to wake up
        }
    }
}
