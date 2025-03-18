//package org.martin.array.threads;
//
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadPoolExecutor;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
///**
// *
// * @author martin
// */
//public class ThreadMachine implements java.io.Serializable{
//    private final ThreadPoolExecutor executor;
//
//    public ThreadMachine() {
//        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
//    }
//    
//    public void addThread(Runnable runnable){
//        executor.execute(runnable);
//        executor.shutdown();
//    }
//    
//}
