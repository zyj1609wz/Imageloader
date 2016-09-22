package com.zyj.app.imageload.util;

import android.os.AsyncTask;

/**
 * Created by Administrator on 2016/9/10.
 */
public class MyTask<Params, Progress, Result>  extends AsyncTask<Params, Progress, Result> {

    private TaskListener taskListener ;

    public MyTask(){

    }

    //执行预处理，它运行于UI线程，可以为后台任务做一些准备工作，比如绘制一个进度条控件。
    @Override
    protected void onPreExecute() {
        if ( taskListener != null ){
            taskListener.start();
        }
    }

    //运行于UI线程，可以对后台任务的结果做出处理，结果就是doInBackground(Params...)的返回值。
    @Override
    protected void onPostExecute(Result result) {
        if ( taskListener != null ){
            taskListener.result( result );
        }
    }

    /**
     * 更新子线程进度，运行于UI线程
     * @param values
     */
    @Override
    protected void onProgressUpdate(Progress... values) {
        if ( taskListener != null ){
            taskListener.update( values[0] );
        }
    }

    //运行与后台线程
    @Override
    protected Result doInBackground(Params... params) {
        if ( taskListener != null ){
            return (Result) taskListener.doInBackground( params[0] );
        }
        return null;
    }

    public MyTask setTaskListener(TaskListener taskListener ){
        this.taskListener = taskListener ;
        return this ;
    }

    /**
     * 更新进度
     * @param progress
     */
    public void updateProgress( Progress progress ){
        publishProgress( progress );
    }

    public interface TaskListener<Params, Progress, Result>{
        void start() ;
        void update(Progress progress) ;
        Result doInBackground(Params params);
        void result(Result result);
    }

    /**
     * 取消一个正在执行的任务
     */
    public void cancle(){
        if ( !isCancelled() ){
            cancel( true ) ;
        }
    }
}
