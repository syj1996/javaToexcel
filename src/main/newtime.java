package main;

import java.util.*;

public class newtime {
    public static  List  getList() {
        List<ResultGet> list = new ArrayList<ResultGet>();
        int i = 0;
        for(int e=500;e<2000;e=e+500)
         for(int S= 1;S<10;S=S+1) {
            for (int N = 2 * S * 10; N < 500; N = N + 2 * S * 10) {
                int t = process(N, S, 1, 12000, e);
                double k = ((double) N) / (t / 1000);
                System.out.println(k);
                ResultGet result = new ResultGet();
                result.setS(S);
                result.setK(k);
                result.setT(Integer.valueOf(t/1000));
                result.setT1(12);
                result.setT2(1);
                result.setN(N);
                list.add(result);
            }
        }
        return list;
    }
//    每次从蓄车池放入的出租车数量为N=2n
//    管理部门设置的上车点个数为S
//    平均每辆车做的人数为K
//    第一辆出租车达到乘车区泊车位的时间为 T1
//    相邻出租车达到泊车位的时间间隔为T2
//    工作人员放上车点人时乘客上车时间为T3
//    出租车停稳后到乘客上车的时间根据每个上车点的出租车数确定最坏情况下司机等待的时间t
    //出租车等到乘客上车的时间就在0和t成一个分布nextGaussian();
    //出租车
    public static int process(int N,int S,int K,int T1,int T2) {
        int num = N / S; //平均每个上车点可以享有的出租车数量
        Taxi[] taxi=new Taxi[N];
        taxi[0]=new Taxi(0,T1,T2,null);
        taxi[1]=new Taxi(1,T1,T2,null);
        for(int i=2;i<N;i++){
            taxi[i]=new Taxi(i,T1,T2,null);
            taxi[i-2].nextTaxi=taxi[i];
        }
        taxi[0].canLeave=true;
        taxi[1].canLeave=true;
        //建立开始服务的出租车队列和停止服务的出租车队列
        Queue<Taxi> startTaxi=new LinkedList<Taxi>();
        Queue<Taxi> endTaxi=new LinkedList<Taxi>();
        int currentTaxi=1;
        //从0时刻开始
        int dt;
        for( dt=0;dt<100000000;dt++){
            if(currentTaxi<=N && ( dt %(taxi[currentTaxi-1].startTime))==0){
                //判断出租车达到泊车区服务准备开始
                //加入开始服务的出租车队列
                startTaxi.add(taxi[currentTaxi-1]);
                if((currentTaxi)%(N/S)==0){
                    //当一个上车点范围的所有出租车都挺稳后上车点范围内的所有出租车
                    //开始等待乘客上车
                    int k=currentTaxi/(N/S);
                    for(int i = currentTaxi-k*(N/S);i<currentTaxi;i++){
                        taxi[i].start(dt);
                        taxi[i].serviceTime();
                    }
                }
                currentTaxi++;
            }
            Queue<Taxi> templist=new LinkedList<Taxi>();
            for(Taxi taxi1 :startTaxi){
                if(taxi1.serviceStart==true){
                    if((dt-taxi1.startServiceTime)==taxi1.serviceTime){
                        taxi1.end();
                        endTaxi.add(taxi1);
                        templist.add(taxi1);
                    }
                }
            }
            startTaxi.removeAll(templist);
            templist=new LinkedList<Taxi>();
            if(endTaxi.size()!=0)
            {
                for(Taxi taxi2 :endTaxi){
                if(taxi2.canLeave==true){
                    taxi2.leave =true;
                    templist.add(taxi2);
                    if(taxi2.nextTaxi!=null) taxi2.nextTaxi.canLeave=true;
                    taxi2.leaveTime=dt;
                }
            }
                endTaxi.removeAll(templist);
            }
            if(taxi[N-1].leave==true) break;
        }
        return dt;
    }
}

class Taxi{
    public boolean serviceStart=false; //等待乘客上车开始时间
    public boolean serviceEnd=false;  //服务结束时间
    public int serviceTime=0;         //总的服务时间正态分布
    public int startTime=0;             //达到泊车区的时间
    public int startServiceTime;       //上车点范围内的车都停稳后开始服务的时间
    public boolean canLeave=false;      //是否可以离开
    public boolean leave =false;         //是否已经离开
    public Taxi nextTaxi;              //后面的车辆
    public int leaveTime=0;               //出租车离开的时间
    public int num;
    public Taxi(int i, int T1, int T2, Taxi taxi) {
        if(i%2==0)
        startTime=T1+T2*(i/2);
        else startTime=T1+T2*(i/2)+1;
        nextTaxi=taxi;
        num=i;
    }
    public void start(int t){
        serviceStart=true;
        startServiceTime=t;
    }
    public void end(){
        serviceEnd=true;
    }
    public void serviceTime(){
        Random dm=new Random();
        int k=(int)((dm.nextGaussian()+1)*60*1000);
        while(k<=0){
            k=(int)((dm.nextGaussian()+1)*60*1000);
        }
        //System.out.println(k);
        serviceTime= k;
    }
}
class ResultGet {
   private Integer S;

   private Integer T;

   private Integer N;

   private Double K;

   private Integer T1;

   private Integer T2;

   public Integer getS() {
       return S;
   }

   public void setS(Integer s) {
       S = s;
   }

   public ResultGet() {
   }

   public Integer getT() {
       return T;
   }

   public void setT(Integer t) {
       T = t;
   }

   public Integer getN() {
       return N;
   }

   public void setN(Integer n) {
       N = n;
   }

   public Double getK() {
       return K;
   }

   public void setK(Double k) {
       K = k;
   }

   public Integer getT1() {
       return T1;
   }

   public void setT1(Integer t1) {
       T1 = t1;
   }

   public Integer getT2() {
       return T2;
   }

   public void setT2(Integer t2) {
       T2 = t2;
   }
}
